package works.akus.social;

import org.bukkit.plugin.java.JavaPlugin;
import works.akus.social.general.SocialManager;

public class Social extends JavaPlugin {

    private static Social instance;

    @Override
    public void onEnable() {
        instance = this;

        SocialManager.setUp();
    }

    public static Social get(){
        return instance;
    }

}
