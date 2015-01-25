package framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tile {
	
	private int x;
	private int y;
	private List<Entryway> exits;
	private String description;
	private List<Item> items;
	private List<Event> events;
	
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
		this.description = description + "\nThere are " + Integer.toString(exits.size()) + " doors.";
		if (exits.size() > 0) {
			for (Entryway e : exits) {
				this.description = description + "\nThe " + e.getOrientation().toString() + " door is ";
				if (e.isOpen()) {
					this.description = description + "open.";
				} else {
					this.description = description + "closed.";
				}
			}
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
	
	public List<Event> getEvents() {
		return events;
	}
	
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
	public void addEvent(Event event) {
		events.add(event);
	}
	
	public void fireEvent(Event event) {
		
	}

}
