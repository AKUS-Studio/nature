package works.akus.mauris.commands;

import org.bukkit.command.CommandSender;

public class HelpCommand implements MaurisCommand {

    //TODO

    @Override
    public String name() {
        return "help";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        sender.sendMessage(prefix + "bruh");
    }
}
