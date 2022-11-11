package works.akus.social.friend.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import works.akus.social.commands.general.SubCommand;
import works.akus.social.friend.Friend;
import works.akus.social.friend.FriendRequest;
import works.akus.social.general.SocialManager;
import works.akus.social.general.SocialPlayer;

import java.util.ArrayList;
import java.util.List;

public class FriendDenyCommand implements SubCommand {
    @Override
    public String name() {
        return "deny";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        Player p = (Player) sender;

        SocialPlayer sp = SocialManager.getSocialPlayer(p);
        Friend f = sp.getAsFriend();

        if(f.getFriendRequests().isEmpty()){
            p.sendMessage(prefix + "У вас нет заявок");
            return;
        }

        if(args.length < 2) {
            FriendRequest fr = f.getLastRequest();
            String from = fr.getOwnerFriend().getName();
            f.denyLastFriendRequest();

            p.sendMessage(prefix + "Вы отклонили заявку " + from + " о добавлении в друзья");
            fr.getOwnerPlayer().sendMessage(prefix + "Ваша заявка в друзья " + sender.getName() + " была отклонена");
        }else{
            String name = args[1];

            FriendRequest fr = f.getFriendRequest(name);

            if(fr == null) {
                p.sendMessage(prefix + "Такой заявки нет");
                return;
            }

            String from = fr.getOwnerFriend().getName();

            f.denyFriendRequest(from);

            p.sendMessage(prefix + "Вы отклонили заявку " + from + " о добавлении в друзья");
            fr.getOwnerPlayer().sendMessage(prefix + "Ваша заявка в друзья " + sender.getName() + " была отклонена");
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return new ArrayList<>();
        Player p = (Player) sender;

        SocialPlayer sp = SocialManager.getSocialPlayer(p);
        Friend fr = sp.getAsFriend();

        return new ArrayList<>(fr.getFriendRequests().keySet());
    }
}
