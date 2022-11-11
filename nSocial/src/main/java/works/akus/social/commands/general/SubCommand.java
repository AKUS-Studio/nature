package works.akus.social.commands.general;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public interface SubCommand {

    default boolean isOnlyPlayer() {
        return false;
    }

    default boolean needPermission() {
        return true;
    }

    default boolean tabCompleteMatcher() {return true; }

    String name();
    default String permission(){
        return "Mauris.command." + name();
    }

    void execute(CommandSender sender, String prefix, String[] args);

    default List<String> tabComplete(CommandSender sender, String[] args){
        return new ArrayList<>();
    }

    default List<String> aliases() {
        return new ArrayList<>();
    }
}
