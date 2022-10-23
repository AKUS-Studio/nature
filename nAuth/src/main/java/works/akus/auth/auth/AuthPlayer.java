package works.akus.auth.auth;

import org.bukkit.entity.Player;
import works.akus.auth.auth.discord.DiscordUser;

import java.util.Date;

public class AuthPlayer {

    public AuthPlayer(DiscordUser user, Player player) {
        this.user = user;
        this.player = player;
    }

    public AuthPlayer(DiscordUser user, Player player, String lastIp, long lastLogged, long lastAuthorized) {
        this.user = user;
        this.player = player;
        this.lastIp = lastIp;
        this.lastLogged = lastLogged;
        this.lastAuthorized = lastAuthorized;
    }

    DiscordUser user;
    Player player;

    String lastIp;
    long lastLogged;
    long lastAuthorized;

    public void updateUser(DiscordUser user) {
        this.user = user;
    }

    public DiscordUser getUser() {
        return user;
    }

    public Player getPlayer() {
        return player;
    }

    public void setLastLogged(long lastLogged) {
        this.lastLogged = lastLogged;
    }

    public String getLastIp() {
        return lastIp;
    }

    public long getLastLogged() {
        return lastLogged;
    }

    public long getLastAuthorized() {
        return lastAuthorized;
    }
}
