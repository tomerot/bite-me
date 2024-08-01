package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import containers.ServerResponseDataContainer;
import entities.AuthorizedEmployee;
import entities.BranchManager;
import entities.Ceo;
import entities.Customer;
import entities.Supplier;
import entities.User;
import enums.Branch;
import enums.CustomerType;
import enums.ServerResponse;
import enums.UserType;
import java.util.ArrayList;
import java.util.List;

public class UserQuery {

	public UserQuery() {
	}

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

	public ServerResponseDataContainer importSupplierInfo(Connection dbConn, User user) {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		String query = "SELECT * FROM suppliers WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, user.getUserName());

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					// Extract data from the result set
					int id = rs.getInt("ID");
					int supplierID = rs.getInt("SupplierID");
					String name = rs.getString("FirstName");
					String email = rs.getString("Email");
					String phone = rs.getString("Phone");
					String city = rs.getString("City");
					String address = rs.getString("Address");
					// Can get also Branch

					Supplier supplier = new Supplier(supplierID, name, city, address, null, user.getUserName(),
							user.getPassword());

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

	public ServerResponseDataContainer importEmployeeInfo(Connection dbConn, User user) {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		String query = "SELECT * FROM employees WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, user.getUserName());

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					// Extract data from the result set
					int id = rs.getInt("ID");
					int supplierID = rs.getInt("SupplierID");
					String firstName = rs.getString("FirstName");
					String lastName = rs.getString("LastName");
					String email = rs.getString("Email");
					String phone = rs.getString("Phone");

					AuthorizedEmployee employee = new AuthorizedEmployee(id, firstName, lastName, email, phone,
							user.getUserName(), user.getPassword(), supplierID);

					response.setMessage(employee);
					response.setResponse(ServerResponse.EMPLOYEE_FOUND);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return response;
	}

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
		String innerQuery = "SELECT * FROM customers WHERE WHERE username = ?";
		String query = "SELECT * FROM users WHERE Type = ? AND Registered = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, "Customer");
			stmt.setInt(2, 0);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					String username = rs.getString("username");
					String password = rs.getString("password");
					int isLoggedIn = rs.getInt("isLoggedIn");
					int registered = rs.getInt("Registered");

					// Extract data from the result set
					try (PreparedStatement stmt1 = dbConn.prepareStatement(innerQuery)) {
						stmt1.setString(1, username);
						try (ResultSet rs1 = stmt1.executeQuery()){
							if (rs1.next()) {
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
				}
			}
		}
		response.setMessage(unRegisteredCustomers);
		response.setResponse(ServerResponse.UNREGISTERED_CUSTOMERS_FOUND);
		return response;
	}
}
