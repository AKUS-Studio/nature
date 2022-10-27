package works.akus.auth.auth;

import org.bukkit.entity.Player;
import works.akus.auth.auth.discord.DiscordUser;

public class AuthPlayer {

    public AuthPlayer(DiscordUser user, Player player, String lastIp, long lastLogged, long lastAuthorized, long createdAt) {
        this.user = user;
        this.player = player;
        this.minecraftUsername = player.getName();
        this.lastIp = lastIp;
        this.lastLogged = lastLogged;
        this.lastAuthorized = lastAuthorized;
        this.createdAt = createdAt;
    }

    public AuthPlayer(DiscordUser user, String player, String lastIp, long lastLogged, long lastAuthorized, long createdAt) {
        this.user = user;
        this.minecraftUsername = player;
        this.lastIp = lastIp;
        this.lastLogged = lastLogged;
        this.lastAuthorized = lastAuthorized;
        this.createdAt = createdAt;
    }

    public String getName() {
        return minecraftUsername;
    }

    boolean isAuthorized;

    DiscordUser user;
    Player player;
    String minecraftUsername;

    String lastIp;
    long lastLogged;
    long lastAuthorized;
    long createdAt;

    public void updateUser(DiscordUser user) {
        this.user = user;
    }

    public void setAuthorized(Player player) {
        this.player = player;
        this.isAuthorized = true;
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

    public long getCreatedAt() {
        return createdAt;
    }
}
