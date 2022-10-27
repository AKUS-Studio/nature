package works.akus.mauris.registry;

import works.akus.mauris.objects.items.TestItem;

public class Defaults {

	public static void registerDefaults() {
		registerDefaultItems();
	}
	
	private static void registerDefaultItems() {
		ItemRegistry.register("example", new TestItem());
	}
	
}
