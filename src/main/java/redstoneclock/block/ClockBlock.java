package redstoneclock.block;

import com.mojang.serialization.MapCodec;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import redstoneclock.networking.OpenEditorS2CPayload;
import redstoneclock.networking.SaveIntervalsC2SPayload;

public class ClockBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    private final static MapCodec<ClockBlock> CODEC = Block.createCodec(ClockBlock::new);

    public ClockBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
            .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
            .with(Properties.POWERED, false)
            .with(Properties.LIT, false));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ClockBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.POWERED);
        builder.add(Properties.HORIZONTAL_FACING);
        builder.add(Properties.LIT);
    }

    @Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock,
            WireOrientation wireOrientation, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);

        var power = 0;
        var direction = state.get(Properties.HORIZONTAL_FACING);
        var dirPos = pos.offset(direction);
        power = world.getEmittedRedstonePower(dirPos, direction);
        world.setBlockState(pos, state.with(Properties.POWERED, power > 0));
        world.scheduleBlockTick(pos, state.getBlock(), 1);
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.getWeakRedstonePower(world, pos, direction);
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (direction.getAxis().isVertical()) {
            return 0;
        }

        if (state.get(Properties.LIT) && world.getBlockEntity(pos) instanceof ClockBlockEntity clockEntity) {
            var oppositeFacing = state.get(Properties.HORIZONTAL_FACING).getOpposite();
            if (direction != Direction.UP && direction != Direction.DOWN && direction != oppositeFacing) {
                return clockEntity.getSignalStrength();
            }
        }

        return 0;
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random); 
        if (world.getBlockEntity(pos) instanceof ClockBlockEntity clockEntity) {
            clockEntity.tick(world, pos, state);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient && world.getBlockEntity(pos) instanceof ClockBlockEntity clockBlockEntity) {
            ServerPlayNetworking.send((ServerPlayerEntity)player, clockBlockEntity.getEditorPayload(pos));
        }
        return ActionResult.SUCCESS;
    }
}

