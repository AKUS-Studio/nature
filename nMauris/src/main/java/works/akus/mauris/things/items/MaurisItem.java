package works.akus.mauris.things.items;

import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import works.akus.mauris.things.MaurisThing;

public abstract class MaurisItem extends MaurisThing {

    public abstract ItemStackBuilder getBuilder();

    public ItemStack getItemStack(){
        return getBuilder().addStringKeysData("maurisid", getId()).createItemStack();
    }

    public void onInteract(PlayerInteractEvent e) {}

    public void onInteractAtEntity(PlayerInteractAtEntityEvent e) { }

    public void onDrop(PlayerDropItemEvent e) {}

    public void onSpawn(ItemSpawnEvent e) {}

    public void onInventoryClick(InventoryClickEvent e) {}

    public void onPickup(EntityPickupItemEvent e) {}

    public void onStartHolding(PlayerItemHeldEvent e) {}

    public void onEndHolding(PlayerItemHeldEvent e) {}


}
