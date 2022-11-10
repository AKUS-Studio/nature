package works.akus.mauris.objects.menu;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.inventory.Inventory;

public class MenuHandler {

	private static ConcurrentHashMap<Inventory, Menu> menus = new ConcurrentHashMap<>();

	public static void register(Menu menu) {
		menus.put(menu.getInventory(), menu);
	}

	public static Menu getMenu(Inventory inventory) {
		return menus.getOrDefault(inventory, null);
	}

}
