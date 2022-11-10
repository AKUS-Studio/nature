package works.akus.mauris.objects.menu;

public enum MenuType {
	
	CHEST_1_ROW(1),
	CHEST_2_ROW(2),
	CHEST_3_ROW(3),
	CHEST_4_ROW(4),
	CHEST_5_ROW(5),
	CHEST_6_ROW(6),
	HOPPER(0),
	ANVIL(0);

	private int rows;

	private MenuType(int rows) {
		this.rows = rows;
	}

	public boolean hasRows() {
		return rows != 0;
	}

	public int getRows() {
		return rows;
	}
	
}

