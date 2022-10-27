package works.akus.world.fishing.types;

public enum CustomFishType {
	
	COD(0, "Треска"), 
	BAMBOO_FISH(1, "Бамбуковая рыба");

	private int customModelData;
	private String name;

	private CustomFishType(int customModelData, String name) {
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
