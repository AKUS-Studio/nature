package works.akus.mauris.objects.menu.title;

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