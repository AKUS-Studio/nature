package works.akus.mauris.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import works.akus.mauris.utils.ColorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManager implements TabExecutor {

    HashMap<String, MaurisCommand> commands = new HashMap<>();
    HashMap<String, MaurisCommand> commandsAliases = new HashMap<>();

    MaurisCommand helpCommand;

    public String getMain(){
        return "mauris";
    }

    public String getPrefix(){
        return ColorUtils.format("#b45ee6| Mauris > #cfa0eb");
    }

    public void setUp(){
        Bukkit.getPluginCommand(getMain()).setExecutor(this);

        //Help Command
        helpCommand = new HelpCommand();
        registerCommands(helpCommand);
        //

        registerCommands(new GiveCommand());
    }

    public void registerCommand(MaurisCommand command){
        commands.put(command.name(), command);

        for(String alias : command.aliases()) commandsAliases.put(alias, command);
    }

    public void registerCommands(MaurisCommand... commands){
        for(MaurisCommand command : commands) registerCommand(command);
    }

    public MaurisCommand getCommand(String commandName){
        commandName = commandName.toLowerCase();

        MaurisCommand mc = commands.get(commandName);
        if(mc == null) return commandsAliases.get(commandName);
        else return mc;
    }


    @Override
    public boolean onCommand(CommandSender sender,Command command,String s, String[] args) {

        //Not a subcommand
        if(args.length < 1){
            if(helpCommand != null) helpCommand.execute(sender, s, args);
            sender.sendMessage(getPrefix() + "Not enough arguments");
            return true;
        }

        //Get a MaurisCommand
        String subCommand = args[0];
        MaurisCommand mc = getCommand(subCommand);

        //MaurisCommand does not exist
        if(mc == null){
            sender.sendMessage(getPrefix() + "Can't find the " + subCommand + " command or alias");
            return true;
        }

        //Check on isOnlyPlayer
        if(mc.isOnlyPlayer() && !(sender instanceof Player)){
            sender.sendMessage(getPrefix() + "You can't run this command from console");
            return true;
        }

        //Check Permissions
        if(mc.needPermission() && !sender.hasPermission(mc.permission())){
            sender.sendMessage(getPrefix() + "Not enough permissions");
            return true;
        }

        mc.execute(sender, getPrefix(), args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length < 2) {
            List<String> subCommands = new ArrayList<>();
            for (MaurisCommand scommand : commands.values()) {
                subCommands.add(scommand.name());
            }
            return subCommands;
        }

        else{
            MaurisCommand mc = getCommand(args[0]);
            if(mc == null) return new ArrayList<>();
            List<String> tabCompleter = mc.tabComplete(args);

            return tabCompleter;
        }
    }

}
