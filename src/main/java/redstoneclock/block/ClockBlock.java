package redstoneclock.block;

import java.util.EnumSet;
import java.util.Set;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import redstoneclock.RedstoneClock;

public class ClockBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    private final static MapCodec<ClockBlock> CODEC = Block.createCodec(ClockBlock::new);

    public ClockBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.POWERED, false).with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ClockBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.POWERED);
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock,
            WireOrientation wireOrientation, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);

        var direction = state.get(Properties.HORIZONTAL_FACING);
        var dirPos = pos.offset(direction);
        var power = world.getEmittedRedstonePower(dirPos, direction);
        world.setBlockState(pos, state.with(Properties.POWERED, power > 0));
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
        if (state.get(Properties.POWERED) && world.getBlockEntity(pos) instanceof ClockBlockEntity clockEntity) {
            var oppositeFacing = state.get(Properties.HORIZONTAL_FACING).getOpposite();
            if (direction != Direction.UP && direction != Direction.DOWN && direction != oppositeFacing) {
                return 15;
            }
        }

        return 0;
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }
}

