package works.akus.mauris.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import works.akus.mauris.registry.ItemRegistry;

public class GiveCommand implements MaurisCommand {
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

        if(!ItemRegistry.isRegistered(id)){
            player.sendMessage(prefix + "That is not a valid MaurisItem");
            return;
        }

        player.getInventory().addItem(ItemRegistry.getItemStack(id));
        player.sendMessage(prefix + "Successfully given " + id);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        List<String> ids = new ArrayList<>(ItemRegistry.getIdSet());
        return ids;
    }
}
