package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import exceptions.ReturnException;
import framework.Item;
import framework.MovableItem;
import framework.World;
import framework.Tile;
import framework.Entryway;

public class MainProcess {
	
	private static int PLAYER_NUM;
	
	private TCPClient tcpClient = new TCPClient();
	private XStream xstream;
	private String errorMessage = "";
	
	private World theWorld;
	private Tile localTile;
	
	public MainProcess() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		xstream = new XStream();
		xstream.alias("World", World.class);
		xstream.alias("Tile", Tile.class);
		xstream.alias("Item", Item.class);
		xstream.alias("MovableItem", MovableItem.class);
		xstream.alias("Entryway", Entryway.class);
		xstream.alias("Character", framework.Character.class);
		try {
			// Connect
			errorMessage = "ERROR: Joining Server Failed. ";
			
			System.out.println("Connecting to Server");
			System.out.print("Enter server IP >");
			//String ip = reader.readLine();
			System.out.print("Enter port >");
			//int port = Integer.parseInt(reader.readLine());
			System.out.println("Using local settings");
			String ip = "127.0.0.1";
			int port = 12345;
			tcpClient.Connect(ip, port);
			
			do {
				System.out.println("Waiting for server...");
				Thread.sleep(5000);
			} while (!tcpClient.IsConnected());
			
			System.out.println("Connected!");
			errorMessage = "ERROR: Joining game failed. ";
			PLAYER_NUM = Integer.parseInt(tcpClient.getData("JOIN"));
			System.out.println("You are player " + PLAYER_NUM);
						
			while(tcpClient.IsConnected()) {
				
				errorMessage = "ERROR: Starting game failed. ";
				String status;
				do {
					status = tcpClient.getData("STATUS");
					Thread.sleep(5000);
				} while (!status.equals("READY"));
				
				errorMessage = "ERROR: Fetching world failed. ";
				theWorld = (World)xstream.fromXML(tcpClient.getData("POLLWORLD"));
				
				System.out.println("Starting Game!");
				
				errorMessage = "Error: Getting actions failed. ";
				
				do {
					
					localTile = theWorld.getPlayerTile(PLAYER_NUM);
					System.out.println(theWorld.describe(PLAYER_NUM));
					
					//prompt for action
					System.out.println("What do you do next?");
					String input = reader.readLine();
					String parseOnServer = parse(input);
					if (!parseOnServer.isEmpty()) {
						tcpClient.sendMessage(parseOnServer);
					}
					//check for event
					//TODO
					// fetch updated info from server
					String xmlWorld = tcpClient.getData("POLLWORLD");
					theWorld = (World)xstream.fromXML(xmlWorld);
					
				} while(true);
			}
		
		
		
		
		
		
		} catch (NumberFormatException e) {
			System.out.println(errorMessage + e.getMessage());
			e.printStackTrace();
			tcpClient.Disconnect();
		} catch (IOException e) {
			System.out.println(errorMessage + e.getMessage());
			e.printStackTrace();
			tcpClient.Disconnect();e.printStackTrace();
		} catch (ReturnException e) {
			System.out.println(errorMessage + e.getMessage());
			e.printStackTrace();
			tcpClient.Disconnect();
		} catch (InterruptedException e) {
			System.out.println(errorMessage + e.getMessage());
			e.printStackTrace();
			tcpClient.Disconnect();
		}		
	}
	
	private int getPlayerNumber(World world) throws IOException, ReturnException {
		int numPlayers = world.GetCurrentPlayerNum();
		if (numPlayers > 0) {
			if (tcpClient.getData("STATUS").equals("WAITING")) {
				
			}
			
			
		}
		return 1;
	}
	
	private String parse(String input) {
		input = input.toLowerCase();
		String result = "";
		String sequence = "";
		
		// look for starting keywords
		
		if (input.contains("move")){
			// get the list of local exits.
			List<String> exits = new ArrayList<String>();
			if (localTile == null) {
				System.out.println("Error: localTile not initialized");
				return "";
			}
			if (localTile.getExits() != null) {
				for (Entryway e : (localTile.getExits())) {
					exits.add(e.getOrientation().toString().toLowerCase());
				}
			}
			if (input.contains("north") && exits.contains("north")) {
				result = "MOVE;NORTH";			
			} else if (input.contains("east") && exits.contains("east")) {
				result = "MOVE;EAST";
			} else if (input.contains("south") && exits.contains("south")) {
				result = "MOVE;SOUTH";
			} else if (input.contains("west") && exits.contains("west")) {
				result = "MOVE;WEST";
			} else System.out.println("You can't go that way.");
			
		} else if (input.contains("look")) {
			
			
		//} else if (input.contains("look intently at")) {
			
		} else if (input.contains("pick up")) {
			
		} else if (input.contains("drop")) {
			
		} else if (input.contains("use")) {
			
		} else System.out.println("I don't understand. Try again?");
		
		
		return result;
	}
	
	
	public static void main(String[] args) {
		new MainProcess();
	}

}
