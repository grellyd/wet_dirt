package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import exceptions.ReturnException;
import framework.Entryway;
import framework.Event;
import framework.Item;
import framework.MovableItem;
import framework.OpenableItem;
import framework.Tile;
import framework.World;

public class MainProcess {
	
	private static int PLAYER_NUM;
	
	private TCPClient tcpClient = new TCPClient();
	private XStream xstream;
	private String errorMessage = "";
	
	private World theWorld;
	private Tile localTile;
	private List<Event> clearedEvents = new ArrayList<Event>();
	
	public MainProcess() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		xstream = new XStream();
		xstream.alias("World", World.class);
		xstream.alias("Tile", Tile.class);
		xstream.alias("Item", Item.class);
		xstream.alias("MovableItem", MovableItem.class);
		xstream.alias("Entryway", Entryway.class);
		xstream.alias("Character", framework.Character.class);
		xstream.alias("Event", framework.Event.class);
		xstream.alias("String", String.class);
		
		try {
			// Connect
			errorMessage = "ERROR: Joining Server Failed. ";
			
			System.out.println("Connecting to Server");
			System.out.print("Enter server IP >");
			String ip = reader.readLine();
			System.out.print("Enter port >");
			int port = Integer.parseInt(reader.readLine());
			//System.out.println("Using local settings");
			//String ip = "127.0.0.1";
			//int port = 12345;
			tcpClient.Connect(ip, port);
			
			while (!tcpClient.IsConnected()) {
				System.out.println("Waiting for server...");
				Thread.sleep(5000);
				tcpClient.Connect(ip, port);
			} 
			
			System.out.println("Connected!");
			errorMessage = "ERROR: Joining game failed. ";
			PLAYER_NUM = Integer.parseInt(tcpClient.getData("JOIN"));
			System.out.println("You are player " + PLAYER_NUM);
						
			while(tcpClient.IsConnected()) {
				
				errorMessage = "ERROR: Starting game failed. ";
				String status = "";
				while (!status.equals("READY")) {
					status = tcpClient.getData("STATUS");
				}
				
				errorMessage = "ERROR: Fetching world failed. ";
				theWorld = (World)xstream.fromXML(tcpClient.getData("POLLWORLD"));
				
				System.out.println("Starting Game!");
				
				errorMessage = "Error: Getting actions failed. ";
				System.out.println(theWorld.describe(PLAYER_NUM));
				
				do {
					System.out.println("What do you do now?");
					String input = reader.readLine();
					UpdateWorld();
					for (Event e : localTile.getEvents()) {
						if (!clearedEvents.contains(e)) {
							switch (e.getFiredBy()) {
							case INTERACT:
								boolean succesfullyInteracted = true;
								for (String s : e.getIteractString().split(";")) {
									if (!input.toLowerCase().contains(s.toLowerCase())) {
										succesfullyInteracted = false;
										break;
									}
								}
								if (succesfullyInteracted) {
									e.fire(theWorld.getCharacters().get(PLAYER_NUM));
									if (e.hasFired()) {
										clearedEvents.add(e);
									}
								}
								break;
							default:
								break;
							}
						}
					}
					String parseOnServer = parse(input);
					if (!parseOnServer.isEmpty()) {
						tcpClient.sendMessage(parseOnServer);
					}
					UpdateWorld();
					for (Event e : localTile.getEvents()) {
						if (!clearedEvents.contains(e)) {
							switch (e.getFiredBy()) {
							case AUTO:
								e.fire(theWorld.getCharacters().get(PLAYER_NUM));
								break;
							case HASITEM:
								for (Item i : theWorld.getCharacters().get(PLAYER_NUM).getInventory()) {
									if (i.getName().equals(e.getFiredItem().getName())) {
										e.fire(theWorld.getCharacters().get(PLAYER_NUM));
										if (e.hasFired()) {
											clearedEvents.add(e);
										}
									}
								}
								break;
							default:
								break;
							}
						}
					}
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
	
	private void UpdateWorld() {
		try {
			theWorld = (World)xstream.fromXML(tcpClient.getData("POLLWORLD"));
			if (localTile != null) {
				if (localTile.getX() != theWorld.getPlayerTile(PLAYER_NUM).getX() || localTile.getY() != theWorld.getPlayerTile(PLAYER_NUM).getY()) {
					System.out.println(theWorld.describe(PLAYER_NUM));
				}
			}
			localTile = theWorld.getPlayerTile(PLAYER_NUM);
		} catch (ReturnException e) {
			e.printStackTrace();
		}
	}
	
	private String parse(String input) {
		input = input.toLowerCase();
		String result = "";
		
		// look for starting keywords
		if (input.contains("move")){
			if (localTile == null) {
				System.out.println("Error: localTile not initialized");
				return "";
			}			
			
			if (localTile.getExits() != null) {
				boolean doorFound = false;
				for (Entryway e : (localTile.getExits())) {
					switch (e.getOrientation()) {
					case North:
						if (input.contains("north")) {
							doorFound = true;
							if (e.isOpen()) {
								result = "MOVE;NORTH";
							} else {
								System.out.println("The way north is closed.");
							}
						}
						break;
					case East:
						if (input.contains("east")) {
							doorFound = true;
							if (e.isOpen()) {
								result = "MOVE;EAST";
							} else {
								System.out.println("The way east is closed.");
							}
						}
						break;
					case South:
						if (input.contains("south")) {
							doorFound = true;
							if (e.isOpen()) {
								result = "MOVE;SOUTH";
							} else {
								System.out.println("The way south is closed.");
							}
						}
						break;
					case West:
						if (input.contains("west")) {
							doorFound = true;
							if (e.isOpen()) {
								result = "MOVE;WEST";
							} else {
								System.out.println("The way west is closed.");
							}
						}
						break;
					}
				}
				if (!doorFound) {
					System.out.println("You can't go this way.");
				}
			}
		} else if (input.contains("look")) {
			if (input.contains("around")) {
				UpdateWorld();
				System.out.println(theWorld.describe(PLAYER_NUM));
			} else {
				boolean itemFound = false;
				for (Item item : theWorld.getPlayerTile(PLAYER_NUM).getItems()) {
					if (input.contains(item.getName())) {
						System.out.println(item.getDescription());
						itemFound = true;
						break;
					}
				}
				if (!itemFound) {
					System.out.println("Look at what?");
				}
			}
		} else if (input.contains("examine")) {
			boolean itemFound = false;
			for (Item item : theWorld.getPlayerTile(PLAYER_NUM).getItems()) {
				if (input.contains(item.getName())) {
					System.out.println(item.getFull_description());
					itemFound = true;
					break;
				}
			}
			if (!itemFound) {
				System.out.println("Invalid item");
			}
		} else if (input.contains("check inventory")) {
			System.out.println("Inventory:");
			System.out.println("--------------");
			for (MovableItem item : theWorld.getCharacters().get(PLAYER_NUM).getInventory()) {
				System.out.println(item.getName());				
			}
			System.out.println("--------------");
		} else if (input.contains("take")) {
			boolean itemFound = false;
			for (Item item : theWorld.getPlayerTile(PLAYER_NUM).getItems()) {
				if (input.contains(item.getName())) {
					if (item.getClass() == MovableItem.class) {
						System.out.println(item.getName() + " placed in inventory.");
						theWorld.getCharacters().get(PLAYER_NUM).addToInventory((MovableItem)item);
						theWorld.getPlayerTile(PLAYER_NUM).getItems().remove(item);
						result = "TAKE;" + item.getName();
					} else {
						System.out.println("You can't carry that!");
					}
					itemFound = true;
					break;
				}
			}
			if (!itemFound) {
				System.out.println("Invalid item");
			}
		} else if (input.contains("drop")) {
			boolean itemFound = false;
			for (MovableItem item : theWorld.getCharacters().get(PLAYER_NUM).getInventory()) {
				if (input.contains(item.getName())) {
					System.out.println("Dropped " + item.getName());
					theWorld.getCharacters().get(PLAYER_NUM).getInventory().remove(item);
					theWorld.getPlayerTile(PLAYER_NUM).getItems().add(item);
					result = "DROP;" + item.getName();
				} else {
					System.out.println("You do not possess that item.");
				}
					
				itemFound = true;
				break;
			}
			if (!itemFound) {
				System.out.println("Invalid item");
			}
		} else if (input.contains("check doors")) {
			System.out.println("Door status:");
			for (Entryway e : (localTile.getExits())) {
				System.out.print("The " + e.getOrientation().toString() + " door is ");
				if (e.isOpen()) {
					System.out.println("open.");
				} else {
					System.out.println("closed.");
				}
			}
			System.out.println("--------------");
		} else if (input.contains("open")) {
			if (input.contains("door")) {
				boolean doorFound = false;
				for (Entryway e : (localTile.getExits())) {
					switch (e.getOrientation()) {
					case North:
						if (input.contains("north")) {
							doorFound = true;
							e.open();
							result = "OPENDOOR;NORTH";
							System.out.println("The door is opened.");
						}
						break;
					case East:
						if (input.contains("east")) {
							doorFound = true;
							e.open();
							result = "OPENDOOR;EAST";
							System.out.println("The door is opened.");
						}
						break;
					case South:
						if (input.contains("south")) {
							doorFound = true;
							e.open();
							result = "OPENDOOR;SOUTH";
							System.out.println("The door is opened.");
						}
						break;
					case West:
						if (input.contains("west")) {
							doorFound = true;
							e.open();
							result = "OPENDOOR;WEST";
							System.out.println("The door is opened.");
						}
						break;
					}
					if (doorFound) {
						break;
					}
				}
				if (!doorFound) {
					System.out.println("Which door?");
				}
			} else {
				for (Item item : localTile.getItems()) {
					if (item.getClass() == OpenableItem.class) {
						if (input.contains(item.getName())) {
							boolean canOpen = ((OpenableItem)item).open();
							if (canOpen) {
								System.out.println("It is open.");
							} else {
								System.out.println("It won't budge.");
							}
							break;
						}
					}
				}
			}
		} else if (input.contains("close")) {
			if (input.contains("door")) {
				boolean doorFound = false;
				for (Entryway e : (localTile.getExits())) {
					switch (e.getOrientation()) {
					case North:
						if (input.contains("north")) {
							doorFound = true;
							e.close();
							result = "CLOSEDOOR;NORTH";
							System.out.println("The door is closed.");
						}
						break;
					case East:
						if (input.contains("east")) {
							doorFound = true;
							e.close();
							result = "CLOSEDOOR;EAST";
							System.out.println("The door is closed.");
						}
						break;
					case South:
						if (input.contains("south")) {
							doorFound = true;
							e.close();
							result = "CLOSEDOOR;SOUTH";
							System.out.println("The door is closed.");
						}
						break;
					case West:
						if (input.contains("west")) {
							doorFound = true;
							e.close();
							result = "CLOSEDOOR;WEST";
							System.out.println("The door is closed.");
						}
						break;
					}
					if (doorFound) {
						break;
					}
				}
				if (!doorFound) {
					System.out.println("Which door?");
				}
			} else {
				for (Item item : localTile.getItems()) {
					if (item.getClass() == OpenableItem.class) {
						if (input.contains(item.getName())) {
							boolean canClose = ((OpenableItem)item).close();
							if (canClose) {
								System.out.println("It is closed.");
							} else {
								System.out.println("It won't budge.");
							}
							break;
						}
					}
				}
			}
		} else if (input.contains("use")) {
			
		} else System.out.println("I don't understand. Try again?");
				
		return result;
	}
	
	
	public static void main(String[] args) {
		new MainProcess();
	}

}
