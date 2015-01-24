package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import sun.net.www.content.audio.x_aiff;
import exceptions.ReturnException;
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
		try {
			// Connect
			errorMessage = "ERROR: Joining Server Failed. ";
			
			System.out.println("Connecting to Server");
			String ip = reader.readLine();
			System.out.print("Enter port >");
			int port = Integer.parseInt(reader.readLine());
			tcpClient.Connect(ip, port);
			
			do {
				System.out.println("Waiting for server...");
			} while (!tcpClient.IsConnected());
			
			System.out.println("Connected!");
			
			// Join a game
			
			
			
			while(tcpClient.IsConnected()) {
				errorMessage = "ERROR: Joining game failed. ";
				PLAYER_NUM = getPlayerNumber(theWorld);
				
				errorMessage = "ERROR: Starting game failed. ";
				String status;
				do {
					status = tcpClient.getData("STATUS");
				} while (!status.equals("READY"));
				
				errorMessage = "ERROR: Fetching world failed. ";
				theWorld = (World)xstream.fromXML(tcpClient.getData("POLLWORLD"));
				
				System.out.println("Starting Game!");
				
				theWorld.getPlayerTile(PLAYER_NUM);
				
				System.out.println(theWorld.describe(PLAYER_NUM));
				
				errorMessage = "Error: Getting first action failed. ";
				
				do {
					//prompt for action
					System.out.println("What do you do next?");
					String input = reader.readLine();
					parse(input);
					//check for event
				} while(true);
			}
		
		
		
		
		
		
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ReturnException e) {
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
			for (Entryway e : (localTile.getExits())) {
				exits.add(e.getOrientation().toString());
			}
			if (input.contains("north") & exits.contains("North")) {
				result = "MOVE;NORTH";			
			} else if (input.contains("east") & exits.contains("East")) {
				result = "MOVE;EAST";
			} else if (input.contains("south") & exits.contains("South")) {
				result = "MOVE;SOUTH";
			} else if (input.contains("west") & exits.contains("WEST")) {
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
