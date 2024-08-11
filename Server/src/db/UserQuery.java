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
					//Date validDate = rs.getDate("validDate");
					float walletBalance = rs.getFloat("WalletBalance");
					// Can get also companyID and Credit Card

					Customer customer = null;

					switch (homeBranch) {
					case "North":
						if (type.equals("Private"))
							customer = new Customer(user.getUserName(), id, CustomerType.PRIVATE, companyId, firstName, 
									lastName, email, phone, Branch.NORTH, credit, cvv, null, walletBalance, 
									user.getisLoggedIn(), user.getRegistered(), user.getPassword());
						else
							customer = new Customer(user.getUserName(), id, CustomerType.BUSINESS, companyId, firstName, 
									lastName, email, phone, Branch.NORTH, credit, cvv, null, walletBalance, 
									user.getisLoggedIn(), user.getRegistered(), user.getPassword());
						break;
					case "Center":
						if (type.equals("Private"))
							customer = new Customer(user.getUserName(), id, CustomerType.PRIVATE, companyId, firstName, 
									lastName, email, phone, Branch.CENTER, credit, cvv, null, walletBalance, 
									user.getisLoggedIn(), user.getRegistered(), user.getPassword());
						else
							customer = new Customer(user.getUserName(), id, CustomerType.BUSINESS, companyId, firstName, 
									lastName, email, phone, Branch.CENTER, credit, cvv, null, walletBalance, 
									user.getisLoggedIn(), user.getRegistered(), user.getPassword());
						break;
					case "South":
						if (type.equals("Private"))
							customer = new Customer(user.getUserName(), id, CustomerType.PRIVATE, companyId, firstName, 
									lastName, email, phone, Branch.SOUTH, credit, cvv, null, walletBalance, 
									user.getisLoggedIn(), user.getRegistered(), user.getPassword());
						else
							customer = new Customer(user.getUserName(), id, CustomerType.BUSINESS, companyId, firstName, 
									lastName, email, phone, Branch.SOUTH, credit, cvv, null, walletBalance, 
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
}
