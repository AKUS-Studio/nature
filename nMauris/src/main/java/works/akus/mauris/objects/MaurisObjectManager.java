package works.akus.mauris.objects;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import works.akus.mauris.Mauris;
import works.akus.mauris.objects.items.MaurisItem;
import works.akus.mauris.objects.items.TestItem;
import works.akus.mauris.objects.listeners.MaurisItemListener;

import java.util.Collection;
import java.util.HashMap;

public class MaurisObjectManager {

    protected HashMap<String, MaurisObject> thingsLoaded;

    public void setUp(){
        thingsLoaded = new HashMap<>();

        registerThing(new TestItem());
        Bukkit.getPluginManager().registerEvents(new MaurisItemListener(this), Mauris.getInstance());
    }

    public MaurisItem getMaurisItem(String id){
        MaurisObject thing = getObject(id);
        if(thing instanceof MaurisItem) return (MaurisItem) thing;

        return null;
    }

    /**
     * Gets MaurisItem from ItemStack's meta
     *
     * @param itemStack ItemStack, based on which meta will determine which is MaurisItem is that
     * @return MaurisItem
     */
    public MaurisItem getMaurisItem(ItemStack itemStack){
        if(itemStack == null) return null;
        ItemMeta meta = itemStack.getItemMeta();
        if(meta == null) return null;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        String value = container.get(new NamespacedKey(Mauris.getInstance(), "maurisid"), PersistentDataType.STRING);

        if(value == null) return null;

        return getMaurisItem(value);
    }

    /**
     * Gets MaurisObject from id
     *
     * @param id ID of the MaurisObject. Example: example_item
     * @return MaurisObject
     */
    public MaurisObject getObject(String id){
        id = id.toLowerCase();
        return thingsLoaded.get(id);
    }

    public Collection<MaurisObject> getAllThings(){
        return thingsLoaded.values();
    }

    //
    public void registerThings(MaurisObject... things){
        for(MaurisObject thing : things) registerThing(thing);
    }

    public void registerThing(MaurisObject thing){
        thingsLoaded.put(thing.getId().toLowerCase(), thing);
    }

    //
    public void unregisterThings(String... ids){
        for(String id : ids) unregisterThing(id);
    }

    public void unregisterThing(String id){
        id = id.toLowerCase();

        thingsLoaded.remove(id);
    }

    public void unregisterThings(MaurisObject... things){
        for(MaurisObject thing : things) unregisterThing(thing);
    }

    public void unregisterThing(MaurisObject thing){
        unregisterThing(thing.getId());
    }


}
