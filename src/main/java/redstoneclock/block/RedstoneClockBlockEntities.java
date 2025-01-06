package redstoneclock.block;

import redstoneclock.RedstoneClock;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RedstoneClockBlockEntities {
    public static BlockEntityType<ClockBlockEntity> CLOCK_ENTITY;
    
    public static void registerBlockEntities() {
        CLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(RedstoneClock.MODID, "clock_block_entity"),
            FabricBlockEntityTypeBuilder.create(ClockBlockEntity::new, RedstoneClockBlocks.CLOCK).build()
        );    
    }
}
