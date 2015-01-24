package framework;

public interface Movable extends Interactable {
	
	public void pickUp(Character newHolder);
	
	public void putDown(Character oldHolder);

}
