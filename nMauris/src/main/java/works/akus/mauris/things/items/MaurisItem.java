package works.akus.mauris.things.items;

import org.bukkit.inventory.ItemStack;
import works.akus.mauris.things.MaurisThing;

public abstract class MaurisItem extends MaurisThing {

    public abstract ItemStackBuilder getBuilder();

    public ItemStack getItemStack(){
        getBuilder().addStringKeysData("maurisid", getId());
        return getBuilder().createItemStack();
    }

}
