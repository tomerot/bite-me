package client;

import java.io.IOException;

import containers.ClientRequestDataContainer;
import containers.ServerResponseDataContainer;
import enums.ClientRequest;
import gui.controllers.ClientConnectFormController;
import ocsf.client.AbstractClient;

//class for handling the client's connection to the server, sending requests, and processing responses.
public class ClientConsole extends AbstractClient {
	// Singleton instance of the client
	private static ClientConsole client = null;
	public static boolean awaitResponse = false;
	private ClientConnectFormController clientController;
	public static ServerResponseDataContainer responseFromServer;

	// constructor getting host,port,clientGuiController, private constructor for
	// singleton
	private ClientConsole(String host, int port, ClientConnectFormController clientController) throws IOException {
		super(host, port);
		this.clientController = clientController;
		// method of AbstractClient to open connection to server
		openConnection();
	}

	// Method to connect client to server, ensuring only one instance exists
	public static boolean connectClientToServer(String host, String port, ClientConnectFormController clientController) {
		// If client already exists, return false
		if (client != null) {
			clientController.printToConsole("The client is already connected!");
			return false;
		}
		try {
			// Create new client instance
			client = new ClientConsole(host, Integer.parseInt(port), clientController);
			return true;
		} catch (IOException ex) {
			clientController.printToConsole("Error while connection Client to Server");
		} catch (Exception e) {
			clientController.printToConsole(e.getMessage());
		}
		return false;
	}
		
	// send massage to the server to disconnect the client
	public static void disconnectClientFromServer() {
	    try {
	        if (client != null) {
	        	ClientRequestDataContainer data = new ClientRequestDataContainer(ClientRequest.DISCONNECT, null);
	            client.sendToServer(data);
	            client.closeConnection();
	            client = null;
	            System.out.println("Client connection closed");
	        }
	    } catch (IOException e) {
	        System.out.println("Error closing client connection: " + e.getMessage());
	    }
	}

    protected void handleMessageFromServer(Object msg) {
        awaitResponse = false;
        responseFromServer = (ServerResponseDataContainer) msg;
        System.out.println(msg);
    }

	public static void handleMessageFromClientUI(ClientRequestDataContainer message) {
		try {
			awaitResponse = true;
			client.openConnection();// in order to send more than one message
			client.sendToServer(message);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			quit();
		}
	}
	
	public static void quit() {
		try {
			client.closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
