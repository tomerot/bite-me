package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import containers.ServerResponseDataContainer;
import entities.AuthorizedEmployee;
import entities.BranchManager;
import entities.Ceo;
import entities.RegisteredCustomer;
import entities.Supplier;
import entities.User;
import enums.Branch;
import enums.CustomerType;
import enums.ServerResponse;


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

					User userInfo = new User(username, password, isLoggedIn, type, registered);
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

					switch(type) {
						case "CEO":
							Ceo ceo = new Ceo(id, firstName, lastName, email, phone, user.getUserName(), user.getPassword());
							response.setMessage(ceo);
							response.setResponse(ServerResponse.CEO_FOUND);
							break;
						case "North Manager":
							BranchManager nManager = new BranchManager(id, firstName, lastName, email, phone, user.getUserName(), user.getPassword(), Branch.NORTH);
							response.setMessage(nManager);
							response.setResponse(ServerResponse.MANAGER_FOUND);
							break;
						case "Center Manager":
							BranchManager cManager = new BranchManager(id, firstName, lastName, email, phone, user.getUserName(), user.getPassword(), Branch.CENTER);
							response.setMessage(cManager);
							response.setResponse(ServerResponse.MANAGER_FOUND);
							break;
						case "South Manager":
							BranchManager sManager = new BranchManager(id, firstName, lastName, email, phone, user.getUserName(), user.getPassword(), Branch.SOUTH);
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
					//Can get also Branch

					Supplier supplier = new Supplier(supplierID, name, city, address, null, user.getUserName(), user.getPassword());

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

					AuthorizedEmployee employee = new AuthorizedEmployee(id, firstName, lastName, email, phone, user.getUserName(), user.getPassword(), supplierID);

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
					String firstName = rs.getString("FirstName");
					String lastName = rs.getString("LastName");
					String email = rs.getString("Email");
					String phone = rs.getString("Phone");
					String homeBranch = rs.getString("HomeBranch");
					float walletBalance = rs.getFloat("WalletBalance");
					//Can get also companyID and Credit Card

					RegisteredCustomer customer = null;

					switch(homeBranch) {
						case "North":
							if(type.equals("Private"))
								customer = new RegisteredCustomer(id, firstName, lastName, email, phone, user.getUserName(), user.getPassword(), Branch.NORTH, walletBalance, CustomerType.PRIVATE);
							else
								customer = new RegisteredCustomer(id, firstName, lastName, email, phone, user.getUserName(), user.getPassword(), Branch.NORTH, walletBalance, CustomerType.BUSINESS);
							break;
						case "Center":
							if(type.equals("Private"))
								customer = new RegisteredCustomer(id, firstName, lastName, email, phone, user.getUserName(), user.getPassword(), Branch.CENTER, walletBalance, CustomerType.PRIVATE);
							else
								customer = new RegisteredCustomer(id, firstName, lastName, email, phone, user.getUserName(), user.getPassword(), Branch.CENTER, walletBalance, CustomerType.BUSINESS);
							break;
						case "South":
							if(type.equals("Private"))
								customer = new RegisteredCustomer(id, firstName, lastName, email, phone, user.getUserName(), user.getPassword(), Branch.SOUTH, walletBalance, CustomerType.PRIVATE);
							else
								customer = new RegisteredCustomer(id, firstName, lastName, email, phone, user.getUserName(), user.getPassword(), Branch.SOUTH, walletBalance, CustomerType.BUSINESS);
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

	public void UpdateUserData(Connection dbConn, User user) throws Exception {
		int affectedRows;
		String query = "UPDATE users SET username=? ,password=? ,isLoggedIn=? ,Type=? ,Registered=? WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, user.getUserName());
			stmt.setString(2, user.getPassword());
			stmt.setInt(3, user.getisLoggedIn());
			stmt.setString(4, user.getUserType());
			stmt.setInt(5, user.getRegistered());
			stmt.setString(6, user.getUserName());
			affectedRows = stmt.executeUpdate();
			if (affectedRows == 0)
				throw new Exception("User update IsLoggedIn failed\n");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
