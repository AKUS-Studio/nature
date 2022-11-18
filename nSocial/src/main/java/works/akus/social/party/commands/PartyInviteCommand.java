package works.akus.social.party.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import works.akus.social.commands.general.SubCommand;
import works.akus.social.general.SocialManager;
import works.akus.social.general.SocialPlayer;
import works.akus.social.party.Party;

import java.util.ArrayList;
import java.util.List;

public class PartyInviteCommand implements SubCommand {
    @Override
    public boolean isOnlyPlayer() {
        return true;
    }

    @Override
    public String name() {
        return "invite";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {

        if(args.length < 2){
            sender.sendMessage(prefix + "Вы не указали кого пригласить");
            return;
        }
        String name = args[1];
        if(!SocialManager.isOnline(name)){
            sender.sendMessage(prefix + name + " оффлайн");
            return;
        }


        SocialPlayer toInvite = SocialManager.getSocialPlayer(name);

        Player p = (Player) sender;
        SocialPlayer sp = SocialManager.getSocialPlayer(p);
        Party party = sp.getParty();

        if(party == null){
            party = new Party(sp);
        }else {
            if(party.isOnlyOwnerCanInvitePlayers() && sp != party.getOwner()){
                sender.sendMessage(prefix + "Вы не создатель пати");
                return;
            }
        }


        party.invitePlayer(sp, toInvite);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {

        List<SocialPlayer> canJoin = new ArrayList<>();
        for(SocialPlayer sp : SocialManager.getSocialPlayers()){
            if(sp.getParty() == null) canJoin.add(sp);
        }

        List<String> names = new ArrayList<>();
        for (SocialPlayer sp : SocialManager.getSocialPlayers()){
            names.add(sp.getName());
        }
        names.remove(sender.getName());

        return names;
    }

    @Override
    public List<String> aliases() {
        return List.of("i", "inv");
    }
}
