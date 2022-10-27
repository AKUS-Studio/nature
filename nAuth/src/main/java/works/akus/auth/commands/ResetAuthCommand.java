package works.akus.auth.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import works.akus.auth.Auth;
import works.akus.auth.auth.AuthManager;
import works.akus.auth.auth.AuthPlayer;
import works.akus.auth.utils.ColorUtils;

import java.util.HashMap;

public class ResetAuthCommand implements CommandExecutor {

    AuthManager authManager;

    public static int RESET_COOLDOWN = 48; // In Hours

    public static final String COMMAND_NAME = "resetauth";
    private static final String RESETOTHERPEOPLE_PERMISSION = "nAuth.commands.resetauth_admin";

    private static final Component CONFIRMATION_MESSAGE = Component.text(ColorUtils.format("&c| Вы уверены, что хотите отвязать свой дискорд аккаунт?"))
            .append(Component.text(ColorUtils.format("&c| Если &aда&c, то пропишите команду: &e/" + COMMAND_NAME + " &cещё раз")));

    private static final Component CONFIRMATION_MESSAGE_OTHER = Component.text(ColorUtils.format("&c| Вы уверены, что хотите отвязать дискорд аккаунт игрока &e%player_name%&c?"))
            .append(Component.text(ColorUtils.format("&c| Если &aда&c, то пропишите команду: &e/" + COMMAND_NAME + " %player_name% &cещё раз")));

    private static final Component SUCCESS_MESSAGE  = Component.text(ColorUtils.format("&a| Аккаунт был отвязан успешно"));
    private static final Component ERROR_COOLDOWN_MESSAGE  = Component.text(ColorUtils.format("&c| Прошло слишком мало времени с привязки аккаунта, попробуйте позже"));
    private static final Component ERROR_ACCOUNT_MESSAGE  = Component.text(ColorUtils.format("&c| Такого аккаунта не существует"));
    private static final Component ERROR_MESSAGE  = Component.text(ColorUtils.format("&c| Ошибка"));


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        authManager = Auth.getInstance().getAuthManager();

        String senderName = sender.getName();

        if(args.length > 0 && sender.hasPermission(RESETOTHERPEOPLE_PERMISSION)){
            sendToOpConfirmation(sender, args[0]);
        }else if(args.length > 0){
            if(args[0].equals(senderName)) sendToConfirmation(sender, senderName);
        }
        else if(sender instanceof Player){
            sendToConfirmation(sender, senderName);
        }else{
            sender.sendMessage("Not a player");
            return true;
        }
        return true;
    }

    private void sendToOpConfirmation(CommandSender sender, String who){
        if(checkConfirmation(sender, who)) return;

        TextReplacementConfig config = TextReplacementConfig.builder().match("%player_name%")
                .replacement(who).build();

        if(!authManager.isPlayerAuth(who)){
            sender.sendMessage(ERROR_ACCOUNT_MESSAGE);
            return;
        }

        sender.sendMessage(CONFIRMATION_MESSAGE_OTHER.replaceText(config));

        confirmationHashMap.remove(sender);
        confirmationHashMap.put(sender, who);
    }

    private void sendToConfirmation(CommandSender sender, String who){
        if(checkConfirmation(sender, who)) return;

        AuthPlayer au = authManager.getAuthPlayer(who);
        long createdAt = au.getCreatedAt();

        if(!sender.hasPermission(RESETOTHERPEOPLE_PERMISSION) && System.currentTimeMillis() < createdAt + authManager.hoursIpAuthorizationLast * 60L * 60L * 1000L){
            sender.sendMessage(ERROR_COOLDOWN_MESSAGE);
            return;
        }

        sender.sendMessage(CONFIRMATION_MESSAGE);

        confirmationHashMap.remove(sender);
        confirmationHashMap.put(sender, who);
    }

    private boolean checkConfirmation(CommandSender sender, String who){
        String whoFromMap = confirmationHashMap.get(sender);
        if(whoFromMap != null && whoFromMap.equals(who)){
            resetPlayer(sender, who);
            confirmationHashMap.remove(sender);
            return true;
        }

        return false;
    }

    private HashMap<CommandSender, String> confirmationHashMap = new HashMap<>();

    private void resetPlayer(CommandSender sender, String player){
        AuthManager manager = Auth.getInstance().getAuthManager();
        boolean succ = manager.deleteAuthPlayer(player);
        if(succ){
            sender.sendMessage(SUCCESS_MESSAGE);
        }else{
            sender.sendMessage(ERROR_MESSAGE);
        }
    }

}
