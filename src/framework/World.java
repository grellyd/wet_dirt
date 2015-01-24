package framework;

public class World {
	private int playerNum;
	private int maxPlayerNum;
	
	public World() {
		playerNum = 0;
		maxPlayerNum = 2;
	}
	
	public int AddPlayer() {
		if (playerNum >= maxPlayerNum) {
			return -1;
		}
		return playerNum++;
	}
	
	public int GetPlayerNum() {
		return playerNum;
	}
	
	public int GetMaxPlayerNum() {
		return maxPlayerNum;
	}
}
