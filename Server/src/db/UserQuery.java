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
 * Provides methods to interact with user-related data in the database.
 * Handles importing, updating, and querying user information.
 */
public class UserQuery {

	public UserQuery() {
	}

    /**
     * Imports user information from the database based on the provided user.
     * 
     * @param dbConn the database connection to use
     * @param user the user whose information is to be imported
     * @return a {@link ServerResponseDataContainer} ServerResponseDataContainer containing the user information or response status
     */
	public ServerResponseDataContainer importUserInfo(Connection dbConn, User user) {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		String query = "SELECT * FROM users WHERE username = ?";

		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, user.getUserName());

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					// Extract data from the result set
					String username = rs.getString("username");
					String password = rs.getString("password");
					int isLoggedIn = rs.getInt("isLoggedIn");
					String type = rs.getString("Type");
					int registered = rs.getInt("Registered");

					// UserType t;
					User userInfo = null;

					switch (type) {
					case "Manager":
						userInfo = new User(username, password, isLoggedIn, UserType.MANAGER, registered);
						break;
					case "Customer":
						userInfo = new User(username, password, isLoggedIn, UserType.CUSTOMER, registered);
						break;
					case "Supplier":
						userInfo = new User(username, password, isLoggedIn, UserType.SUPPLIER, registered);
						break;
					case "Employee":
						userInfo = new User(username, password, isLoggedIn, UserType.EMPLOYEE, registered);
						break;
					default:
						break;
					}

