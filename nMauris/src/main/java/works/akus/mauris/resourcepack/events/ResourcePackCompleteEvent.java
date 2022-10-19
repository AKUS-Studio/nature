package works.akus.mauris.resourcepack.events;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import works.akus.mauris.resourcepack.ResourcePackManager;

public class ResourcePackCompleteEvent extends PlayerResourcePackStatusEvent {

    ResourcePackManager resourcePackManager;

    public ResourcePackCompleteEvent(Player who, Status resourcePackStatus, ResourcePackManager manager) {
        super(who, resourcePackStatus);
        this.resourcePackManager = manager;
    }

    public ResourcePackManager getResourcePackManager() {
        return resourcePackManager;
    }
}
