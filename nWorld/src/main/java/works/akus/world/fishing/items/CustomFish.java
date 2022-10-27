package works.akus.world.fishing.items;

import org.bukkit.Material;

import works.akus.mauris.objects.items.ItemStackBuilder;
import works.akus.mauris.objects.items.MaurisItem;
import works.akus.mauris.registry.ItemRegistry;
import works.akus.world.fishing.types.CustomFishType;

public class CustomFish extends MaurisItem{
	
	private CustomFishType type;

	public CustomFish(CustomFishType type) {
		this.type = type;
	}
	
	@Override
	public void supplyBuilder(ItemStackBuilder builder) {
		builder
			.setMaterial(Material.COD)
			.setCustomModelData(type.getCustomModelData())
			.setDisplayName(type.getName());
		
	}

}
