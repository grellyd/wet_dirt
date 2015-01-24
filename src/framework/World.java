package framework;

import java.util.ArrayList;
import java.util.List;

public class World {
	
	private static int MIN_PLAYER_NUM = 0;
	private static int MAP_WIDTH = 25;
	private static int MAP_HEIGHT = 25;
	
	private int currentPlayerNum;
	private int maxPlayerNum;
	private List<Character> characters;
	private Character emptyCharacter;
	private Tile[][] theMap;
	
	public World(int maxPlayerNum_) {
		currentPlayerNum = 0;
		maxPlayerNum = maxPlayerNum_;
		emptyCharacter = new Character("", "", "");
		characters = new ArrayList<Character>();
	}

	
	public void generateWorld() {
		createCharacters(maxPlayerNum);
		createMap();
	}
	
	public Character getEmptyCharacter() {
		return emptyCharacter;
	}
	
	private void createCharacters(int numPlayers) {
		for (int i = 0; i < numPlayers; i++) {
			Character newChar = new Character("Player " + i, "A strange fellow.", "Soft and squishy.");
			newChar.setId(i);
			currentPlayerNum++;
			characters.add(newChar);
		}
	}	
	
	private void createMap() {
		theMap =  new Tile [MAP_WIDTH][MAP_HEIGHT];
		addNarrativePoints();
		addPlayerPositions();
	}
	
	public int GetCurrentPlayerNum() {
		return currentPlayerNum;
	}
	
	public int GetMaxPlayerNum() {
		return maxPlayerNum;
	}
	
	private void addNarrativePoints() {
		//TODO
	}
	
	private void addPlayerPositions() {
		//TODO
	}
	
	private Tile[][] getTheMap() {
		return theMap;
	}
	
}