package works.akus.mauris;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import works.akus.mauris.commands.CommandManager;
import works.akus.mauris.listeners.MaurisItemListener;
import works.akus.mauris.listeners.ResourcePackListener;
import works.akus.mauris.objects.menu.InventoryHandler;
import works.akus.mauris.registry.Defaults;
import works.akus.mauris.resourcepack.ResourcePackManager;

public class Mauris extends JavaPlugin {

	private static Mauris instance;

	// Managers
	private CommandManager commandManager;
	private ResourcePackManager resourcePackUpdater;
	private InventoryHandler inventoryHandler;

	private ProtocolManager protocolManager;

	public void onEnable() {
		instance = this;
		createConfig();

		// Registries
		Defaults.registerDefaults();

		// Managers
		protocolManager = ProtocolLibrary.getProtocolManager();
		inventoryHandler = new InventoryHandler(protocolManager, instance);
		
		commandManager = new CommandManager();
		commandManager.setUp();

		loadResourcePackManager();

		// Listeners
		registerListeners();

	}

	@Override
	public void onDisable() {
		super.onDisable();

		if (resourcePackUpdater != null) {
			resourcePackUpdater.stopServer();
		}
	}

	private void registerListeners() {
		Bukkit.getPluginManager().registerEvents(new MaurisItemListener(), Mauris.getInstance());
		Bukkit.getPluginManager().registerEvents(new ResourcePackListener(resourcePackUpdater), Mauris.getInstance());
	}

	private void loadResourcePackManager() {
		if (isGithubTokenSet()) {
			resourcePackUpdater = new ResourcePackManager();
			resourcePackUpdater.setup();
		} else {
			getLogger().warning("GitHub token is not set, resource pack updater was not enabled.");
		}
	}

	private boolean isGithubTokenSet() {
		final String path = "resource-pack-updater.github-token";
		if (!this.getConfig().isSet(path))
			return false;
		return !(this.getConfig().getString(path).isBlank());
	}

	private void createConfig() {
		File config = new File(getDataFolder() + File.separator + "config.yml");
		if (!config.exists()) {
			getConfig().options().copyDefaults(true);
			saveDefaultConfig();
		}
	}

	public static Mauris getInstance() {
		return instance;
	}

	public ProtocolManager getProtocolManager() {
		return this.protocolManager;
	}
	
	public InventoryHandler getInventoryHandler() {
		return inventoryHandler;
	}
}
