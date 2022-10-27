package works.akus.mauris.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import works.akus.mauris.resourcepack.ResourcePackManager;
import works.akus.mauris.resourcepack.events.ResourcePackCompleteEvent;

import static works.akus.mauris.utils.ColorUtils.format;

public class ResourcePackListener implements Listener {


    private static final String SUCCESSFULLY_LOADED_MESSAGE = format("&aРесурспак успешно установлен!");
    private static final String DECLINE_KICK_MESSAGE = format("Ресурспак необходим для игры на сервере \n\n" +
            "Чтобы включить ресурспак: \n" +
            "&lСписок Серверов &r> &lВыбрать наш сервер &r> &lРедактировать &r> &lРесурспак Cервера: &a&lВКЛ");

    private static final String FAILED_TO_DOWNLOAD_KICK_MESSAGE = format("Произошла ошибка при скачивании ресурспака");

    private static final Component RESOURCEPACK_PROMPT = Component.text(format("&e- Чтобы играть на сервере вам нужно принять ресурспак -"));


    public ResourcePackListener(ResourcePackManager manager) {
        this.manager = manager;
    }

    ResourcePackManager manager;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.setResourcePack(manager.getResourcepackUrl(), manager.getResourcePackHash(), RESOURCEPACK_PROMPT);
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent e){

        PlayerResourcePackStatusEvent.Status status = e.getStatus();
        Player p = e.getPlayer();

        switch (status){
            case ACCEPTED: break;
            case SUCCESSFULLY_LOADED: {
                p.sendMessage(SUCCESSFULLY_LOADED_MESSAGE);

                ResourcePackCompleteEvent resourcePackCompleteEvent = new ResourcePackCompleteEvent(p, status, manager);
                Bukkit.getPluginManager().callEvent(resourcePackCompleteEvent);
                break;
            }
            case FAILED_DOWNLOAD: {
                p.kick(Component.text(FAILED_TO_DOWNLOAD_KICK_MESSAGE));
                break;
            }
            case DECLINED: {
                p.kick(Component.text(DECLINE_KICK_MESSAGE));
                break;
            }
        }

    }

}
