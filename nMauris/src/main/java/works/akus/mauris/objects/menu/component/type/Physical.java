package works.akus.mauris.objects.menu.component.type;

import java.util.Map;

import org.bukkit.inventory.ItemStack;

public interface Physical {

	/**
	 * @return A Map, where inventory slot is the key and an item is the value
	 */
	public Map<Integer, ItemStack> getItems();

}
