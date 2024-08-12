package client;

import java.io.IOException;

/**
 * This class manages the client's connection to the server using the OSCF framework.
 * It handles sending requests to the server and processing responses.
 * It also ensures that only one instance of the client exists at a time (singleton pattern).
 */

import containers.ClientRequestDataContainer;
import containers.ServerResponseDataContainer;
import enums.ClientRequest;
import enums.ServerResponse;
import gui.controllers.ClientConnectFormController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import ocsf.client.AbstractClient;

//class for handling the client's connection to the server, sending requests, and processing responses.
public class ClientConsole extends AbstractClient {
	// Singleton instance of the client
	private static ClientConsole client = null;
	public static boolean awaitResponse = false;
	private ClientConnectFormController clientController;
	public static ServerResponseDataContainer responseFromServer;

	/**
	 * Private constructor to initialize the client with the server's host and port,
	 * and the client controller for UI interactions.
	 *
	 * @param host             the server's host address
	 * @param port             the server's port number
	 * @param clientController the controller managing client-side UI interactions
	 * @throws IOException if an I/O error occurs while opening the connection
	 */
	private ClientConsole(String host, int port, ClientConnectFormController clientController) throws IOException {
		super(host, port);
		this.clientController = clientController;
		// method of AbstractClient to open connection to server
		openConnection();
	}

	/**
	 * Connects the client to the server, ensuring only one instance of the client
	 * is created.
	 *
	 * @param host             the server's host address
	 * @param port             the server's port number as a string
	 * @param clientController the controller managing client-side UI interactions
	 * @return true if the client was successfully connected / false if it is
	 *         already connected
	 */

	// Method to connect client to server, ensuring only one instance exists
	public static boolean connectClientToServer(String host, String port,
			ClientConnectFormController clientController) {
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

	/**
	 * Disconnects the client from the server and closes the connection.
	 */
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

	/**
	 * Handles messages received from the server and updates the response container.
	 *
	 * @param msg the message received from the server
	 */
	protected void handleMessageFromServer(Object msg) {
		awaitResponse = false;
		responseFromServer = (ServerResponseDataContainer) msg;
		
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		if(ServerResponse.MSG_TO_DISPLAY_FOR_CUSTOEMR.equals(response.getResponse())){
			Platform.runLater(() -> {
	            Alert alert = new Alert(Alert.AlertType.INFORMATION);
	            alert.setTitle("Order Simulation");
	            alert.setHeaderText("New Order Status!");
	            alert.setContentText((String) response.getMessage());
	            alert.showAndWait();
	        });
		}
	}

	/**
	 * Sends a message from the client UI to the server and waits for a response.
	 *
	 * @param message the message to be sent to the server
	 */
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

	/**
	 * Closes the client connection and exit the application.
	 */
	public static void quit() {
		try {
			client.closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
