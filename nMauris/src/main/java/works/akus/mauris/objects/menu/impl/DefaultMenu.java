package works.akus.mauris.objects.menu.impl;

import net.kyori.adventure.text.TextComponent;
import works.akus.mauris.objects.menu.Menu;
import works.akus.mauris.objects.menu.MenuType;

public class DefaultMenu extends Menu {

	public DefaultMenu(MenuType type, TextComponent title) {
		setMenuType(type);
		setTitle(title);
		buildInventory();
	}
	

}
