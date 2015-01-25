package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import client.UI;

import com.thoughtworks.xstream.XStream;

import framework.Entryway;
import framework.Item;
import framework.MovableItem;
import framework.Tile;
import framework.World;

public class WetDirtServer {
	private static final String VERSION = "0.1";
	
	private NetworkManager networkManager;
	private XStream xstream;
	private World world;
	
	public WetDirtServer() {
		xstream = new XStream();
		xstream.alias("World", World.class);
		xstream.alias("Tile", Tile.class);
		xstream.alias("Item", Item.class);
		xstream.alias("MovableItem", MovableItem.class);
		xstream.alias("Entryway", Entryway.class);
		xstream.alias("Character", framework.Character.class);
		
		System.out.println("wet_dirt - Server v" + VERSION);
		//System.out.print("Enter the filename of the world you would like to load >");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String filename = "";
		String xmlWorld = "";
		try {
			
			boolean success = false;
			do {
				System.out.print("Enter the filename of the world you would like to load >");
				filename = reader.readLine();
				if (filename.equals("d")) {
					filename = "worlds/test.xml";
				}
				UI.createAndShowGUI();
				try {
					xmlWorld = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
					success = true;
				} catch (NoSuchFileException e) {
					System.out.println("There exists no such world. Please try again.");
				}
			} while (!success);
			
			world = (World)xstream.fromXML(xmlWorld);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (world != null) {
			networkManager = new NetworkManager(world);
			Thread networkThread = new Thread(networkManager);
			
			networkThread.start();
			
			while (true) {}
		} else {
			System.out.println("Invalid world selected");
		}
	}
	
	public static void main(String[] args) {
		new WetDirtServer();
	}
	
}
