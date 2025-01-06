package redstoneclock;

import net.fabricmc.api.ModInitializer;
import redstoneclock.block.RedstoneClockBlockEntities;
import redstoneclock.block.RedstoneClockBlocks;
import redstoneclock.item.RedstoneClockItemGroups;
import redstoneclock.item.RedstoneClockItems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedstoneClock implements ModInitializer {
	public static final String MODID = "redstoneclock";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final RedstoneClockConfig CONFIG = new RedstoneClockConfig();

	@Override
	public void onInitialize() {
		CONFIG.initialLoad();

		RedstoneClockBlocks.registerBlocks();
		RedstoneClockBlockEntities.registerBlockEntities();
		RedstoneClockItems.registerItems();
		RedstoneClockItemGroups.registerItemGroups();
	}
}