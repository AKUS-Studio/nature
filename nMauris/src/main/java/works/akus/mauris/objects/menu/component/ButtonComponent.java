package works.akus.mauris.objects.menu.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;

import works.akus.mauris.objects.fonts.GlyphBuilder;
import works.akus.mauris.objects.menu.component.type.Interactable;
import works.akus.mauris.objects.menu.component.type.Physical;
import works.akus.mauris.objects.menu.component.type.Visual;
import works.akus.mauris.utils.InventoryUtils;

public class ButtonComponent extends MenuComponent implements Physical, Visual, Interactable {

	private ItemStack buttonItem;
	private List<Integer> slots;
	private GlyphBuilder builder;
	private int offset;

	// Returns a boolean, whether to cancel the click event or not
	private Function<InventoryClickEvent, Boolean> click;

	public ButtonComponent(@Nullable ItemStack buttonItem, @NotNull GlyphBuilder builder, int offset, int firstSlot, int secondSlot) {
		this.buttonItem = buttonItem.clone();
		this.builder = builder;
		this.offset = offset;
		this.slots = InventoryUtils.getAllSlotsInArea(firstSlot, secondSlot);
	}

	public ButtonComponent(@NotNull GlyphBuilder builder, int offset, int firstSlot, int secondSlot) {
		this(null, builder, offset, firstSlot, secondSlot);
	}

	/**
	 * Add a InventoryClickEvent listener to this button. 
	 */
	public void onClick(Function<InventoryClickEvent, Boolean> click) {
		this.click = click;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public GlyphBuilder getVisual() {
		return builder;
	}

	@Override
	public Map<Integer, ItemStack> getItems() {

		if (buttonItem == null) {
			return Maps.newHashMap();
		}
		
		HashMap<Integer, ItemStack> map = new HashMap<>();
		slots.forEach(slot -> map.put(slot, buttonItem.clone()));
		return map;
	}

	@Override
	public void interact(InventoryClickEvent event, int slot) {
		if (slots.contains(slot)) {
			boolean result = this.click.apply(event);
			event.setCancelled(result);
		}
	}

}
