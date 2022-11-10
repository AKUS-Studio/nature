package works.akus.world.camp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CampCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //TEMP
        if(!sender.isOp()) return true;

        if(args.length > 0){
            String target = args[0];
            Player p = Bukkit.getPlayer(target);
            if(p == null) return true;

            CampWorld.teleportPlayerToCamp(p);

            sender.sendMessage("Camp | Successfully teleported " + target + " to the camp");

            return true;
        }

        if(!(sender instanceof Player)) sender.sendMessage("Camp | Can't teleport " + sender.getName());
        Player p = (Player) sender;
        CampWorld.teleportPlayerToCamp(p);

        return true;
    }

}
