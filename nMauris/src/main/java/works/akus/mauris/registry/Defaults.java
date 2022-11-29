package works.akus.mauris.registry;

import net.kyori.adventure.sound.Sound;
import org.checkerframework.checker.units.qual.C;
import works.akus.mauris.objects.items.TestItem;
import works.akus.mauris.objects.sounds.CustomSound;

public class Defaults {

	public static void registerDefaults() {
		registerDefaultItems();
		registerDefaultSounds();
	}
	
	private static void registerDefaultItems() {
		ItemRegistry.register("example", new TestItem());
	}
	private static void registerDefaultSounds() {
		SoundRegistry.registerSound(new CustomSound("nature", "test.mystic.impact")
				.setDefaultCategory(Sound.Source.PLAYER));
	}
	
}
