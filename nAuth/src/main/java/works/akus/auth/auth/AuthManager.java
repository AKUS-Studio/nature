package works.akus.auth.auth;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.yaml.snakeyaml.tokens.Token;
import works.akus.auth.Auth;
import works.akus.auth.auth.discord.DiscordAPI;
import works.akus.auth.auth.discord.DiscordUser;
import works.akus.auth.auth.discord.OAuthDiscord;
import works.akus.auth.auth.discord.TokenInfo;
import works.akus.auth.auth.events.AuthEvent;
import works.akus.auth.db.AuthDatabase;
import works.akus.auth.utils.PrisonSystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AuthManager implements Listener {

    public final boolean AUTH_AFTER_RESOURCEPACK = true;

    public final boolean IpAuthorization = true;
    public final int hoursIpAuthorizationLast = 48;

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

    //Cache
    HashMap<Player, AuthPlayer> authorizedPlayers = new HashMap<>();
    List<Player> playersInAuth = new ArrayList<>();
    //

    AuthDatabase authDatabase;
    OAuthDiscord oAuth;
    public void setUp(){
        auth = Auth.getInstance();
        authDatabase = new AuthDatabase();
        authDatabase.setUp();

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


        String ip = p.getAddress().getAddress().getHostAddress();

        //Checking Database
        AuthPlayer authDatabasePlayer = authDatabase.loadAuthPlayer(p);
        String discord_id = null;

        long timestamp = new Date().getTime();
        if(authDatabasePlayer != null) {
            long lastAuthorized = authDatabasePlayer.getLastAuthorized();
            String storedIp = authDatabasePlayer.getLastIp();
            discord_id = authDatabasePlayer.getUser().getId();

            TokenInfo storedToken = authDatabasePlayer.getUser().getTokenInfo();

            //Auto IP Logging
            boolean ipAuth = IpAuthorization && storedIp.equals(ip) && lastAuthorized + hoursIpAuthorizationLast * 60L * 60L * 1000L > timestamp;

            if (ipAuth) {
                DiscordUser user = DiscordAPI.getDiscordUserByToken(storedToken);
                authDatabasePlayer.updateUser(user);
                authDatabasePlayer.setLastLogged(timestamp);
                authDatabase.saveAuthPlayer(authDatabasePlayer);

                cancelAuthPlayer(p);

                clearChat(p);
                p.sendMessage(getSuccessAuthMessage(user));
                return;
            }
        }

        playersInAuth.add(p);
        //Adding Pending Auth Player and when authorized do lambda
        String storedDiscord_id = discord_id;
        oAuth.addPending(p, (du) ->{
            if(storedDiscord_id != null && !du.getId().equals(storedDiscord_id)){
                cancelAuthPlayer(p);
                p.kick(Component.text("Ваш майнкрафт никнейм привязан к другому дискорд аккаунту"));
                return;
            }

            String minecraftUsernameByDiscordId = authDatabase.getMinecraftUsername(du.getId());
            if(storedDiscord_id == null && minecraftUsernameByDiscordId != null){
                p.kick(Component.text("Ваш дискорд аккаунт уже зарегестрирован на другой майнкрафт аккаунт"));
                return;
            }


            AuthPlayer au = new AuthPlayer(du, p, ip, timestamp, timestamp);
            AuthEvent event = new AuthEvent(au);

            Bukkit.getPluginManager().callEvent(event);
            if(event.isCancelled()) return;

            authDatabase.saveAuthPlayer(au);
            authDatabase.saveToken(du.getId(), du.getTokenInfo());

            cancelAuthPlayer(p);

            clearChat(p);
            p.sendMessage(getSuccessAuthMessage(du));

            authorizedPlayers.put(p, au);
        });

    }

    public void cancelAuthPlayer(Player p){
        PrisonSystem.unlockPlayer(p);

        if(!playersInAuth.contains(p)) return;
        playersInAuth.remove(p);
    }

    public AuthPlayer getAuthPlayer(Player player){
        return authorizedPlayers.get(player);
    }

    //Spamming Authentication message
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
