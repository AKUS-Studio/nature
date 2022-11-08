package works.akus.world.camp;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class CampWorld {

    public CampWorld(JavaPlugin plugin){
        this.plugin = plugin;

        loadWorld();
    }

    public static final String CAMP_WORLD_NAME = "camp_world";
    public World campWorld;
    public JavaPlugin plugin;

    public CampListener listener;

    public void loadWorld(){
        campWorld = Bukkit.getWorld(CAMP_WORLD_NAME);

        listener = new CampListener(this);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public World getBukkitWorld(){
        return campWorld;
    }



}
