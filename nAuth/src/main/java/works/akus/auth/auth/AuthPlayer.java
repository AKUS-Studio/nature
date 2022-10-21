package works.akus.auth.auth;

import org.bukkit.entity.Player;
import works.akus.auth.auth.discord.DiscordUser;

public class AuthPlayer {

    public AuthPlayer(DiscordUser user, Player player) {
        this.user = user;
        this.player = player;
    }

    DiscordUser user;
    Player player;

    public DiscordUser getUser() {
        return user;
    }

    public Player getPlayer() {
        return player;
    }
}
