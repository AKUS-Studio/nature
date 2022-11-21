package works.akus.world.fishing.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum CustomFishType {
	
	COD(0, "Треска", "cod"), 
	BAMBOO_FISH(1, "Бамбуковая рыба", "bamboo_fish");

	private int customModelData;
	private String name;
	private String id;

	private static final List<CustomFishType> VALUES =
		    Collections.unmodifiableList(Arrays.asList(values()));
	private static final int NUM_OF_VALUES = VALUES.size();
	
	private CustomFishType(int customModelData, String name, String id) {
		this.customModelData = customModelData;
		this.name = name;
		this.id = id;
	}

	public int getCustomModelData() {
		return customModelData;
	}

	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}
	
	private static final Random RANDOM = new Random();
	public static CustomFishType getRandomFishType() {
		return VALUES.get(RANDOM.nextInt(NUM_OF_VALUES));
	}

}
