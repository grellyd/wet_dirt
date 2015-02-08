package framework;

import java.util.List;

public class Tile {
	
	private int x;
	private int y;
	private List<Entryway> exits;
	private String description;
	private List<Item> items;
	private List<DirtEvent> events;
	
	public Tile(int xCord, int yCord, List<Entryway> theExits, String theDescription, List<Item> theItems) {
		this.x = xCord;
		this.y = yCord;
		if (theExits.size() == 4) {
			this.exits = theExits;
		} else //throw new error
		this.description = theDescription;
		this.items = theItems;
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

	public List<Entryway> getExits() {
		return exits;
	}

	public void setExits(List<Entryway> exits_) {
		this.exits = exits_;
	}

	public String getDescription() {
		this.description = "--------------\n" + description;
		this.description = description + "\nThere are " + Integer.toString(exits.size()) + " doors and " + Integer.toString(items.size()) + " items\n";
		if (exits.size() > 0) {
			for (Entryway e : exits) {
				this.description += "--> The " + e.getOrientation().toString() + " door is ";
				if (e.isOpen()) {
					this.description += "open.\n";
				} else {
					this.description += "closed.\n";
				}
			}
			this.description += "You see the following items:\n";
			for (Item i : items) {
				this.description += "--> " + i.getName() + "\n";
			}
			this.description += "--------------";
		}
		
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public List<DirtEvent> getEvents() {
		return events;
	}
	
	public void setEvents(List<DirtEvent> events) {
		this.events = events;
	}
	
	public void addEvent(DirtEvent event) {
		events.add(event);
	}
	
	public void fireEvent(DirtEvent event) {
		
	}

}
