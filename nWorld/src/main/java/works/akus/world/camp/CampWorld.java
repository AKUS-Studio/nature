package works.akus.world.camp;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import works.akus.social.general.SocialManager;
import works.akus.social.general.SocialPlayer;
import works.akus.social.party.Party;

import java.util.ArrayList;
import java.util.List;

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
        hideEveryoneAndHimself(p, getPlayerListExceptParty(p));
        if(!p.isOp()) p.setGameMode(GameMode.ADVENTURE);
    }

    public void leaveWorld(Player p){
        showEveryoneAndHimself(p, getPlayerListExceptParty(p));
    }

    private List<Player> getPlayerListExceptParty(Player p){
        SocialPlayer sp = SocialManager.getSocialPlayer(p);
        Party party = sp.getParty();

        List<Player> playersInParty = party.getBukkitPlayers();

        List<Player> endList = new ArrayList<>();
        for(Player pl1 : campWorld.getPlayers()){
            if(playersInParty.contains(pl1)) continue;
            endList.add(pl1);
        }

        return endList;
    }

    public static void teleportPlayerToCamp(Player p){
        Location l = campWorld.getSpawnLocation();
        p.teleportAsync(l);
    }


    /**
     * Shows every player from the CampWorld and Himself to the others
     */
    private void showEveryoneAndHimself(Player p, List<Player> players){
        for(Player otherPlayer : players){
            if(p == otherPlayer) continue;

            p.showPlayer(plugin, otherPlayer);
            otherPlayer.showPlayer(plugin, p);
        }
    }

    /**
     * Shows every player from the Party and Himself to the others
     */
    public void showParty(Player p, Party party){
        showEveryoneAndHimself(p, party.getBukkitPlayers());
    }

    /**
     * Hides every player from the Party and Himself to the others
     */
    public void hideParty(Player p, Party party){
        hideEveryoneAndHimself(p, party.getBukkitPlayers());
    }

    /**
     * Hides every player from the CampWorld and Himself to the others
     */
    private void hideEveryoneAndHimself(Player p, List<Player> players){
        for(Player otherPlayer : players){
            if(p == otherPlayer) continue;

            p.hidePlayer(plugin, otherPlayer);
            otherPlayer.hidePlayer(plugin, p);
        }
    }

    public World getBukkitWorld(){
        return campWorld;
    }



}
