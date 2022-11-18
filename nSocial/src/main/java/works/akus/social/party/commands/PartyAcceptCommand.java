package works.akus.social.party.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import works.akus.social.commands.general.SubCommand;
import works.akus.social.general.SocialManager;
import works.akus.social.general.SocialPlayer;
import works.akus.social.party.Party;

import java.util.ArrayList;
import java.util.List;

public class PartyAcceptCommand implements SubCommand {
    @Override
    public boolean isOnlyPlayer() {
        return true;
    }

    @Override
    public String name() {
        return "accept";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {

        SocialPlayer spSender = SocialManager.getSocialPlayer(sender.getName());
        if(spSender.getPartyInvites().isEmpty()){
            sender.sendMessage(prefix + "У вас нет приглашений в пати");
            return;
        }

        if(args.length < 2){
            Party party = (Party) spSender.getPartyInvites().keySet().toArray()[0];

            party.sendMessage(sender.getName() + " вступил в пати!");
            party.accept(spSender);
            sender.sendMessage(party.getMessagePrefixText() + "Вы вступили в пати");
            return;
        }

        String name = args[1];
        if(!getPlayersInvite(sender).contains(name)){
            sender.sendMessage(prefix + name + "Вас не приглашал в пати");
            return;
        }

        SocialPlayer spArg = SocialManager.getSocialPlayer(name);
        Party party = spArg.getParty();

        party.sendMessage(sender.getName() + " вступил в пати!");
        party.accept(spArg);
        sender.sendMessage(party.getMessagePrefixText() + "Вы вступили в пати");
    }

    public List<String> getPlayersInvite(CommandSender sender){
        SocialPlayer sp = SocialManager.getSocialPlayer(sender.getName());

        List<String> namesThatInvite = new ArrayList<>();
        for(SocialPlayer spinvite : sp.getPartyInvites().values()){
            namesThatInvite.add(spinvite.getName());
        }

        return namesThatInvite;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return getPlayersInvite(sender);
    }
}
