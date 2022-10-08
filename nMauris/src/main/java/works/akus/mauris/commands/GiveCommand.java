package works.akus.mauris.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import works.akus.mauris.Mauris;
import works.akus.mauris.managers.ThingManager;
import works.akus.mauris.things.MaurisThing;
import works.akus.mauris.things.items.MaurisItem;

import java.util.ArrayList;
import java.util.List;

public class GiveCommand implements MaurisCommand{
    @Override
    public String name() {
        return "give";
    }

    @Override
    public List<String> aliases() {
        return List.of("g");
    }

    @Override
    public boolean isOnlyPlayer() {
        return true;
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        Player player = (Player) sender;

        if(args.length < 2){
            player.sendMessage(prefix + "Please provide an id of a MaurisItem");
            return;
        }
        String id = args[1];

        ThingManager thingManager = Mauris.getInstance().getThingManager();
        MaurisItem item = thingManager.getMaurisItem(id);
        if(item == null){
            player.sendMessage(prefix + "That is not a valid MaurisItem");
            return;
        }

        player.getInventory().addItem(item.getItemStack());
        player.sendMessage(prefix + "Successfully given " + id);
    }

    @Override
    public List<String> tabComplete(String[] args) {

        String currentArg = args[args.length - 1];
        ThingManager thingManager = Mauris.getInstance().getThingManager();

        List<String> ids = new ArrayList<>();
        for(MaurisThing thing : thingManager.getAllThings()){
            if(thing instanceof MaurisItem && thing.getId().contains(currentArg)) ids.add(thing.getId());
        }

        return ids;
    }
}
