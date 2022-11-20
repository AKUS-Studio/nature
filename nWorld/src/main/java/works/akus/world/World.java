package works.akus.world;

import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import works.akus.world.camp.CampWorld;
import works.akus.world.fishing.FishingManager;

public class World extends JavaPlugin {

	private static World instance;
	
	private FishingManager fishingManager;

	private CampWorld campWorld;
	
	private ProtocolManager protocolManager;

	@Override
	public void onEnable() {
		instance = this;
		protocolManager = ProtocolLibrary.getProtocolManager();
		
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

	public ProtocolManager getProtocolManager() {
		return protocolManager;
	}

}
