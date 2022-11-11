package works.akus.mauris.objects.menu.component.type;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Interactable {

	/**
	 * Trigger an interact event at a given slot. Used for components that can be
	 * interacted with.
	 * 
	 * @param event
	 * @param slot The raw slot that was interacted with
	 */
	public void interact(InventoryClickEvent event, int slot);

}
