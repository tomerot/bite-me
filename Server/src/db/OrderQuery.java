package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import containers.ServerResponseDataContainer;
import entities.Customer;
import entities.Supplier;
import enums.ServerResponse;

public class OrderQuery {
	public OrderQuery() {

	}

	
    /**
     * Retrieves a list of suppliers based on the customer's home branch.
     *
     * @param dbConn the database connection
     * @param customer the customer whose branch is used for querying
     * @return a ServerResponseDataContainer containing the list of suppliers and response status
     * @throws SQLException if a database access error occurs
     */
	public ServerResponseDataContainer getSuppliersByBranch(Connection dbConn, Customer customer) throws SQLException {
		ServerResponseDataContainer response = new ServerResponseDataContainer();
		List<Supplier> suppliers = new ArrayList<>();
		String query = "SELECT ID, Name, Email, Phone, Address, City FROM Suppliers WHERE Branch = ?";
		String br = null;
		switch(customer.getHomeBranch().toString()){
		case "North Branch": br = "North";
		case "Center Branch": br = "Center";
		case "South Branch": br = "South";
		}
		

		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1,br); //NEED TO BE SPECIFIC TO CUSTOMER BRANCH!!!!

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {

					// Extract data from the result set
					int id = rs.getInt("ID");
					String name = rs.getString("Name");
					String email = rs.getString("Email");
					String phone = rs.getString("Phone");
					String address = rs.getString("Address");
					String city = rs.getString("City");


					Supplier supplier = new Supplier(id, name, city, address, null, email, phone, null, null);
					suppliers.add(supplier);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		response.setMessage(suppliers);
		response.setResponse(ServerResponse.SUPPLIER_DATA_BY_BRANCH);
		return response;
	}
}
