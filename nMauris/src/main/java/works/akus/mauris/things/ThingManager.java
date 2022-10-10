package works.akus.mauris.things;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import works.akus.mauris.Mauris;
import works.akus.mauris.things.items.TestItem;
import works.akus.mauris.things.items.MaurisItem;
import works.akus.mauris.things.listeners.MaurisItemListener;

import java.util.Collection;
import java.util.HashMap;

public class ThingManager {

    protected HashMap<String, MaurisThing> thingsLoaded;

    public void setUp(){
        thingsLoaded = new HashMap<>();

        registerThing(new TestItem());
        Bukkit.getPluginManager().registerEvents(new MaurisItemListener(this), Mauris.getInstance());
    }

    public MaurisItem getMaurisItem(String id){
        MaurisThing thing = getThing(id);
        if(thing instanceof MaurisItem) return (MaurisItem) thing;

        return null;
    }

    public MaurisItem getMaurisItem(ItemStack itemStack){
        if(itemStack == null) return null;
        ItemMeta meta = itemStack.getItemMeta();
        if(meta == null) return null;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        String value = container.get(new NamespacedKey(Mauris.getInstance(), "maurisid"), PersistentDataType.STRING);

        if(value == null) return null;

        return getMaurisItem(value);
    }

    public MaurisThing getThing(String id){
        id = id.toLowerCase();
        return thingsLoaded.get(id);
    }

    public Collection<MaurisThing> getAllThings(){
        return thingsLoaded.values();
    }

    //
    public void registerThings(MaurisThing... things){
        for(MaurisThing thing : things) registerThing(thing);
    }

    public void registerThing(MaurisThing thing){
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

    public void unregisterThings(MaurisThing... things){
        for(MaurisThing thing : things) unregisterThing(thing);
    }

    public void unregisterThing(MaurisThing thing){
        unregisterThing(thing.getId());
    }


}
