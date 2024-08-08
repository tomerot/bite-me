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
		String query = "SELECT * FROM suppliers WHERE Username = ?";
		System.out.println("Query");
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, user.getUserName());
			
			try (ResultSet rs = stmt.executeQuery()) {
				
				if (rs.next()) {
					System.out.println("In While");
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

    /**
     * Updates user data in the database.
     * 
     * @param dbConn the database connection to use
     * @param user the user whose data is to be updated
     * @throws Exception if the update fails
     */
	public void updateUserData(Connection dbConn, User user) throws Exception {
		int affectedRows;
		String query = "UPDATE users SET username=? ,password=? ,isLoggedIn=? ,Type=? ,Registered=? WHERE username = ?";
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
