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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import containers.ClientRequestDataContainer;
import containers.ServerResponseDataContainer;
import db.DBConnectionDetails;
import db.DBController;
import db.QueryControl;
import db.ReportGenerator;
import entities.Item;
import entities.ItemInOrder;
import entities.BranchManager;
import entities.Customer;
import entities.Order;
import entities.Supplier;
import entities.SupplierIncome;
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
	private static Connection dbConn;
	private ReportGenerator reportGenerator;
    private Thread reportThread;
    private Map<Integer, ConnectionToClient> clientMap = new HashMap<>();

	
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
		case GET_ORDERS_DATA:{
			Integer supplierID=(Integer)data.getMessage();
			handleGetOrdersData(supplierID,client);
			break;
		}
		case SUPPLIER_UPDATE_ORDER_STATUS:{
			int[] orderInfo = (int[])data.getMessage();
			handleSupplierUpdateOrderStatus(orderInfo, client);
			break;
		}
			
		case SUPPLIER_REFRESH_AWAITING_ORDERS: {
			Integer supplierID = (Integer)data.getMessage();
			handleSupplierRefreshAwaitingOrders(supplierID, client);
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
		    
        case GET_SUPPLIER_ITEMS:
            Supplier supplier = (Supplier) data.getMessage();
            try {
                handleImportSupplierItems(supplier,client);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            break;

        case GET_RELEVANT_CITIES:
            Supplier supplier1 = (Supplier) data.getMessage();
            try {
                handleImportRelevantCities(supplier1,client);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            break;
            
        case UPDATE_ORDER_AND_ITEMS:
        	List<Object> receivedList = (List<Object>) data.getMessage();
        	Order receivedOrder = (Order) receivedList.get(0);
        	Map<ItemInOrder, Integer> receivedCart = (Map<ItemInOrder, Integer>) receivedList.get(1);
        	try {
				handleUpdateOrderAndItem(receivedOrder, receivedCart, client);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	break;
        	
        case UPDATE_CUSTOMER_WALLET:
        	List<Object> listOW = (List<Object>) data.getMessage();
        	Order requestedOrder = (Order) listOW.get(0);
        	float walletUsedAmount = (Float) listOW.get(1);
        	try {
				handleUpdateCustomerWallet(requestedOrder,walletUsedAmount,client);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	break;
        	
        case CUSTOMER_LOGOUT:
        	customerLogoutRemoveFromMap(client);
        	break;		
        	
        case SEND_CUSTOMER_MSG:
        	List<Object> listIdMsg = (List<Object>) data.getMessage();
        	int customerID = (int) listIdMsg.get(0);
        	String relevantMSG = (String) listIdMsg.get(1);
        	sendToSpecificClient(customerID,relevantMSG);
        	ServerResponseDataContainer response = new ServerResponseDataContainer();
        	response.setResponse(ServerResponse.MSG_WAS_SENT);
        	try {
				client.sendToClient(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
        	break;
        	
		default:
		    return;
	}
}
	
	
	/**
	 * Sends a message to a specific client identified by their customer ID.
	 * 
	 * @param customerID the unique identifier of the client to whom the message should be sent
	 * @param msg the message to be sent to the client
	 */
	public void sendToSpecificClient(int customerID, String msg) {
	    ConnectionToClient client = clientMap.get(customerID);
	    ServerResponseDataContainer response = new ServerResponseDataContainer();
	    response.setMessage(msg);
	    response.setResponse(ServerResponse.MSG_TO_DISPLAY_FOR_CUSTOEMR);
	    if (client != null && client.isAlive()) {
	        try {
	            client.sendToClient(response);
	        } catch (IOException e) {
	            System.out.println("Error sending message to client: " + e.getMessage());
	        }
	    } else {
	        System.out.println("Client with ID " + customerID + " not found or not connected.");
	    }
	}
	
	/**
	 * Handles the process of logging out a customer and removing their connection from the client map.
	 * 
	 * @param client the `ConnectionToClient` object representing the client's connection to be logged out
	 */
	private void customerLogoutRemoveFromMap(ConnectionToClient client) {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		response.setResponse(ServerResponse.CUSTOMER_LOGGED_OUT);
		removeClientByConnection(client);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles the process of updating a customer's wallet balance based on an order and the amount used,
	 * and sends a response to the client.
	 * 
	 * @param order the `Order` object containing information about the order related to the wallet update
	 * @param walletUsedAmount the amount deducted from the customer's wallet
	 * @param client the `ConnectionToClient` object representing the client to whom the response will be sent
	 * 
	 * @throws SQLException if an error occurs while updating the wallet balance in the database
	 */
	private void handleUpdateCustomerWallet(Order order,Float walletUsedAmount ,ConnectionToClient client) throws SQLException {
		QueryControl.orderQueries.updateCustomerWalletBalance(dbConn, order, walletUsedAmount);
		try {
			client.sendToClient(new ServerResponseDataContainer());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Handles the process of updating an order and its associated items in the database, and sends
	 * a response to the client.
	 * 
	 * @param order the `Order` object containing information about the order to be updated
	 * @param receivedCart a map where keys are `ItemInOrder` objects representing items in the order,
	 * and values are integers representing the quantities of those items
	 * @param client the `ConnectionToClient` object representing the client to whom the response will be sent
	 * 
	 * @throws SQLException if an error occurs while updating the order and items in the database
	 */
	private void handleUpdateOrderAndItem(Order order,Map<ItemInOrder, Integer> receivedCart ,ConnectionToClient client) throws SQLException {
		ServerResponseDataContainer response =QueryControl.orderQueries.updateOrderAndItems(dbConn, order, receivedCart);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Handles the process of updating an item's information in the database and sends the response
	 * to the client.
	 * 
	 * @param item the `Item` object containing updated information about the item
	 * @param client the `ConnectionToClient` object representing the client to whom the response will be sent
	 * @throws SQLException if an error occurs while updating the item information in the database
	 */
	private void handleUpdateItemRequest(Item item, ConnectionToClient client) {
		ServerResponseDataContainer response = QueryControl.employeeQuery.UpdateItemInfo(dbConn, item);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Handles the request to retrieve the full list of items for a specified supplier and sends
	 * the response to the client.
	 * 
	 * @param supplierID the ID of the supplier for whom the full list of items is requested
	 * @param client the `ConnectionToClient` object representing the client to whom the response will be sent
	 * 
	 * @throws SQLException if an error occurs while retrieving the item list from the database
	 */
	private void handleGetFullItemsListRequest(Integer supplierID, ConnectionToClient client) {
		ServerResponseDataContainer response = QueryControl.employeeQuery.FetchFullItemsListInfo(dbConn, supplierID);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles the request to retrieve a list of items for a specified supplier and sends
	 * the response to the client.
	 * 
	 * @param supplierID the ID of the supplier for whom the list of items is requested
	 * @param client the `ConnectionToClient` object representing the client to whom the response will be sent
	 * 
	 * @throws SQLException if an error occurs while retrieving the item list from the database
	 */
	private void handleGetItemsListRequest(Integer supplierID, ConnectionToClient client) {
		ServerResponseDataContainer response = QueryControl.employeeQuery.FetchItemsListInfo(dbConn, supplierID);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles the request to add a new item to the database and sends the response
	 * to the client.
	 *
	 * @param item the `Item` object containing the data to be added to the database
	 * @param client the `ConnectionToClient` object representing the client to whom the response will be sent
	 * 
	 * @throws SQLException if an error occurs while adding the item to the database
	 */
	private void handleAddItemData(Item item, ConnectionToClient client) {
		ServerResponseDataContainer response = QueryControl.employeeQuery.AddItemInfo(dbConn, item);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles the request to remove an item from the database and sends the response
	 * to the client.
	 * 
	 * @param itemData a map containing the item data required to identify and remove the item from the database.
	 * @param client the `ConnectionToClient` object representing the client to whom the response will be sent.
	 * 
	 * @throws SQLException if an error occurs while removing the item from the database.
	 */
	private void handleRemoveItemData(Map<String,Integer> itemData, ConnectionToClient client) {
		ServerResponseDataContainer response = QueryControl.employeeQuery.RemoveItemInfo(dbConn, itemData);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles the request to fetch orders data for a specific supplier and sends the response
	 * to the client.
	 * 
	 * @param supplierID the ID of the supplier for whom the orders data is to be fetched.
	 * @param client the `ConnectionToClient` object representing the client to whom the response will be sent.
	 * 
	 * @throws IOException if an error occurs while sending the response to the client.
	 */
	public void handleGetOrdersData(Integer supplierID, ConnectionToClient client){
		ServerResponseDataContainer response=QueryControl.supplierQuery.getOrdersData(dbConn, supplierID);
		try {
			client.sendToClient(response);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Fetches data for a report based on the specified date range and branch.
	 * 
	 * @param startOfLastMonth the start date of the last month for which the report data is to be fetched.
	 * @param endOfLastMonth the end date of the last month for which the report data is to be fetched.
	 * @param branch the branch for which the report data is to be retrieved.
	 * 
	 * @return a {@link ServerResponseDataContainer} containing the fetched report data.
	 */
	public static ServerResponseDataContainer fetchDataForReport(LocalDate startOfLastMonth, LocalDate endOfLastMonth, String branch) {
	    ServerResponseDataContainer response = QueryControl.serverQueries.fetchOrdersReportData(dbConn, startOfLastMonth, endOfLastMonth, branch);
	    return response;
	}
	
	/**
	 * Inserts data for a report into the database.
	 * 
	 * @param data is a HASHMAP containing the report data, where the keys are data labels and the values
	 * are the corresponding numerical data to be inserted.
	 * @param branch the branch associated with the report data.
	 * @param year the year associated with the report data.
	 * @param month the month associated with the report data.
	 */
	public static void insertDataForReport(HashMap<String, Integer> data, String branch, int year, int month) {
	    QueryControl.serverQueries.insertOrdersReportData(dbConn, data, branch, year, month);
	}
	
	
	/**
	 * Fetches performance report data from the database.
	 * 
	 * @param startOfLastMonth the start date of the reporting period, which is the beginning of the last month.
	 * @param endOfLastMonth the end date of the reporting period, which is the end of the last month.
	 * @param branch the branch for which the performance report data is to be fetched.
	 * 
	 * @return a {@link ServerResponseDataContainer} containing the fetched performance report data.
	 */
	public static ServerResponseDataContainer fetchDataForPerformanceReport(LocalDate startOfLastMonth, LocalDate endOfLastMonth, String branch) {
	    ServerResponseDataContainer response = QueryControl.serverQueries.fetchPerformanceReportData(dbConn, startOfLastMonth, endOfLastMonth, branch);
	    return response;
	}
	
	/**
	 * Inserts performance report data into the database.
	 * 
	 * @param data is a HASHMAP containing the performance report data, where keys are data labels and values are corresponding integers.
	 * @param branch the branch to which the performance report data belongs.
	 * @param year the year for which the performance report data is being inserted.
	 * @param month the month for which the performance report data is being inserted.
	 * 
	 * @throws SQLException if there is an error accessing the database.
	 */
	public static void insertDataForPerformanceReport(HashMap<String, Integer> data, String branch, int year, int month) throws SQLException {
	    QueryControl.serverQueries.insertPerformanceReport(dbConn, data, branch, year, month);
	}

	/**
	 * Fetches income report data from the database for the specified period and branch.
	 * 
	 * @param startOfLastMonth the start date of the last month for which income report data is being fetched.
	 * @param endOfLastMonth the end date of the last month for which income report data is being fetched.
	 * @param branch the branch for which income report data is being fetched.
	 * 
	 * @return a {@link ServerResponseDataContainer} containing the income report data.
	 */
	public static ServerResponseDataContainer fetchDataForIncomeReport(LocalDate startOfLastMonth, LocalDate endOfLastMonth, String branch) {
	    ServerResponseDataContainer response = QueryControl.serverQueries.fetchIncomeReportData(dbConn, startOfLastMonth, endOfLastMonth, branch);
	    return response;
	}
	
	
	/**
	 * Inserts income report data into the database for the specified month and year.
	 * 
	 * @param data is a LIST of {@link SupplierIncome} objects containing the income report data to be inserted.
	 * @param branch the branch for which the income report data is being inserted.
	 * @param year the year for which the income report data is being inserted.
	 * @param month the month for which the income report data is being inserted.
	 * 
	 * @throws SQLException if there is an error while interacting with the database.
	 */
	public static void insertDataForIncomeReport(List<SupplierIncome> data, String branch, int year, int month) throws SQLException {
	    QueryControl.serverQueries.insertIncomeReport(dbConn, data, branch, year, month);
	}

	
	/**
	 * Imports a list of customers from a specified file and inserts them into the database.
	 * 
	 * @param path the file path where the customer data is located.
	 * 
	 * @throws SQLException if there is an error while interacting with the database, or FilePath problems
	 * 
	 */
	public static void importCustomerSimulation(String path) throws SQLException {
		  QueryControl.serverQueries.insertCustomersList(dbConn, path);

	}
	
	/**
	 * Marks all users as logged out in the database.
	 * 
	 * @throws SQLException if there is an error while interacting with the database.
	 */
	public static void setAllUsersLoggedOut() throws SQLException{
		QueryControl.serverQueries.setAllUsersLoggedOut(dbConn);
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
			break;
		case CUSTOMER:
			response = QueryControl.userQueries.importCustomerInfo(dbConn, user);
			Customer customer = (Customer) response.getMessage();
			clientMap.put(customer.getId(), client);
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
	
    private void handleImportSupplierItems(Supplier supplier, ConnectionToClient client) throws SQLException{
        ServerResponseDataContainer response = QueryControl.orderQueries.importSupplierItems(dbConn, supplier);
        try {
            client.sendToClient(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	private void handleImportRelevantCities(Supplier supplier, ConnectionToClient client) throws SQLException {
		ServerResponseDataContainer response = QueryControl.orderQueries.FetchRelevantCities(dbConn, supplier);
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Handles a request to update the status of an order.
	 *
	 * @param orderInfo an array of integers where the first element is the order ID and the second element is the status flag
	 * @param client the client that requested the update
	 */
	public void handleSupplierUpdateOrderStatus(int[] orderInfo, ConnectionToClient client){
		ServerResponseDataContainer response = QueryControl.supplierQuery.UpdateOrderStatus(dbConn, orderInfo);
		try {   
			client.sendToClient(response);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles a request to refresh awaiting orders for a given supplier.
	 *
	 * @param supplierID the ID of the supplier for whom to refresh awaiting orders
	 * @param client the client that requested the data
	 */
	public void handleSupplierRefreshAwaitingOrders(int supplierID, ConnectionToClient client){
		ServerResponseDataContainer response = QueryControl.supplierQuery.RefreshAwaitingOrders(dbConn, supplierID);
		try {
			client.sendToClient(response);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
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
			server.startReportGenerator();
			return true;
			// update connection in server gui.
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("\"Error - could not listen for clients!\"");
			return false;
		}
	}
	
    private void startReportGenerator() {
        reportGenerator = new ReportGenerator();
        reportThread = new Thread(reportGenerator);
        reportThread.start();
    }

    private void stopReportGenerator() {
        if (reportGenerator != null) {
            reportGenerator.stop();
            reportThread.interrupt(); // Interrupt the sleep to stop immediately
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
	
    public void removeClientByConnection(ConnectionToClient client) {
        // Iterate through the map entries
        for (Map.Entry<Integer, ConnectionToClient> entry : clientMap.entrySet()) {
            // Check if the value matches the client to remove
            if (entry.getValue().equals(client)) {
                // Remove the entry
                clientMap.remove(entry.getKey());
                break; // Exit the loop after removing the entry
            }
        }
    }

	// stop server from listening and close him
	public static void stopServer() {
		// if there is no server return
		if (server == null)
			return;
		try {
			server.stopListening();
			server.close();
			server.stopReportGenerator();
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
