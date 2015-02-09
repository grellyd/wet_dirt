package framework;

import java.util.ArrayList;
import java.util.List;

public class DirtEvent {
	private int id;
	private String description;
	private String failureText;
	private String successText;
	private String distancedDescription;
	private int range;
	private Tile tile;
	
	public enum FIREDBY {
		INTERACT
	};
	
	private FIREDBY firedBy;
	private String interactString;
	
	private List<MovableItem> rewards = new ArrayList<MovableItem>();
	private List<String> questions = new ArrayList<String>();
	private List<String> correctAnswers = new ArrayList<String>();
	private List<String> answers = new ArrayList<String>();
	
	public DirtEvent(Tile tile) {
		this.tile = tile;
	}
	
	public DirtEvent(DirtEvent event) {
		tile = event.tile;
		id = event.id;
		description = event.description;
		failureText = event.failureText;
		successText = event.successText;
		distancedDescription = event.distancedDescription;
		range = event.range;
		firedBy = event.firedBy;
		interactString = event.interactString;
		rewards = event.rewards;
		questions = event.questions;
		correctAnswers = event.correctAnswers;
		answers = new ArrayList<String>();
	}
	
	public List<String> getQuestions() {
		return questions;
	}
	
	public List<String> getUserAnswers() {
		return answers;
	}
	
	public boolean passed() {
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
			return true;
		} else {
			return false;
		}
	}
	
	public String getSuccessText() {
		return successText;
	}
	
	public String getFailureText() {
		return failureText;
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
	
	public Tile getTile() {
		return tile;
	}
}
