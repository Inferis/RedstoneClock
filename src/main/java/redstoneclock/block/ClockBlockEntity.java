package redstoneclock.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import redstoneclock.networking.OpenEditorS2CPayload;

public class ClockBlockEntity extends BlockEntity {
    private final String ACTIVE_INTERVAL_KEY = "active_interval";
    private final String INACTIVE_INTERVAL_KEY = "inactive_interval";
    
    private int signalStrength = 15;
    private int cycle = 0;
    private int activeInterval = 20;
    private int inactiveInterval = 20;

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

    public OpenEditorS2CPayload getEditorPayload(BlockPos pos) {
        return new OpenEditorS2CPayload(pos, activeInterval, inactiveInterval);
    }

    public void updateIntervals(int activeInterval, int inactiveInterval) {
        this.activeInterval = activeInterval;
        this.inactiveInterval = inactiveInterval;
        this.markDirty();
    }

    @Override
    protected void readNbt(NbtCompound nbt, WrapperLookup registries) {
        super.readNbt(nbt, registries);

        if (nbt.contains(ACTIVE_INTERVAL_KEY)) {
            activeInterval = nbt.getInt(ACTIVE_INTERVAL_KEY);
        }
        if (nbt.contains(INACTIVE_INTERVAL_KEY)) {
            inactiveInterval = nbt.getInt(INACTIVE_INTERVAL_KEY);
        }
    }
    
    @Override
    protected void writeNbt(NbtCompound nbt, WrapperLookup registries) {
        super.writeNbt(nbt, registries);

        nbt.putInt(ACTIVE_INTERVAL_KEY, activeInterval);
        nbt.putInt(INACTIVE_INTERVAL_KEY, inactiveInterval);
    }
}
