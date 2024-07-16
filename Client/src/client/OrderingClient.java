package client;

import java.io.IOException;
import java.util.List;

import gui.ClientGuiController;
import gui.HomeClientPageController;
import gui.ViewAndUpdateController;
import javafx.application.Platform;
import logic.ClientActionsEnum;
import logic.ClientDataContainer;
import logic.Order;
import logic.ServerActionsEnum;
import logic.ServerDataContainer;
import ocsf.client.AbstractClient;

//class for handling the client's connection to the server, sending requests, and processing responses.
public class OrderingClient extends AbstractClient {
	// Singleton instance of the client
	private static OrderingClient client = null;
	private ClientGuiController clientController;
	private static ViewAndUpdateController viewController;
	private static HomeClientPageController homeController;

	// constructor getting host,port,clientGuiController, private constructor for
	// singleton
	private OrderingClient(String host, int port, ClientGuiController clientController) throws IOException {
		super(host, port);
		this.clientController = clientController;
		// method of AbstractClient to open connection to server
		openConnection();
	}

	public static void setViewController(ViewAndUpdateController controller) {
		viewController = controller;
	}
	public static void setHomeController(HomeClientPageController controller) {
		homeController = controller;
	}

	// Method to connect client to server, ensuring only one instance exists
	public static boolean connectClientToServer(String host, String port, ClientGuiController clientController) {
		// If client already exists, return false
		if (client != null) {
			clientController.printToConsole("The client is already connected!");
			return false;
		}
		try {
			// Create new client instance
			client = new OrderingClient(host, Integer.parseInt(port), clientController);
			return true;
		} catch (IOException ex) {
			clientController.printToConsole("Error while connection Client to Server");
		} catch (Exception e) {
			clientController.printToConsole(e.getMessage());
		}
		return false;
	}

	// Method that requests to bring all orders from the server
	public static void requestToFetchAllOrders() {
		try {
			ClientDataContainer data = new ClientDataContainer(ClientActionsEnum.FETCH_ORDERS, null);
			client.sendToServer(data);
		} catch (IOException ex) {
			client.clientController.printToConsole("Error while sending search request to the Server");
		}
	}

	// Method to request an order update from the server
	public static void requestToUpdateOrder(Order order) {
		ClientDataContainer data = new ClientDataContainer(ClientActionsEnum.UPDATE_ORDER, order);
		try {
			client.sendToServer(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	// send massage to the server to disconnect the client
	public static void disconnectClientFromServer() {
	    try {
	        if (client != null) {
	        	ClientDataContainer data = new ClientDataContainer(ClientActionsEnum.DISCONNECT, null);
	            client.sendToServer(data);
	            client.closeConnection();
	            client = null;
	            System.out.println("Client connection closed");
	        }
	    } catch (IOException e) {
	        System.out.println("Error closing client connection: " + e.getMessage());
	    }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	// Method to handle messages received from the server
	// Using Enums we will have much more messages that we will need to handle
	protected void handleMessageFromServer(Object msg) {
		ServerDataContainer data = (ServerDataContainer) msg;
		ServerActionsEnum action = data.getAction();
		switch (action) {
		case ALL_ORDERS: // list of all orders
			List<Order> orders = (List<Order>) data.getMessage();
			ViewAndUpdateController.updateOrdersInTable(orders);
			break;
		case UPDATE_ORDER_RESULT: //update order result msg
			boolean success = (boolean) data.getMessage();
			if (success) {
				viewController.updateMessage("Order updated successfully");
			} else {
				viewController.updateMessage("Failed to update order. Make sure ID is correct.");
			}
			break;
		case UPDATE_CONNECTION_INFO:
			String addressHost = (String) data.getMessage();
			//homeController.updateConnectionLabel(addressHost);
            Platform.runLater(() -> {
                if (homeController != null) {
                    homeController.updateConnectionLabel(addressHost);
                }
            });
		default:
			// in the full project we will implement more things here
			return;
		}
	}
}
