package works.akus.social;

import org.bukkit.plugin.java.JavaPlugin;

public class Social extends JavaPlugin {

    private static Social instance;

    @Override
    public void onEnable() {
        instance = this;

    }

    public static Social get(){
        return instance;
    }

}
