package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import containers.ServerResponseDataContainer;
import entities.Category;
import entities.Order;
import entities.OrderType;
import entities.SupplyMethod;

public class SupplierQuery {

	// Get orders data from server (including all the items per order)
		public static ServerResponseDataContainer getOrdersData(Connection dbConn, int supplierID) {
			//first get all the orders per the supplier.
			
			String ordersQuery = "SELECT * FROM orders WHERE SupplierID = ?;";			
			ServerResponseDataContainer response = new ServerResponseDataContainer();
			try (PreparedStatement stmt = dbConn.prepareStatement(ordersQuery)) {
				stmt.setInt(1, supplierID);
				
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						int OrderID = rs.getInt("OrderID");
						System.out.println("ID "+OrderID);
						String recipientName = rs.getString("Recipient");
						System.out.println("rec\n");
						String recipientPhone = rs.getString("RecipientPhone");
						System.out.println("recPhome\n");
						String city = rs.getString("City");
						System.out.println("city");
						String address = rs.getString("Address");
						System.out.println("add");
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
						String approvalTime = rs.getString("ApprovalTime");
						String arrivalTime = rs.getString("arrivalTime");
						float totalPrice = rs.getFloat("TotalPrice");
						String status = rs.getString("Status");
						System.out.println("ststus");

						Order order = new Order(OrderID, recipientName,recipientPhone,city,address,supplyMethod, orderType, reqDate, reqTime, approvalTime,arrivalTime, totalPrice,status);
						System.out.println(order.getOrderID());
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			response.setMessage(null);
			response.setResponse(null);
			return response;
		}
}
