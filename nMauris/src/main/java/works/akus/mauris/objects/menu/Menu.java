package works.akus.mauris.objects.menu;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import works.akus.mauris.objects.fonts.GlyphBuilder;
import works.akus.mauris.objects.menu.component.MenuComponent;
import works.akus.mauris.objects.menu.component.type.Interactable;
import works.akus.mauris.objects.menu.component.type.Physical;
import works.akus.mauris.objects.menu.component.type.Visual;
import works.akus.mauris.utils.InventoryUtils;

public abstract class Menu {

	// Main inventory variable
	private Inventory inventory;

	// Default values
	private MenuType type = MenuType.CHEST_6_ROW;
	private TextComponent title = Component.text("Inventory");

	// Menu state
	private ConcurrentHashMap<String, MenuComponent> components = new ConcurrentHashMap<>();
	private boolean loaded = false;

	// ------------------------------------

	public boolean hasLoaded() {
		return loaded;
	}

	public void addComponent(String id, MenuComponent component) {
		this.components.put(id, component);
	}

	public void removeComponent(String id) {
		this.components.remove(id);
	}

	public void clearComponents() {
		this.components.clear();
	}

	public Map<String, MenuComponent> getComponents() {
		return Collections.unmodifiableMap(this.components);
	}

	public void setMenuType(MenuType type) {
		this.type = type;
	}

	public void setTitle(TextComponent title) {
		this.title = title;
	}

	/**
	 * Open the Menu for a given player and update their inventory title.
	 */
	public void open(Player player) {
		player.openInventory(inventory);
		InventoryUtils.setPlayerInventoryTitle(player, title);
		player.updateInventory();
	}

	/**
	 * Provides an internal mechanism, which allows to check whether a menu has
	 * "loaded" or not. Being loaded means that all the items and other processes
	 * have finished, for example if the menu is async. If not loaded, you can for
	 * example cancel the interact events.
	 * 
	 * @return Whether the menu is loaded
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Provides an internal mechanism, which allows to check whether a menu has
	 * "loaded" or not. Being loaded means that all the items and other processes
	 * have finished, for example if the menu is async. If not loaded, you can for
	 * example cancel the interact events.
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public Inventory getInventory() {
		return inventory;
	}

	// ------------------------------------

	public void onClick(Player player, InventoryClickEvent event) {
	}

	public void onClose(Player player, InventoryCloseEvent event) {
	}

	public void onDrag(Player player, InventoryDragEvent event) {
	}

	public void onOpen(Player player, InventoryOpenEvent event) {
	}

	// ------------------------------------

	/**
	 * Trigger an InventoryInteractEvent for all {@link Interactable} components in
	 * this Menu. This method takes the raw slot from the event and feeds it to all
	 * interactable components.
	 */
	public void interactWithComponents(InventoryClickEvent event) {
		final int rawSlot = event.getRawSlot();

		for (MenuComponent component : components.values()) {
			// Only go through interactable components
			if (!(component instanceof Interactable))
				continue;

			Interactable interactable = (Interactable) component;
			interactable.interact(event, rawSlot);
		}

	}

	/**
	 * Render all of the current components inside of the component map, both
	 * {@link Visual} and {@link Physical}.
	 */
	public void renderAll() {
		renderVisuals();
		renderPhysical();
	}

	/**
	 * Render only the visual components and update the inventory title for all
	 * viewers
	 */
	public void renderVisuals() {
		GlyphBuilder builder = new GlyphBuilder();

		for (MenuComponent component : components.values()) {
			// Only render visual components
			if (!(component instanceof Visual))
				continue;

			Visual visual = (Visual) component;

			builder.offset(visual.getOffset());
			builder.append(visual.getVisual());

		}

		this.title = builder.buildAsComponent();
		renameInventory(this.title);
	}

	/**
	 * Render only the physical components and update the contents for all viewers
	 */
	public void renderPhysical() {

		this.inventory.clear();

		for (MenuComponent component : components.values()) {
			// Only go through interactable components
			if (!(component instanceof Physical))
				continue;

			Physical physical = (Physical) component;

			for (Entry<Integer, ItemStack> item : physical.getItems().entrySet()) {
				this.inventory.setItem(item.getKey(), item.getValue());
			}

		}

		for (HumanEntity entity : inventory.getViewers()) {
			Player player = (Player) entity;
			player.updateInventory();
		}

	}

	/**
	 * Builds the {@link Inventory} object according to the given {@link MenuType}
	 * and title.
	 */
	public void buildInventory() {
		if (this.type.hasRows()) {
			final int slots = this.type.getRows() * 9;

			this.inventory = Bukkit.createInventory(null, slots, title);
			MenuHolder.register(this);
			return;
		}

		if (this.type == MenuType.ANVIL)
			this.inventory = Bukkit.createInventory(null, InventoryType.ANVIL, title);
		if (this.type == MenuType.HOPPER)
			this.inventory = Bukkit.createInventory(null, InventoryType.HOPPER, title);
		MenuHolder.register(this);
	}

	/**
	 * Send the new inventory title to all viewers (packet)
	 */
	private void renameInventory(TextComponent title) {
		for (HumanEntity viewer : inventory.getViewers()) {
			Player player = (Player) viewer;
			InventoryUtils.setPlayerInventoryTitle(player, title);
			player.updateInventory();
		}
	}
}
