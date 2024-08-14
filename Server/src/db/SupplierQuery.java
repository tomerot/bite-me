package db;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import containers.ServerResponseDataContainer;
import entities.Category;
import entities.Item;
import entities.ItemInOrder;
import entities.Order;
import entities.OrderType;
import entities.SupplyMethod;
import enums.ServerResponse;

public class SupplierQuery {
	
	/**
	 * Fetches orders data including associated items for a specific supplier.
	 *
	 * @param dbConn the database connection used to execute the SQL query
	 * @param supplierID the ID of the supplier whose orders are to be fetched
	 * @return a {@link ServerResponseDataContainer} containing a map of orders to lists of items and a response message
	 */
    public ServerResponseDataContainer getOrdersData(Connection dbConn, int supplierID) {
        ServerResponseDataContainer response = new ServerResponseDataContainer();
        Map<Order, ArrayList<ItemInOrder>> ordersMap = new HashMap<>();
        String ordersQuery = "SELECT o.*, c.Email, c.Phone FROM orders AS o JOIN customers AS c ON o.CustomerID = c.ID WHERE o.SupplierID = ? AND o.Status IN ('Approved', 'Awaiting');";

        try (PreparedStatement stmt = dbConn.prepareStatement(ordersQuery)) {
            stmt.setInt(1, supplierID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = createOrderFromResultSet(rs);
                    List<ItemInOrder> itemsList = fetchItemsForOrder(dbConn, order.getOrderID());
                    ordersMap.put(order, new ArrayList<>(itemsList));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.setMessage(ordersMap);
        response.setResponse(null);
        return response;
    }

    /**
     * Updates the status of an order based on the provided information.
     *
     * @param dbConn the database connection used to execute the SQL query
     * @param orderInfo an array where the first element is the order ID and the second element is the status flag
     * @return a {@link ServerResponseDataContainer} containing the result of the operation and the approval time if applicable
     */
    public ServerResponseDataContainer UpdateOrderStatus(Connection dbConn, int[] orderInfo) {
        ServerResponseDataContainer response = new ServerResponseDataContainer();
        int orderID = orderInfo[0];
        int statusFlag = orderInfo[1];
        int takeaway = orderInfo[2]; //1 if its takeaway
        String newStatus = statusFlag == 0 ? "Approved" : "Ready";
        String sql = statusFlag == 0 ? "UPDATE orders SET Status = ?, ApprovalTime = ?, ApprovalDate = ? WHERE OrderID = ?" : "UPDATE orders SET Status = ? WHERE OrderID = ?";
        //String approvalTime = statusFlag == 0 ? getCurrTime() : null;
        String currDate = getTodayDate();
        String currTime = getCurrTime();

        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
			if (takeaway == 1) {
				stmt.setString(1, "On-time");
			} else {
				stmt.setString(1, newStatus);
			}
            if (statusFlag == 0) {
                stmt.setString(2, currTime);
                stmt.setString(3, currDate);
                stmt.setInt(4, orderID);
            } else {
                stmt.setInt(2, orderID);
                if (takeaway == 1) {
                	updateTakeAwayArrivalTimeDate(dbConn,currTime,currDate,orderID);
                }
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 1) {
                response.setMessage(statusFlag == 0 ? currTime : null);
                response.setResponse(ServerResponse.SUPPLIER_UPDATE_ORDER_STATUS_SUCCESS);
            } else {
                response.setResponse(null);
                response.setMessage(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }
    
    
    /**
     * Updates the arrival time and date of a takeaway order in the `orders` table.
     * 
     * @param dbConn the connection to the database
     * @param currTime the current arrival time to be set, in `HH:mm:ss` format
     * @param currDate the current arrival date to be set, in `yyyy-MM-dd` format
     * @param orderID the unique identifier of the order to be updated
     */
    private void updateTakeAwayArrivalTimeDate(Connection dbConn, String currTime, String currDate, int orderID) {
    	String sql = "UPDATE orders SET  ArrivalTime = ?, ArrivalDate = ? WHERE OrderID = ?";
    	try (PreparedStatement stmt = dbConn.prepareStatement(sql)){
    		stmt.setString(1, currTime);
    		stmt.setString(2, currDate);
    		stmt.setInt(3, orderID);
    		
    		stmt.executeUpdate();
    		
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Refreshes and retrieves awaiting orders for a specific supplier.
     *
     * @param dbConn the database connection used to execute the SQL query
     * @param supplierID the ID of the supplier whose awaiting orders are to be fetched
     * @return a {@link ServerResponseDataContainer} containing a map of orders to lists of items and a response message
     */
    public ServerResponseDataContainer RefreshAwaitingOrders(Connection dbConn, int supplierID) {
        ServerResponseDataContainer response = new ServerResponseDataContainer();
        String query = "SELECT o.*, c.Email, c.Phone FROM orders AS o JOIN customers AS c ON o.CustomerID = c.ID WHERE o.SupplierID = ? AND o.Status ='Awaiting';";
        Map<Order, ArrayList<ItemInOrder>> ordersMap = new HashMap<>();

        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, supplierID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = createOrderFromResultSet(rs);
                    List<ItemInOrder> itemsList = fetchItemsForOrder(dbConn, order.getOrderID());
                    ordersMap.put(order, new ArrayList<>(itemsList));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.setMessage(ordersMap);
        response.setResponse(null);
        return response;
    }
    
    
		
	// Helper method to create Order object
    /**
     * Creates an order object from the provided {@link ResultSet}.
     *
     * @param rs the {@link ResultSet} containing the order data
     * @return an {@link Order} object populated with data from the {@link ResultSet}
     * @throws SQLException if a database access error occurs or the {@link ResultSet} is not properly configured
     */
    private Order createOrderFromResultSet(ResultSet rs) throws SQLException {
        int orderID = rs.getInt("OrderID");
        String customerID = rs.getString("CustomerID");
        String recipientName = rs.getString("Recipient");
        String recipientPhone = rs.getString("Recipient Phone");
        String city = rs.getString("City");
        String address = rs.getString("Address");
        String supplyMethodName = rs.getString("SupplyOption");
        SupplyMethod supplyMethod;
        switch(supplyMethodName) {
			case "Takeaway":
				supplyMethod = SupplyMethod.TAKEAWAY;
				break;
			case "Robot Delivery":
				supplyMethod = SupplyMethod.ROBOT;
				break;
			case "Basic Delivery":
				supplyMethod = SupplyMethod.BASIC;
				break;
			case "Shared Delivery":
				supplyMethod = SupplyMethod.SHARED;
				break;
			default:
				supplyMethod = null;
        }
        
        String typeName = rs.getString("Type");
        OrderType orderType;
        switch (typeName) {
			case "Pre-order":
				orderType = OrderType.PRE_ORDER;
				break;
			case "Regular":
				orderType = OrderType.REGULAR;
				break;
			default:
				orderType = null;
        }
        
        String reqDate = rs.getString("RequestDate");
        String reqTime = rs.getString("RequestTime");
        String approvalTime = rs.getString("ApprovalTime");
        String arrivalTime = rs.getString("ArrivalTime");
        float totalPrice = rs.getFloat("TotalPrice");
        String status = rs.getString("Status");
        String recipientEmail = rs.getString("Email");
        String customerPhone = rs.getString("Phone");

        return new Order(orderID, customerID, recipientName, customerPhone, recipientEmail, city, address, supplyMethod, orderType, reqDate, reqTime, approvalTime, arrivalTime, totalPrice, status);
    }
	    
	    
    /**
    * Fetches a list of ItemsInOrder objects associated with a specific order ID.
    *
    * @param dbConn the database connection used to execute the SQL query
    * @param orderID the ID of the order for which to fetch items
    * @return a {@link List} of {@link ItemInOrder} objects associated with the specified order ID
    * @throws SQLException if a database access error occurs or the SQL query fails
    */
    private List<ItemInOrder> fetchItemsForOrder(Connection dbConn, int orderID) throws SQLException {
        List<ItemInOrder> itemsList = new ArrayList<>();
        String itemsQuery = "SELECT iio.*, i.Price FROM items_in_orders iio JOIN items i ON iio.ItemID = i.ID WHERE iio.OrderID = ?;";
        
        try (PreparedStatement itemStmt = dbConn.prepareStatement(itemsQuery)) {
            itemStmt.setInt(1, orderID);
            try (ResultSet rs2 = itemStmt.executeQuery()) {
                while (rs2.next()) {
                    int itemID = rs2.getInt("ItemID");
                    String itemName = rs2.getString("ItemName");
                    String itemSize = rs2.getString("Size");
                    String itemDoneness = rs2.getString("Doneness");
                    String itemRestrictions = rs2.getString("Restrictions");
                    int itemQuantity = rs2.getInt("Quantity");
                    float itemPrice = rs2.getFloat("Price");
                    ItemInOrder item = new ItemInOrder(itemID, itemName, itemSize, itemDoneness, itemRestrictions, itemQuantity, itemPrice);
                    itemsList.add(item);
                }
            }
        }
        return itemsList;
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
}