package works.akus.mauris;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import works.akus.mauris.commands.CommandManager;
import works.akus.mauris.listeners.MaurisItemListener;
import works.akus.mauris.listeners.ResourcePackListener;
import works.akus.mauris.objects.menu.title.ContainerTitleHandler;
import works.akus.mauris.registry.Defaults;
import works.akus.mauris.resourcepack.ResourcePackManager;

public class Mauris extends JavaPlugin {

	private static final String TOKEN_NOT_SET = "GitHub token is not set, resource pack updater was not enabled.";
	private static final String TOKEN_PATH = "resource-pack-updater.github-token";
	private static final String CONFIG = "config.yml";

	private static Mauris instance;

	private ContainerTitleHandler containerTitleHandler;
	private ResourcePackManager resourcePackUpdater;
	private ProtocolManager protocolManager;
	private CommandManager commandManager;

	@Override
	public void onEnable() {
		instance = this;

		createConfig();
		loadResourcePackManager();

		Defaults.registerDefaults();

		protocolManager = ProtocolLibrary.getProtocolManager();
		containerTitleHandler = new ContainerTitleHandler(protocolManager, instance);
		
		commandManager = new CommandManager();
		commandManager.initialize();

		registerListeners();

	}

	@Override
	public void onDisable() {
		if (resourcePackUpdater != null) {
			resourcePackUpdater.stopServer();
		}
	}

	private void registerListeners() {
		Bukkit.getPluginManager().registerEvents(new MaurisItemListener(), instance);
		Bukkit.getPluginManager().registerEvents(new ResourcePackListener(resourcePackUpdater), instance);
	}

	private void loadResourcePackManager() {
		if (isGithubTokenSet()) {
			resourcePackUpdater = new ResourcePackManager();
			resourcePackUpdater.setup();
			return;
		}

		getLogger().warning(TOKEN_NOT_SET);
	}

	private boolean isGithubTokenSet() {
		final String path = TOKEN_PATH;
		return (getConfig().isSet(path)) && !(getConfig().getString(path).isBlank());
	}

	private void createConfig() {
		final File config = new File(getDataFolder() + File.separator + CONFIG);
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

	public ContainerTitleHandler getContainerTitleHandler() {
		return containerTitleHandler;
	}
}
