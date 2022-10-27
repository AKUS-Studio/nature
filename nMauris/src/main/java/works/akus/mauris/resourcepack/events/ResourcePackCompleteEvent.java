package works.akus.mauris.resourcepack.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import works.akus.mauris.resourcepack.ResourcePackManager;

public class ResourcePackCompleteEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    ResourcePackManager resourcePackManager;

    Player who;

    public ResourcePackCompleteEvent(Player who, ResourcePackManager manager) {
        this.resourcePackManager = manager;
        this.who = who;
    }

    public Player getWho() {
        return who;
    }

    public ResourcePackManager getResourcePackManager() {
        return resourcePackManager;
    }
}
