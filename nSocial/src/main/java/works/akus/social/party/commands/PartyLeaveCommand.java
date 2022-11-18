package works.akus.social.party.commands;

import org.bukkit.command.CommandSender;
import works.akus.social.commands.general.SubCommand;
import works.akus.social.general.SocialManager;
import works.akus.social.general.SocialPlayer;
import works.akus.social.party.Party;

import java.util.List;

public class PartyLeaveCommand implements SubCommand {
    @Override
    public boolean isOnlyPlayer() {
        return true;
    }

    @Override
    public String name() {
        return "leave";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {

        SocialPlayer spSender = SocialManager.getSocialPlayer(sender.getName());
        Party party = spSender.getParty();

        if(party == null){
            sender.sendMessage(prefix + "Вы не в пати");
            return;
        }

        sender.sendMessage(party.getMessagePrefixText() + "Вы вышли из пати");
        party.sendMessage(sender.getName() + " вышел из пати по своей воле", List.of(spSender));
        party.removePlayer(spSender);
    }

    @Override
    public List<String> aliases() {
        return List.of("l");
    }
}
