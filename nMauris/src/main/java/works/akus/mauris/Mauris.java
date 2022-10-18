package works.akus.mauris;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import works.akus.mauris.commands.CommandManager;
import works.akus.mauris.objects.MaurisObjectManager;
import works.akus.mauris.resourcepack.ResourcePackManager;

public class Mauris extends JavaPlugin {

	static Mauris instance;

	MaurisObjectManager thingManager;
	CommandManager commandManager;

	ResourcePackManager resourcePackUpdater;

	public void onEnable() {
		instance = this;
		createConfig();

		// Managers Setup
		thingManager = new MaurisObjectManager();
		thingManager.setUp();

		commandManager = new CommandManager();
		commandManager.setUp();
		//

		if (isGithubTokenSet()) {
			resourcePackUpdater = new ResourcePackManager();
			resourcePackUpdater.setup();
		} else {
			getLogger().warning("GitHub token is not set, resource pack updater was not enabled.");
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();

		if (resourcePackUpdater != null) {
			resourcePackUpdater.stopServer();
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

	public MaurisObjectManager getThingManager() {
		return thingManager;
	}

	public static Mauris getInstance() {
		return instance;
	}
}
