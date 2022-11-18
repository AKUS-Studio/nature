package works.akus.social.party.commands;

import org.bukkit.command.CommandSender;
import works.akus.social.commands.general.SubCommand;
import works.akus.social.general.SocialManager;
import works.akus.social.general.SocialPlayer;
import works.akus.social.party.Party;

import java.util.ArrayList;
import java.util.List;

public class PartyDenyCommand implements SubCommand {
    @Override
    public String name() {
        return "deny";
    }

    @Override
    public boolean isOnlyPlayer() {
        return true;
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        SocialPlayer spSender = SocialManager.getSocialPlayer(sender.getName());
        if(spSender.getPartyInvites().isEmpty()){
            sender.sendMessage(prefix + "У вас нет приглашений в пати");
            return;
        }

        if(args.length < 2){
            Party party = (Party) spSender.getPartyInvites().keySet().toArray()[spSender.getPartyInvites().size() - 1];

            SocialPlayer spTarget = spSender.getPartyInvites().get(party);

            party.deny(spSender);
            spTarget.getPlayer().sendMessage(party.getMessagePrefixText() + sender.getName() + " отклонил заявку в пати");
            sender.sendMessage(prefix + "Вы отклонили заявку в пати" );
            return;
        }

        String name = args[1];
        if(!getPlayersInvite(sender).contains(name)){
            sender.sendMessage(prefix + name + "Вас не приглашал в пати");
            return;
        }

        SocialPlayer spArg = SocialManager.getSocialPlayer(name);
        Party party = spArg.getParty();
        SocialPlayer spTarget = spSender.getPartyInvites().get(party);

        party.deny(spSender);
        spTarget.getPlayer().sendMessage(party.getMessagePrefixText() + sender.getName() + " отклонил заявку в пати");
        sender.sendMessage(prefix + "Вы отклонили заявку в пати" );
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
