package framework;

public class MovableItem extends Item implements Movable {
	
	private Character holder;
	
	public MovableItem(String des, String feeling) {
		super(des, feeling);
	}

	@Override
	public void pickUp(Character newHolder) {
		holder = newHolder;
	}

	@Override
	public void putDown(Character oldHolder) {
		if (oldHolder.equals(holder)) {
			holder = new Character("", "", "");
		}
	}
}
