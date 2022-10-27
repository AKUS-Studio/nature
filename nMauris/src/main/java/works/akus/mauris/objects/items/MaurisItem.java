package works.akus.mauris.objects.items;

import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public abstract class MaurisItem  {

    public abstract ItemStackBuilder getBuilder();

    public void onInteract(PlayerInteractEvent e) {}

    public void onInteractAtEntity(PlayerInteractAtEntityEvent e) { }

    public void onDrop(PlayerDropItemEvent e) {}

    public void onSpawn(ItemSpawnEvent e) {}

    public void onInventoryClick(InventoryClickEvent e) {}

    public void onPickup(EntityPickupItemEvent e) {}

    public void onStartHolding(PlayerItemHeldEvent e) {}

    public void onEndHolding(PlayerItemHeldEvent e) {}


}
