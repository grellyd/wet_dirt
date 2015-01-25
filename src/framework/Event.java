package framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
	private Item firedItem;
	private String interactString;
	
	private List<String> questions = new ArrayList<String>();
	private List<String> correctAnswers = new ArrayList<String>();
	private List<String> answers = new ArrayList<String>();
	
	public Event(Tile tile) {
		this.tile = tile;
	}
	
	public void fire(Character c) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("--------------");
		System.out.println(description);
		for (String question : questions) {
			System.out.println(question);
			try {
				answers.add(reader.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			System.out.println(successText);
			fired = true;
		} else {
			System.out.println(failureText);
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
	
	public void setFiredItem(Item firedItem) {
		this.firedItem = firedItem;
	}
	
	public Item getFiredItem() {
		return firedItem;
	}
	
	public void setIteractString(String interactString) {
		this.interactString = interactString;
	}
	
	public String getIteractString() {
		return interactString;
	}
	
	public boolean hasFired() {
		return fired;
	}
	
	public Tile getTile() {
		return tile;
	}
}
