package works.akus.mauris.objects.menu;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.inventory.Inventory;

/**
 * For internal use in Menus. Keeps track of currently used menus to correctly
 * detect them in events.
 */
public class MenuHolder {

	protected static ConcurrentHashMap<Inventory, Menu> MENUS = new ConcurrentHashMap<>();

	protected static void register(Menu menu) {
		MENUS.put(menu.getInventory(), menu);
	}

	protected static Menu getMenu(Inventory inventory) {
		return MENUS.getOrDefault(inventory, null);
	}

}
