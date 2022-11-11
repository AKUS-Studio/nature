package works.akus.social.general;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import works.akus.social.Social;
import works.akus.social.friend.commands.FriendAcceptCommand;
import works.akus.social.friend.commands.FriendAddCommand;
import works.akus.social.friend.commands.FriendDenyCommand;
import works.akus.social.friend.commands.FriendRemoveCommand;
import works.akus.social.commands.HelpCommand;
import works.akus.social.commands.general.CommandManager;

import java.util.HashMap;

public class SocialManager {

    static HashMap<Player, SocialPlayer> socialPlayersLoaded = new HashMap<>();

    static CommandManager friendCommands;

    public static void setUp(){
        socialPlayersLoaded.clear();

        friendCommands = new CommandManager(Social.get(), "friend");
        HelpCommand helpCommand = new HelpCommand(friendCommands);

        friendCommands.registerCommands(
                new FriendAddCommand(),
                new FriendAcceptCommand(),
                new FriendRemoveCommand(),
                new FriendDenyCommand(),
                helpCommand
        );

        SocialListener listener = new SocialListener();
        Bukkit.getPluginManager().registerEvents(listener, Social.get());
    }

    public static void registerPlayer(Player p){
        SocialPlayer sp = getSocialPlayer(p);
        socialPlayersLoaded.put(p, sp);
    }

    private static void registerPlayer(Player p, SocialPlayer sp){
        socialPlayersLoaded.put(p, sp);
    }

    public static void unregisterPlayer(Player p){
        SocialPlayer sp = socialPlayersLoaded.get(p);
        if(sp != null) sp.save();

        socialPlayersLoaded.remove(p);
    }

    private static void unregisterPlayer(Player p, SocialPlayer sp){
        if(sp != null) sp.save();

        socialPlayersLoaded.remove(p);
    }

    public static boolean isOnline(String name){
        Player p = Bukkit.getPlayer(name);
        return p != null && p.isOnline();
    }

    public static SocialPlayer getSocialPlayer(Player p){
        return getSocialPlayer(p.getName());
    }

    public static SocialPlayer getSocialPlayer(String name){
        Player p = Bukkit.getPlayer(name);
        SocialPlayer sp = socialPlayersLoaded.get(p);
        if(sp != null) return sp;

        sp = SocialPlayer.load(name);
        if(isOnline(name)) registerPlayer(p, sp);
        return sp;
    }

}
