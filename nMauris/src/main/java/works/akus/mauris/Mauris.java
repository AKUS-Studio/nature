package works.akus.mauris;

import org.bukkit.plugin.java.JavaPlugin;
import works.akus.mauris.commands.CommandManager;
import works.akus.mauris.objects.MaurisObjectManager;
import works.akus.mauris.resourcepack.ResourcePackManager;

import java.io.File;

public class Mauris extends JavaPlugin {

	static Mauris instance;

	MaurisObjectManager thingManager;
	CommandManager commandManager;

	ResourcePackManager resourcePackUpdater;

	public void onEnable() {
		instance = this;
		createConfig();

		//Managers Setup
		thingManager = new MaurisObjectManager();
		thingManager.setUp();

		commandManager = new CommandManager();
		commandManager.setUp();
		//

		resourcePackUpdater = new ResourcePackManager();
		resourcePackUpdater.setup();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		resourcePackUpdater.stopServer();
	}

	private void createConfig(){
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
