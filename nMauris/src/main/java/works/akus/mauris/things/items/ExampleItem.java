package works.akus.mauris.things.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class ExampleItem extends MaurisItem {

    @Override
    public ItemStackBuilder getBuilder() {
        ItemStackBuilder builder = new ItemStackBuilder();
        builder
                .setMaterial(Material.STICK)
                .setCustomModelData(3)
                .setDisplayName("Example Item")
                .addEnchant(Enchantment.KNOCKBACK, 10)
                .addLore("Everybody needs a little something")
                .addItemFlags(ItemFlag.HIDE_ENCHANTS);

        return builder;
    }

    @Override
    public String getId() {
        return "example";
    }

}
