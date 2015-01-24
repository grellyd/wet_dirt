package framework;

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
	
	public Tile[][] getTheMap() {
		return theMap;
	}
	
	public void fireEvent(int tileX, int tileY, int index) {
		if (tileX > 0 && tileX < mapWidth && tileY > 0 && tileY < mapHeight) {
			if (theMap[tileX][tileY].getEvents().size() > index) {
				//theMap[tileX][tileY].
			}
		}
	}
	
	public String describe(int playerNum) {
		Character activeChar = characters.get(playerNum);
		int x = activeChar.getX();
		int y = activeChar.getY();
		Tile activeTile = theMap[x][y];
		return activeTile.getDescription();
	}
	
}