package framework;

public class Entryway extends Thing implements Openable {
	
	private String feel;
	private boolean isOpen;
	
	public enum Direction {
		North,
		East,
		South,
		West
	}
	
	private Direction orientation;

	public Entryway(String des, String feeling) {
		super(des);
		this.feel = feeling;
		this.isOpen = false;
	}

	public String lookAt() {
		return basic_description;
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

	@Override
	public String touch() {
		return feel;
	}

	public boolean isOpen() {
		return isOpen;
	}

	@Override
	public String lookIntently() {
		// TODO Auto-generated method stub
		return null;
	}

	public Direction getOrientation() {
		return orientation;
	}

	public void setOrientation(Direction orientation) {
		this.orientation = orientation;
	}


	
}
