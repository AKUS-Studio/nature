package works.akus.world;

import org.bukkit.plugin.java.JavaPlugin;

import works.akus.world.fishing.FishingManager;

public class World extends JavaPlugin {

	private static World instance;
	
	private FishingManager fishingManager;

	@Override
	public void onEnable() {
		instance = this;
		
		fishingManager = new FishingManager(instance);
	}

	@Override
	public void onDisable() {

	}

	public static World get() {
		return instance;
	}

	public FishingManager getFishingManager() {
		return fishingManager;
	}

}
