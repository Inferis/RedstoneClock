package redstoneclock.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import redstoneclock.RedstoneClock;

public class RedstoneClockBlocks {
    public static ClockBlock CLOCK;

    interface BlockMaker<T extends Block> {
        T makeBlock(RegistryKey<Block> key);
    }

    private static <T extends Block> T registerBlock(String name, BlockMaker<T> blockMaker) {
        RedstoneClock.LOGGER.info("Registering block: " + RedstoneClock.MODID + ":" + name);

        var identifier = Identifier.of(RedstoneClock.MODID, name);
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, identifier);
        return Registry.register(
            Registries.BLOCK,
            identifier,
            blockMaker.makeBlock(key));
    }


    public static void registerBlocks() {
        CLOCK = registerBlock("clock", key -> { 
            return new ClockBlock(AbstractBlock.Settings.copy(Blocks.REPEATER).registryKey(key)); 
        });
    }
    
}
