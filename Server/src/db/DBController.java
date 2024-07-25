package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
}
