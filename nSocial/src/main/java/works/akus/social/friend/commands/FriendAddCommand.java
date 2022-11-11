package works.akus.social.friend.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import works.akus.social.commands.general.SubCommand;
import works.akus.social.friend.Friend;
import works.akus.social.general.SocialManager;
import works.akus.social.general.SocialPlayer;

import java.util.ArrayList;
import java.util.List;

public class FriendAddCommand implements SubCommand {

    @Override
    public boolean isOnlyPlayer() {
        return true;
    }

    @Override
    public String name() {
        return "add";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        Player p = (Player) sender;

        if(args.length < 2){
            p.sendMessage(prefix + "Нужно ввести имя того, кого вы хотите добавить в друзья");
            return;
        }

        SocialPlayer sp = SocialManager.getSocialPlayer(p);
        Friend fr = sp.getAsFriend();

        String name = args[1];

        if(name.equals(sender.getName())){
            p.sendMessage(prefix + " дурашка?");
            return;
        }

        if(fr.getFriends().contains(name)){
            p.sendMessage(prefix + name + " уже есть у вас в друзьях");
            return;
        }

        if(!SocialManager.isOnline(name)) {
            p.sendMessage(prefix + name + " оффлайн");
            return;
        }

        fr.sendFriendRequest(name);
        p.sendMessage(prefix + "Отправил запрос к " + name);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> listPlayer = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            listPlayer.add(p.getName());
        }

        return listPlayer;
    }

}
