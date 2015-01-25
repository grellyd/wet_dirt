package framework;

public class OpenableMovableItem extends MovableItem implements Openable {

	private boolean isOpen;
	
	public OpenableMovableItem(String des, String feeling) {
		super(des, feeling);
	}

	@Override
	public boolean open() {
		isOpen = true;
		return true;
	}

	@Override
	public boolean close() {
		isOpen = false;
		return true;
	}
	
	public boolean isOpen() {
		return isOpen;
	}

}
