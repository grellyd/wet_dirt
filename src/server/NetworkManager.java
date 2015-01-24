package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.thoughtworks.xstream.XStream;

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
		while (running) {}
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
							writer.write(response);
							writer.newLine();
							writer.flush();
							break;
						case "POLLWORLD":
							writer.write(xstream.toXML(world).replace(System.getProperty("line.separator"),  ""));
							writer.newLine();
							writer.flush();
							break;
						case "MOVE":
							if (split.length > 1) {
								switch (split[1]) {
								case "NORTH":
									//Move player north
									break;
								case "EAST":
									//Move player north
									break;
								case "SOUTH":
									//Move player north
									break;
								case "WEST":
									//Move player north
									break;
								}
								writer.write("OK");
								writer.newLine();
								writer.flush();
							} else {
								writer.write("INVALID");
								writer.newLine();
								writer.flush();
							}
							break;
						case "PICKUP":
							try {
								if (split.length > 1) {
									int thingId = Integer.parseInt(split[1]);
									//Pickup thing
									writer.write("OK");
									writer.newLine();
									writer.flush();
								} else {
									writer.write("INVALID");
									writer.newLine();
									writer.flush();
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
									writer.write("OK");
									writer.newLine();
									writer.flush();
								} else {
									writer.write("INVALID");
									writer.newLine();
									writer.flush();
								}
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							break;
						default:
							writer.write("INVALID: " + split[0]);
							writer.newLine();
							writer.flush();
							break;
						}
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
