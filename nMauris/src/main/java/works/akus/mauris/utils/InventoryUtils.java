package works.akus.mauris.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import net.kyori.adventure.text.TextComponent;
import works.akus.mauris.Mauris;

public class InventoryUtils {

	public static List<Integer> getAllSlotsInArea(int firstSlot, int secondSlot) {
		List<Integer> results = new ArrayList<>();

		int max = Math.max(firstSlot, secondSlot);
		int min = Math.min(firstSlot, secondSlot);

		int startY = (max / 9);
		int endY = (min / 9);
		int startX = (max - (startY * 9));
		int endX = (min - (endY * 9));

		for (int y = startY; y < (endY + 1); y++)
			for (int x = startX; x < (endX + 1); x++)
				results.add(x + (y * 9));

		Collections.sort(results);

		return results;
	}

	public static void setPlayerInventoryTitle(Player player, TextComponent newTitle) {
		Mauris.getInstance().getContainerTitleHandler().setPlayerInventoryTitle(player, newTitle);
	}

}
