package works.akus.mauris.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public interface MaurisCommand {

    default boolean isOnlyPlayer() {
        return false;
    }

    default boolean needPermission () {
        return true;
    }

    String name();
    default String permission(){
        return "Mauris.command." + name();
    }

    void execute(CommandSender sender, String prefix, String[] args);

    default List<String> tabComplete(String[] args){
        return new ArrayList<>();
    }

    default List<String> aliases() {
        return new ArrayList<>();
    }

}
