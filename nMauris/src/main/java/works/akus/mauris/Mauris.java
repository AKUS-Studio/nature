package works.akus.mauris;

import org.bukkit.plugin.java.JavaPlugin;
import works.akus.mauris.commands.CommandManager;
import works.akus.mauris.things.ThingManager;

public class Mauris extends JavaPlugin {

	static Mauris instance;

	ThingManager thingManager;
	CommandManager commandManager;

	public void onEnable() {
		instance = this;

		thingManager = new ThingManager();
		thingManager.setUp();

		commandManager = new CommandManager();
		commandManager.setUp();
	}

	public ThingManager getThingManager() {
		return thingManager;
	}

	public static Mauris getInstance() {
		return instance;
	}
}
