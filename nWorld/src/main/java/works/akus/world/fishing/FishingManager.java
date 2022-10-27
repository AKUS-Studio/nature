package works.akus.world.fishing;

import org.bukkit.plugin.java.JavaPlugin;

import works.akus.mauris.registry.ItemRegistry;
import works.akus.world.fishing.items.CustomFish;
import works.akus.world.fishing.items.CustomFishingRod;
import works.akus.world.fishing.process.FishingProcessListener;
import works.akus.world.fishing.process.FishingProcessesHandler;
import works.akus.world.fishing.types.CustomFishType;
import works.akus.world.fishing.types.FishingRodType;

public class FishingManager {

	private FishingProcessesHandler fishingProcessesHandler;
	
	public FishingManager(JavaPlugin plugin) {
		fishingProcessesHandler = new FishingProcessesHandler(plugin);
		plugin.getServer().getPluginManager().registerEvents(new FishingProcessListener(), plugin);
		
		registerItems();
	}
	
	private void registerItems() {
		// Register fishing rods
		ItemRegistry.register("bamboo_fishing_rod", new CustomFishingRod(FishingRodType.BAMBOO_FISHING_ROD));
		ItemRegistry.register("standard_fishing_rod", new CustomFishingRod(FishingRodType.STANDARD_FISHING_ROD));
		
		// Register fishes
		ItemRegistry.register("cod", new CustomFish(CustomFishType.COD));
		ItemRegistry.register("bamboo_fish", new CustomFish(CustomFishType.BAMBOO_FISH));
	}
	
	public FishingProcessesHandler getFishingProcessesHandler() {
		return fishingProcessesHandler;
	}
	
}