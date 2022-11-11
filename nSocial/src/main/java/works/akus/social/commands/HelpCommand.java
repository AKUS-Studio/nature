package works.akus.social.commands;

import org.bukkit.command.CommandSender;
import works.akus.social.commands.general.CommandManager;
import works.akus.social.commands.general.SubCommand;

public class HelpCommand implements SubCommand {

    public HelpCommand(CommandManager manager){
        this.manager = manager;
    }

    CommandManager manager;

    // TODO
    @Override
    public String name() {
        return "help";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {

    }
}
