package redstoneclock.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import redstoneclock.RedstoneClock;

public class RedstoneClockItemGroups {
    public static void registerItemGroups() {
        RedstoneClock.LOGGER.info("Registering item groups for " + RedstoneClock.MODID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> {
            content.add(RedstoneClockItems.CLOCK);
        });
    }    
}
