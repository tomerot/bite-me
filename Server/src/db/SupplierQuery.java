package db;

import java.sql.Timestamp;
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
    public static ServerResponseDataContainer getOrdersData(Connection dbConn, int supplierID) {
        ServerResponseDataContainer response = new ServerResponseDataContainer();
        Map<Order, ArrayList<ItemInOrder>> ordersMap = new HashMap<>();
        String ordersQuery = "SELECT o.*, c.Email FROM orders AS o JOIN customers AS c ON o.CustomerID = c.ID WHERE o.SupplierID = ? AND o.Status IN ('Approved', 'Awaiting');";

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
    public static ServerResponseDataContainer UpdateOrderStatus(Connection dbConn, int[] orderInfo) {
        ServerResponseDataContainer response = new ServerResponseDataContainer();
        int orderID = orderInfo[0];
        int statusFlag = orderInfo[1];
        String newStatus = statusFlag == 0 ? "Approved" : "Ready";
        String sql = statusFlag == 0 ? "UPDATE orders SET Status = ?, ApprovalTime = ? WHERE OrderID = ?" : "UPDATE orders SET Status = ? WHERE OrderID = ?";
        Timestamp approvalTime = statusFlag == 0 ? new Timestamp(System.currentTimeMillis()) : null;

        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            if (statusFlag == 0) {
                stmt.setTimestamp(2, approvalTime);
                stmt.setInt(3, orderID);
            } else {
                stmt.setInt(2, orderID);
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 1) {
                response.setMessage(statusFlag == 0 ? approvalTime : null);
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
     * Refreshes and retrieves awaiting orders for a specific supplier.
     *
     * @param dbConn the database connection used to execute the SQL query
     * @param supplierID the ID of the supplier whose awaiting orders are to be fetched
     * @return a {@link ServerResponseDataContainer} containing a map of orders to lists of items and a response message
     */
    public static ServerResponseDataContainer RefreshAwaitingOrders(Connection dbConn, int supplierID) {
        ServerResponseDataContainer response = new ServerResponseDataContainer();
        String query = "SELECT o.*, c.Email FROM orders AS o JOIN customers AS c ON o.CustomerID = c.ID WHERE o.SupplierID = ? AND o.Status ='Awaiting';";
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
    private static Order createOrderFromResultSet(ResultSet rs) throws SQLException {
        int orderID = rs.getInt("OrderID");
        String customerID = rs.getString("CustomerID");
        String recipientName = rs.getString("Recipient");
        String recipientPhone = rs.getString("Recipient Phone");
        String city = rs.getString("City");
        String address = rs.getString("Address");
        String supplyMethodName = rs.getString("SupplyOption");
        SupplyMethod supplyMethod;
        switch(supplyMethodName) {
			case "TakeAway":
				supplyMethod = SupplyMethod.TAKEAWAY;
				break;
			case "Robot":
				supplyMethod = SupplyMethod.ROBOT;
				break;
			case "Basic":
				supplyMethod = SupplyMethod.BASIC;
				break;
			case "Shared":
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

        return new Order(orderID, customerID, recipientName, recipientPhone, recipientEmail, city, address, supplyMethod, orderType, reqDate, reqTime, approvalTime, arrivalTime, totalPrice, status);
    }
	    
	    
    /**
    * Fetches a list of ItemsInOrder objects associated with a specific order ID.
    *
    * @param dbConn the database connection used to execute the SQL query
    * @param orderID the ID of the order for which to fetch items
    * @return a {@link List} of {@link ItemInOrder} objects associated with the specified order ID
    * @throws SQLException if a database access error occurs or the SQL query fails
    */
    private static List<ItemInOrder> fetchItemsForOrder(Connection dbConn, int orderID) throws SQLException {
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
}
