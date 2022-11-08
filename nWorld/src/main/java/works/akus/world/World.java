package works.akus.world;

import org.bukkit.plugin.java.JavaPlugin;

import org.checkerframework.checker.units.qual.C;
import works.akus.world.camp.CampWorld;
import works.akus.world.fishing.FishingManager;

public class World extends JavaPlugin {

	private static World instance;
	
	private FishingManager fishingManager;

	private CampWorld campWorld;

	@Override
	public void onEnable() {
		instance = this;
		
		fishingManager = new FishingManager(instance);

		campWorld = new CampWorld(instance);
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
