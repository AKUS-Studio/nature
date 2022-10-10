package works.akus.mauris.things.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.HashMap;

public class TestItem extends MaurisItem {

    @Override
    public ItemStackBuilder getBuilder() {
        ItemStackBuilder builder = new ItemStackBuilder();
        builder
                .setMaterial(Material.STICK)
                .setCustomModelData(3)
                .setDisplayName("&6Example Item")
                .addEnchant(Enchantment.KNOCKBACK, 10)
                .addLore("#36454fEverybody needs a little something")
                .addItemFlags(ItemFlag.HIDE_ENCHANTS);

        return builder;
    }

    @Override
    public String getId() {
        return "example";
    }

    //Testing Things


    //Deleting block
    @Override
    public void onInteract(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        e.getClickedBlock().setType(Material.AIR);
        e.getPlayer().getWorld().playSound(e.getPlayer(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);

        super.onInteract(e);
    }

    //Particles And Sound
    HashMap<Player, Long> itemsHoldingCooldown = new HashMap<>();
    int cooldown = 5;
    @Override
    public void onStartHolding(PlayerItemHeldEvent e) {
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


        super.onStartHolding(e);
    }

    @Override
    public void onEndHolding(PlayerItemHeldEvent e) {
        Location l = e.getPlayer().getLocation();
        l.getWorld().playSound(e.getPlayer(), Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 1, 1);

        super.onEndHolding(e);
    }
}
