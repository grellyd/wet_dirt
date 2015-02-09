package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import framework.DirtEvent;
import framework.Entryway;
import framework.Item;
import framework.MovableItem;
import framework.OpenableItem;
import framework.Tile;
import framework.World;

public class NetworkManager implements Runnable {	
	private ServerSocket ss;
	private Socket socket;
	private final int PORT = 12345;
	private boolean running;
	
	private World world;
	private XStream xstream;
	
	public NetworkManager(World world) {
		System.out.println("Initiating NetworkManager...");
		try {
			this.world = world;
			xstream = new XStream();
			xstream.alias("World", World.class);
			running = true;
			ss = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		for (int i = 0; i < world.GetReqPlayerNum(); i++) {
			try {
				System.out.println("Awaiting player " + i + "...");
				socket = ss.accept();
				new Thread(new ClientThread(socket, world)).start();
				System.out.println("Player connected.");
			} catch (IOException e) {
				System.out.println("Error binding to port");
				Cleanup();
				break;
			}
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (running) {
			System.out.print("> ");
			try {
				String command = reader.readLine();
				switch (command) {
				case "DumpWorldState":
					System.out.println(xstream.toXML(world));
					break;
				case "GenerateGlobalEvent":
					int x, y, range;
					String desc;
					System.out.print("X: ");
					x = Integer.parseInt(reader.readLine());
					System.out.print("Y: ");
					y = Integer.parseInt(reader.readLine());
					System.out.print("Range: ");
					range = Integer.parseInt(reader.readLine());
					System.out.print("Description: ");
					desc = reader.readLine();
					DirtEvent event = new DirtEvent(new Tile(x, y, null, "", null));
					event.setDescription(desc);
					event.setRange(range);
					world.fireEvent(event);
					break;
				default:
					System.out.println("Invalid command: " + command);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Cleanup();
	}
	
	public class ClientThread implements Runnable {
		private Socket socket;
		private World world;
		private int playerId;
		private boolean running = true;
		private List<Integer> clearedEvents = new ArrayList<Integer>();
		
		public ClientThread(Socket socket, World world) {
			this.socket = socket;
			this.world = world;
			
			synchronized(world) {
				playerId = world.AddPlayer();
				if (playerId < 0) {
					running = false;
				}
			}
		}

		@Override
		public void run() {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				while (running) {
					String rawmsg = reader.readLine();
					try {
						if (rawmsg == null) {
							System.out.println("Player " + playerId + " disconnected.");
							world.RemovePlayer();
							//TODO: Add handling for when a player disconnects
							running = false;
							break;
						}
						
						String response = "";
						switch (rawmsg) {
						case "STATUS":
							if (world.GetCurrentPlayerNum() < world.GetReqPlayerNum()) {
								response = "WAITING";
							} else {
								response = "READY";
							}
							break;
						case "START":
							response = world.describe(playerId);
							break;
						default:
							response = Parse(rawmsg);
							break;
						}
						
						response = response.replaceAll("\n", "::NEWLINE");
						writer.write(response);
						writer.newLine();
						writer.flush();
						System.out.println("Player " + playerId + ": " + rawmsg);
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}
			} catch (NullPointerException e) {
				System.out.println("Connection died");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private String FireEvent(DirtEvent event) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				
				String result = "--------------\n" + event.getDescription() + "\n";
				for (String question : event.getQuestions()) {
					result += question;
					writer.write(result.replaceAll("\n", "::NEWLINE"));
					writer.newLine();
					writer.flush();
					event.getUserAnswers().add(reader.readLine());
					result = "";
				}
				if (event.passed()) {
					result = event.getSuccessText() + "\n";
					clearedEvents.add(event.getId());
					if (event.getRewards().size() > 0) {
						result += "----REWARDS----\n";
						for (MovableItem item : event.getRewards()) {
							world.getCharacters().get(playerId).addToInventory(item);
							result += item.getName() + " added to inventory!\n";
						}
						result += "---------------";
					}
					return result;
				} else {
					return event.getFailureText();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}	
		}
		
		private String Parse(String input) {
			input = input.toLowerCase();
			String result = "";
			Tile curTile = world.getPlayerTile(playerId);
			
			boolean interactedWithEvent = false;
			for (DirtEvent e : curTile.getEvents()) {
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
							if (!input.contains(s.toLowerCase())) {
								successfullyInteracted = false;
								break;
							}
						}
						interactedWithEvent = successfullyInteracted;
						if (successfullyInteracted) {
							DirtEvent event = new DirtEvent(e);
							result = FireEvent(event);
						}
						break;
					default:
						break;
					}
				}
			}
			if (interactedWithEvent) {
				return result;
			}
			
			// look for starting keywords
			if (input.contains("move")){				
				if (curTile.getExits() != null) {
					boolean doorFound = false;
					for (Entryway e : (curTile.getExits())) {
						switch (e.getOrientation()) {
						case North:
							if (input.contains("north")) {
								doorFound = true;
								if (e.isOpen()) {
									System.out.println("Moving player " + playerId + " north");
									world.MovePlayer(playerId, World.DIRECTION.NORTH);
									result = world.describe(playerId);
								} else {
									result = "The way north is closed.";
								}
							}
							break;
						case East:
							if (input.contains("east")) {
								doorFound = true;
								if (e.isOpen()) {
									System.out.println("Moving player " + playerId + " east");
									world.MovePlayer(playerId, World.DIRECTION.EAST);
									result = world.describe(playerId);
								} else {
									result = "The way east is closed.";
								}
							}
							break;
						case South:
							if (input.contains("south")) {
								doorFound = true;
								if (e.isOpen()) {
									System.out.println("Moving player " + playerId + " south");
									world.MovePlayer(playerId, World.DIRECTION.SOUTH);
									result = world.describe(playerId);
								} else {
									result = "The way south is closed.";
								}
							}
							break;
						case West:
							if (input.contains("west")) {
								doorFound = true;
								if (e.isOpen()) {
									System.out.println("Moving player " + playerId + " west");
									world.MovePlayer(playerId, World.DIRECTION.WEST);
									result = world.describe(playerId);
								} else {
									result = "The way west is closed.";
								}
							}
							break;
						}
					}
					if (!doorFound) {
						result = "You can't go this way.";
					}
				}
			} else if (input.contains("look")) {
				if (input.contains("around")) {
					result = world.describe(playerId);
				} else {
					boolean itemFound = false;
					for (Item item : curTile.getItems()) {
						if (input.contains(item.getName())) {
							result = item.getDescription();
							itemFound = true;
							break;
						}
					}
					if (!itemFound) {
						result = "Look at what?";
					}
				}
			} else if (input.contains("examine")) {
				if (input.contains("inventory")) {
					result = "Looking closer at your inventory, you have";
					List<MovableItem> inventory = world.getCharacters().get(playerId).getInventory();
					if (inventory.size() == 0) {
						result += " nothing.";
					} else {
						result += ":\n";
						for (MovableItem item : inventory) {
							result += "--> " + item.getName() + " : " + item.getFull_description() + "\n";
						}
						result += "--------------";
					}
				} else {
					boolean itemFound = false;
					for (Item item : curTile.getItems()) {
						if (input.contains(item.getName())) {
							result = item.getFull_description();
							itemFound = true;
							break;
						}
					}
					if (!itemFound) {
						result = "I don't know what that is...";
					}
				}
			} else if (input.contains("check inventory")) {
				result = "Inventory:\n";
				result += "--------------\n";
				for (MovableItem item : world.getCharacters().get(playerId).getInventory()) {
					result += item.getName() + "\n";				
				}
				result += "--------------";
			} else if (input.contains("take")) {
				boolean itemFound = false;
				for (Item item : curTile.getItems()) {
					if (input.contains(item.getName())) {
						if (item.getClass() == MovableItem.class) {
							result = item.getName() + " placed in inventory.";
							world.getCharacters().get(playerId).addToInventory((MovableItem)item);
							curTile.getItems().remove(item);
						} else {
							result = "You can't carry that!";
						}
						itemFound = true;
						break;
					}
				}
				if (!itemFound) {
					result = "I don't know what that is...";
				}
			} else if (input.contains("drop")) {
				boolean itemFound = false;
				for (MovableItem item : world.getCharacters().get(playerId).getInventory()) {
					if (input.contains(item.getName())) {
						result = "Dropped " + item.getName();
						world.getCharacters().get(playerId).getInventory().remove(item);
						curTile.getItems().add(item);
					} else {
						result = "You do not possess that item.";
					}
						
					itemFound = true;
					break;
				}
				if (!itemFound) {
					result = "I don't know what that is...";
				}
			} else if (input.contains("check doors")) {
				result = "Door status:\n";
				for (Entryway e : (curTile.getExits())) {
					result += "The " + e.getOrientation().toString() + " door is ";
					if (e.isOpen()) {
						result += "open.\n";
					} else {
						result += "closed.\n";
					}
				}
				result += "--------------";
			} else if (input.contains("open")) {
				if (input.contains("door")) {
					boolean doorFound = false;
					for (Entryway e : curTile.getExits()) {
						if (input.contains("north") && e.getOrientation().toString().toLowerCase().equals("north")) {
							e.open();
							doorFound = true;
							result = "The door is opened";
							Tile playerTile = curTile;
							if (playerTile.getY() - 1 >= 0) {
								List <Entryway> adjacentExits = world.getMapTile(playerTile.getX(), playerTile.getY() - 1).getExits();
								for (Entryway ae : adjacentExits) {
									if (ae.getOrientation().toString().toLowerCase().equals("south")) {
										ae.open();
										break;
									}
								}
							}
							break;
						} else if (input.contains("east") && e.getOrientation().toString().toLowerCase().equals("east")) {
							e.open();
							doorFound = true;
							result = "The door is opened";
							Tile playerTile = curTile;
							if (playerTile.getX() + 1 >= 0) {
								List <Entryway> adjacentExits = world.getMapTile(playerTile.getX() + 1, playerTile.getY()).getExits();
								for (Entryway ae : adjacentExits) {
									if (ae.getOrientation().toString().toLowerCase().equals("west")) {
										ae.open();
										break;
									}
								}
							}
							break;
						} else if (input.contains("west") && e.getOrientation().toString().toLowerCase().equals("west")) {
							e.open();
							doorFound = true;
							result = "The door is opened";
							Tile playerTile = curTile;
							if (playerTile.getX() - 1 >= 0) {
								List <Entryway> adjacentExits = world.getMapTile(playerTile.getX() - 1, playerTile.getY()).getExits();
								for (Entryway ae : adjacentExits) {
									if (ae.getOrientation().toString().toLowerCase().equals("east")) {
										ae.open();
										break;
									}
								}
							}
							break;
						} else if (input.contains("south") && e.getOrientation().toString().toLowerCase().equals("south")) {
							e.open();
							doorFound = true;
							result = "The door is opened";
							Tile playerTile = curTile;
							if (playerTile.getY() + 1 < world.getMapHeight()) {
								List <Entryway> adjacentExits = world.getMapTile(playerTile.getX(), playerTile.getY() + 1).getExits();
								for (Entryway ae : adjacentExits) {
									if (ae.getOrientation().toString().toLowerCase().equals("north")) {
										ae.open();
										break;
									}
								}
							}
							break;
						}
					}
					if (!doorFound) {
						result = "Which door?";
					}
				} else {
					for (Item item : curTile.getItems()) {
						if (item.getClass() == OpenableItem.class) {
							if (input.contains(item.getName())) {
								boolean canOpen = ((OpenableItem)item).open();
								if (canOpen) {
									result = "It is open.";
								} else {
									result = "It won't budge.";
								}
								break;
							}
						}
					}
				}
			} else if (input.contains("close")) {
				if (input.contains("door")) {
					boolean doorFound = false;
					for (Entryway e : curTile.getExits()) {
						if (input.contains("north") && e.getOrientation().toString().toLowerCase().equals("north")) {
							e.close();
							doorFound = true;
							result = "The door is opened";
							Tile playerTile = curTile;
							if (playerTile.getY() - 1 >= 0) {
								List <Entryway> adjacentExits = world.getMapTile(playerTile.getX(), playerTile.getY() - 1).getExits();
								for (Entryway ae : adjacentExits) {
									if (ae.getOrientation().toString().toLowerCase().equals("south")) {
										ae.close();
										break;
									}
								}
							}
							break;
						} else if (input.contains("east") && e.getOrientation().toString().toLowerCase().equals("east")) {
							e.close();
							doorFound = true;
							result = "The door is opened";
							Tile playerTile = curTile;
							if (playerTile.getX() + 1 >= 0) {
								List <Entryway> adjacentExits = world.getMapTile(playerTile.getX() + 1, playerTile.getY()).getExits();
								for (Entryway ae : adjacentExits) {
									if (ae.getOrientation().toString().toLowerCase().equals("west")) {
										ae.close();
										break;
									}
								}
							}
							break;
						} else if (input.contains("west") && e.getOrientation().toString().toLowerCase().equals("west")) {
							e.close();
							doorFound = true;
							result = "The door is opened";
							Tile playerTile = curTile;
							if (playerTile.getX() - 1 >= 0) {
								List <Entryway> adjacentExits = world.getMapTile(playerTile.getX() - 1, playerTile.getY()).getExits();
								for (Entryway ae : adjacentExits) {
									if (ae.getOrientation().toString().toLowerCase().equals("east")) {
										ae.close();
										break;
									}
								}
							}
							break;
						} else if (input.contains("south") && e.getOrientation().toString().toLowerCase().equals("south")) {
							e.close();
							doorFound = true;
							result = "The door is opened";
							Tile playerTile = curTile;
							if (playerTile.getY() + 1 < world.getMapHeight()) {
								List <Entryway> adjacentExits = world.getMapTile(playerTile.getX(), playerTile.getY() + 1).getExits();
								for (Entryway ae : adjacentExits) {
									if (ae.getOrientation().toString().toLowerCase().equals("north")) {
										ae.close();
										break;
									}
								}
							}
							break;
						}
					}
					if (!doorFound) {
						result = "Which door?";
					}
				} else {
					for (Item item : curTile.getItems()) {
						if (item.getClass() == OpenableItem.class) {
							if (input.contains(item.getName())) {
								boolean canClose = ((OpenableItem)item).close();
								if (canClose) {
									result = "It is closed.";
								} else {
									result = "It won't budge.";
								}
								break;
							}
						}
					}
				}
			} else if (input.contains("use")) {
				result = "WE SHOULD CODE THIS AT SOME POINT";
			}
			if (result.equals("")) {
				result = "Sorry, I don't quite understand...";
			}
			return result;
		}
	}
	
	public void Terminate() {
		running = false;
		Cleanup();
	}
	
	public void Cleanup() {
		try {
			System.out.println("Cleaning up server thread...");
			if (socket != null) {
				socket.close();
			}
			ss.close();
		} catch (IOException e) {
			System.out.println("Error cleaning up");
			System.exit(1);
		}
	}
}
