package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import containers.ServerResponseDataContainer;
import entities.User;
import enums.ServerResponse;

public class UserQuery {

	public UserQuery() {
	}

	public ServerResponseDataContainer FatchUserInfo(Connection dbConn, User user) {
		String query = "SELECT username, password, isLoggedIn, Type, Registered FROM users WHERE username = ?";
		ServerResponseDataContainer response = new ServerResponseDataContainer();

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
				}
				else {
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
