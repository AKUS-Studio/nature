package works.akus.social;

import org.bukkit.plugin.java.JavaPlugin;
import works.akus.social.general.SocialManager;
import works.akus.social.party.namegenerator.NameGenerator;

public class Social extends JavaPlugin {

    private static Social instance;

    @Override
    public void onEnable() {
        instance = this;

        NameGenerator.load();
        SocialManager.setUp();
    }

    @Override
    public void onDisable() {
        SocialManager.saveAll();
    }

    public static Social get(){
        return instance;
    }

}
