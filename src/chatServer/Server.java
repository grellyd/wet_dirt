package chatServer;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Hashtable;

public class Server {
	
	private OutputStream outputStream;
	private ServerSocket ss;
	private Hashtable outputStreams = new Hashtable();

	public static void main(String[] args) throws IOException {
		
		// Get the port # from the command line
		int port = Integer.parseInt(args[0]);
		
		// Create a Server object, which will automatically begin
		// accepting connections
		new Server (port);
		
	}
	
	public Server(int port) throws IOException {
		listen(port);		
	}
	
	public void listen (int port) throws IOException {
		// Create the ServerSocket 
		ss = new ServerSocket (port);
		
		// Print we're ready to go
		System.out.println("Listening on ServerSocket " + ss);
		
		// keep listening forever, using the while loop
		// as a blocking lock
		
		while (true) {
			
			// Get the next connection
			Socket s = ss.accept();
			
			// Print we have the connection
			System.out.println("Connected to " + s );
			
			// Create a DataOutputStream for writing data
			// to the other side. 
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			
			// Save this stream so it doesn't need to be remade
			outputStreams.put(s, dout);
			
			// Create a new thread for this connection, and then
			// forget about it. This thread will deal with the
			// new connection, and is an object of type ServerThread
			ServerThread sthread = new ServerThread (this, s);
			
		}
	}
	
	// Get an enumeration of all the OutputStreams, one for each client
	// that is connected.	
	Enumeration getOutputStreams() {
		return outputStreams.elements();
	}
	
	public void sendToAll(String message) {
		
		// We sync on this because another thread might be
		// calling removeConnection as we tried to walk through the list.
		synchronized(outputStreams) {
			// for each client...
			for (Enumeration e = getOutputStreams(); e.hasMoreElements();) {
				// get the stream...
				DataOutputStream dout = (DataOutputStream) e.nextElement();
				// and send the message
				try {
					dout.writeUTF(message);
				} catch (IOException ie) {
					System.out.println("IOError in sendToAll in Server");
				}
			}
		}
	}
	
	public void removeConnection(Socket socket) {
		// Remove a socket, and its correspoinding output stream, from out list
		// make sure it doesn't mess up sendToAll
		synchronized(outputStreams) {
			// Tell the world
			System.out.println("Removing connection to " + socket);
			// remove it from our list
			outputStreams.remove(socket);
			// make sure its closed
			try {
				socket.close();
			} catch (IOException ie) {
				System.out.println("Error closing " + socket);
				ie.printStackTrace();
			}
		}
	}

}
