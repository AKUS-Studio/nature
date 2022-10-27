package works.akus.mauris.objects.items;

import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public abstract class MaurisItem  {

    public abstract ItemStackBuilder getBuilder();

    public void onInteract(MaurisItem maurisItem, ItemStack item, PlayerInteractEvent e) {}

    public void onInteractAtEntity(MaurisItem maurisItem, ItemStack item, PlayerInteractAtEntityEvent e) { }

    public void onDrop(MaurisItem maurisItem, ItemStack item, PlayerDropItemEvent e) {}

    public void onSpawn(MaurisItem maurisItem, ItemStack item, ItemSpawnEvent e) {}

    public void onInventoryClick(MaurisItem maurisItem, ItemStack item, InventoryClickEvent e) {}

    public void onPickup(MaurisItem maurisItem, ItemStack item, EntityPickupItemEvent e) {}

    public void onStartHolding(MaurisItem maurisItem, ItemStack item, PlayerItemHeldEvent e) {}

    public void onEndHolding(MaurisItem maurisItem, ItemStack item, PlayerItemHeldEvent e) {}


}
