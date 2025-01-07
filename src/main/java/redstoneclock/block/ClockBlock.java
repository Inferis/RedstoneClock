package redstoneclock.block;

import java.util.EnumSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import redstoneclock.RedstoneClock;

public class ClockBlock extends Block implements BlockEntityProvider {
    private Set<Direction> inputs = EnumSet.noneOf(Direction.class);
    private Set<Direction> outputs = EnumSet.noneOf(Direction.class);

    public ClockBlock(Settings settings) {
        super(settings);

        setDefaultState(getDefaultState().with(Properties.POWERED, false));

        inputs.add(Direction.EAST);
        inputs.add(Direction.WEST);
        outputs.add(Direction.NORTH);
        outputs.add(Direction.SOUTH);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ClockBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.POWERED);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock,
            WireOrientation wireOrientation, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);

        RedstoneClock.LOGGER.info("neighborUpdate: " + state + ", " + pos + ", " + sourceBlock);

        world.setBlockState(pos, state.with(Properties.POWERED, false));
        for (var direction: inputs) {
            var dirPos = pos.offset(direction);
            var power = world.getEmittedRedstonePower(dirPos, direction);
            RedstoneClock.LOGGER.info(direction + " emitted=" + power);
            if (power > 0) {
                RedstoneClock.LOGGER.info("POWER ON");
                world.setBlockState(pos, state.with(Properties.POWERED, true));
                break;
            }
        }
    }

    // @Override
    // protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView,
    //         BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
    //     BlockState result = super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);

    //     RedstoneClock.LOGGER.info("getStateForNeighborUpdate: " + state + ", " + pos + ", " + direction + ", " + neighborPos + ", " + neighborState);

    //     inputs.add(Direction.EAST);
    //     inputs.add(Direction.WEST);

    //     if (inputs.contains(direction)) {
    //         var dirPos = pos.offset(direction);
    //         var power = world.getEmittedRedstonePower(dirPos, direction);
    //         RedstoneClock.LOGGER.info(direction + " emitted=" + power);
    //         if (power > 0) {
    //             RedstoneClock.LOGGER.info("POWER ON");
    //             return result.with(Properties.POWERED, true);
    //         }
    //     }

    //     return result.with(Properties.POWERED, false);
    // }

    // @Override
    // protected boolean emitsRedstonePower(BlockState state) {
    //     return state.get(Properties.POWERED);
    // }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.getWeakRedstonePower(world, pos, direction);
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (state.get(Properties.POWERED) && world.getBlockEntity(pos) instanceof ClockBlockEntity clockEntity) {
            Set<Direction> outputs = EnumSet.noneOf(Direction.class);
            outputs.add(Direction.NORTH);
            outputs.add(Direction.SOUTH);

            if (outputs.contains(direction)) {
                RedstoneClock.LOGGER.info("getWeakRedstonePower: " + direction + " powered");
                return 15;
            }
        }

        RedstoneClock.LOGGER.info("getWeakRedstonePower: " + direction + " unpowered");
        return 0;
    }
}

