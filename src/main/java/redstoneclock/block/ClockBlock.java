package redstoneclock.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import redstoneclock.RedstoneClock;

public class ClockBlock extends Block implements BlockEntityProvider {
    public ClockBlock(Settings settings) {
        super(settings);
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

        if (world.isClient) {
            return;
        }

        RedstoneClock.LOGGER.info("neighborUpdate: " + state + ", " + pos + ", " + sourceBlock + ", " + wireOrientation + ", " + notify);

        if (world.getBlockEntity(pos) instanceof ClockBlockEntity clockEntity) {
            var power = 0;
            for (var direction: DIRECTIONS) {
                var dirPos = pos.offset(direction);

                power = world.getEmittedRedstonePower(dirPos, direction);
                RedstoneClock.LOGGER.info(direction + ": emitted " + power);

                if (power > 0) {
                    break;
                }
            }

            world.setBlockState(pos, state.with(Properties.POWERED, power > 0));
        }
    }
}

