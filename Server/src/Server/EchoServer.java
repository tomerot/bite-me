// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.List;

import db.DBConnectionDetails;
import db.DBController;
import gui.ServerPortController;
import logic.ClientActionsEnum;
import logic.ClientDataContainer;
import logic.Order;
import logic.ServerActionsEnum;
import logic.ServerDataContainer;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */

public class EchoServer extends AbstractServer {
	// Use Singleton DesignPattern -> only 1 server may be running in our system.
	private static EchoServer server = null;
	private ServerPortController serverController;
	private Connection dbConn;

	private EchoServer(int port, ServerPortController serverController, Connection dbConn) {
		super(port);
		this.serverController = serverController;
		this.dbConn = dbConn;
	}

	// Method to handle messages received from the client
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String clientIp = client.getInetAddress().getHostAddress(); // maybe will need to user client ip
		ClientDataContainer data = (ClientDataContainer) msg;
		ClientActionsEnum action = data.getAction();
		switch (action) {
		case FETCH_ORDERS:
			handleFetchOrders(client);
			break;
		case UPDATE_ORDER:
			handleUpdateOrder((Order) data.getMessage(), client);
			break;
		default:
			// we will implement here alot of more things in the final project
			return;
		}
	}	
	
	//send client message with his IP,host,status
	private void handleClientConnection(ConnectionToClient client) {
	    String clientIp = client.getInetAddress().getHostAddress();
	    String clientHostName = client.getInetAddress().getHostName();
	    String addressHost;
	    //if they the same user tried entering server IP as "LOCALHOST" which will get the IP instead the host name
	    if (clientIp.equals(clientHostName)) {
	    	try {
				clientHostName = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
	    }
	    addressHost = "Client IP: " + clientIp + "\nClient Host Name: " + clientHostName + "\nStatus: Connected";
	    ServerDataContainer data = new ServerDataContainer(ServerActionsEnum.UPDATE_CONNECTION_INFO, addressHost);
	    try {
	        client.sendToClient(data);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	// Method to handle Updating Order
	private void handleUpdateOrder(Order order, ConnectionToClient client) {
		boolean success = DBController.updateOrder(dbConn, order);
		try {
			ServerDataContainer response = new ServerDataContainer(ServerActionsEnum.UPDATE_ORDER_RESULT, success);
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 2 methods that are for receiving all order, and send massage to the client
	 */
	// Method to handle fetching all orders
	private void handleFetchOrders(ConnectionToClient client) {
		List<Order> orders = DBController.fetchOrders(dbConn);
		sendOrdersToClient(orders, client);
	}

	// Method to send the client list of all orders
	private void sendOrdersToClient(List<Order> orders, ConnectionToClient client) {
		try {
			ServerDataContainer response = new ServerDataContainer(ServerActionsEnum.ALL_ORDERS, orders);
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// method to start the server
	public static boolean  startServer(DBConnectionDetails db, Integer port, ServerPortController serverController) {
		// try to connect the database
		Connection dbConn = DBController.connectToMySqlDB(db);
		// if failed -> can't start the server.
		if (dbConn == null) {
			System.out.println("Can't start server! Connection to database failed!");
			return false;
		}
		System.out.println("Connection to database succeed");

		// Singleton DesignPattern. Only 1 instance of server is available.
		if (server != null) {
			System.out.println("There is already a connected server");
			return false;
		}

		server = new EchoServer(port, serverController, dbConn);

		try {
			server.listen();
			return true;
			// update connection in server gui.
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("\"Error - could not listen for clients!\"");
			return false;
		}
	}

	
	//when client connect call handleClientConnection -> send message to the client with his IP,host,status
	@Override
	protected void clientConnected(ConnectionToClient client) {
	    super.clientConnected(client);
	    handleClientConnection(client);
	}
	
	public static void stopServer() {
		// if there is no server return
		if(server==null)
			return;
		try {
			server.stopListening();
			server.close();
			server=null;
		}catch(IOException ex) {
			System.out.println("Error while closing server");
			ex.printStackTrace();
		}
	}
	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server started and is listening for connections on port " + getPort());
		try {
			System.out.format("ipv4 address to connect (if on same E-Net host) is : %s\n",
					InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}
}
