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

	// Get orders data from server (including all the items per order)
		public static ServerResponseDataContainer getOrdersData(Connection dbConn, int supplierID) {			
			ServerResponseDataContainer response = new ServerResponseDataContainer();
			Map<Order, ArrayList<ItemInOrder>> ordersMap = new HashMap<>();
			//first get all the orders with status awaiting/approved and the  email of the recipient per supplier 			
			String ordersQuery = "SELECT o.*, c.Email FROM orders AS o JOIN customers AS c ON o.CustomerID = c.ID WHERE o.SupplierID = ? AND o.Status IN ('Approved', 'Awaiting');";

			try (PreparedStatement stmt = dbConn.prepareStatement(ordersQuery)) {
				stmt.setInt(1, supplierID);
				
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						int OrderID = rs.getInt("OrderID");
						System.out.println("ID "+OrderID);
						String customerID = rs.getString("CustomerID");
						System.out.println("customerID " + customerID);
						String recipientName = rs.getString("Recipient");
						System.out.println("rec");
						String recipientPhone = rs.getString("Recipient Phone");
						System.out.println("recPhone");
						String city = rs.getString("City");
						System.out.println("city");
						String address = rs.getString("Address");
						System.out.println("address");
						String supplyMethodName = rs.getString("SupplyOption");
						System.out.println("supMethod");
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
						System.out.println("type");
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
						System.out.println("reqDate");
						String reqTime = rs.getString("RequestTime");
						System.out.println("reqTime");
						String approvalTime = rs.getString("ApprovalTime");
						System.out.println("ApprovalTime");
						String arrivalTime = rs.getString("arrivalTime");
						System.out.println("ArrivalTime");
						float totalPrice = rs.getFloat("TotalPrice");
						System.out.println("price");
						String status = rs.getString("Status");
						System.out.println("status = "+  status);
						String recipientEmail = rs.getString("Email");
						System.out.println(recipientEmail);

						Order order = new Order(OrderID, customerID, recipientName,recipientPhone, recipientEmail,city,address,supplyMethod, orderType, reqDate, reqTime, approvalTime,arrivalTime, totalPrice,status);
						System.out.println(order);
						
						//now we will create an arrayList of all the items in certain order.
						ArrayList<ItemInOrder> itemsList = new ArrayList<ItemInOrder>();
						
						//This result includes all columns from items_in_orders along with the Price from the items table.
						String itemsQuery = "SELECT iio.*, i.Price FROM items_in_orders iio JOIN items i ON iio.ItemID = i.ID WHERE iio.OrderID = ?;";
						try (PreparedStatement itemStmt = dbConn.prepareStatement(itemsQuery)){
							itemStmt.setInt(1, order.getOrderID());
							try (ResultSet rs2 = itemStmt.executeQuery()){
								while (rs2.next()) {
									System.out.println("In itemInOrder While");
									int itemID = rs2.getInt("ItemID");
									String itemName = rs2.getString("ItemName");
									String itemSize = rs2.getString("size");
									String itemDoneness = rs2.getString("Doneness");
									String itemRestrictions = rs2.getString("Restrictions");
									int itemQuantity = rs2.getInt("Quantity");
									float itemPrice = rs2.getFloat("Price");
									System.out.println(itemPrice);
									
									ItemInOrder item = new ItemInOrder(itemID, itemName, itemSize, itemDoneness, itemRestrictions, itemQuantity, itemPrice);
									itemsList.add(item);
									System.out.println("item after add.. " + item);
								}
								System.out.println(itemsList);
								//put in the map the order and it's items list
								ordersMap.put(order, itemsList); 
							}
							catch (SQLException e) {
								e.printStackTrace();
							}
						}
						catch (SQLException e) {
							e.printStackTrace();
						}
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			response.setMessage(ordersMap);
			response.setResponse(null);
			return response;
		}
		
		
		
		//if orderInfo[1]==0 -> from 'Awaiting' to 'Approved' ,else(orderInfo[1]==1) from 'Approved' to 'Ready'
		public static ServerResponseDataContainer UpdateOrderStatus(Connection dbConn, int[] orderInfo) {
			ServerResponseDataContainer response = new ServerResponseDataContainer();
			int orderID = orderInfo[0];
	        int statusFlag = orderInfo[1];
	        String newStatus;
	        String sql;
	        
	        Timestamp approvalTime = new Timestamp(System.currentTimeMillis());
	        
	        // Determine the new status and SQL query based on statusFlag
	        if (statusFlag == 0) {
	            newStatus = "Approved";
	            sql = "UPDATE orders SET Status = ?, ApprovalTime = ? WHERE OrderID = ?";
	        } 
	        else // if (statusFlag == 1) 
	        {
	            newStatus = "Ready";
	            sql = "UPDATE orders SET Status = ? WHERE OrderID = ?";		        
	        }
	        
	        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
	            stmt.setString(1, newStatus);

	            // Set ApprovalTime if statusFlag is 0 (Approved)
	            if (statusFlag == 0) { 
	                stmt.setTimestamp(2, approvalTime); //update approval time
	                stmt.setInt(3, orderID);
	            } else {
	                // Only need to set status for "Ready" update
	                stmt.setInt(2, orderID);
	            }
	            
	            int rowsAffected = stmt.executeUpdate();
	            if(rowsAffected == 1) {
	            	if(statusFlag == 0)
	            		response.setMessage(approvalTime);
	            	else
	            		response.setMessage(null);
					response.setResponse(ServerResponse.SUPPLIER_UPDATE_ORDER_STATUS_SUCCESS);
				 }
				 else {
					 response.setMessage(null);
					 response.setResponse(null);
				 }	
	       
	       } catch (SQLException e) {
	    	   e.printStackTrace();
            
	       	}
			return response;
		}
		
		
		public static ServerResponseDataContainer RefreshAwaitingOrders(Connection dbConn, int supplierID) {
			ServerResponseDataContainer response = new ServerResponseDataContainer();
			String query = "SELECT o.*, c.Email FROM orders AS o JOIN customers AS c ON o.CustomerID = c.ID WHERE o.SupplierID = ? AND o.Status ='Awaiting';";
			Map<Order, ArrayList<ItemInOrder>> ordersMap = new HashMap<>();
			
			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
				stmt.setInt(1, supplierID);
				
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						int OrderID = rs.getInt("OrderID");
						System.out.println("ID "+ OrderID);
						String customerID = rs.getString("CustomerID");
						System.out.println("customerID " + customerID);
						String recipientName = rs.getString("Recipient");
						System.out.println("rec");
						String recipientPhone = rs.getString("Recipient Phone");
						System.out.println("recPhone");
						String city = rs.getString("City");
						System.out.println("city");
						String address = rs.getString("Address");
						System.out.println("address");
						String supplyMethodName = rs.getString("SupplyOption");
						System.out.println("supMethod");
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
						System.out.println("type");
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
						System.out.println("reqDate");
						String reqTime = rs.getString("RequestTime");
						System.out.println("reqTime");
						String approvalTime = rs.getString("ApprovalTime");
						System.out.println("ApprovalTime");
						String arrivalTime = rs.getString("arrivalTime");
						System.out.println("ArrivalTime");
						float totalPrice = rs.getFloat("TotalPrice");
						System.out.println("price");
						String status = rs.getString("Status");
						System.out.println("status = "+  status);
						String recipientEmail = rs.getString("Email");
						System.out.println(recipientEmail);

						Order order = new Order(OrderID, customerID, recipientName,recipientPhone, recipientEmail,city,address,supplyMethod, orderType, reqDate, reqTime, approvalTime,arrivalTime, totalPrice,status);
						System.out.println(order);
						
						//now we will create an arrayList of all the items in certain order.
						ArrayList<ItemInOrder> itemsList = new ArrayList<ItemInOrder>();
						
						//This result includes all columns from items_in_orders along with the Price from the items table.
						String itemsQuery = "SELECT iio.*, i.Price FROM items_in_orders iio JOIN items i ON iio.ItemID = i.ID WHERE iio.OrderID = ?;";
						try (PreparedStatement itemStmt = dbConn.prepareStatement(itemsQuery)){
							itemStmt.setInt(1, order.getOrderID());
							try (ResultSet rs2 = itemStmt.executeQuery()){
								while (rs2.next()) {
									System.out.println("In itemInOrder While");
									int itemID = rs2.getInt("ItemID");
									String itemName = rs2.getString("ItemName");
									String itemSize = rs2.getString("size");
									String itemDoneness = rs2.getString("Doneness");
									String itemRestrictions = rs2.getString("Restrictions");
									int itemQuantity = rs2.getInt("Quantity");
									float itemPrice = rs2.getFloat("Price");
									System.out.println(itemPrice);
									
									ItemInOrder item = new ItemInOrder(itemID, itemName, itemSize, itemDoneness, itemRestrictions, itemQuantity, itemPrice);
									itemsList.add(item);
									System.out.println("item after add.. " + item);
								}
								System.out.println(itemsList);
								//put in the map the order and it's items list
								ordersMap.put(order, itemsList); 
							}
							catch (SQLException e) {
								e.printStackTrace();
							}
						}
						catch (SQLException e) {
							e.printStackTrace();
						}
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			response.setMessage(ordersMap);
			response.setResponse(null);
			return response;
		}
}
