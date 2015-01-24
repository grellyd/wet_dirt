package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.thoughtworks.xstream.XStream;

import framework.Event;
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
					Event event = new Event(new Tile(x, y, null, "", null));
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
							running = false;
							break;
						}
						
						String response = "";
						String[] split = rawmsg.split(";");

						
						switch (split[0]) {
						case "STATUS":
							if (world.GetCurrentPlayerNum() < world.GetReqPlayerNum()) {
								response = "WAITING";
							} else {
								response = "READY";
							}
							break;
						case "JOIN":
							if (world.GetCurrentPlayerNum() < world.GetReqPlayerNum()) {
								response = Integer.toString(world.GetCurrentPlayerNum() + 1);
							}
						case "POLLWORLD":
							response = xstream.toXML(world).replace(System.getProperty("line.separator"),  "");
							break;
						case "MOVE":
							if (split.length > 1) {
								switch (split[1]) {
								case "NORTH":
									world.MovePlayer(playerId, World.DIRECTION.NORTH);
									break;
								case "EAST":
									world.MovePlayer(playerId, World.DIRECTION.EAST);
									break;
								case "SOUTH":
									world.MovePlayer(playerId, World.DIRECTION.SOUTH);
									break;
								case "WEST":
									world.MovePlayer(playerId, World.DIRECTION.WEST);
									break;
								case "TELEPORT":
									// Teleport to new location
									break;
								}
								response = "OK";
							} else {
								response = "INVALID";
							}
							break;
						case "PICKUP":
							try {
								if (split.length > 1) {
									int thingId = Integer.parseInt(split[1]);
									//Pickup thing
									response = "OK";
								} else {
									response = "INVALID";
								}
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							break;
						case "DROP":
							try {
								if (split.length > 1) {
									int thingId = Integer.parseInt(split[1]);
									//Drop thing
									response = "OK";
								} else {
									response = "INVALID";	
								}
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							break;
						default:
							response = "INVALID: " + split[0];
							break;
						}
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
