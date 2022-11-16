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
import works.akus.social.party.Party;
import works.akus.social.party.commands.PartyAcceptCommand;
import works.akus.social.party.commands.PartyInviteCommand;
import works.akus.social.party.commands.PartyLeaveCommand;
import works.akus.social.party.commands.PartyRemoveCommand;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class SocialManager {

    static HashMap<Player, SocialPlayer> socialPlayersLoaded = new HashMap<>();

    static CommandManager friendCommands;
    static CommandManager partyCommands;

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

        partyCommands = new CommandManager(Social.get(), "party");
        HelpCommand pHelpCommand = new HelpCommand(friendCommands);

        partyCommands.registerCommands(
                new PartyAcceptCommand(),
                new PartyInviteCommand(),
                new PartyRemoveCommand(),
                new PartyLeaveCommand(),
                pHelpCommand
        );

        SocialListener listener = new SocialListener();
        Bukkit.getPluginManager().registerEvents(listener, Social.get());
        Bukkit.getOnlinePlayers().forEach(SocialManager::registerPlayer);
    }

    public static void saveAll(){
        for(SocialPlayer sp : socialPlayersLoaded.values()){
            sp.save();
            return;
        }
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
        socialPlayersLoaded.remove(p);

        if(sp == null) {
            return;
        }

        sp.save();
        Party party = sp.getParty();
        if(party != null) {
            party.removePlayer(sp);
            party.sendMessage(sp.getName() + " вышел из пати, так как вышел с сервера");
        }
    }

    public static boolean isOnline(String name){
        Player p = Bukkit.getPlayer(name);
        return p != null && p.isOnline();
    }

    public static Collection<SocialPlayer> getSocialPlayers(){
        return socialPlayersLoaded.values();
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
