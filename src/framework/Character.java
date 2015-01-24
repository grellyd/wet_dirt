package framework;

import java.util.List;

import exceptions.ItemNotFoundException;

public class Character implements Interactable {
	
	private String name;
	private int id;
	private String basic_description;
	private String feel;
	private int x;
	private int y;
	
	private List<MovableItem> inventory;
	
	public Character(String name, String desc, String feel) {
		this.basic_description = desc;
		this.feel=feel;
	}

	@Override
	public String lookAt() {
		return basic_description;
	}

	@Override
	public String touch() {
		return feel;
	}

	@Override
	public String lookIntently() {
		return basic_description + "Its name is " + name;
	}
	
	
	
	public List<MovableItem> getInventory() {
		return inventory;
	}

	public void removeFromInventory(MovableItem itemToRemove) throws ItemNotFoundException {
		if(inventory.contains(itemToRemove)) {
			inventory.remove(itemToRemove);
		} else throw new ItemNotFoundException();
	}
	
	public void addToInventory(MovableItem itemToAdd) {
		if(itemToAdd != null) {
			inventory.add(itemToAdd);
		}
	}

	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int newId) {
		this.id = newId;
	}
}