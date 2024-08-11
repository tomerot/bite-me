package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import containers.ServerResponseDataContainer;
import entities.Category;
import entities.Customer;
import entities.Item;
import entities.ItemInOrder;
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

					Supplier supplier = new Supplier(id, name, city, address, branchName, email, phone);
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
		String query = "SELECT * FROM orders WHERE CustomerID = ? AND Status IN ('Awaiting', 'Approved', 'Ready')";

		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, customer.getId());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int orderID = rs.getInt("OrderID");
					String recipient = rs.getString("Recipient");
					String recipientPhone = rs.getString("Recipient Phone");
					int supplierID = rs.getInt("SupplierID");
					String city = rs.getString("City");
					String address =rs.getString("Address");
					String branch = rs.getString("Branch");
					String supplyOption = rs.getString("SupplyOption");
					String type = rs.getString("Type");
					String requestedDate = rs.getString("RequestDate");
					String requestedTime = rs.getString("RequestTime");
					String approvalTime = rs.getString("ApprovalTime");
					String approvalDate = rs.getString("ApprovalDate");
					String arrivalTime = rs.getString("ArrivalTime"); //?
					float totalPrice = rs.getFloat("TotalPrice");
					String status = rs.getString("Status");

					Order order = new Order(null, orderID, recipient, recipientPhone, getSupplyOption(supplyOption),
							getSupplierById(dbConn,supplierID), customer, getBranch(branch), requestedDate, requestedTime, getOrderType(type),
							totalPrice, approvalTime, arrivalTime, status, city, address, approvalDate, null);
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
		String query = "SELECT * FROM orders WHERE CustomerID = ? AND Status IN ('On-time', 'Late')";

		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, customer.getId());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int orderID = rs.getInt("OrderID");
					String recipient = rs.getString("Recipient");
					String recipientPhone = rs.getString("Recipient Phone");
					int supplierID = rs.getInt("SupplierID");
					String city = rs.getString("City");
					String address =rs.getString("Address");
					String branch = rs.getString("Branch");
					String supplyOption = rs.getString("SupplyOption");
					String type = rs.getString("Type");
					String requestedDate = rs.getString("RequestDate");
					String requestedTime = rs.getString("RequestTime");
					String approvalTime = rs.getString("ApprovalTime");
					String approvalDate = rs.getString("ApprovalDate");
					String arrivalTime = rs.getString("ArrivalTime");
					String arrivalDate = rs.getString("ArrivalDate");
					float totalPrice = rs.getFloat("TotalPrice");
					String status = rs.getString("Status");

					Order order = new Order(null, orderID, recipient, recipientPhone, getSupplyOption(supplyOption),
							getSupplierById(dbConn,supplierID), customer, getBranch(branch), requestedDate, requestedTime, getOrderType(type),
							totalPrice, approvalTime, arrivalTime, status, city, address, approvalDate, arrivalDate);
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
		String query = "UPDATE orders SET ArrivalTime = ? ,ArrivalDate = ? ,status = ? WHERE OrderID = ?";
		boolean late = isLateByTimeDiffAndStatus(order.getType(),order.getApprovalTimer(), order.getApprovalDate());
		String status;
		String currTime = getCurrTime();
		String currDate = getTodayDate();
		if (late) {
			status = "Late";
			updateCustomerWalletBalance(dbConn,order,0);
			}
		else {status = "On-time";}
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, currTime);
			stmt.setString(2, currDate);
			stmt.setString(3, status);
			stmt.setInt(4, order.getOrderID());
			stmt.executeUpdate();
		}
	}
	
    public ServerResponseDataContainer importSupplierItems(Connection dbConn, Supplier supplier) throws SQLException{
        ServerResponseDataContainer response = new ServerResponseDataContainer();
        String query = "SELECT * FROM items WHERE SupplierID = ?";
        List<Item> specificSupplierItems = new ArrayList<>();
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, supplier.getSupplierID());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int itemID = rs.getInt("ID");
                    String name = rs.getString("Name");
                    String category = rs.getString("Category");
                    String description = rs.getString("Description");
                    boolean customSize = rs.getBoolean("CustomSize");
                    boolean customDonenessDegree = rs.getBoolean("CustomDoneness");
                    boolean customRestrictions = rs.getBoolean("CustomRestrictions");
                    float price = rs.getFloat("Price");
                    
                    Item item = new Item(itemID, supplier.getSupplierID(), name, categoryStringToEnum(category), description, customSize, customDonenessDegree, customRestrictions, price);
                    specificSupplierItems.add(item);
                }
            }
        }
        response.setMessage(specificSupplierItems);
        response.setResponse(ServerResponse.SUPPLIER_ITEMS);
        
        return response;
    }
	
    
    
	public ServerResponseDataContainer FetchRelevantCities(Connection dbConn, Supplier supplier) throws SQLException {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		List<String> cities = new ArrayList<>();
		String query = "SELECT City FROM suppliers_cities WHERE SupplierID = ?";

		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, supplier.getSupplierID());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String city =rs.getString("City");
					
					cities.add(city);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		response.setMessage(cities);
		response.setResponse(ServerResponse.SUPPLIER_RELEVANT_CITIES);
		return response;
	}
	
	
	public ServerResponseDataContainer updateOrderAndItems(Connection dbConn, Order order, Map<ItemInOrder, Integer> receivedCart)
			throws SQLException {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		// Ensure auto-commit is turned off for transaction management
		dbConn.setAutoCommit(false);

		try {
			// Insert into orders table with manually assigned OrderID
			String insertOrderSQL = "INSERT INTO orders (CustomerID, Recipient, `Recipient Phone`, SupplierID, City, Address, Branch, SupplyOption, Type, RequestDate, RequestTime, ApprovalTime, ApprovalDate, ArrivalTime, TotalPrice, Status) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			try (PreparedStatement orderStmt = dbConn.prepareStatement(insertOrderSQL,
					Statement.RETURN_GENERATED_KEYS)) {
				orderStmt.setInt(1, order.getCustomer().getId());
				orderStmt.setString(2, order.getRecipient());
				orderStmt.setString(3, order.getRecipientPhone());
				orderStmt.setInt(4, order.getSupplier().getSupplierID());
				orderStmt.setString(5, order.getCity());
				orderStmt.setString(6, order.getAddress());
				orderStmt.setString(7, order.getBranchName().toShortStringTwo()); // Assuming this is correct
				String supplyOption = getDatabaseSupplyOption(order.getSupplyOption());
				orderStmt.setString(8, supplyOption); // Use the mapping function
				String orderType = getDatabaseOrderType(order.getType());
				orderStmt.setString(9, orderType); // Use the mapping function
				orderStmt.setDate(10, java.sql.Date.valueOf(order.getRequestedDate()));
				orderStmt.setString(11, order.getRequestedTime());
				orderStmt.setString(12, null); //approvalTime
				orderStmt.setDate(13, null);
				orderStmt.setTime(14, null);
				orderStmt.setFloat(15, order.getTotalPrice());
				orderStmt.setString(16, "Awaiting");		

				// Execute the insert statement
				int affectedRows = orderStmt.executeUpdate();
				
				// Update items_in_order table with the new OrderID
				if (affectedRows > 0) {
	                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
	                    if (generatedKeys.next()) {
	                        int newOrderID = generatedKeys.getInt(1);

	                        response.setMessage(newOrderID);
	                        response.setResponse(ServerResponse.UPDATED_ORDER_ID);
	                        
	                        // Insert items into items_in_orders table
	                        String insertItemSQL = "INSERT INTO items_in_orders (OrderID, CustomerID, ItemID, SupplierID, ItemName, Category, Size, Doneness, Restrictions, Quantity) "
	                                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	                        try (PreparedStatement itemStmt = dbConn.prepareStatement(insertItemSQL)) {
	                            for (Map.Entry<ItemInOrder, Integer> entry : receivedCart.entrySet()) {
	                                ItemInOrder item = entry.getKey();
	                                int quantity = entry.getValue();

	                                itemStmt.setInt(1, newOrderID);
	                                itemStmt.setInt(2, order.getCustomer().getId());
	                                itemStmt.setInt(3, item.getItemID());
	                                itemStmt.setInt(4, item.getSupplierID());
	                                itemStmt.setString(5, item.getName());
	                                itemStmt.setString(6, item.getType().toString());
	                                itemStmt.setString(7, item.getSize());
	                                itemStmt.setString(8, item.getDonenessDegree());
	                                itemStmt.setString(9, item.getRestrictions());
	                                itemStmt.setInt(10, quantity);
                         
	                                itemStmt.addBatch();
	                            }

	                            // Execute batch insert
	                            itemStmt.executeBatch();
	                        }
	                    }
	                }
	            }
	        }

	        // Commit the transaction
	        dbConn.commit();
		} catch (SQLIntegrityConstraintViolationException | java.sql.BatchUpdateException e) {
			System.err.println("Nice try");
			response.setResponse(ServerResponse.ITEM_WAS_DELETED);
			response.setMessage("The Supplier Deleted this item before you could finish the order");
	        dbConn.rollback();
	        return response;
	    } catch (SQLException e) {
	        dbConn.rollback();
	        System.err.println("SQL Error: " + e.getMessage());
	        throw e;
	    } finally {
	        dbConn.setAutoCommit(true);
	    }
	    return response;
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
	// if amount == 0 then refund, alse remove amount from total price
	public void updateCustomerWalletBalance(Connection dbConn, Order order, float amount) throws SQLException {
		double updateAmount = 0;
		String query = "Select * FROM orders WHERE OrderID = ?";
		
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, order.getOrderID());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int customerID = rs.getInt("CustomerID");
					float amountPaid = rs.getFloat("TotalPrice");
					
					if (amount == 0) {
					//get the amount i need to update
						updateAmount = amountPaid * 0.5;	
					}
					else {
						updateAmount = ((-1) * amount ); 
					}
					
					//nested query to update the wallet balance
					String updateWalletQuery = "UPDATE customers SET WalletBalance = WalletBalance + ? WHERE ID = ?";
					try (PreparedStatement updateStmt = dbConn.prepareStatement(updateWalletQuery)) {
                        updateStmt.setDouble(1, updateAmount);
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
					Supplier supplier = new Supplier(supplierID, name, city, address, getBranch(branch), email, phone);
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
		if (method.equals("Takeaway")) {
			return SupplyMethod.TAKEAWAY;
		}
		if (method.equals("Robot Delivery")) {
			return SupplyMethod.ROBOT;
		}
		if (method.equals("Basic Delivery")) {
			return SupplyMethod.BASIC;
		} else {
			return SupplyMethod.SHARED;
		}
	}
	
	private  String getDatabaseSupplyOption(SupplyMethod supplyMethod) {
	    switch (supplyMethod) {
	        case TAKEAWAY:
	            return "Takeaway"; // Matches your database enum value
	        case ROBOT:
	            return "Robot Delivery";
	        case BASIC:
	            return "Basic Delivery";
	        case SHARED:
	            return "Shared Delivery";
	        default:
	            throw new IllegalArgumentException("Unexpected value: " + supplyMethod);
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
	
	private  String getDatabaseOrderType(OrderType orderType) {
	    switch (orderType) {
	        case PRE_ORDER:
	            return "Pre-order"; // Matches your database enum value
	        case REGULAR:
	            return "Regular";
	        default:
	            throw new IllegalArgumentException("Unexpected value: " + orderType);
	    }
	}
	
	
    /**
     * Determines if the order is late based on the time difference and order type.
     *
     * @param type the type of the order
     * @param approvalTime the approval time of the order
     * @return true if the order is late, false otherwise
     */
	private boolean isLateByTimeDiffAndStatus(OrderType type , String approvalTime , String approvalDateStr) {
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Adjust the pattern to match your date format
	    LocalDate approvalDate = LocalDate.parse(approvalDateStr, dateFormatter);
	    
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime givenTime = LocalTime.parse(approvalTime, formatter);
		LocalTime currentTime = LocalTime.now();
		LocalDate currentDate = LocalDate.now();
		
		Duration duration = Duration.between(givenTime, currentTime);
		long totalMinutes = duration.toMinutes();
	    if (type.toString().equals("Regular")) {
	        if (totalMinutes > 60 || approvalDate.isBefore(currentDate)) {
	            return true;
	        }
	    } else if (type.toString().equals("PreOrder")) {
	        if (totalMinutes > 20 || approvalDate.isBefore(currentDate)) {
	            return true;
	        }
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
	
	public String getTodayDate() {
	    LocalDate today = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    return today.format(formatter);
	}
	
	private Category categoryStringToEnum(String type) {
        if (type.equals("Main Course")) {
            return Category.MAINCOURSE;
        }
        if (type.equals("First Course")) {
            return Category.FIRSTCOURSE;
        }
        if (type.equals("Salad")){
            return Category.SALAD;
        }
        if (type.equals("Dessert")) {
            return Category.DESSERT;
        }
        else return Category.BEVERAGE;
    }
}

