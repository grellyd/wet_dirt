package framework;

public class OpenableMovableItem extends MovableItem implements Openable {

	private boolean isOpen;
	
	public OpenableMovableItem(String des, String feeling) {
		super(des, feeling);
	}

	@Override
	public void open() {
		isOpen = true;

	}

	@Override
	public void close() {
		isOpen = false;

	}
	
	public boolean isOpen() {
		return isOpen;
	}

}
