package works.akus.mauris;

import org.bukkit.plugin.java.JavaPlugin;
import works.akus.mauris.commands.CommandManager;
import works.akus.mauris.resourcepack.ResourcePackUpdater;
import works.akus.mauris.things.ThingManager;

import java.io.File;

public class Mauris extends JavaPlugin {

	static Mauris instance;

	ThingManager thingManager;
	CommandManager commandManager;

	ResourcePackUpdater resourcePackUpdater;

	public void onEnable() {
		instance = this;
		createConfig();

		//Managers Setup
		thingManager = new ThingManager();
		thingManager.setUp();

		commandManager = new CommandManager();
		commandManager.setUp();
		//

		resourcePackUpdater = new ResourcePackUpdater();
		resourcePackUpdater.setUp();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		resourcePackUpdater.stop();
	}

	private void createConfig(){
		File config = new File(getDataFolder() + File.separator + "config.yml");
		if (!config.exists()) {
			getConfig().options().copyDefaults(true);
			saveDefaultConfig();
		}
	}

	public ThingManager getThingManager() {
		return thingManager;
	}

	public static Mauris getInstance() {
		return instance;
	}
}
