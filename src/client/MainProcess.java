package client;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import exceptions.ReturnException;
import framework.Entryway;
import framework.DirtEvent;
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
	private List<Integer> clearedEvents = new ArrayList<Integer>();
	
	public MainProcess() {
		xstream = new XStream();
		xstream.alias("World", World.class);
		xstream.alias("Tile", Tile.class);
		xstream.alias("Item", Item.class);
		xstream.alias("MovableItem", MovableItem.class);
		xstream.alias("Entryway", Entryway.class);
		xstream.alias("Character", framework.Character.class);
		xstream.alias("Event", framework.DirtEvent.class);
		xstream.alias("String", String.class);
		
		try {
			UI.createAndShowGUI();
			// Connect
			errorMessage = "ERROR: Joining Server Failed. ";
			
			UI.addToOutput("Connecting to Server", false);
			UI.addToOutput("Enter server IP >", false);
			String ip = UI.getInputResult();
			UI.addToOutput("Enter port >", false);
			int port = Integer.parseInt(UI.getInputResult());
			//theUI.addToOutput("Using local settings");
			//String ip = "127.0.0.1";
			//int port = 12345;
			tcpClient.Connect(ip, port);
			
			while (!tcpClient.IsConnected()) {
				UI.addToOutput("Waiting for server...", false);
				Thread.sleep(5000);
				tcpClient.Connect(ip, port);
			} 
			
			UI.addToOutput("Connected!", false);
			errorMessage = "ERROR: Joining game failed. ";
			PLAYER_NUM = Integer.parseInt(tcpClient.getData("JOIN"));
			UI.addToOutput("You are player " + PLAYER_NUM, false);
						
			while(tcpClient.IsConnected()) {
				
				errorMessage = "ERROR: Starting game failed. ";
				String status = "";
				while (!status.equals("READY")) {
					status = tcpClient.getData("STATUS");
				}
				
				errorMessage = "ERROR: Fetching world failed. ";
				theWorld = (World)xstream.fromXML(tcpClient.getData("POLLWORLD"));
				
				UI.addToOutput("Starting Game!", false);
				
				errorMessage = "Error: Getting actions failed. ";
				UI.addToOutput(theWorld.describe(PLAYER_NUM), false);
				
				do {
					UI.addToOutput("What do you do now?", false);
					String input = UI.getInputResult();
					UpdateWorld();
					boolean interactedWithEvent = false;
					for (DirtEvent e : localTile.getEvents()) {
						boolean isCleared = false;
						for (Integer integer : clearedEvents) {
							if (integer.intValue() == e.getId()) {
								isCleared = true;
								break;
							}
						}
						if (!isCleared) {
							switch (e.getFiredBy()) {
							case INTERACT:
								boolean successfullyInteracted = true;
								for (String s : e.getIteractString().split(";")) {
									if (!input.toLowerCase().contains(s.toLowerCase())) {
										successfullyInteracted = false;
										break;
									}
								}
								interactedWithEvent = successfullyInteracted;
								if (successfullyInteracted) {
									e.fire(theWorld.getCharacters().get(PLAYER_NUM));
									if (e.hasFired()) {
										clearedEvents.add(e.getId());
										if (e.getRewards().size() > 0) {
											UI.addToOutput("----REWARDS----", false);
											for (MovableItem item : e.getRewards()) {
												theWorld.getCharacters().get(PLAYER_NUM).addToInventory(item);
												UI.addToOutput(item.getName() + " added to inventory!", false);
												tcpClient.sendMessage("TAKE;" + item.getName());
											}
											UI.addToOutput("---------------", false);
										}
									}
								}
								break;
							default:
								break;
							}
						}
					}
					if (interactedWithEvent) {
						continue;
					}
					String parseOnServer = parse(input);
					if (!parseOnServer.isEmpty()) {
						tcpClient.sendMessage(parseOnServer);
					}
					UpdateWorld();
					for (DirtEvent e : localTile.getEvents()) {
						boolean isCleared = false;
						for (Integer integer : clearedEvents) {
							if (integer.intValue() == e.getId()) {
								isCleared = true;
								break;
							}
						}
						if (!isCleared) {
							switch (e.getFiredBy()) {
							case AUTO:
								e.fire(theWorld.getCharacters().get(PLAYER_NUM));
								break;
							case HASITEM:
								for (Item i : theWorld.getCharacters().get(PLAYER_NUM).getInventory()) {
									if (i.getName().equals(e.getFiredItemName())) {
										e.fire(theWorld.getCharacters().get(PLAYER_NUM));
										if (e.hasFired()) {
											clearedEvents.add(e.getId());
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
			UI.addToOutput(errorMessage + e.getMessage(), false);
			e.printStackTrace();
			tcpClient.Disconnect();
		} catch (ReturnException e) {
			UI.addToOutput(errorMessage + e.getMessage(), false);
			e.printStackTrace();
			tcpClient.Disconnect();
		} catch (InterruptedException e) {
			UI.addToOutput(errorMessage + e.getMessage(), false);
			e.printStackTrace();
			tcpClient.Disconnect();
		}		
	}
	
	private void UpdateWorld() {
		try {
			theWorld = (World)xstream.fromXML(tcpClient.getData("POLLWORLD"));
			if (localTile != null) {
				if (localTile.getX() != theWorld.getPlayerTile(PLAYER_NUM).getX() || localTile.getY() != theWorld.getPlayerTile(PLAYER_NUM).getY()) {
					UI.addToOutput(theWorld.describe(PLAYER_NUM), false);
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
				UI.addToOutput("Error: localTile not initialized", false);
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
								UI.addToOutput("The way north is closed.", false);
							}
						}
						break;
					case East:
						if (input.contains("east")) {
							doorFound = true;
							if (e.isOpen()) {
								result = "MOVE;EAST";
							} else {
								UI.addToOutput("The way east is closed.", false);
							}
						}
						break;
					case South:
						if (input.contains("south")) {
							doorFound = true;
							if (e.isOpen()) {
								result = "MOVE;SOUTH";
							} else {
								UI.addToOutput("The way south is closed.", false);
							}
						}
						break;
					case West:
						if (input.contains("west")) {
							doorFound = true;
							if (e.isOpen()) {
								result = "MOVE;WEST";
							} else {
								UI.addToOutput("The way west is closed.", false);
							}
						}
						break;
					}
				}
				if (!doorFound) {
					UI.addToOutput("You can't go this way.", false);
				}
			}
		} else if (input.contains("look")) {
			if (input.contains("around")) {
				UpdateWorld();
				UI.addToOutput(theWorld.describe(PLAYER_NUM), false);
			} else {
				boolean itemFound = false;
				for (Item item : theWorld.getPlayerTile(PLAYER_NUM).getItems()) {
					if (input.contains(item.getName())) {
						UI.addToOutput(item.getDescription(), false);
						itemFound = true;
						break;
					}
				}
				if (!itemFound) {
					UI.addToOutput("Look at what?", false);
				}
			}
		} else if (input.contains("examine")) {
			if (input.contains("inventory")) {
				String output = "Looking closer at your inventory, you have";
				List<MovableItem> inventory = theWorld.getCharacters().get(PLAYER_NUM).getInventory();
				if (inventory.size() == 0) {
					output += " nothing.";
				} else {
					output += ":\n";
					for (MovableItem item : inventory) {
						output += "--> " + item.getName() + " : " + item.getFull_description() + "\n";
					}
				}
				UI.addToOutput(output, false);
			} else {
				boolean itemFound = false;
				for (Item item : theWorld.getPlayerTile(PLAYER_NUM).getItems()) {
					if (input.contains(item.getName())) {
						UI.addToOutput(item.getFull_description(), false);
						itemFound = true;
						break;
					}
				}
				if (!itemFound) {
					UI.addToOutput("Invalid item", false);
				}
			}
		} else if (input.contains("check inventory")) {
			UI.addToOutput("Inventory:", false);
			UI.addToOutput("--------------", false);
			for (MovableItem item : theWorld.getCharacters().get(PLAYER_NUM).getInventory()) {
				UI.addToOutput(item.getName(), false);				
			}
			UI.addToOutput("--------------", false);
		} else if (input.contains("take")) {
			boolean itemFound = false;
			for (Item item : theWorld.getPlayerTile(PLAYER_NUM).getItems()) {
				if (input.contains(item.getName())) {
					if (item.getClass() == MovableItem.class) {
						UI.addToOutput(item.getName() + " placed in inventory.", false);
						theWorld.getCharacters().get(PLAYER_NUM).addToInventory((MovableItem)item);
						theWorld.getPlayerTile(PLAYER_NUM).getItems().remove(item);
						result = "TAKE;" + item.getName();
					} else {
						UI.addToOutput("You can't carry that!", false);
					}
					itemFound = true;
					break;
				}
			}
			if (!itemFound) {
				UI.addToOutput("Invalid item", false);
			}
		} else if (input.contains("drop")) {
			boolean itemFound = false;
			for (MovableItem item : theWorld.getCharacters().get(PLAYER_NUM).getInventory()) {
				if (input.contains(item.getName())) {
					UI.addToOutput("Dropped " + item.getName(), false);
					theWorld.getCharacters().get(PLAYER_NUM).getInventory().remove(item);
					theWorld.getPlayerTile(PLAYER_NUM).getItems().add(item);
					result = "DROP;" + item.getName();
				} else {
					UI.addToOutput("You do not possess that item.", false);
				}
					
				itemFound = true;
				break;
			}
			if (!itemFound) {
				UI.addToOutput("Invalid item", false);
			}
		} else if (input.contains("check doors")) {

			UI.addToOutput("Door status:", false);
			String output = "";

			for (Entryway e : (localTile.getExits())) {
				output += "The " + e.getOrientation().toString() + " door is ";
				if (e.isOpen()) {

					output += "open.\n";
				} else {
					output += "closed.\n";
				}
			}
			UI.addToOutput(output, false);
			UI.addToOutput("--------------", false);

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
							UI.addToOutput("The door is opened.", false);
						}
						break;
					case East:
						if (input.contains("east")) {
							doorFound = true;
							e.open();
							result = "OPENDOOR;EAST";
							UI.addToOutput("The door is opened.", false);
						}
						break;
					case South:
						if (input.contains("south")) {
							doorFound = true;
							e.open();
							result = "OPENDOOR;SOUTH";
							UI.addToOutput("The door is opened.", false);
						}
						break;
					case West:
						if (input.contains("west")) {
							doorFound = true;
							e.open();
							result = "OPENDOOR;WEST";
							UI.addToOutput("The door is opened.", false);
						}
						break;
					}
					if (doorFound) {
						break;
					}
				}
				if (!doorFound) {
					UI.addToOutput("Which door?", false);
				}
			} else {
				for (Item item : localTile.getItems()) {
					if (item.getClass() == OpenableItem.class) {
						if (input.contains(item.getName())) {
							boolean canOpen = ((OpenableItem)item).open();
							if (canOpen) {
								UI.addToOutput("It is open.", false);
							} else {
								UI.addToOutput("It won't budge.", false);
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
							UI.addToOutput("The door is closed.", false);
						}
						break;
					case East:
						if (input.contains("east")) {
							doorFound = true;
							e.close();
							result = "CLOSEDOOR;EAST";
							UI.addToOutput("The door is closed.", false);
						}
						break;
					case South:
						if (input.contains("south")) {
							doorFound = true;
							e.close();
							result = "CLOSEDOOR;SOUTH";
							UI.addToOutput("The door is closed.", false);
						}
						break;
					case West:
						if (input.contains("west")) {
							doorFound = true;
							e.close();
							result = "CLOSEDOOR;WEST";
							UI.addToOutput("The door is closed.", false);
						}
						break;
					}
					if (doorFound) {
						break;
					}
				}
				if (!doorFound) {
					UI.addToOutput("Which door?", false);
				}
			} else {
				for (Item item : localTile.getItems()) {
					if (item.getClass() == OpenableItem.class) {
						if (input.contains(item.getName())) {
							boolean canClose = ((OpenableItem)item).close();
							if (canClose) {
								UI.addToOutput("It is closed.", false);
							} else {
								UI.addToOutput("It won't budge.", false);
							}
							break;
						}
					}
				}
			}
		} else if (input.contains("use")) {
			
		} else UI.addToOutput("I don't understand. Try again?", false);
				
		return result;
	}
	
	
	public static void main(String[] args) {
		new MainProcess();
	}

}
