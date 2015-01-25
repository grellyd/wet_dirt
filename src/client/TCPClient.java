package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import exceptions.ReturnException;

public class TCPClient {
	private Socket serversocket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private boolean connected;
	
	public TCPClient() {
		serversocket = new Socket();
		connected = false;
	}
	
	public void Connect(String host, int port) {
		try {
			if (serversocket.isConnected()) {
				serversocket.close();
			}

			serversocket = new Socket(host, port);
			reader = new BufferedReader(new InputStreamReader(serversocket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(serversocket.getOutputStream()));
			connected = true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void sendMessage(String msg) {
		if (connected) {
			try {
				writer.write(msg);
				writer.newLine();
				writer.flush();		
				System.out.println(reader.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Not connected...");
		}
	}
	
	public void Disconnect() {
		try {
			serversocket.close();
			connected = false;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public boolean IsConnected() {
		return connected;
	}

	public String getData(String msg) throws ReturnException {
		String result = "";
		if (connected) {
			try {
				writer.write(msg);
				writer.newLine();
				writer.flush();
				result = reader.readLine();
				if (result == null) {
					System.out.println("Connection to server lost...");
					Disconnect();
					return "";
				}
				if (result.equals("")) {
					throw new ReturnException("Return was empty.");
				} 
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Not connected...");
		}
		return result;
		
	}
	

}