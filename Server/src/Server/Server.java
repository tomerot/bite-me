// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.Map;
import java.sql.SQLException;
import java.util.List;
import containers.ClientRequestDataContainer;
import containers.ServerResponseDataContainer;
import db.DBConnectionDetails;
import db.DBController;
import db.QueryControl;
import entities.Item;
import entities.BranchManager;
import entities.Customer;
import entities.Order;
import entities.User;
import enums.Branch;
import enums.ClientRequest;
import enums.ServerResponse;
import enums.UserType;
import gui.controllers.ServerPortController;
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

public class Server extends AbstractServer {
	// Use Singleton DesignPattern -> only 1 server may be running in our system.
	private static Server server = null;
	private ServerPortController serverController;
	private Connection dbConn;

	
    /**
     * Constructs a new Server instance with the specified port, server controller, and database connection.
     * 
     * @param port the port number on which the server listens for connections
     * @param serverController the controller used to update the server status and connected clients in the GUI
     * @param dbConn the database connection to use for processing client requests
     */
	private Server(int port, ServerPortController serverController, Connection dbConn) {
		super(port);
		this.serverController = serverController;
		this.dbConn = dbConn;
	}

	
    /**
     * Handles messages received from clients. The method processes different types of requests
     * based on the {@link ClientRequest} enum and performs corresponding actions.
     * 
     * @param msg the message received from the client (ClientRequestDataContainer)
     * @param client the client that sent the message
     */
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		ClientRequestDataContainer data = (ClientRequestDataContainer) msg;
		ClientRequest request = data.getRequest();
		User user;
		Customer customer;
		// switch case on the request from server
		switch (request) {
		// all cases
		case DISCONNECT: 
			clientDisconnected(client);
			break;

		case GET_USER_DATA:
			user = (User) data.getMessage();
			handleUserData(user, client);
			break;

		case GET_SPECIFIC_USER_DATA:
			User specificUser = (User) data.getMessage();
			handleSpecificUserData(specificUser, client);
			break;

		case UPDATE_USER_DATA:
			user = (User) data.getMessage();
			try {
				handleUpdateUser(user, client);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case UPDATE_IS_LOGGED_IN:
			user = (User) data.getMessage();
			try {
				handleUpdateIsLoggedIn(user, client);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case GET_ORDER_DATA:{
			Integer supplierID=(Integer)data.getMessage();
			handleGetOrdersData(supplierID,client);
			break;
		}
			
		case ADD_ITEM_DATA:
			Item item = (Item) data.getMessage();
			handleAddItemData(item, client);
			break;
			
		case REMOVE_ITEM:
			Map<String,Integer> itemData = (Map<String,Integer>)data.getMessage();
			handleRemoveItemData(itemData, client);
			break;
			
		case GET_ITEMS_LIST:
			Integer supplierID = (Integer)data.getMessage();
			handleGetItemsListRequest(supplierID, client);
			break;
			
			
		case GET_FULL_ITEMS_LIST:
			Integer suppID = (Integer)data.getMessage();
			handleGetFullItemsListRequest(suppID, client);
			break;
			
		case UPDATE_ITEM:
			System.out.println("Server got message  from client to get update item");
			Item updatedItem = (Item)data.getMessage();
			handleUpdateItemRequest(updatedItem, client);
			break;

		case FETCH_CUSTOMERS_DATA:
			BranchManager manager = (BranchManager) data.getMessage();
			try {
				handleCustomersData(manager, client);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case UPDATE_CUSTOMERS_REGISTER:
			List<String> userList = (List<String>) data.getMessage();
			try {
				handleUpdateCustomersRegister(userList, client);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case FETCH_REPORT_DATA:{
			List<String> reportInfo = (List<String>) data.getMessage();
			try {
				handleReportInfo(reportInfo, client);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;}
		case FETCH_QUARTER_REPORT_DATA:{
			List<String> reportInfo = (List<String>) data.getMessage();
			try {
				handleQuarterReportInfo(reportInfo, client);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;}
			
//		case FETCH_BRANCH_RESTAURANTS:
//			customer = (Customer) data.getMessage();
//			try {
//				handleRestaurantsData(customer,client);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			break;
			
		case FETCH_BRANCH_RESTAURANTS:{
			Branch branchName = (Branch) data.getMessage();
			try {
				handleRestaurantsData(branchName, client);
			} catch(SQLException e) {
				e.printStackTrace();
			}
			break;
		}
			
		case FETCH_CUSTOMER_WAITING_ORDERS:
			customer = (Customer) data.getMessage();
			try {
				handleCustomersWaitingOrders(customer,client);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
			
		case FETCH_CUSTOMER_HISTORY_ORDERS:
			customer = (Customer) data.getMessage();
			try {
				handleCustomersHistoryOrders(customer,client);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
			
		case UPDATE_ORDER_STATUS_AND_TIME:
			Order order = (Order) data.getMessage();
			try {
				handleOrderStatusTimeUpdate(order,client);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		default:
		    return;

	}
}
	
	private void handleUpdateItemRequest(Item item, ConnectionToClient client) {
		ServerResponseDataContainer response = QueryControl.userQueries.UpdateItemInfo(dbConn, item);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleGetFullItemsListRequest(Integer supplierID, ConnectionToClient client) {
		ServerResponseDataContainer response = QueryControl.userQueries.FetchFullItemsListInfo(dbConn, supplierID);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleGetItemsListRequest(Integer supplierID, ConnectionToClient client) {
		ServerResponseDataContainer response = QueryControl.userQueries.FetchItemsListInfo(dbConn, supplierID);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleAddItemData(Item item, ConnectionToClient client) {
		ServerResponseDataContainer response = QueryControl.userQueries.AddItemInfo(dbConn, item);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleRemoveItemData(Map<String,Integer> itemData, ConnectionToClient client) {
		ServerResponseDataContainer response = QueryControl.userQueries.RemoveItemInfo(dbConn, itemData);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleGetOrdersData(Integer supplierID, ConnectionToClient client){
		ServerResponseDataContainer response=QueryControl.userQueries.getOrdersData(dbConn, supplierID);
		try {
			client.sendToClient(response);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}


    /**
     * Handles the request to fetch specific user data based on user type.
     * 
     * @param user the user for whom data is to be fetched
     * @param client the client that requested the data
     */
	private void handleSpecificUserData(User user, ConnectionToClient client) {
		UserType type = user.getUserType();
		ServerResponseDataContainer response = null;
		switch (type) {
		case MANAGER:
			response = QueryControl.userQueries.importManagerInfo(dbConn, user);
			break;
		case SUPPLIER:
			response = QueryControl.userQueries.importSupplierInfo(dbConn, user);
			break;
		case EMPLOYEE:
			response = QueryControl.userQueries.importEmployeeInfo(dbConn, user);
			System.out.println(response.getResponse());
			break;
		case CUSTOMER:
			response = QueryControl.userQueries.importCustomerInfo(dbConn, user);
			break;
		default:
			break;
		}
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Handles the request to update user data in the database.
     * 
     * @param user the user whose data is to be updated
     * @param client the client that requested the update
     * @throws Exception if an error occurs while updating the user data
     */
	private void handleUpdateUser(User user, ConnectionToClient client) throws Exception {
	    QueryControl.userQueries.updateUserData(dbConn, user);
	    try {
	        client.sendToClient(new ServerResponseDataContainer());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
    /**
     * Handles the request to update the login status of a user.
     * 
     * @param user the user whose login status is to be updated
     * @param client the client that requested the update
     * @throws Exception if an error occurs while updating the login status
     */
	private void handleUpdateIsLoggedIn(User user, ConnectionToClient client) throws Exception {
	    QueryControl.userQueries.updateIsLoggedIn(dbConn, user);
	    try {
	        client.sendToClient(new ServerResponseDataContainer());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
    /**
     * Handles the request to fetch customer data.
     * 
     * @param manager the branch manager requesting the customer data
     * @param client the client that requested the data
     * @throws SQLException if an error occurs while fetching the customer data
     */
	private void handleCustomersData(BranchManager manager, ConnectionToClient client) throws SQLException {
		ServerResponseDataContainer response = QueryControl.managersQuery.importCustomerList(dbConn, manager);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Handles the request to fetch user data.
     * 
     * @param user the user for whom data is to be fetched
     * @param client the client that requested the data
     */
	private void handleUserData(User user, ConnectionToClient client) {
		ServerResponseDataContainer response = QueryControl.userQueries.importUserInfo(dbConn, user);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Handles the request to update the customer registration data.
     * 
     * @param userList a list of users
     * @param client the client that requested the update
     * @throws Exception if an error occurs while updating the customer registration data
     */
	private void handleUpdateCustomersRegister(List<String> userList, ConnectionToClient client) throws Exception {
		QueryControl.managersQuery.updateUsersRegister(dbConn, userList);
		try {
			client.sendToClient(new ServerResponseDataContainer());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void handleReportInfo(List<String> reportInfo, ConnectionToClient client) throws Exception {
		ServerResponseDataContainer response = QueryControl.managersQuery.importReportData(dbConn, reportInfo);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void handleQuarterReportInfo(List<String> reportInfo, ConnectionToClient client) throws Exception {
		ServerResponseDataContainer response = QueryControl.managersQuery.importQuarterReportData(dbConn, reportInfo);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//    /**
//     * Handles the request to fetch restaurant data based on a customer's request - Same Branch as supplier.
//     * 
//     * @param customer the customer requesting the restaurant data, Same Branch as supplier
//     * @param client the client that requested the data
//     * @throws SQLException if an error occurs while fetching the restaurant data
//     */
//	private void handleRestaurantsData(Customer custoemr, ConnectionToClient client) throws SQLException {
//		ServerResponseDataContainer response = QueryControl.orderQueries.importSuppliersByBranch(dbConn, custoemr);
//	    try {
//	        client.sendToClient(response);
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
//	}
	
    /**
     * Handles the request to fetch restaurant data based on a customer's request.
     * 
     * @param branchName the branch it's restaurants are requested
     * @param client the client that requested the data
     * @throws SQLException if an error occurs while fetching the restaurant data
     */
	private void handleRestaurantsData(Branch branchName, ConnectionToClient client) throws SQLException {
		ServerResponseDataContainer response = QueryControl.orderQueries.importSuppliersByBranch(dbConn, branchName);
	    try {
	        client.sendToClient(response);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/**
	 * Handles the retrieval of the customer's waiting orders and sends the response to the client.
	 *
	 * @param customer the customer whose waiting orders are to be retrieved
	 * @param client the client connection to send the response to
	 * @throws SQLException if a database access error occurs
	 */
	private void handleCustomersWaitingOrders(Customer custoemr, ConnectionToClient client) throws SQLException{
		ServerResponseDataContainer response = QueryControl.orderQueries.importCustomerWaitingOrders(dbConn, custoemr);
	    try {
	        client.sendToClient(response);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/**
	 * Handles the retrieval of the customer's historical orders and sends the response to the client.
	 *
	 * @param customer the customer whose historical orders are to be retrieved
	 * @param client   the client connection to send the response to
	 * @throws SQLException if a database access error occurs
	 */
	private void handleCustomersHistoryOrders(Customer custoemr, ConnectionToClient client) throws SQLException{
		ServerResponseDataContainer response = QueryControl.orderQueries.importCustomerHistoryOrders(dbConn, custoemr);
	    try {
	        client.sendToClient(response);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/**
	 * Handles the update of the order status and arrival time
	 *
	 * @param order  the order to be updated
	 * @param client the client connection to send the response to
	 * @throws SQLException if a database access error occurs
	 */
	private void handleOrderStatusTimeUpdate(Order order, ConnectionToClient client) throws SQLException{
		QueryControl.orderQueries.updateOrderStatusTime(dbConn, order);
	    try {
	        client.sendToClient(new ServerResponseDataContainer());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 * 
	 * 
	 * 
			FROM HERE WE DONT TOUCH!
	 * 
	 * 
	 * 
	 * 
	*/
	
	
	
	
    /**
     * Starts the server with the specified database connection details, port, and server controller.
     * 
     * @param db the database connection details
     * @param port the port number on which the server listens for connections
     * @param serverController the controller used to update the server status and connected clients in the GUI
     * @return true if the server starts successfully, false otherwise
     */
	public static boolean startServer(DBConnectionDetails db, Integer port, ServerPortController serverController) {
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

		server = new Server(port, serverController, dbConn);

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

	// send client message with his IP,host,status
	private void handleClientConnection(ConnectionToClient client) {
		String clientIP = client.getInetAddress().getHostAddress();
		String clientHostName = client.getInetAddress().getHostName();
		if (clientIP.equals(clientHostName)) {
			try {
				clientHostName = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String clientInfo = "IP:" + clientIP + "\nHOST:" + clientHostName + "\nStatus: Connected";
		ServerResponseDataContainer data = new ServerResponseDataContainer(ServerResponse.UPDATE_CONNECTION_INFO,
				clientInfo);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * Called when a client connects to the server. Updates the server controller with the client's information
     * and sends a message to the client with their connection details.
     * 
     * @param client the client that has connected
     */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		super.clientConnected(client);
		String clientIP = client.getInetAddress().getHostAddress();
		String clientHostName = client.getInetAddress().getHostName();

		if (clientIP.equals(clientHostName)) {
			try {
				clientHostName = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

		String clientInfo = "IP:" + clientIP + " HOST:" + clientHostName;
		serverController.addConnectedClient(clientInfo);
		handleClientConnection(client);
	}

    /**
     * Called when a client disconnects from the server. Updates the server controller to remove the client's information.
     * 
     * @param client the client that has disconnected
     */
	@Override
	protected void clientDisconnected(ConnectionToClient client) {
		super.clientDisconnected(client);
		String clientIP = client.getInetAddress().getHostAddress();
		String clientHostName = client.getInetAddress().getHostName();

		if (clientIP.equals(clientHostName)) {
			try {
				clientHostName = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

		String clientInfo = "IP:" + clientIP + " HOST:" + clientHostName;
		serverController.removeConnectedClient(clientInfo);
	}

	// stop server from listening and close him
	public static void stopServer() {
		// if there is no server return
		if (server == null)
			return;
		try {
			server.stopListening();
			server.close();
			server = null;
		} catch (IOException ex) {
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
