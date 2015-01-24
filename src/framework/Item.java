package framework;

public class Item extends Thing implements Interactable {
	
	protected String feel;
	
	public Item(String des, String feeling) {
		super(des);
		this.feel = feeling;
	}


	@Override
	public String touch() {
		return feel;
	}

	@Override
	public String lookAt() {
		return basic_description;
	}

	@Override
	public String lookIntently() {
		return basic_description;
	}

}
