package works.akus.mauris.objects.menu.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.kyori.adventure.text.Component;
import works.akus.mauris.objects.fonts.GlyphBuilder;
import works.akus.mauris.objects.items.ItemStackBuilder;
import works.akus.mauris.objects.menu.Menu;
import works.akus.mauris.objects.menu.MenuType;
import works.akus.mauris.objects.menu.component.ButtonComponent;
import works.akus.mauris.objects.menu.component.ImageComponent;
import works.akus.mauris.objects.menu.component.ItemComponent;

public class ExampleMenu extends Menu {

	public ExampleMenu() {

		setMenuType(MenuType.CHEST_6_ROW);
		setTitle(Component.text("Test"));
		buildInventory();

		final GlyphBuilder backgroundImage = new GlyphBuilder().append("Test");
		final ImageComponent background = new ImageComponent(backgroundImage, -8);

		final ItemStackBuilder builder = new ItemStackBuilder().setMaterial(Material.DIRT);
		final ItemComponent item = new ItemComponent(builder, 0);

		final ButtonComponent button = new ButtonComponent(builder.createItemStack(), new GlyphBuilder().append("text"), -20, 10, 43);

		item.onClick((event) -> {

			event.getWhoClicked().sendMessage("Oh no! You clicked an item");
			return true; // Cancel the event

		});
		
		button.onClick((event) -> {
			
			event.getWhoClicked().sendMessage("Clicked a button");
			return true;
			
		});

		addComponent("background", background);
		addComponent("button", button);
		addComponent("item", item);

		renderAll();

	}

	@Override
	public void onClick(Player player, InventoryClickEvent event) {
		event.setCancelled(true);
	}

}
