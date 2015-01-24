package framework;

public class EntryPoint {
	
	private static int MAX_PLAYERS;
	
	public World theWorld;

	public static void main(String[] args) {
		
		// init argument in the future could be number of max players.
		MAX_PLAYERS = 2;
		World theWorld = new World(MAX_PLAYERS);
		theWorld.generateWorld();
	}
}
