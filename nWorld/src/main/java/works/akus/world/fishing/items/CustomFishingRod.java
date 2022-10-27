package works.akus.world.fishing.items;

import org.bukkit.Material;
import org.bukkit.entity.FishHook;

import works.akus.mauris.objects.items.ItemStackBuilder;
import works.akus.mauris.objects.items.MaurisItem;
import works.akus.world.fishing.types.FishingRodType;

public class CustomFishingRod extends MaurisItem{

	private FishingRodType type;
	
	public CustomFishingRod(FishingRodType type) {
		this.type = type;
	}
	
	@Override
	public void supplyBuilder(ItemStackBuilder builder) {
		builder
			.setMaterial(Material.FISHING_ROD)
			.setCustomModelData(type.getCustomModelData())
			.setDisplayName(type.getName());
	}

}
