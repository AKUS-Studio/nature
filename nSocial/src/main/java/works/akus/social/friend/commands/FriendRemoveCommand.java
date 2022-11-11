package works.akus.social.friend.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import works.akus.social.commands.general.SubCommand;
import works.akus.social.friend.Friend;
import works.akus.social.general.SocialManager;
import works.akus.social.general.SocialPlayer;

import java.util.ArrayList;
import java.util.List;

public class FriendRemoveCommand implements SubCommand {
    @Override
    public String name() {
        return "remove";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        Player p = (Player) sender;

        if(args.length < 2){
            p.sendMessage(prefix + "Нужно ввести имя того, кого вы хотите удалить из друзей");
            return;
        }

        String name = args[1];

        SocialPlayer sp = SocialManager.getSocialPlayer(p);
        Friend fr = sp.getAsFriend();

        if(!fr.getFriends().contains(name)){
            p.sendMessage(prefix + name + " Нету у вас в друзьях");
            return;
        }

        fr.removeFriend(name);
        p.sendMessage(prefix + "успешно удалил " + name + " из друзей");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) return new ArrayList<>();
        Player p = (Player) sender;

        SocialPlayer sp = SocialManager.getSocialPlayer(p);
        Friend fr = sp.getAsFriend();

        return fr.getFriends();
    }
}
