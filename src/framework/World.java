package framework;

import java.util.ArrayList;
import java.util.List;

public class World {
	
	private int mapWidth = 25;
	private int mapHeight = 25;
	private int currentPlayerNum;
	private int reqPlayerNum;
	private List<Character> characters;
	private List<Tile> theMap;
	private List<DirtEvent> globalEvents;
	private List<MapIndex> lookupTable;
	private int numX;
	private int numY;
	
	public enum DIRECTION {
		NORTH,
		EAST,
		SOUTH,
		WEST
	};
	
	public World() {
		theMap = new ArrayList<Tile>();
	}
	
	public void generateLookupTable() {
		lookupTable = new ArrayList<MapIndex>();
		for(int i = 0; i<theMap.size(); i++) {
			// Generate and populate new object.
			MapIndex myMapIndex = new MapIndex();
			myMapIndex.setX(theMap.get(i).getX());
			myMapIndex.setY(theMap.get(i).getY());
			myMapIndex.setI(i);
			// Throw into the lookupTable
			lookupTable.add(myMapIndex);
			numX = Math.max(numX, theMap.get(i).getX()+1);
			numY = Math.max(numY, theMap.get(i).getY()+1);
		}
		//Reorder
		lookupTable.sort(new MapIndexComparator());
		// Fill in blank spaces.
		int curX = 0;
		int curY = 0;
		for(int i = 0; i<lookupTable.size(); i++) {
			MapIndex currentMapIndex = lookupTable.get(i);
			int expectedIndex = curY*numX + curX;
			int currentIndex = currentMapIndex.getY()*numX + currentMapIndex.getX();
			while (expectedIndex < currentIndex) {
				lookupTable.add(expectedIndex, new MapIndex(curX, curY));
				expectedIndex++;
				if (curX < (numX-1)) {
					curX++;
				} else {
					curY++;
					curX = 0;
				}
			}
			if (curX < (numX-1)) {
				curX++;
			} else {
				curY++;
				curX = 0;
			}
		}
		
		
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
	
	public List<Tile> getTheMap() {
		return theMap;
	}
	
	public int getMapWidth() {
		return mapWidth;
	}
	
	public int getMapHeight() {
		return mapHeight;
	}
	
	public Tile getMapTile(int x, int y) {
		try {
			assert(x >= 0 && y >=0);
			assert(x < numX && y < numY);
			MapIndex myIndex = lookupTable.get(y*numX + x);
			Tile myTile = theMap.get(myIndex.getI());
			return myTile;
			
		} catch (AssertionError e) {
			System.out.println(e.getMessage() + "Cannot access the requested tile!");
			System.out.println(e.getStackTrace());
		}
		return new Tile(0, 0, new ArrayList<Entryway>(), "This is not the room you are looking for.", new ArrayList<Item>());
	}
	
	public void fireEvent(DirtEvent event) {
		globalEvents.add(event);
		if (event.getRange() > 0) {
			for (int i = event.getTile().getX() - event.getRange(); i < event.getTile().getX() + event.getRange(); i++) {
				for (int j = event.getTile().getY() - event.getRange(); j < event.getTile().getY() + event.getRange(); j++) {
					if (i > 0 && i < mapWidth && j > 0 && j < mapHeight && i != event.getTile().getX() && j != event.getTile().getY()) {
						DirtEvent distancedEvent = new DirtEvent(getMapTile(i, j));
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
			return getMapTile(x, y);
		}
		return null;
	}
	
	public String describe(int playerNum) {
		Character activeChar = characters.get(playerNum);
		int x = activeChar.getX();
		int y = activeChar.getY();
		Tile activeTile = getMapTile(x, y);
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
	
	// For Testing purposes
	public void addTile(Tile tile) {
		theMap.add(tile);
	}

	public List<MapIndex> getLookupTable() {
		return lookupTable;
	}
	
}