					// User userInfo = new User(username, password, isLoggedIn, t, registered);
					response.setMessage(userInfo);
					response.setResponse(ServerResponse.USER_FOUND);
				} else {
					response.setResponse(ServerResponse.USER_NOT_FOUND);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return response;
	}

	   /**
     * Imports manager information from the database based on the provided user.
     * 
     * @param dbConn the database connection to use
     * @param user the user whose manager information is to be imported
     * @return a {@link ServerResponseDataContainer} containing the manager information or response status
     */
	public ServerResponseDataContainer importManagerInfo(Connection dbConn, User user) {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		String query = "SELECT * FROM managers WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, user.getUserName());

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					// Extract data from the result set
					int id = rs.getInt("ID");
					String type = rs.getString("Type");
					String firstName = rs.getString("FirstName");
					String lastName = rs.getString("LastName");
					String email = rs.getString("Email");
					String phone = rs.getString("Phone");

					switch (type) {
					case "CEO":
						Ceo ceo = new Ceo(id, firstName, lastName, email, phone, user.getUserName(),
								user.getPassword(),user.getisLoggedIn(),user.getRegistered());
						response.setMessage(ceo);
						response.setResponse(ServerResponse.CEO_FOUND);
						break;
					case "North Manager":
						BranchManager nManager = new BranchManager(id, firstName, lastName, email, phone,
								user.getUserName(), user.getPassword(), Branch.NORTH, user.getisLoggedIn(),user.getRegistered());
						response.setMessage(nManager);
						response.setResponse(ServerResponse.MANAGER_FOUND);
						break;
					case "Center Manager":
						BranchManager cManager = new BranchManager(id, firstName, lastName, email, phone,
								user.getUserName(), user.getPassword(), Branch.CENTER, user.getisLoggedIn(),user.getRegistered());
						response.setMessage(cManager);
						response.setResponse(ServerResponse.MANAGER_FOUND);
						break;
					case "South Manager":
						BranchManager sManager = new BranchManager(id, firstName, lastName, email, phone,
								user.getUserName(), user.getPassword(), Branch.SOUTH, user.getisLoggedIn(),user.getRegistered());
						response.setMessage(sManager);
						response.setResponse(ServerResponse.MANAGER_FOUND);
						break;
					default:
						break;
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return response;
	}

    /**
     * Imports supplier information from the database based on the provided user.
     * 
     * @param dbConn the database connection to use
     * @param user the user whose supplier information is to be imported
     * @return a {@link ServerResponseDataContainer} containing the supplier information or response status
     */
	public ServerResponseDataContainer importSupplierInfo(Connection dbConn, User user) {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		String query = "SELECT * FROM suppliers WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, user.getUserName());

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					// Extract data from the result set
					int supplierID = rs.getInt("ID");
					String name = rs.getString("Name");
					String email = rs.getString("Email");
					String phone = rs.getString("Phone");
					String city = rs.getString("City");
					String address = rs.getString("Address");
					String branch = rs.getString("Branch");
					// Can get also Branch
										
					Supplier supplier = null;
					switch (branch) {
					case "North":
						supplier = new Supplier(supplierID, name, city, address, Branch.NORTH, user.getUserName(),
								user.getPassword());
						break;
					case "Center":
						supplier = new Supplier(supplierID, name, city, address, Branch.CENTER, user.getUserName(),
								user.getPassword());
						break;
					case "South":
						supplier = new Supplier(supplierID, name, city, address, Branch.SOUTH, user.getUserName(),
								user.getPassword());
						break;

					}


					response.setMessage(supplier);
					response.setResponse(ServerResponse.SUPPLIER_FOUND);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return response;
	}

    /**
     * Imports employee information from the database based on the provided user.
     * 
     * @param dbConn the database connection to use
     * @param user the user whose employee information is to be imported
     * @return a {@link ServerResponseDataContainer} containing the employee information or response status
     */
	public ServerResponseDataContainer importEmployeeInfo(Connection dbConn, User user) {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		String query = "SELECT * FROM employees WHERE Username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, user.getUserName());

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					System.out.println("FOUND");
					// Extract data from the result set
					int id = rs.getInt("ID");
					int supplierID = rs.getInt("SupplierID");
					String firstName = rs.getString("FirstName");
					String lastName = rs.getString("LastName");
					String email = rs.getString("Email");
					String phone = rs.getString("Phone");

					//AuthorizedEmployee employee = new AuthorizedEmployee(id, firstName, lastName, email, phone,user.getUserName(), user.getPassword(), supplierID);
					AuthorizedEmployee employee = new AuthorizedEmployee(id, firstName, lastName, email, phone,user.getUserName(), user.getPassword(), supplierID, user.getisLoggedIn(), user.getRegistered() );
					

					response.setMessage(employee);
					response.setResponse(ServerResponse.EMPLOYEE_FOUND);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		System.out.println(response.getResponse());
		return response;
	}

    /**
     * Imports customer information from the database based on the provided user.
     * 
     * @param dbConn the database connection to use
     * @param user the user whose customer information is to be imported
     * @return a {@link ServerResponseDataContainer} containing the customer information or response status
     */
	public ServerResponseDataContainer importCustomerInfo(Connection dbConn, User user) {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		String query = "SELECT * FROM customers WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, user.getUserName());

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					// Extract data from the result set
					int id = rs.getInt("ID");
					String type = rs.getString("Type");
					int companyId = rs.getInt("CompanyID");
					String firstName = rs.getString("FirstName");
					String lastName = rs.getString("LastName");
					String email = rs.getString("Email");
					String phone = rs.getString("Phone");
					String homeBranch = rs.getString("HomeBranch");
					String credit = rs.getString("Credit");
					String cvv = rs.getString("CVV");
					Date validDate = rs.getDate("validDate");
					float walletBalance = rs.getFloat("WalletBalance");
					// Can get also companyID and Credit Card

					Customer customer = null;

					switch (homeBranch) {
					case "North":
						if (type.equals("Private"))
							customer = new Customer(user.getUserName(), id, CustomerType.PRIVATE, companyId, firstName, 
									lastName, email, phone, Branch.NORTH, credit, cvv, validDate, walletBalance, 
									user.getisLoggedIn(), user.getRegistered(), user.getPassword());
						else
							customer = new Customer(user.getUserName(), id, CustomerType.BUSINESS, companyId, firstName, 
									lastName, email, phone, Branch.NORTH, credit, cvv, validDate, walletBalance, 
									user.getisLoggedIn(), user.getRegistered(), user.getPassword());
						break;
					case "Center":
						if (type.equals("Private"))
							customer = new Customer(user.getUserName(), id, CustomerType.PRIVATE, companyId, firstName, 
									lastName, email, phone, Branch.CENTER, credit, cvv, validDate, walletBalance, 
									user.getisLoggedIn(), user.getRegistered(), user.getPassword());
						else
							customer = new Customer(user.getUserName(), id, CustomerType.BUSINESS, companyId, firstName, 
									lastName, email, phone, Branch.CENTER, credit, cvv, validDate, walletBalance, 
									user.getisLoggedIn(), user.getRegistered(), user.getPassword());
						break;
					case "South":
						if (type.equals("Private"))
							customer = new Customer(user.getUserName(), id, CustomerType.PRIVATE, companyId, firstName, 
									lastName, email, phone, Branch.SOUTH, credit, cvv, validDate, walletBalance, 
									user.getisLoggedIn(), user.getRegistered(), user.getPassword());
						else
							customer = new Customer(user.getUserName(), id, CustomerType.BUSINESS, companyId, firstName, 
									lastName, email, phone, Branch.SOUTH, credit, cvv, validDate, walletBalance, 
									user.getisLoggedIn(), user.getRegistered(), user.getPassword());
						break;
					default:
						break;
					}

					response.setMessage(customer);
					response.setResponse(ServerResponse.CUSTOMER_FOUND);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return response;
	}
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
     * Updates user data in the database.
     * 
     * @param dbConn the database connection to use
     * @param user the user whose data is to be updated
     * @throws Exception if the update fails
     */
	public void updateUserData(Connection dbConn, User user) throws Exception {
		int affectedRows;
		String query = "UPDATE users SET username=? AND password=? AND isLoggedIn=? AND Type=? AND Registered=? WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, user.getUserName());
			stmt.setString(2, user.getPassword());
			stmt.setInt(3, user.getisLoggedIn());
			UserType t = user.getUserType();
			if (t.equals(UserType.CEO))
				stmt.setString(4, UserType.MANAGER.toString());
			else
				stmt.setString(4, user.getUserType().toString());
			stmt.setInt(5, user.getRegistered());
			stmt.setString(6, user.getUserName());
			affectedRows = stmt.executeUpdate();
			if (affectedRows == 0)
				throw new Exception("User update failed\n");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

    /**
     * Updates the login status of a user in the database.
     * 
     * @param dbConn the database connection to use
     * @param user the user whose login status is to be updated
     * @throws Exception if the update fails
     */
	public void updateIsLoggedIn(Connection dbConn, User user) throws Exception {
		String query = "UPDATE users SET isLoggedIn = ? WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, user.getisLoggedIn());
			stmt.setString(2, user.getUserName());
			int affectedRow = stmt.executeUpdate();
			if (affectedRow == 0)
				throw new Exception("IsLoggedIn update failed\n");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	// Get orders data from server
	public ServerResponseDataContainer getOrdersData(Connection dbConn, int supplierID) {
		String query = "SELECT o.OrderID, o.Recipient, o.RecipientPhone, o.City, o.Address, o.SupplyOption, o.Type, " +
	               "o.RequestDate, o.RequestTime, o.TotalPrice, o.Status " +
	               "FROM orders o " +
	               "WHERE o.SupplierID = ?";
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		List<Order> list = new ArrayList<>();
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, supplierID);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int OrderID = rs.getInt("OrderID");
					System.out.println("ID "+OrderID);
					String name = rs.getString("Recipient");
					System.out.println("rec\n");
					String phone = rs.getString("RecipientPhone");
					System.out.println("recPhome\n");
					String city = rs.getString("City");
					System.out.println("city");
					String address = rs.getString("Address");
					System.out.println("add");
					String supplyOption = rs.getString("SupplyOption");
					System.out.println("supOption");
					String type = rs.getString("Type");  
					System.out.println("type");
					OrderType orderType;
					switch (type) {
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
					System.out.println("time");
					float totalPrice = rs.getFloat("TotalPrice");
					System.out.println("price");
					String status = rs.getString("Status");
					System.out.println("ststus");
//					int customerID = rs.getInt("ID");
//					System.out.println("cusID");
//					String firstName = rs.getString("FirstName");
//					System.out.println("FN");
//					String lastName = rs.getString("LastName");
//					System.out.println("LN");
//					String email = rs.getString("Email");
//					System.out.println("email");
//					String phoneNumber = rs.getString("Phone");
//					System.out.println("cusPhone");
//					RegisteredCustomer customer = new RegisteredCustomer(customerID, firstName, lastName, email,
//							phoneNumber, null, null, null, 0.0f, CustomerType.PRIVATE);

					//Order order = new Order(OrderID, null, reqDate, reqTime, orderType, totalPrice);
					//list.add(order);
					
					// Log each order for debugging
				    //System.out.println("Order added: " + order);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		response.setMessage(list);
		return response;
	}
	
	//add item to database
		public ServerResponseDataContainer AddItemInfo(Connection dbConn, Item item) {
			// we distinguish between two items according to (name, supplierID), if there is  an item with these exact fields we don't permit to add it to the database.

			ServerResponseDataContainer response = new ServerResponseDataContainer();
			String query = "SELECT id FROM items WHERE Name = ? and SupplierID = ?";

			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
				stmt.setString(1, item.getName());
				stmt.setInt(2, item.getSupplierID());

				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						// There is already existing item like this in the database, we cannot add it.
						response.setMessage("This item alread exists in the database");
						response.setResponse(ServerResponse.ADD_FAIL);
					} else {
						// the item does not exist in the database, we can add it. id field we don't insert, the database defines it automatically.
						String insertQuery = "INSERT into items (SupplierID, Name, Category, Description, CustomSize, CustomDoneness, CustomRestrictions, Price)"
								+ "VALUES(?,?,?,?,?,?,?,?);";

						 int size = item.getCustomSize() ? 1 : 0;
		                 int doneness = item.getCustomDonenessDegree() ? 1 : 0;
		                 int restrictions = item.getCustomRestrictions() ? 1 : 0;

						try (PreparedStatement insertStm = dbConn.prepareStatement(insertQuery)) {
							insertStm.setInt(1, item.getSupplierID());
							insertStm.setString(2, item.getName());
							insertStm.setString(3, item.getType().toString());
							insertStm.setString(4, item.getDescription());
							insertStm.setInt(5, size);
							insertStm.setInt(6, doneness);
							insertStm.setInt(7, restrictions);
							insertStm.setFloat(8, item.getPrice());

							
							int res = insertStm.executeUpdate();
							System.out.println("second Query passed");
							if(res>0) {
								response.setMessage("The item was added successuflly!");
								response.setResponse(ServerResponse.ADD_SUCCESS);
							}
							
							else {
								response.setMessage("failed to add item because error in db");
								response.setResponse(ServerResponse.ADD_FAIL);
							}

						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			return response;
		}
		
		
		public ServerResponseDataContainer FetchItemsListInfo(Connection dbConn, int supplierID) {
			System.out.println("stam");
			String query = "SELECT Name, Category FROM items WHERE SupplierID = ?";
			ServerResponseDataContainer response = new ServerResponseDataContainer();
			Map<String,String> map = new HashMap<>();

			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
				stmt.setInt(1, supplierID);

				try (ResultSet rs = stmt.executeQuery()) {
					System.out.println("before while");
					while(rs.next()) {
						map.put(rs.getString("Name"), rs.getString("Category"));
						System.out.println("in while");
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			System.out.println(map);
			response.setMessage(map);
			response.setResponse(null);
			return response;
		}
		
		// here we get all the items *instances* that belongs to certain supplier.
		public ServerResponseDataContainer FetchFullItemsListInfo(Connection dbConn, int supplierID) {
			System.out.println("Thanks God... We are going to bring the items full list");
			String query = "SELECT * FROM items WHERE SupplierID = ?;";
			ServerResponseDataContainer response = new ServerResponseDataContainer();
			Map<String,Item> itemsMap = new HashMap<>();

			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
				stmt.setInt(1, supplierID);

				try (ResultSet rs = stmt.executeQuery()) {
					while(rs.next()) {
						//create item instance and put it in the map
						int itemID = rs.getInt("ID");
						int SupplierID = rs.getInt("SupplierID");
						String itemName = rs.getString("Name");
						String categoryName = rs.getString("Category");
						Category category;
						switch(categoryName) {
						case "Main Course":
							category = Category.MAINCOURSE;
							break;
						case "First Course":
							category = Category.FIRSTCOURSE;
							break;
						case "Salad":
							category = Category.SALAD;
							break;
						case "Dessert":
							category = Category.DESSERT;
							break;
						case "Beverage":
							category = Category.BEVERAGE;
							break;
						default:
							category = null;
					}
						String description = rs.getString("Description");
						boolean customSize = rs.getInt("CustomSize") == 1 ? true : false;
						boolean customDoneness = rs.getInt("CustomDoneness") == 1 ? true : false; 
						boolean customRestrictions = rs.getInt("CustomRestrictions") == 1 ? true : false; 
						int price  = rs.getInt("Price");
						
						Item item = new Item(itemID, SupplierID, itemName, category, description, customSize, customDoneness, customRestrictions, price);
						itemsMap.put(itemName, item);
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			response.setMessage(itemsMap);
			response.setResponse(null);
			return response;
		}
		
		public ServerResponseDataContainer RemoveItemInfo(Connection dbConn, Map<String,Integer> itemData) {
			String query = "DELETE FROM items WHERE Name = ? AND SupplierID = ?;";
			ServerResponseDataContainer response = new ServerResponseDataContainer();

			// get the data from the map  (we know that this map has exactly one entry.)
		    String itemName = itemData.keySet().iterator().next();
	        int supplierID = itemData.get(itemName);
	        
			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {			
				stmt.setString(1, itemName);
				stmt.setInt(2, supplierID);

				 int isRowAffected = stmt.executeUpdate(); //returns how many rows were deleted.
				 
				 if(isRowAffected == 1) {
					response.setMessage("The item was deleted successfully");
					response.setResponse(ServerResponse.DELETE_ITEM_SUCCESS);
				 }
				 else {
					 response.setMessage("failed to delete item because error in db");
					 response.setResponse(ServerResponse.DELETE_ITEM_FAIL);
				 }
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return response;
		}
		
		public ServerResponseDataContainer UpdateItemInfo(Connection dbConn, Item item) {
			String query = "UPDATE items SET Description = ?, CustomSize = ?, CustomDoneness = ?, CustomRestrictions = ?, Price = ? WHERE ID = ?;";
			ServerResponseDataContainer response = new ServerResponseDataContainer();
	        
			int CustomSize = item.getCustomSize() ? 1:0;
			int CustomDoneness = item.getCustomDonenessDegree() ? 1:0;
			int CustomRestrictions = item.getCustomRestrictions() ? 1:0;
			
			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {			
				stmt.setString(1, item.getDescription());
				stmt.setInt(2, CustomSize);
				stmt.setInt(3, CustomDoneness);
				stmt.setInt(4, CustomRestrictions);
				stmt.setFloat(5, item.getPrice());
				stmt.setInt(6, item.getItemID());

				 int isRowAffected = stmt.executeUpdate(); //returns how many rows were updated.
				 
				 if(isRowAffected == 1) {
					response.setMessage("The item was updated successfully");
					response.setResponse(ServerResponse.UPDATE_ITEM_SUCCESS);
				 }
				 else {
					 response.setMessage("failed to update item because error in db");
					 response.setResponse(ServerResponse.UPDATE_ITEM_FAIL);
				 }
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return response;
		}
		
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
					Date validDate = rs1.getDate("validDate");
					float walletBalance = rs1.getFloat("WalletBalance");
					int isLoggedIn= rs1.getInt("IsLoggedIn");
					int registered= rs1.getInt("Registered");
					String password= rs1.getString("Password");
					switch (homeBranch) {
					case "North":
						if (type.equals("Private"))
							customer = new Customer(username, id, CustomerType.PRIVATE, companyId, firstName, 
									lastName, email, phone, Branch.NORTH, credit, cvv, validDate, walletBalance, 
									isLoggedIn, registered, password);
						else
							customer = new Customer(username, id, CustomerType.BUSINESS, companyId, firstName, 
									lastName, email, phone, Branch.NORTH, credit, cvv, validDate, walletBalance, 
									isLoggedIn, registered, password);
						break;
					case "Center":
						if (type.equals("Private"))
							customer = new Customer(username, id, CustomerType.PRIVATE, companyId, firstName, 
									lastName, email, phone, Branch.CENTER, credit, cvv, validDate, walletBalance, 
									isLoggedIn, registered, password);
						else
							customer = new Customer(username, id, CustomerType.BUSINESS, companyId, firstName, 
									lastName, email, phone, Branch.CENTER, credit, cvv, validDate, walletBalance, 
									isLoggedIn, registered, password);
						break;
					case "South":
						if (type.equals("Private"))
							customer = new Customer(username, id, CustomerType.PRIVATE, companyId, firstName, 
									lastName, email, phone, Branch.SOUTH, credit, cvv, validDate, walletBalance, 
									isLoggedIn, registered, password);
						else
							customer = new Customer(username, id, CustomerType.BUSINESS, companyId, firstName, 
									lastName, email, phone, Branch.SOUTH, credit, cvv, validDate, walletBalance, 
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
