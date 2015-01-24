package framework;

public abstract class Thing {
	
	// This class is for the objects (things) within the world.
	// basic_description should be "this is a ..."
	
	protected String name;
	protected String basic_description;
	protected String full_description;

	public String getFull_description() {
		return full_description;
	}

	public void setFull_description(String full_description) {
		this.full_description = full_description;
	}

	public Thing(String des) {
		this.basic_description = des;
	}
	
	
	
}
