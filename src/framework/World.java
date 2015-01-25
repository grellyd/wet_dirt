package framework;

import java.util.List;

public class World {
	
	private int mapWidth = 25;
	private int mapHeight = 25;
	private int currentPlayerNum;
	private int reqPlayerNum;
	private List<Character> characters;
	private Tile[][] theMap;
	private List<Event> globalEvents;
	
	public enum DIRECTION {
		NORTH,
		EAST,
		SOUTH,
		WEST
	};
	
	public World() {
	}
	
	public void MovePlayer(int playerId, DIRECTION dir) {
		Character curCharacter = characters.get(playerId);
		int xDest = curCharacter.getX();
		int yDest = curCharacter.getY();
		switch (dir) {
		case NORTH:
			if (yDest - 1 >= 0) {
				characters.get(playerId).setY(characters.get(playerId).getY() - 1);
			}
			break;
		case EAST:
			if (xDest + 1 < mapWidth) {
				characters.get(playerId).setX(characters.get(playerId).getX() + 1);
			}
			break;
		case WEST:
			if (xDest - 1 >= 0) {
				characters.get(playerId).setX(characters.get(playerId).getX() - 1);
			}
			break;
		case SOUTH:
			if (yDest + 1 < mapHeight) {
				characters.get(playerId).setY(characters.get(playerId).getY() + 1);
			}
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
	
	public void fireEvent(Event event) {
		globalEvents.add(event);
		if (event.getRange() > 0) {
			for (int i = event.getTile().getX() - event.getRange(); i < event.getTile().getX() + event.getRange(); i++) {
				for (int j = event.getTile().getY() - event.getRange(); j < event.getTile().getY() + event.getRange(); j++) {
					if (i > 0 && i < mapWidth && j > 0 && j < mapHeight && i != event.getTile().getX() && j != event.getTile().getY()) {
						Event distancedEvent = new Event(theMap[i][j]);
						distancedEvent.setDescription(event.getDistancedDescription());
						distancedEvent.setDistancedDescription("");
						distancedEvent.setRange(0);
						globalEvents.add(event);
					}
				}
			}
		}
	}
	
	public Tile getPlayerTile(int playerNum) {
		Character activeChar = characters.get(playerNum);
		int x = activeChar.getX();
		int y = activeChar.getY();
		if (x >= 0 && x < mapWidth && y >= 0 && y < mapHeight) {
			return theMap[x][y];
		}
		return null;
	}
	
	public String describe(int playerNum) {
		Character activeChar = characters.get(playerNum);
		int x = activeChar.getX();
		int y = activeChar.getY();
		Tile activeTile = theMap[x][y];
		String description = activeTile.getDescription();
		if (activeChar.getGlobalEventsRead() < globalEvents.size()) {
			for (int i = activeChar.getGlobalEventsRead(); i <= globalEvents.size(); i++) {
				if (globalEvents.get(i).getTile().getX() == x && globalEvents.get(i).getTile().getY() == y) {
					description.concat("\n" + globalEvents.get(i));
				}
			}
			activeChar.setGlobalEventsRead(globalEvents.size());
		}
		return description;
	}

	public List<Character> getCharacters() {
		return characters;
	}

	public void setCharacters(List<Character> characters) {
		this.characters = characters;
	}


	
}