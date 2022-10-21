package works.akus.auth.utils;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import works.akus.auth.Auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrisonSystem implements Listener {

    private static HashMap<Player, LockedPlayer> lockedPlayers = new HashMap<>();

    public static void lockPlayer(Player player){
        LockedPlayer lp = new LockedPlayer(player);
        lockedPlayers.put(player, lp);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setGameMode(GameMode.SPECTATOR);

                ArmorStand stand = spawnArmorStand(player.getLocation());
                player.setSpectatorTarget(stand);
                lp.setStand(stand);
            }
        }.run();
    }

    public static void unlockPlayer(Player p){
        unlockPlayer(getLockedPlayer(p));
    }

    public static LockedPlayer getLockedPlayer(Player p){
        return lockedPlayers.get(p);
    }

    public static void unlockPlayer(LockedPlayer p){
        if(p == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                    p.getPlayer().setSpectatorTarget(null);
                    p.getStand().remove();
                    p.getPlayer().setGameMode(GameMode.SURVIVAL);
            }
        }.runTask(Auth.getInstance());

        lockedPlayers.remove(p.getPlayer());
    }

    private static ArmorStand spawnArmorStand(Location location){
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        stand.setInvisible(true);
        stand.setCollidable(false);
        stand.setGravity(false);
        stand.setCanMove(false);

        return stand;
    }

    public static void clearAll(){
        for(Player p : lockedPlayers.keySet()) unlockPlayer(p);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        unlockPlayer(e.getPlayer());
    }

    //Canceling Events
    @EventHandler
    public void onPlayerSneak(PlayerStopSpectatingEntityEvent e){
        if(lockedPlayers.containsKey(e.getPlayer())) e.setCancelled(true);
    }


}
