package works.akus.mauris.objects.menu.component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import works.akus.mauris.objects.items.ItemStackBuilder;

public class ItemComponent extends MenuComponent implements Physical, Interactable {

	private ItemStack item = null;
	private int slot = 0;

	// Returns a boolean, whether to cancel the click event or not
	private Function<InventoryClickEvent, Boolean> click;

	public ItemComponent() {
	}

	public ItemComponent(ItemStack item, int slot) {
		this.item = item;
		this.slot = slot;
	}

	public ItemComponent(ItemStackBuilder builder, int slot) {
		this(builder.createItemStack(), slot);
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public ItemStack getItem() {
		return item;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public void onClick(Function<InventoryClickEvent, Boolean> click) {
		this.click = click;
	}

	@Override
	public void interact(InventoryClickEvent event, int slot) {
		if (this.slot != slot)
			return;
		boolean result = this.click.apply(event);
		event.setCancelled(result);
	}

	@Override
	public Map<Integer, ItemStack> getItems() {
		final HashMap<Integer, ItemStack> map = new HashMap<>();
		map.put(slot, item);
		return Collections.unmodifiableMap(map);
	}

}
