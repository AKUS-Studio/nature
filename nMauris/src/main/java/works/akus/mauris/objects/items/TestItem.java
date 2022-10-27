package works.akus.mauris.objects.items;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class TestItem extends MaurisItem {

    @Override
    public void supplyBuilder(ItemStackBuilder builder) {
        builder
                .setMaterial(Material.STICK)
                .setCustomModelData(3)
                .setDisplayName("&6Example Item")
                .addEnchant(Enchantment.KNOCKBACK, 10)
                .addLore("#36454fEverybody needs a little something")
                .addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    //Deleting block
    @Override
    public void onInteract(MaurisItem maurisItem, ItemStack item, PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        e.getClickedBlock().setType(Material.AIR);
        e.getPlayer().getWorld().playSound(e.getPlayer(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
    }

    //Particles And Sound
    HashMap<Player, Long> itemsHoldingCooldown = new HashMap<>();
    int cooldown = 5;
    @Override
    public void onStartHolding(MaurisItem maurisItem, ItemStack item, PlayerItemHeldEvent e) {
        Location l = e.getPlayer().getLocation();

        //Cooldown Particles
        Long timestamp = itemsHoldingCooldown.get(e.getPlayer());
        if(timestamp != null){
            if(timestamp + cooldown * 1000L < System.currentTimeMillis()){
                l.getWorld().spawnParticle(Particle.FLAME, l, 100, 10, 10, 10);
                itemsHoldingCooldown.remove(e.getPlayer());
            }
        }else{
            itemsHoldingCooldown.put(e.getPlayer(), System.currentTimeMillis());
        }

        // Sound
        l.getWorld().playSound(e.getPlayer(), Sound.BLOCK_AMETHYST_BLOCK_FALL, 1, 1);
    }

    @Override
    public void onEndHolding(MaurisItem maurisItem, ItemStack item, PlayerItemHeldEvent e) {
        Location l = e.getPlayer().getLocation();
        l.getWorld().playSound(e.getPlayer(), Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 1, 1);
    }
}
