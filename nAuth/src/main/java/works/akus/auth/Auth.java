package works.akus.auth;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import works.akus.auth.auth.AuthManager;
import works.akus.auth.commands.ResetAuthCommand;
import works.akus.auth.utils.PrisonSystem;

public class Auth extends JavaPlugin {

    static Auth instance;

    AuthManager authManager;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getPluginCommand(ResetAuthCommand.COMMAND_NAME).setExecutor(new ResetAuthCommand());

        Bukkit.getPluginManager().registerEvents(new PrisonSystem(), this);

        authManager = new AuthManager();
        authManager.setUp();
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public static Auth getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        PrisonSystem.clearAll();
        authManager.stop();
    }
}