package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import containers.ServerResponseDataContainer;
import entities.Customer;
import entities.Order;
import entities.OrderType;
import entities.Supplier;
import entities.SupplyMethod;
import enums.Branch;
import enums.ServerResponse;
import enums.UserType;

public class OrderQuery {
	public OrderQuery() {

	}

//	/**
//	 * Retrieves a list of suppliers based on the customer's home branch.
//	 *
//	 * @param dbConn   the database connection
//	 * @param customer the customer whose branch is used for querying
//	 * @return a ServerResponseDataContainer containing the list of suppliers and
//	 *         response status
//	 * @throws SQLException if a database access error occurs
//	 */
//	public ServerResponseDataContainer importSuppliersByBranch(Connection dbConn, Customer customer)
//			throws SQLException {
//		ServerResponseDataContainer response = new ServerResponseDataContainer();
//		List<Supplier> suppliers = new ArrayList<>();
//		String query = "SELECT ID, Name, Email, Phone, Address, City FROM Suppliers WHERE Branch = ?";
//		String br = null;
//		switch (customer.getHomeBranch().toString()) {
//		case "North Branch":
//			br = "North";
//			break;
//		case "Center Branch":
//			br = "Center";
//			break;
//		case "South Branch":
//			br = "South";
//			break;
//		}
//
//		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
//			stmt.setString(1, br);
//
//			try (ResultSet rs = stmt.executeQuery()) {
//				while (rs.next()) {
//
//					// Extract data from the result set
//					int id = rs.getInt("ID");
//					String name = rs.getString("Name");
//					String email = rs.getString("Email");
//					String phone = rs.getString("Phone");
//					String address = rs.getString("Address");
//					String city = rs.getString("City");
//
//					Supplier supplier = new Supplier(id, name, city, address, null, email, phone, null, null);
//					suppliers.add(supplier);
//				}
//
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//		response.setMessage(suppliers);
//		response.setResponse(ServerResponse.SUPPLIER_DATA_BY_BRANCH);
//		return response;
//	}
	
	/**
	 * Retrieves a list of suppliers based on a branch
	 *
	 * @param dbConn   the database connection
	 * @param branchName the branch whom restaurants are needed
	 * @return a ServerResponseDataContainer containing the list of suppliers and
	 *         response status
	 * @throws SQLException if a database access error occurs
	 */
	public ServerResponseDataContainer importSuppliersByBranch(Connection dbConn, Branch branchName)
			throws SQLException {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		List<Supplier> suppliers = new ArrayList<>();
		String query = "SELECT ID, Name, Email, Phone, Address, City FROM Suppliers WHERE Branch = ?";

		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, branchName.toShortString());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {

					// Extract data from the result set
					int id = rs.getInt("ID");
					String name = rs.getString("Name");
					String email = rs.getString("Email");
					String phone = rs.getString("Phone");
					String address = rs.getString("Address");
					String city = rs.getString("City");

					Supplier supplier = new Supplier(id, name, city, address, branchName, email, phone, null, null);
					suppliers.add(supplier);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		response.setMessage(suppliers);
		response.setResponse(ServerResponse.SUPPLIER_DATA_BY_BRANCH);
		return response;
	}

    /**
     * Retrieves a list of waiting orders for the specified customer.
     *
     * @param dbConn   the database connection
     * @param customer the customer whose waiting orders are to be retrieved
     * @return a ServerResponseDataContainer containing the list of waiting orders and response status
     * @throws SQLException if a database access error occurs
     */
	public ServerResponseDataContainer importCustomerWaitingOrders(Connection dbConn, Customer customer)
			throws SQLException {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		List<Order> waitingOrders = new ArrayList<>();
		String query = "SELECT o.* FROM orders o JOIN orders_participants op ON o.OrderID = op.OrderID WHERE op.CustomerID = ? AND o.Status IN ('Awaiting', 'Approved', 'Ready')";

		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, customer.getId());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int orderID = rs.getInt("OrderID");
					String recipient = rs.getString("Recipient");
					String recipientPhone = rs.getString("Recipient Phone");
					int supplierID = rs.getInt("SupplierID");
					String branch = rs.getString("Branch");
					String supplyOption = rs.getString("SupplyOption");
					String type = rs.getString("Type");
					String requestedDate = rs.getString("RequestDate");
					String requestedTime = rs.getString("RequestTime");
					String approvalTime = rs.getString("ApprovalTime");
					String arrivalTime = rs.getString("ArrivalTime");
					float totalPrice = rs.getFloat("TotalPrice");
					String status = rs.getString("Status");

