package framework;

import java.util.ArrayList;
import java.util.List;

public class World {
	
	private int mapWidth = 25;
	private int mapHeight = 25;
	private int currentPlayerNum;
	private int reqPlayerNum;
	private List<Character> characters;
	private Tile[][] theMap;
	
	public enum DIRECTION {
		NORTH,
		EAST,
		SOUTH,
		WEST
	};
	
	public World() {
		currentPlayerNum = 0;
		reqPlayerNum = 2;
		characters = new ArrayList<Character>();
		theMap = new Tile[mapWidth][mapHeight];
	}
	
	public void MovePlayer(int playerId, DIRECTION dir) {
		switch (dir) {
		case NORTH:
			characters.get(playerId).setY(characters.get(playerId).getY() - 1);
			break;
		case EAST:
			characters.get(playerId).setX(characters.get(playerId).getX() + 1);
			break;
		case WEST:
			characters.get(playerId).setX(characters.get(playerId).getX() - 1);
			break;
		case SOUTH:
			characters.get(playerId).setY(characters.get(playerId).getY() + 1);
			break;
		}
	}

	public int AddPlayer() {
		return currentPlayerNum++;
	}
	
	public void RemovePlayer() {
		currentPlayerNum--;
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
	
}