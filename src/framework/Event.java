package framework;

public class Event {
	private String description;
	private String distancedDescription;
	private int range;
	private Tile tile;
	
	public Event(Tile tile) {
		this.tile = tile;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDistancedDescription() {
		return distancedDescription;
	}
	
	public void setDistancedDescription(String description) {
		this.distancedDescription = description;
	}
	
	public int getRange() {
		return range;
	}
	
	public void setRange(int range) {
		this.range = range;
	}
	
	public Tile getTile() {
		return tile;
	}
}
