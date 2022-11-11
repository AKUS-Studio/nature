package works.akus.mauris.objects.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class MenuListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		final Inventory inventory = event.getView().getTopInventory();
		final Menu menu = MenuHolder.getMenu(inventory);

		if (menu == null) {
			return;
		}

		menu.onClick((Player) event.getWhoClicked(), event);
		menu.interactWithComponents(event);
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		final Inventory inventory = event.getView().getTopInventory();
		final Menu menu = MenuHolder.getMenu(inventory);

		if (menu == null) {
			return;
		}

		menu.onClose((Player) event.getPlayer(), event);
	}

	@EventHandler
	public void onOpen(InventoryOpenEvent event) {
		final Inventory inventory = event.getView().getTopInventory();
		final Menu menu = MenuHolder.getMenu(inventory);

		if (menu == null) {
			return;
		}

		menu.onOpen((Player) event.getPlayer(), event);
	}

	@EventHandler
	public void onDrag(InventoryDragEvent event) {
		final Inventory inventory = event.getView().getTopInventory();
		final Menu menu = MenuHolder.getMenu(inventory);

		if (menu == null) {
			return;
		}

		menu.onDrag((Player) event.getWhoClicked(), event);
	}

}
