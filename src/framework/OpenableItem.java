package framework;

public class OpenableItem extends Item implements Openable {

	private boolean isOpen;
	
	public OpenableItem(String des, String feeling) {
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

}