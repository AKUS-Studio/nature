package works.akus.mauris.objects.menu.component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import net.kyori.adventure.text.Component;
import works.akus.mauris.objects.fonts.GlyphBuilder;
import works.akus.mauris.objects.items.ItemStackBuilder;
import works.akus.mauris.objects.menu.component.type.Physical;
import works.akus.mauris.objects.menu.component.type.Visual;

public class ImageComponent extends MenuComponent implements Visual, Physical {

	private GlyphBuilder image;
	private int offset;

	private ItemStack tooltipItem;
	private int slot;

	public ImageComponent(GlyphBuilder image, int offset) {
		this.image = image;
		this.offset = offset;
	}

	public ImageComponent(GlyphBuilder image, int offset, ItemStackBuilder tooltipBuilder, List<Component> tooltip, int slot) {
		this.image = image;
		this.offset = offset;
		this.slot = slot;

		for (Component component : tooltip)
			tooltipBuilder.addLore(component);

		this.tooltipItem = tooltipBuilder.createItemStack();
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public GlyphBuilder getVisual() {
		return image;
	}

	@Override
	public Map<Integer, ItemStack> getItems() {
		if (tooltipItem == null)
			return Maps.newHashMap();
		HashMap<Integer, ItemStack> map = Maps.newHashMap();
		map.put(slot, tooltipItem);
		return Collections.unmodifiableMap(map);
	}

}
