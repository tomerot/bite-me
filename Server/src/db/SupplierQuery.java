package db;

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
import entities.ItemInOrder;
import entities.Order;
import entities.OrderType;
import entities.SupplyMethod;
import enums.ServerResponse;

public class SupplierQuery {

	// Get orders data from server (including all the items per order)
		public static ServerResponseDataContainer getOrdersData(Connection dbConn, int supplierID) {
			//first get all the orders per the supplier.			
			ServerResponseDataContainer response = new ServerResponseDataContainer();
			Map<Order, ArrayList<ItemInOrder>> ordersMap = new HashMap<>();
			String ordersQuery = "SELECT * FROM orders WHERE SupplierID = ? AND Status IN ('Awaiting', 'Approved');";
			try (PreparedStatement stmt = dbConn.prepareStatement(ordersQuery)) {
				stmt.setInt(1, supplierID);
				
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						int OrderID = rs.getInt("OrderID");
						System.out.println("ID "+OrderID);
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

						Order order = new Order(OrderID, recipientName,recipientPhone,city,address,supplyMethod, orderType, reqDate, reqTime, approvalTime,arrivalTime, totalPrice,status);
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
