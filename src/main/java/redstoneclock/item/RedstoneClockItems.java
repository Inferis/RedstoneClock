package redstoneclock.item;

import redstoneclock.RedstoneClock;
import redstoneclock.block.RedstoneClockBlocks;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class RedstoneClockItems {
    public static ClockBlockItem CLOCK;

    interface ItemMaker<T extends Item> {
        T makeItem(RegistryKey<Item> key);
    }

    private static <T extends Item> T registerItem(String name, ItemMaker<T> itemMaker) {
        RedstoneClock.LOGGER.info("Registering item: " + RedstoneClock.MODID + ":" + name);

        var identifier = Identifier.of(RedstoneClock.MODID, name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, identifier);
        T item = itemMaker.makeItem(key);
        return Registry.register(Registries.ITEM, identifier, item);
    }

    public static void registerItems() {
        RedstoneClock.LOGGER.info("Registering mod items for " + RedstoneClock.MODID);

        CLOCK = registerItem("clock", key -> { 
            return new ClockBlockItem(RedstoneClockBlocks.CLOCK, new Item.Settings().useBlockPrefixedTranslationKey().registryKey(key)); 
        });
    }    
}