					Order order = new Order(null, orderID, recipient, recipientPhone, getSupplyOption(supplyOption),
							getSupplierById(dbConn,supplierID), customer, getBranch(branch), requestedDate, requestedTime, getOrderType(type),
							totalPrice, approvalTime, arrivalTime, status);
					waitingOrders.add(order);
				}
			}
		}
		response.setMessage(waitingOrders);
		response.setResponse(ServerResponse.SUPPLIER_WAITING_ORDERS);
		return response;
	}

    /**
     * Retrieves a list of historical orders for the specified customer.
     *
     * @param dbConn   the database connection
     * @param customer the customer whose historical orders are to be retrieved
     * @return a ServerResponseDataContainer containing the list of historical orders and response status
     * @throws SQLException if a database access error occurs
     */
	public ServerResponseDataContainer importCustomerHistoryOrders(Connection dbConn, Customer customer) throws SQLException {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		List<Order> waitingOrders = new ArrayList<>();
		String query = "SELECT o.* FROM orders o JOIN orders_participants op ON o.OrderID = op.OrderID WHERE op.CustomerID = ? AND o.Status IN ('On-time', 'Late')";

		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, customer.getId());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int orderID = rs.getInt("OrderID");
					String recipient = rs.getString("Recipient");
					String recipientPhone = rs.getString("Recipient Phone");
					int supplierID = rs.getInt("SupplierID");
					String branch = rs.getString("Branch");
					String supplyOption = rs.getString("SupplyOption");
					String type = rs.getString("Type");
					String requestedDate = rs.getString("RequestDate");
					String requestedTime = rs.getString("RequestTime");
					String approvalTime = rs.getString("ApprovalTime");
					String arrivalTime = rs.getString("ArrivalTime");
					float totalPrice = rs.getFloat("TotalPrice");
					String status = rs.getString("Status");

					Order order = new Order(null, orderID, recipient, recipientPhone, getSupplyOption(supplyOption),
							getSupplierById(dbConn,supplierID), customer, getBranch(branch), requestedDate, requestedTime, getOrderType(type),
							totalPrice, approvalTime, arrivalTime, status);
					waitingOrders.add(order);
				}
			}
		}
		response.setMessage(waitingOrders);
		response.setResponse(ServerResponse.SUPPLIER_HISTORY_ORDERS);
		return response;
	}

    /**
     * Updates the status and arrival time of the specified order.
     * IF THE ORDER IS LATE WE UPDATE THE WALLED BY 50% OF TOTAL PRICE!
     *
     * @param dbConn the database connection
     * @param order  the order to be updated
     * @throws SQLException if a database access error occurs
     */
	public void updateOrderStatusTime(Connection dbConn, Order order) throws SQLException {
		String query = "UPDATE orders SET ArrivalTime = ? ,status = ? WHERE OrderID = ?";
		boolean late = isLateByTimeDiffAndStatus(order.getType(),order.getApprovalTimer());
		String status;
		String currTime = getCurrTime();
		if (late) {
			status = "Late";
			updateCustomerWalletBalance(dbConn,order);
			}
		else {status = "On-time";}
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, currTime);
			stmt.setString(2, status);
			stmt.setInt(3, order.getOrderID());
			stmt.executeUpdate();
		}
	}
	

	
	

					/* PRIAVET METHODS HERE FOR COMFORT*/
	
	
	/**
	 * Updates the wallet balance of customers who participated in a given order (LATE ORDER).
	 * 
	 * @param dbConn The database connection to use for the query and update operations.
	 * @param order The `Order` object containing the `OrderID` used to identify the relevant order.
	 * 
	 * @throws SQLException If an SQL error occurs during the execution of the queries. This includes errors 
	 *                       related to the database connection, query syntax, or data manipulation.
	 */
	private void updateCustomerWalletBalance(Connection dbConn, Order order) throws SQLException {
		String query = "Select * FROM orders_participants WHERE OrderID = ?";
		
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, order.getOrderID());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int customerID = rs.getInt("CustomerID");
					float amountPaid = rs.getFloat("AmountPaid");
					
					//get the amount i need to update
					double refundAmount = amountPaid * 0.5;
					
					//nested query to update the wallet balance
					String updateWalletQuery = "UPDATE customers SET WalletBalance = WalletBalance + ? WHERE ID = ?";
					try (PreparedStatement updateStmt = dbConn.prepareStatement(updateWalletQuery)) {
                        updateStmt.setDouble(1, refundAmount);
                        updateStmt.setInt(2, customerID);
                        updateStmt.executeUpdate();
                    }
				}
			}
		}
	}
	
	
    /**
     * Retrieves a supplier by its ID.
     *
     * @param dbConn the database connection
     * @param supplierID the ID of the supplier to retrieve
     * @return the Supplier object if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
	private Supplier getSupplierById(Connection dbConn, int supplierID) throws SQLException {
		String query = "SELECT ID, Name, Email, Phone, Address, City, Branch FROM suppliers WHERE ID = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, supplierID);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					int id = rs.getInt("ID");
					String name = rs.getString("Name");
					String email = rs.getString("Email");
					String phone = rs.getString("Phone");
					String address = rs.getString("Address");
					String city = rs.getString("City");
					String branch = rs.getString("Branch");

					Supplier supplier = new Supplier(supplierID, name, city, address, getBranch(branch), email, phone,
							null, null);
					return supplier;
				}
			}
		}
		return null;
	}

    /**
     * Converts a string to a SupplyMethod enum.
     *
     * @param method the string representation of the supply method
     * @return the corresponding SupplyMethod enum
     */
	private SupplyMethod getSupplyOption(String method) {
		if (method.equals("TakeAway")) {
			return SupplyMethod.TAKEAWAY;
		}
		if (method.equals("Robot")) {
			return SupplyMethod.ROBOT;
		}
		if (method.equals("Basic")) {
			return SupplyMethod.BASIC;
		} else {
			return SupplyMethod.SHARED;
		}
	}

	   /**
     * Converts a string to a Branch enum.
     *
     * @param branch the string representation of the branch
     * @return the corresponding Branch enum
     */
	private Branch getBranch(String branch) {
		if (branch.equals("North")) {
			return Branch.NORTH;
		}
		if (branch.equals("Center")) {
			return Branch.CENTER;
		} else {
			return Branch.SOUTH;
		}
	}

    /**
     * Converts a string to an OrderType enum.
     *
     * @param type the string representation of the order type
     * @return the corresponding OrderType enum
     */
	private OrderType getOrderType(String type) {
		if (type.equals("Pre-order")) {
			return OrderType.PRE_ORDER;
		} else {
			return OrderType.REGULAR;
		}
	}
	
	
    /**
     * Determines if the order is late based on the time difference and order type.
     *
     * @param type the type of the order
     * @param approvalTime the approval time of the order
     * @return true if the order is late, false otherwise
     */
	private boolean isLateByTimeDiffAndStatus(OrderType type , String approvalTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime givenTime = LocalTime.parse(approvalTime, formatter);
		LocalTime currentTime = LocalTime.now();
		Duration duration = Duration.between(givenTime, currentTime);
		long totalMinutes = duration.toMinutes();
		if (type.toString().equals("Regular") && totalMinutes > 60) {
			return true;
		}
		
		else if (type.toString().equals("PreOrder") && totalMinutes > 20) {
			return true;
		}
		return false;
	}
	
    /**
     * Retrieves the current time formatted as HH:mm:ss.
     *
     * @return the current time as a string
     */
	private String getCurrTime() {
		LocalTime currentTime = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
	}
}

