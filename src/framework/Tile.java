package framework;

import java.util.ArrayList;
import java.util.List;

public class Tile {
	
	private int x;
	private int y;
	private boolean[] exits;
	private String description;
	private List things;
	
	public Tile(int xCord, int yCord, boolean[] theExits, String theDescription, ArrayList theThings) {
		this.x = xCord;
		this.y = yCord;
		if (theExits.length == 4) {
			this.exits = theExits;
		} else //throw new error
		this.description = theDescription;
		this.things = theThings;
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

	public boolean[] getExits() {
		return exits;
	}

	public void setExits(boolean[] exits) {
		this.exits = exits;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List getThings() {
		return things;
	}

	public void setThings(List things) {
		this.things = things;
	}

}
