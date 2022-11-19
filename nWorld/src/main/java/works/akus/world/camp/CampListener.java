package works.akus.world.camp;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import works.akus.social.party.Party;
import works.akus.social.party.commands.PartyLeaveCommand;
import works.akus.social.party.events.PartyAddEvent;
import works.akus.social.party.events.PartyRemoveEvent;

public class CampListener implements Listener {

    works.akus.world.World plugin;

    CampWorld campWorld;
    public CampListener(CampWorld world){
        this.campWorld = world;
        plugin = works.akus.world.World.get();
    }

    @EventHandler
    public void playerLeaveParty(PartyRemoveEvent addEvent){
        campWorld.hideParty(addEvent.getPlayer().getPlayer(), addEvent.getParty());
    }

    @EventHandler
    public void playerJoinParty(PartyAddEvent addEvent){
        campWorld.showParty(addEvent.getPlayer().getPlayer(), addEvent.getParty());
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
