package framework;

import java.util.ArrayList;
import java.util.List;

import client.UI;

public class Event {
	private int id;
	private String description;
	private String failureText;
	private String successText;
	private String distancedDescription;
	private int range;
	private Tile tile;
	private boolean fired = false;
	
	public enum FIREDBY {
		AUTO,
		HASITEM,
		INTERACT
	};
	
	private FIREDBY firedBy;
	private String firedItemName;
	private String interactString;
	
	private List<MovableItem> rewards = new ArrayList<MovableItem>();
	private List<String> questions = new ArrayList<String>();
	private List<String> correctAnswers = new ArrayList<String>();
	private List<String> answers = new ArrayList<String>();
	
	public Event(Tile tile) {
		this.tile = tile;
	}
	
	public void fire(Character c) {
		UI.addToOutput("--------------");
		UI.addToOutput(description);
		for (String question : questions) {
			UI.addToOutput(question);
			answers.add(UI.getInputResult());
		}
		boolean correctAnswer = true;
		for (int i = 0; i < correctAnswers.size(); i++) {
			for (String a : correctAnswers.get(i).split(";")) {
				if (!answers.get(i).toLowerCase().contains(a.toLowerCase())) {
					correctAnswer = false;
					break;
				}
			}
			if (!correctAnswer) {
				break;
			}
		}
		if (correctAnswer) {
			UI.addToOutput(successText);
			fired = true;
		} else {
			UI.addToOutput(failureText);
		}
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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
	
	public void setFiredBy(FIREDBY firedBy) {
		this.firedBy = firedBy;
	}
	
	public FIREDBY getFiredBy() {
		return firedBy;
	}
	
	public void setFiredItemName(String firedItemName) {
		this.firedItemName = firedItemName;
	}
	
	public String getFiredItemName() {
		return firedItemName;
	}
	
	public void setIteractString(String interactString) {
		this.interactString = interactString;
	}
	
	public String getIteractString() {
		return interactString;
	}
	
	public void setRewards(List<MovableItem> rewards) {
		this.rewards = rewards;
	}
	
	public List<MovableItem> getRewards() {
		return rewards;
	}
	
	public boolean hasFired() {
		return fired;
	}
	
	public Tile getTile() {
		return tile;
	}
}
