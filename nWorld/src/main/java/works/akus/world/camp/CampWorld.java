package works.akus.world.camp;

import io.papermc.paper.entity.RelativeTeleportFlag;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CampWorld {

    public CampWorld(JavaPlugin plugin){
        this.plugin = plugin;

        loadAll();
    }

    public static final String CAMP_WORLD_NAME = "camp_world";
    public static World campWorld;
    public JavaPlugin plugin;

    public CampListener listener;

    public void loadAll(){
        loadWorld();

        listener = new CampListener(this);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        Bukkit.getPluginCommand("camp").setExecutor(new CampCommand());
    }

    private void loadWorld(){
        boolean createdBefore = Bukkit.getWorld(CAMP_WORLD_NAME) != null;

        WorldCreator wc = new WorldCreator(CAMP_WORLD_NAME);
        wc.type(WorldType.FLAT);
        wc.generateStructures(false);
        wc.generatorSettings("{\"layers\": []}");

        campWorld = wc.createWorld();

        if(!createdBefore){
            Location spawnLocation = new Location(campWorld, 0, 64, 0);
            campWorld.setSpawnLocation(spawnLocation);
            campWorld.setBlockData(spawnLocation.add(0,-2,0), Material.STONE.createBlockData());

            campWorld.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
            campWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            campWorld.setGameRule(GameRule.FALL_DAMAGE, false);
            campWorld.setGameRule(GameRule.DROWNING_DAMAGE, false);
            campWorld.setGameRule(GameRule.FIRE_DAMAGE, false);
            campWorld.setGameRule(GameRule.FREEZE_DAMAGE, false);
            campWorld.setDifficulty(Difficulty.PEACEFUL);
            campWorld.setPVP(false);
        }
    }

    public void joinWorld(Player p){
        hideEveryoneAndHimself(p);
    }

    public void leaveWorld(Player p){
        showEveryoneAndHimself(p);
    }

    public static void teleportPlayerToCamp(Player p){
        Location l = campWorld.getSpawnLocation();
        p.teleportAsync(l);
    }


    /**
     * Shows every player from the CampWorld and Himself to the others
     */
    private void showEveryoneAndHimself(Player p){
        for(Player otherPlayer : campWorld.getPlayers()){
            p.showPlayer(plugin, otherPlayer);
            otherPlayer.showPlayer(plugin, p);
        }
    }

    /**
     * Hides every player from the CampWorld and Himself to the others
     */
    private void hideEveryoneAndHimself(Player p){
        for(Player otherPlayer : campWorld.getPlayers()){
            p.hidePlayer(plugin, otherPlayer);
            otherPlayer.hidePlayer(plugin, p);
        }
    }

    public World getBukkitWorld(){
        return campWorld;
    }



}
