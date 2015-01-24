package server;

import framework.World;

public class WetDirtServer {
	private NetworkManager networkManager;
	private World world;
	
	public WetDirtServer() {
		world = new World();
		
		networkManager = new NetworkManager(world);
		Thread networkThread = new Thread(networkManager);
		
		networkThread.start();
		
		while (true) {}
	}
	
	public static void main(String[] args) {
		new WetDirtServer();
	}
	
}
