package works.akus.social.party.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import works.akus.social.commands.general.SubCommand;
import works.akus.social.general.SocialManager;
import works.akus.social.general.SocialPlayer;

import java.util.List;

public class PartyMessageCommand implements SubCommand {
    @Override
    public boolean isOnlyPlayer() {
        return SubCommand.super.isOnlyPlayer();
    }

    @Override
    public String name() {
        return "message";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {

        SocialPlayer spSender = SocialManager.getSocialPlayer(sender.getName());
        if (spSender.getParty() == null) {
            sender.sendMessage(prefix + "Вы не в пати");
            return;
        }

        if(args.length < 2){
            sender.sendMessage(prefix + "Напишите что-нибудь");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 1 ; i < args.length; i++){
            sb.append(args[i]).append(" ");
        }

        spSender.getParty().sendMessage(Component.text(spSender.getName() + " > ").color(spSender.getParty().getDisplayColor())
                .append(Component.text(sb.toString()).color(spSender.getParty().getDisplayColor(50))), false);
    }

    @Override
    public List<String> aliases() {
        return List.of("msg");
    }
}
