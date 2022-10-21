package works.akus.auth.auth;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.ComponentBuilderApplicable;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;
import works.akus.auth.Auth;
import works.akus.auth.auth.discord.DiscordUser;
import works.akus.auth.auth.discord.OAuthDiscord;
import works.akus.auth.utils.PrisonSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AuthManager implements Listener {

    public final boolean AUTH_AFTER_RESOURCEPACK = true;

    private void clearChat(Player p){
        for(int i = 0 ; i < 10 ; i++){
            p.sendMessage(" ");
        }
    }

    private Component getPlayerAuthText(Player p){
        return
                Component.text("-------------------------------\n\n" +
                                "Для того чтобы зайти на сервер, вам нужно авторизоваться через Дискорд\n\n").append(

                                Component.text("Чтобы авторизоваться, нажмите здесь").decorate(TextDecoration.UNDERLINED).decorate(TextDecoration.BOLD)
                                        .clickEvent(ClickEvent.openUrl(oAuth.getAuthLink(p))))
                        .append(Component.text("\n \n \n --------------------------------"));
    }

    private Component getSuccessAuthMessage(DiscordUser user){
        return Component.text("Вы успешно вошли! " + user.getName() + "#" + user.getDiscriminator());
    }

    Auth auth;

    HashMap<Player, AuthPlayer> authorizedPlayers = new HashMap<>();
    List<Player> playersInAuth = new ArrayList<>();

    OAuthDiscord oAuth;
    public void setUp(){
        auth = Auth.getInstance();
        oAuth = new OAuthDiscord();

        new BukkitRunnable() {
            @Override
            public void run() {
                oAuth.setUp();
            }
        }.runTaskAsynchronously(auth);

        Bukkit.getPluginManager().registerEvents(this, auth);
        chatSpamAuthStart();
    }

    public void stop(){
        oAuth.stopServer();
    }

    public void authPlayer(Player p){
        if(playersInAuth.contains(p)) throw new RuntimeException("Tried to authorize player several times");

        new BukkitRunnable() {
            @Override
            public void run() {
                PrisonSystem.lockPlayer(p);
            }
        }.runTask(Auth.getInstance());

        playersInAuth.add(p);

        oAuth.addPending(p, (du) ->{
            cancelAuthPlayer(p);
            clearChat(p);
            p.sendMessage(getSuccessAuthMessage(du));
            authorizedPlayers.put(p, new AuthPlayer(du, p));
        });

    }

    public AuthPlayer getAuthPlayer(Player player){
        return authorizedPlayers.get(player);
    }

    public void cancelAuthPlayer(Player p){
        if(!playersInAuth.contains(p)) return;

        PrisonSystem.unlockPlayer(p);
        playersInAuth.remove(p);
    }

    public void chatSpamAuthStart(){
        new BukkitRunnable() {
            @Override
            public void run() {

                for(Player p : playersInAuth){
                    clearChat(p);
                    p.sendMessage(getPlayerAuthText(p));
                }

            }
        }.runTaskTimerAsynchronously(auth, 20, 20);
    }

    //Dot Listeners
    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();

        cancelAuthPlayer(p);
        if(getAuthPlayer(p) != null) authorizedPlayers.remove(p);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e){
        if(AUTH_AFTER_RESOURCEPACK) return;

        authPlayer(e.getPlayer());
    }

    @EventHandler
    private void onResourcePackDownloaded(PlayerResourcePackStatusEvent event){
        if(!AUTH_AFTER_RESOURCEPACK) return;

        PlayerResourcePackStatusEvent.Status status = event.getStatus();
        if(status != PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) return;

        authPlayer(event.getPlayer());
    }

}
