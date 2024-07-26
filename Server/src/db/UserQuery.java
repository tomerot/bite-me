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
