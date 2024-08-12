package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import containers.ServerResponseDataContainer;
import entities.AuthorizedEmployee;
import entities.BranchManager;
import entities.Category;
import entities.Ceo;
import entities.Item;
import entities.Order;
import entities.OrderType;
import entities.Customer;
import entities.Supplier;
import entities.SupplierIncome;
import entities.SupplierQuarterReportData;
import entities.User;
import enums.Branch;
import enums.CustomerType;
import enums.ServerResponse;
import enums.UserType;
/**
 * Handles SQL queries related to various reports and user management tasks.
 */
public class ManagersQuery {
	public ManagersQuery() {
	}
    /**
     * Imports report data based on the provided report type and information.
     *
     * @param dbConn the database connection to use
     * @param reportInfo a list of strings containing report parameters: [reportType, branch, month, year]
     * @return a {@link ServerResponseDataContainer} containing the report data
     * @throws SQLException if a database access error occurs
     */
	public ServerResponseDataContainer importReportData(Connection dbConn, List<String> reportInfo) throws SQLException {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		List<String> returnData=new ArrayList<String>();
		List<SupplierIncome> SupplierIncomeList=new ArrayList<SupplierIncome>();
		switch (reportInfo.get(0)) {
		case "Orders Reports":{
			int salad=0;
			int firstCourse=0;
			int mainCourse=0;
			int dessert=0;
			int beverage=0;
			int tupelsCtr=0;
			String query = "SELECT * FROM orders_reports WHERE Branch = ? AND Month = ? AND Year = ?";
			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
				stmt.setString(1, reportInfo.get(1));
				stmt.setInt(2, Integer.valueOf(reportInfo.get(2)));
				stmt.setInt(3, Integer.valueOf(reportInfo.get(3)));
				try (ResultSet rs = stmt.executeQuery()){
					while (rs.next()) {
						tupelsCtr++;
						switch (rs.getString("Category")) {
						case "Salad":
							salad+=rs.getInt("Orders");
							break;
						case "First Course":
							firstCourse+=rs.getInt("Orders");
							break;
						case "Main Course":
							mainCourse+=rs.getInt("Orders");
							break;
						case "Dessert":
							dessert+=rs.getInt("Orders");
							break;
						case "Beverage":
							beverage+=rs.getInt("Orders");
							break;	
						}
					}
					if(tupelsCtr!=0) {
						returnData.add(Integer.toString(salad));
						returnData.add(Integer.toString(firstCourse));
						returnData.add(Integer.toString(mainCourse));
						returnData.add(Integer.toString(dessert));
						returnData.add(Integer.toString(beverage));
					}
					response.setMessage(returnData);
					response.setResponse(ServerResponse.ORDER_REPORT);
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case "Income Reports":{
			int supplierID=0;
			int incomes=0;
			SupplierIncome supplierIncome;
			String supplierName=null;
			String query = "SELECT * FROM incomes_reports WHERE Branch = ? AND Month = ? AND Year = ?";
			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
				stmt.setString(1, reportInfo.get(1));
				stmt.setInt(2, Integer.valueOf(reportInfo.get(2)));
				stmt.setInt(3, Integer.valueOf(reportInfo.get(3)));
				try (ResultSet rs = stmt.executeQuery()){
					while (rs.next()) {
						supplierID=rs.getInt("SupplierID");
						incomes=rs.getInt("Incomes");
						supplierName=rs.getString("SupplierName");
						supplierIncome= new SupplierIncome(supplierID, supplierName, incomes);
						SupplierIncomeList.add(supplierIncome);
					}
				response.setMessage(SupplierIncomeList);
				response.setResponse(ServerResponse.INCOME_REPORT);	
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case "Performance Reports":{
			String query = "SELECT * FROM performance_reports WHERE Branch = ? AND Month = ? AND Year = ?";
			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
				stmt.setString(1, reportInfo.get(1));
				stmt.setString(2, reportInfo.get(2));
				stmt.setInt(3, Integer.valueOf(reportInfo.get(3)));
				try (ResultSet rs = stmt.executeQuery()){
					if (rs.next()) {
						int onTime = rs.getInt("OnTime");
						int late = rs.getInt("Late");
						returnData.add(Integer.toString(onTime));
						returnData.add(Integer.toString(late));
					}
					response.setMessage(returnData);
					response.setResponse(ServerResponse.PERFORMANCE_REPORT);
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}}
		return response;
	}
    /**
     * Imports quarterly report data.
     *
     * @param dbConn the database connection to use
     * @param reportInfo a list of strings containing report parameters: [quarter, branch, year]
     * @return a {@link ServerResponseDataContainer} containing the quarterly report data
     * @throws SQLException if a database access error occurs
     */
	public ServerResponseDataContainer importQuarterReportData(Connection dbConn, List<String> reportInfo) throws SQLException {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		List<SupplierQuarterReportData> SupplierQuarterReportList=new ArrayList<SupplierQuarterReportData>();
		String query=null;
		if((java.time.Year.now().getValue()==Integer.valueOf(reportInfo.get(2)))&&(((java.time.YearMonth.now().getMonthValue()+2)/3)<=Integer.valueOf(reportInfo.get(1)))){
			response.setMessage(SupplierQuarterReportList);
			response.setResponse(ServerResponse.QUARTER_REPORT_DATA);
			}
		else {
			switch(reportInfo.get(1)) {
			case "1":{
				query = "SELECT\r\n"
						+ "    s.ID AS SupplierID,\r\n"
						+ "    s.Name AS supplierName,\r\n"
						+ "    COALESCE(COUNT(o.OrderID), 0) AS totalOrders,\r\n"
						+ "    ROUND(COALESCE(SUM(o.TotalPrice), 0), 2) AS totalIncome\r\n"
						+ "FROM\r\n"
						+ "    suppliers s\r\n"
						+ "LEFT JOIN orders o ON s.ID = o.SupplierID\r\n"
						+ "    AND YEAR(o.RequestDate) = ?\r\n"
						+ "    AND MONTH(o.RequestDate) BETWEEN 1 AND 3\r\n"
						+ "    AND s.Branch = o.Branch\r\n"
						+ "WHERE\r\n"
						+ "    s.Branch = ?\r\n"
						+ "GROUP BY\r\n"
						+ "    s.ID, s.Name\r\n"
						+ "ORDER BY totalIncome DESC;";		
				break;
			}
			case "2":{
				query = "SELECT\r\n"
						+ "    s.ID AS SupplierID,\r\n"
						+ "    s.Name AS supplierName,\r\n"
						+ "    COALESCE(COUNT(o.OrderID), 0) AS totalOrders,\r\n"
						+ "    ROUND(COALESCE(SUM(o.TotalPrice), 0), 2) AS totalIncome\r\n"
						+ "FROM\r\n"
						+ "    suppliers s\r\n"
						+ "LEFT JOIN orders o ON s.ID = o.SupplierID\r\n"
						+ "    AND YEAR(o.RequestDate) = ?\r\n"
						+ "    AND MONTH(o.RequestDate) BETWEEN 4 AND 6\r\n"
						+ "    AND s.Branch = o.Branch\r\n"
						+ "WHERE\r\n"
						+ "    s.Branch = ?\r\n"
						+ "GROUP BY\r\n"
						+ "    s.ID, s.Name\r\n"			
						+ "ORDER BY totalIncome DESC;";			
				break;
			}
			case "3":{
				query = "SELECT\r\n"
						+ "    s.ID AS SupplierID,\r\n"
						+ "    s.Name AS supplierName,\r\n"
						+ "    COALESCE(COUNT(o.OrderID), 0) AS totalOrders,\r\n"
						+ "    ROUND(COALESCE(SUM(o.TotalPrice), 0), 2) AS totalIncome\r\n"
						+ "FROM\r\n"
						+ "    suppliers s\r\n"
						+ "LEFT JOIN orders o ON s.ID = o.SupplierID\r\n"
						+ "    AND YEAR(o.RequestDate) = ?\r\n"
						+ "    AND MONTH(o.RequestDate) BETWEEN 7 AND 9\r\n"
						+ "    AND s.Branch = o.Branch\r\n"
						+ "WHERE\r\n"
						+ "    s.Branch = ?\r\n"
						+ "GROUP BY\r\n"
						+ "    s.ID, s.Name\r\n"
						+ "ORDER BY totalIncome DESC;";
				break;
			}
			case "4":{
				query = "SELECT\r\n"
						+ "    s.ID AS SupplierID,\r\n"
						+ "    s.Name AS supplierName,\r\n"
						+ "    COALESCE(COUNT(o.OrderID), 0) AS totalOrders,\r\n"
						+ "    ROUND(COALESCE(SUM(o.TotalPrice), 0), 2) AS totalIncome\r\n"
						+ "FROM\r\n"
						+ "    suppliers s\r\n"
						+ "LEFT JOIN orders o ON s.ID = o.SupplierID\r\n"
						+ "    AND YEAR(o.RequestDate) = ?\r\n"
						+ "    AND MONTH(o.RequestDate) BETWEEN 10 AND 12\r\n"
						+ "    AND s.Branch = o.Branch\r\n"
						+ "WHERE\r\n"
						+ "    s.Branch = ?\r\n"
						+ "GROUP BY\r\n"
						+ "    s.ID, s.Name\r\n"
						+ "ORDER BY totalIncome DESC;";
				break;
			}
			}
			
			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
				stmt.setString(1, reportInfo.get(2));
				stmt.setString(2, reportInfo.get(0));
				try (ResultSet rs = stmt.executeQuery()){
					while (rs.next()) {
						int totalOrders = rs.getInt("totalOrders");
						float totalIncome = rs.getFloat("totalIncome");
						String supplierName = rs.getString("supplierName");
						int SupplierID = rs.getInt("SupplierID");
						SupplierQuarterReportData supplierQuarterReportData = new SupplierQuarterReportData(SupplierID, supplierName, totalIncome, totalOrders);
						SupplierQuarterReportList.add(supplierQuarterReportData);
					}
					response.setMessage(SupplierQuarterReportList);
					response.setResponse(ServerResponse.QUARTER_REPORT_DATA);
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
	/**
	 * Imports a list of unregistered customers from the database for a given branch managed by the provided manager.
	 * The customers are those who are marked as unregistered.
	 * 
	 * @param dbConn the database connection to use
	 * @param manager the branch manager requesting the customer list
	 * @return a {@link ServerResponseDataContainer} containing a list of unregistered {@link Customer} objects
	 * @throws SQLException if a database access error occurs
	 */
	public ServerResponseDataContainer importCustomerList(Connection dbConn, BranchManager manager) throws SQLException {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		List<Customer> unRegisteredCustomers= new ArrayList<Customer>();
		Customer customer = null;
		String queryTest = "SELECT c.*, u.IsLoggedIn, u.Registered, u.Password  FROM customers AS c INNER JOIN users AS u ON c.Username = u.Username WHERE c.HomeBranch = ? AND u.Registered=0 AND u.Type='Customer'";
		try (PreparedStatement stmt = dbConn.prepareStatement(queryTest)) {
			stmt.setString(1, manager.getbranchType().toShortString());
			try (ResultSet rs1 = stmt.executeQuery()){
				while (rs1.next()) {
					String username= rs1.getString("Username");
					int id = rs1.getInt("ID");
					String type = rs1.getString("Type");
					int companyId = rs1.getInt("CompanyID");
					String firstName = rs1.getString("FirstName");
					String lastName = rs1.getString("LastName");
					String email = rs1.getString("Email");
					String phone = rs1.getString("Phone");
					String homeBranch = rs1.getString("HomeBranch");
					String credit = rs1.getString("Credit");
					String cvv = rs1.getString("CVV");
					int validYear = rs1.getInt("ValidYear");
					int validMonth = rs1.getInt("ValidMonth");
					float walletBalance = rs1.getFloat("WalletBalance");
					int isLoggedIn= rs1.getInt("IsLoggedIn");
					int registered= rs1.getInt("Registered");
					String password= rs1.getString("Password");
					switch (homeBranch) {
					case "North":
						if (type.equals("Private"))
							customer = new Customer(username, id, CustomerType.PRIVATE, companyId, firstName, 
									lastName, email, phone, Branch.NORTH, credit, cvv, validYear, validMonth, walletBalance, 
									isLoggedIn, registered, password);
						else
							customer = new Customer(username, id, CustomerType.BUSINESS, companyId, firstName, 
									lastName, email, phone, Branch.NORTH, credit, cvv, validYear, validMonth, walletBalance, 
									isLoggedIn, registered, password);
						break;
					case "Center":
						if (type.equals("Private"))
							customer = new Customer(username, id, CustomerType.PRIVATE, companyId, firstName, 
									lastName, email, phone, Branch.CENTER, credit, cvv, validYear, validMonth, walletBalance, 
									isLoggedIn, registered, password);
						else
							customer = new Customer(username, id, CustomerType.BUSINESS, companyId, firstName, 
									lastName, email, phone, Branch.CENTER, credit, cvv, validYear, validMonth, walletBalance, 
									isLoggedIn, registered, password);
						break;
					case "South":
						if (type.equals("Private"))
							customer = new Customer(username, id, CustomerType.PRIVATE, companyId, firstName, 
									lastName, email, phone, Branch.SOUTH, credit, cvv, validYear, validMonth, walletBalance, 
									isLoggedIn, registered, password);
						else
							customer = new Customer(username, id, CustomerType.BUSINESS, companyId, firstName, 
									lastName, email, phone, Branch.SOUTH, credit, cvv, validYear, validMonth, walletBalance, 
									isLoggedIn, registered, password);
						break;
					default:
						break;
					}
					unRegisteredCustomers.add(customer);
				}
			}
		}
		response.setMessage(unRegisteredCustomers);
		response.setResponse(ServerResponse.UNREGISTERED_CUSTOMERS_FOUND);
		return response;
	}
    /**
     * Updates the registration status of a list of users.
     * 
     * @param dbConn the database connection to use
     * @param userList a list of usernames of the users to be updated
     * @throws Exception if the update fails
     */
	public void updateUsersRegister(Connection dbConn, List<String> userList) throws Exception {
		int affectedRows=0;
		String query = "UPDATE users SET Registered=1 WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			for(String username : userList) {
				stmt.setString(1, username);
				affectedRows += stmt.executeUpdate();
			}
			if (affectedRows == 0)
				throw new Exception("User update failed\n");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
