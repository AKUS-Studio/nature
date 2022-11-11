package works.akus.social.general;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SocialListener implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        SocialManager.registerPlayer(p);
    }

    @EventHandler
    public void onLeaveEvent(PlayerQuitEvent e){
        Player p = e.getPlayer();
        SocialManager.unregisterPlayer(p);
    }

}
