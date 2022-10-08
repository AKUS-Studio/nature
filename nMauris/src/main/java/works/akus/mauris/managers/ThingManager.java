package works.akus.mauris.managers;

import works.akus.mauris.things.MaurisThing;
import works.akus.mauris.things.items.ExampleItem;
import works.akus.mauris.things.items.MaurisItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ThingManager {

    protected HashMap<String, MaurisThing> thingsLoaded;

    public void setUp(){
        thingsLoaded = new HashMap<>();

        registerThing(new ExampleItem());
    }

    public MaurisItem getMaurisItem(String id){
        MaurisThing thing = getThing(id);
        if(thing instanceof MaurisItem) return (MaurisItem) thing;

        return null;
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
