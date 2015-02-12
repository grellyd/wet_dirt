package chatServer;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {
	
	private Server server;
	private Socket socket;
	
	// Socket obj is key as this thread uses a socket
	// to communicate with the other side
	
	// Server obj is 
	
	public ServerThread(Server server, Socket sock) {
		
		// save params
		this.server = server;
		this.socket = sock;
		
		// start the thread. This will execute the run method.
		start();
	}
	
	public void run() {
		try {
			// Create a DataInputStream for communication
			// The client is using a DataOutputStream to write
			DataInputStream din = new DataInputStream(socket.getInputStream());
			
			while (true) {
				
				// read the next message
				String message = din.readUTF();
				
				System.out.println("Sending " + message);
				
				// could rename to broadcast?
				server.sendToAll(message);
			}
		} catch (EOFException ie) {
			System.out.println("EOFException in ServerThread");
		} catch (IOException ie) {
			ie.printStackTrace();
			System.out.println("IOException in ServerThread");
		} finally {
			// As the connection is closed, have the server deal
			// with it.
			server.removeConnection(socket);
		}
	}

}
