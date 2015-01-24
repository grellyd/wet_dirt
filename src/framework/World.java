package framework;

import java.util.ArrayList;
import java.util.List;

public class World {
	
	private static int MIN_PLAYER_NUM = 0;
	private static int MAP_WIDTH = 25;
	private static int MAP_HEIGHT = 25;
	
	private int currentPlayerNum;
	private int reqPlayerNum;
	private List<Character> characters;
	private Character emptyCharacter;
	private Tile[][] theMap;
	
	public World() {
		currentPlayerNum = 0;
		emptyCharacter = new Character("", "", "");
		characters = new ArrayList<Character>();
	}
	
	public Character getEmptyCharacter() {
		return emptyCharacter;
	}

	public int AddPlayer() {
		return currentPlayerNum++;
	}
	
	public int GetCurrentPlayerNum() {
		return currentPlayerNum;
	}
	
	public int GetReqPlayerNum() {
		return reqPlayerNum;
	}	
	
	private Tile[][] getTheMap() {
		return theMap;
	}
	
	public String describe(int playerNum) {
		Character activeChar = characters.get(playerNum);
		int x = activeChar.getX();
		int y = activeChar.getY();
		Tile activeTile = theMap[x][y];
		return activeTile.getDescription();
	}
	
}