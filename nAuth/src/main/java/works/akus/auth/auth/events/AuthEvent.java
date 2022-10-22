package works.akus.auth.auth.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import works.akus.auth.auth.AuthPlayer;
import works.akus.auth.auth.discord.DiscordUser;

public class AuthEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    //
    AuthPlayer authPlayer;
    Player player;
    DiscordUser discordUser;

    public AuthEvent(AuthPlayer authplayer){
        this.authPlayer = authplayer;
        this.player = authplayer.getPlayer();
        this.discordUser = authplayer.getUser();
    }

    public AuthPlayer getAuthPlayer() {
        return authPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public DiscordUser getDiscordUser() {
        return discordUser;
    }

    boolean cancelled;
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }



}
