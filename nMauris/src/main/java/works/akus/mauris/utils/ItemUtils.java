package works.akus.mauris.utils;

import org.bukkit.inventory.ItemStack;

public class ItemUtils {

	public static boolean isEmpty(ItemStack item) {
		if (item == null || item.getType().isAir()) return true;
		return false;
	}
	
	public static boolean hasItemMeta(ItemStack item) {
		return !(isEmpty(item)) && item.hasItemMeta();
	}
	
}
