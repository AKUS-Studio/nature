package works.akus.world.fishing.types;

public enum FishingRodType {

	STANDARD_FISHING_ROD(0, "Удочка"), 
	BAMBOO_FISHING_ROD(1, "Бамбуковая удочка");

	private int customModelData;
	private String name;

	private FishingRodType(int customModelData, String name) {
		this.customModelData = customModelData;
		this.name = name;
	}

	public int getCustomModelData() {
		return customModelData;
	}

	public String getName() {
		return name;
	}

}
