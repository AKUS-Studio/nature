package works.akus.mauris.utils;

public class OpenContainerData {
	private int windowId;
	private Object containerType;

	public OpenContainerData(int windowId, Object containerType) {
		this.windowId = windowId;
		this.containerType = containerType;
	}

	public int getWindowId() {
		return windowId;
	}

	public Object getContainerType() {
		return containerType;
	}
}