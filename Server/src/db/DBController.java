package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.Order;

/**
 * The DB Controller class is the way our application "talk" with the database.
 * This class use "mysql-connector-java-8.0.13".
 */
public class DBController {

	/**
	 * This method is trying to connect to mySQL database, using jdbc driver. This
	 * method is being called from server, and return it's connection.
	 * 
	 * @param db - class which contains the required information for database
	 *           (hostname,username,password)
	 * @return if connected successfully -> return the new Connection. else ->
	 *         return null.
	 */
	public static Connection connectToMySqlDB(DBConnectionDetails db) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			System.out.println("Driver definition failed");
		}

		try {
			String url = "jdbc:mysql://" + db.getIp() + "/" + db.getName() + "?serverTimezone=IST";
			Connection conn = DriverManager.getConnection(url, db.getUsername(), db.getPassword());

			return conn;
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return null;
		}
	}

	// Method to save all orders from the db into list which is returned to the
	// server
	public static List<Order> fetchOrders(Connection dbConn) {
		List<Order> orders = new ArrayList<>();

		// SQL query to retrieve orders
		String query = "SELECT restName, orderNumber, totalPrice, orderListNumber, orderAddress from orders";

		try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String restaurant = rs.getString("restName");
				int orderNumber = rs.getInt("orderNumber");
				double totalPrice = rs.getDouble("totalPrice");
				String orderListNum = rs.getString("orderListNumber");
				String orderAddress = rs.getString("orderAddress");

				Order order = new Order(restaurant, orderNumber, totalPrice, orderListNum, orderAddress);
				orders.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle exception, log or throw it as needed
		}

		return orders;
	}

	// Method to update specific order given from the server
	public static boolean updateOrder(Connection dbConn, Order order) {
		String query = "UPDATE orders SET totalPrice = ?, orderAddress = ? WHERE orderNumber = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setDouble(1, order.getTotalPrice());
			stmt.setString(2, order.getOrderAddress());
			stmt.setInt(3, order.getOrderNumber());
			int affectedRows = stmt.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
