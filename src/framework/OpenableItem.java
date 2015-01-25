package framework;

public class OpenableItem extends Item implements Openable {

	private boolean isOpen = false;
	
	public OpenableItem(String des, String feeling) {
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
}
