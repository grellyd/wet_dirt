package framework;

public class Entryway extends Thing implements Openable {
	
	private String feel;
	private boolean isOpen;
	
	private enum Direction {
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
	public void open() {
		isOpen = true;
	}

	@Override
	public void close() {
		isOpen = false;
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
