package client;

import exceptions.ReturnException;

public class MainProcess {
	
	private TCPClient tcpClient = new TCPClient();
	
	public MainProcess() {
		try {
			UI.createAndShowGUI();
			
			UI.addToOutput("Connecting to Server");
			UI.addToOutput("Enter server IP >");
			String ip = UI.getInputResult();
			UI.addToOutput("Enter port >");
			int port = Integer.parseInt(UI.getInputResult());
			
			tcpClient.Connect(ip, port);
			
			while (!tcpClient.IsConnected()) {
				UI.addToOutput("Waiting for server...");
				Thread.sleep(5000);
				tcpClient.Connect(ip, port);
			} 
			
			while(tcpClient.IsConnected()) {
	
				UI.addToOutput("Connected!");
				UI.addToOutput("Checking for other players...");
				
				String status = "";
				while (!status.equals("READY")) {
					status = tcpClient.getData("STATUS");
				}
				
				UI.addToOutput("Good to go! Enjoy your wet_dirt experience!");
				UI.addToOutput(tcpClient.getData("START").replaceAll("::NEWLINE", "\n"));
				
				do {
					UI.addToOutput("What do you do now?");
					String input = UI.getInputResult();
					String response = tcpClient.getData(input).replaceAll("::NEWLINE", "\n");
					if (response.equals("QUIT")) {
						break;
					}
					UI.addToOutput(response);
				} while (true);
			}
		} catch (InterruptedException e) {
			UI.addToOutput("An error occured. Connection interrupted.");
			tcpClient.Disconnect();
		} catch (ReturnException e) {
			UI.addToOutput("An error occured.");
			tcpClient.Disconnect();
		}
	}
	
	public static void main(String[] args) {
		new MainProcess();
	}

}
