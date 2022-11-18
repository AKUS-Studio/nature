package works.akus.social.party.commands;

import org.bukkit.command.CommandSender;
import works.akus.social.commands.general.SubCommand;
import works.akus.social.general.SocialManager;
import works.akus.social.general.SocialPlayer;
import works.akus.social.party.Party;

import java.util.ArrayList;
import java.util.List;

public class PartyRemoveCommand implements SubCommand {
    @Override
    public String name() {
        return "remove";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {

        if(args.length < 2){
            sender.sendMessage(prefix + "Вы не указали кого удалить");
            return;
        }

        SocialPlayer spSender = SocialManager.getSocialPlayer(sender.getName());

        if (spSender.getParty() == null) {
            sender.sendMessage(prefix + "Вы не в пати");
            return;
        }

        Party party = spSender.getParty();
        if(spSender != party.getOwner()){
            sender.sendMessage(prefix + "Вы не создатель пати");
            return;
        }

        String name = args[1];
        if(!canRemove(sender).contains(name)){
            sender.sendMessage(prefix + "Такого игрока нет в вашем пати");
            return;
        }

        SocialPlayer spTarget = SocialManager.getSocialPlayer(name);
        party.sendMessage(name + " успешно удалён из вашего пати");
        party.removePlayer(spTarget);
    }

    public List<String> canRemove(CommandSender sender){
        SocialPlayer spSender = SocialManager.getSocialPlayer(sender.getName());

        List<String> names = new ArrayList<>();
        Party party = spSender.getParty();
        if(party == null) return names;

        for(SocialPlayer sp : party.getPlayers()){
            if(party.getOwner() == sp) continue;
            names.add(sp.getName());
        }

        return names;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return canRemove(sender);
    }
}
