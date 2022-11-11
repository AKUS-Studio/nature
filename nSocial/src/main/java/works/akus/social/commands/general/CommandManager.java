package works.akus.social.commands.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import works.akus.social.utils.ColorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManager implements TabExecutor {

    SubCommand defaultCommand;

    HashMap<String, SubCommand> commands = new HashMap<>();
    HashMap<String, SubCommand> commandsAliases = new HashMap<>();

    public static String getPrefix(){
        return ColorUtils.format("#b45ee6 > #cfa0eb");
    }

    public CommandManager(JavaPlugin plugin, String mainCommand){
        plugin.getCommand(mainCommand).setExecutor(this);
    }

    public void registerCommand(SubCommand command){
        commands.put(command.name(), command);

        for(String alias : command.aliases()) commandsAliases.put(alias, command);
    }

    public void registerCommands(SubCommand... subCommands){
        for(SubCommand command : subCommands) registerCommand(command);
    }

    public SubCommand getCommand(String commandName){
        commandName = commandName.toLowerCase();

        SubCommand sc = commands.get(commandName);
        if(sc == null) return commandsAliases.get(commandName);
        else return sc;
    }

    public void setDefaultCommand(SubCommand defaultCommand){
        this.defaultCommand = defaultCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        //Not a subcommand
        if(args.length < 1){
            if(defaultCommand != null) defaultCommand.execute(sender, s, args);
            sender.sendMessage(getPrefix() + "Недостаточно аргументов");
            return true;
        }

        //Get a SubCommand
        String subCommand = args[0];
        SubCommand sc = getCommand(subCommand);

        //SubCommand does not exist
        if(sc == null){
            sender.sendMessage(getPrefix() + "Не могу найти команду " + subCommand);
            return true;
        }

        //Check on isOnlyPlayer
        if(sc.isOnlyPlayer() && !(sender instanceof Player)){
            sender.sendMessage(getPrefix() + "Вы не можете проиграть эту команду с консоли");
            return true;
        }

        //Check Permissions
        if(sc.needPermission() && !sender.hasPermission(sc.permission())){
            sender.sendMessage(getPrefix() + "Недостаточно привелегий");
            return true;
        }

        sc.execute(sender, getPrefix(), args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            List<String> subCommands = new ArrayList<>();
            for (SubCommand scommand : commands.values()) {
                subCommands.add(scommand.name());
            }
            return subCommands;
        }

        else{
            SubCommand mc = getCommand(args[0]);
            if(mc == null) return new ArrayList<>();

            List<String> tabCompleter = mc.tabComplete(sender, args);

            //Tab Complete Matcher
            if(mc.tabCompleteMatcher()){
                String lastArg = args[args.length - 1];
                List<String> tabMatchList = new ArrayList<>();

                for(String tab : tabCompleter) if(tab.contains(lastArg)) tabMatchList.add(tab);

                tabCompleter = tabMatchList;
            }

            return tabCompleter;
        }
    }
}
