package redstoneclock.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import redstoneclock.RedstoneClock;

public class ClockBlockEntity extends BlockEntity {
    private int signalStrength = 15;
    private int cycle = 0;
    private int activeInterval = 6;
    private int inactiveInterval = 10;

    public ClockBlockEntity(BlockPos pos, BlockState state) {
        super(RedstoneClockBlockEntities.CLOCK_ENTITY, pos, state);
    }

    public int getSignalStrength() {
        return cycle < activeInterval ? signalStrength : 0;
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (state.get(Properties.POWERED)) {
            cycle = (cycle + 1) % (activeInterval + inactiveInterval);
            var isLit = state.get(Properties.LIT);
            if (cycle < activeInterval) {
                if (!isLit) {
                    world.setBlockState(pos, state.with(Properties.LIT, true));
                }
            }
            else if (isLit) {
                world.setBlockState(pos, state.with(Properties.LIT, false));
            }
            world.scheduleBlockTick(pos, state.getBlock(), 1);
        }
        else {
            cycle = -1;
            world.setBlockState(pos, state.with(Properties.LIT, false));
        }
    }
}
