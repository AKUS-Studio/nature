package works.akus.mauris.objects.menu.component;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Interactable {

	public void interact(InventoryClickEvent event, int slot);

}
