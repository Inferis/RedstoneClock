package redstoneclock.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ClockBlockEntity extends BlockEntity {
    public ClockBlockEntity(BlockPos pos, BlockState state) {
        super(RedstoneClockBlockEntities.CLOCK_ENTITY, pos, state);
    }
    
}
