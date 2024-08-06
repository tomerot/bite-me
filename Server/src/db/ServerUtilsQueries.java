package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import containers.ServerResponseDataContainer;
import entities.User;
import enums.ServerResponse;
import enums.UserType;

public class ServerUtilsQueries {

    public ServerResponseDataContainer importUserInfo(Connection dbConn) {
        ServerResponseDataContainer response = new ServerResponseDataContainer();
        String query = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            // Set the username parameter in the query
            stmt.setString(1, "CEO");

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Extract data from the result set
                    String password = rs.getString("password");
                    int isLoggedIn = rs.getInt("isLoggedIn");
                    String type = rs.getString("Type");
                    int registered = rs.getInt("Registered");

                    User userInfo = null;

                    // Handle different user types
                    switch (type) {
                        case "Manager":
                            userInfo = new User("CEO", password, isLoggedIn, UserType.MANAGER, registered);
                            break;
                        case "Customer":
                            userInfo = new User("CEO", password, isLoggedIn, UserType.CUSTOMER, registered);
                            break;
                        case "Supplier":
                            userInfo = new User("CEO", password, isLoggedIn, UserType.SUPPLIER, registered);
                            break;
                        case "Employee":
                            userInfo = new User("CEO", password, isLoggedIn, UserType.EMPLOYEE, registered);
                            break;
                        default:
                            // Handle unknown types
                            System.err.println("Unknown user type: " + type);
                            break;
                    }

                    response.setMessage(userInfo);
                    response.setResponse(userInfo != null ? ServerResponse.USER_FOUND : ServerResponse.USER_NOT_FOUND);
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
}
