package works.akus.mauris.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import works.akus.mauris.objects.items.MaurisItem;
import works.akus.mauris.registry.ItemRegistry;

public class MaurisItemListener implements Listener {


    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        ItemStack item = e.getItem();
        MaurisItem maurisItem = ItemRegistry.getMaurisItem(item);
        if(maurisItem == null) return;

        maurisItem.onInteract(maurisItem, item, e);
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e){
        ItemStack item = e.getPlayer().getInventory().getItem(e.getHand());
        MaurisItem maurisItem = ItemRegistry.getMaurisItem(item);
        if(maurisItem == null) return;

        maurisItem.onInteractAtEntity(maurisItem, item, e);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        ItemStack item = e.getItemDrop().getItemStack();
        MaurisItem maurisItem = ItemRegistry.getMaurisItem(item);
        if(maurisItem == null) return;

        maurisItem.onDrop(maurisItem, item, e);
    }

    @EventHandler
    public void onSpawned(ItemSpawnEvent e){
        ItemStack item = e.getEntity().getItemStack();
        MaurisItem maurisItem = ItemRegistry.getMaurisItem(item);
        if(maurisItem == null) return;

        maurisItem.onSpawn(maurisItem, item, e);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        ItemStack item = e.getCurrentItem();
        MaurisItem maurisItem = ItemRegistry.getMaurisItem(item);
        if(maurisItem == null) return;

        maurisItem.onInventoryClick(maurisItem, item, e);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e){
        ItemStack item = e.getItem().getItemStack();
        MaurisItem maurisItem = ItemRegistry.getMaurisItem(item);
        if(maurisItem == null) return;

        maurisItem.onPickup(maurisItem, item, e);
    }

    @EventHandler
    public void onStartHolding(PlayerItemHeldEvent e){
        ItemStack item = e.getPlayer().getInventory().getItem(e.getNewSlot());
        MaurisItem maurisItem = ItemRegistry.getMaurisItem(item);
        if(maurisItem == null) return;

        maurisItem.onStartHolding(maurisItem, item, e);
    }

    @EventHandler
    public void onEndHolding(PlayerItemHeldEvent e){
        ItemStack item = e.getPlayer().getInventory().getItem(e.getPreviousSlot());
        MaurisItem maurisItem = ItemRegistry.getMaurisItem(item);
        if(maurisItem == null) return;

        maurisItem.onEndHolding(maurisItem, item, e);
    }


}
