package works.akus.mauris.objects.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import works.akus.mauris.objects.MaurisObjectManager;
import works.akus.mauris.objects.items.MaurisItem;

public class MaurisItemListener implements Listener {

    MaurisObjectManager manager;
    public MaurisItemListener(MaurisObjectManager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        ItemStack item = e.getItem();
        MaurisItem mitem = manager.getMaurisItem(item);
        if(mitem == null) return;

        mitem.onInteract(e);
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e){
        ItemStack item = e.getPlayer().getInventory().getItem(e.getHand());
        MaurisItem mitem = manager.getMaurisItem(item);
        if(mitem == null) return;

        mitem.onInteractAtEntity(e);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        ItemStack item = e.getItemDrop().getItemStack();
        MaurisItem mitem = manager.getMaurisItem(item);
        if(mitem == null) return;

        mitem.onDrop(e);
    }

    @EventHandler
    public void onSpawned(ItemSpawnEvent e){
        ItemStack item = e.getEntity().getItemStack();
        MaurisItem mitem = manager.getMaurisItem(item);
        if(mitem == null) return;

        mitem.onSpawn(e);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        ItemStack item = e.getCurrentItem();
        MaurisItem mitem = manager.getMaurisItem(item);
        if(mitem == null) return;

        mitem.onInventoryClick(e);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e){
        ItemStack item = e.getItem().getItemStack();
        MaurisItem mitem = manager.getMaurisItem(item);
        if(mitem == null) return;

        mitem.onPickup(e);
    }

    @EventHandler
    public void onStartHolding(PlayerItemHeldEvent e){
        ItemStack item = e.getPlayer().getInventory().getItem(e.getNewSlot());
        MaurisItem mitem = manager.getMaurisItem(item);
        if(mitem == null) return;

        mitem.onStartHolding(e);
    }

    @EventHandler
    public void onEndHolding(PlayerItemHeldEvent e){
        ItemStack item = e.getPlayer().getInventory().getItem(e.getPreviousSlot());
        MaurisItem mitem = manager.getMaurisItem(item);
        if(mitem == null) return;

        mitem.onEndHolding(e);
    }


}
