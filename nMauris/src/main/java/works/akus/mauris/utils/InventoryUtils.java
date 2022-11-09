package works.akus.mauris.utils;

import org.bukkit.entity.Player;

import net.kyori.adventure.text.TextComponent;
import works.akus.mauris.Mauris;

public class InventoryUtils {

	public static void setPlayerInventoryTitle(Player player, TextComponent newTitle) {
		Mauris.getInstance().getInventoryHandler().setPlayerInventoryTitle(player, newTitle);
	}

}
