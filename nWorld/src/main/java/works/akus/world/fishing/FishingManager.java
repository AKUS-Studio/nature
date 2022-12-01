package works.akus.world.fishing;

import org.bukkit.plugin.java.JavaPlugin;

import works.akus.mauris.objects.fonts.Glyph;
import works.akus.mauris.objects.sounds.CustomSound;
import works.akus.mauris.registry.GlyphRegistry;
import works.akus.mauris.registry.ItemRegistry;
import works.akus.mauris.registry.SoundRegistry;
import works.akus.world.fishing.items.CustomFish;
import works.akus.world.fishing.items.CustomFishingRod;
import works.akus.world.fishing.process.FishingProcessListener;
import works.akus.world.fishing.process.FishingProcessesHandler;
import works.akus.world.fishing.types.CustomFishType;
import works.akus.world.fishing.types.FishingRodType;

public class FishingManager {

	private FishingProcessesHandler fishingProcessesHandler;
	
	public FishingManager(JavaPlugin plugin) {
		fishingProcessesHandler = new FishingProcessesHandler(plugin);
		plugin.getServer().getPluginManager().registerEvents(new FishingProcessListener(), plugin);
		
		registerItems();
		regiserGlyphs();
		registerSounds();
	}
	
	private void registerItems() {
		// Register fishing rods
		ItemRegistry.register("bamboo_fishing_rod", new CustomFishingRod(FishingRodType.BAMBOO_FISHING_ROD));
		ItemRegistry.register("standard_fishing_rod", new CustomFishingRod(FishingRodType.STANDARD_FISHING_ROD));
		
		// Register fishes
		ItemRegistry.register(CustomFishType.COD.getId(), new CustomFish(CustomFishType.COD));
		ItemRegistry.register(CustomFishType.BAMBOO_FISH.getId(), new CustomFish(CustomFishType.BAMBOO_FISH));
	}

	private void registerSounds(){
		SoundRegistry.registerSound(new CustomSound("nature", "fishing.unwind"));

		SoundRegistry.registerSound(new CustomSound("nature", "fishing.reel"));
		SoundRegistry.registerSound(new CustomSound("nature", "fishing.reel.start"));

		SoundRegistry.registerSound(new CustomSound("nature", "fishing.splash"));

		SoundRegistry.registerSound(new CustomSound("nature", "fishing.throw"));
		SoundRegistry.registerSound(new CustomSound("nature", "fishing.throw.start"));
		SoundRegistry.registerSound(new CustomSound("nature", "fishing.throw.end"));

		SoundRegistry.registerSound(new CustomSound("nature", "fishing.failure"));
		SoundRegistry.registerSound(new CustomSound("nature", "fishing.success"));
	}
	
	private void regiserGlyphs() {
		// Register tension bar components
		GlyphRegistry.registerGlyph(new Glyph("tension_bar", '\uE000', "fishing",39));
		GlyphRegistry.registerGlyph(new Glyph("tension_line", '\uE001', "fishing",1));
	}
	
	public FishingProcessesHandler getFishingProcessesHandler() {
		return fishingProcessesHandler;
	}
	
}
