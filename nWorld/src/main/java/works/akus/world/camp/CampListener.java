package works.akus.world.camp;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class CampListener implements Listener {

    works.akus.world.World plugin;

    CampWorld campWorld;
    public CampListener(CampWorld world){
        this.campWorld = world;
        plugin = works.akus.world.World.get();
    }

    /**
     * HighPriority is necessary because if other event hides and shows people
     * after playerJoinWorld(); everything is going to be broken
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void playerJoinWorld(PlayerChangedWorldEvent e){
        World currentWorld = e.getPlayer().getWorld();
        World fromWorld = e.getFrom();

        if(currentWorld == campWorld.getBukkitWorld()){
            campWorld.joinWorld(e.getPlayer());
        }else if(fromWorld == campWorld.getBukkitWorld()){
            campWorld.leaveWorld(e.getPlayer());
        }

    }



}
