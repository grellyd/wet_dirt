package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.thoughtworks.xstream.XStream;

import sun.net.www.content.audio.x_aiff;
import exceptions.ReturnException;
import framework.World;

public class MainProcess {
	
	private static int PLAYER_NUM;
	
	private TCPClient tcpClient = new TCPClient();
	private XStream xstream;
	private String errorMessage = "";
	
	private World theWorld;
	
	
	
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
				
				theWorld.describe(PLAYER_NUM);
				
				errorMessage = "Error: Getting first action failed. ";
				
				do {
					//prompt for action
					
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
	
	
	public static void main(String[] args) {
		new MainProcess();
	}

}
