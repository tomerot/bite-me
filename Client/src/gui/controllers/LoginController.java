package gui.controllers;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginController {

	@FXML
	private Button btnConnect;

	@FXML
	private Button btnExit;

	@FXML
	private TextField usernameField;

	@FXML
	private TextField passwordField;
	
	@FXML
	private Label infoLabel;
	
	@FXML
	private void onConnectClicked(ActionEvent event) throws Exception {
		String username = usernameField.getText();
		String password = passwordField.getText();
		
		if (username.isEmpty() || password.isEmpty()) {
			infoLabel.setText("Username and password cannot be empty.");
			infoLabel.setStyle("-fx-text-fill: red;");
			return;
		}
		User user = new User(username,password);
		ClientMainController.requestUserData(user);
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		switch(response.getResponse()) {
		
		case USER_NOT_FOUND:
			infoLabel.setText("Username Not Found");
			infoLabel.setStyle("-fx-text-fill: red;");
			break;
			
		case USER_FOUND:
			User userData = (User) response.getMessage();
			if (password.equals(userData.getPassword())) {
				//CORRECT
				if(userData.getisLoggedIn() == 1) {
					infoLabel.setText("User already logged in!");
					infoLabel.setStyle("-fx-text-fill: red;");
					return;
				}
				else {
					if(userData.getRegistered() == 0) {
						infoLabel.setText("User not approved yet");
						infoLabel.setStyle("-fx-text-fill: red;");
						return;
					}
					else {
						userData.setisLoggedIn(1);
						ClientMainController.requestUpdateUserData(userData);
						if(userData.getUserType().equals("Customer")) {
							displayWindow(event, "Customer Home Page", "CustomerHomeScreen", userData);
							// update isLoggedIn in Db!
						}
						if(userData.getUserType().equals("CEO")) {
							displayWindow(event, "CEO Home Page", "CeoScreen", userData);
							// update isLoggedIn in Db!!
						}
						if(userData.getUserType().equals("North Manager")) {
							displayWindow(event, "North Manager Home Page", "BranchManagerScreen", userData);
							// update isLoggedIn in Db!
						}
						if(userData.getUserType().equals("South Manager")) {
							displayWindow(event, "South Manager Home Page", "BranchManagerScreen", userData);
							// update isLoggedIn in Db!
						}
						if(userData.getUserType().equals("Center Manager")) {
							displayWindow(event, "Center Manager Home Page", "BranchManagerScreen", userData);
							// update isLoggedIn in Db!
						}
						if(userData.getUserType().equals("Supplier")) {
							displayWindow(event, "Supplier Home Page", "SupplierScreen", userData);
							// update isLoggedIn in Db!
						}
						if(userData.getUserType().equals("Employee")) {
							displayWindow(event, "Employee Home Page", "EmployeeScreen", userData);
							// update isLoggedIn in Db!
						}
					}
				}
			}
			else {
				infoLabel.setText("Password is incorrect");
				infoLabel.setStyle("-fx-text-fill: red;");
				return;
			}
		default:
			break;
		}
	}
	
	public void displayWindow(ActionEvent event, String title, String page, User user) throws Exception {
		String view = "/gui/view/" + page + ".fxml";
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
	    Pane root = loader.load();
	    Object controller;
    	controller= loader.getController();
	    switch(user.getUserType()) {
	    case "CEO":
    	    ((CeoHomeScreenController) controller).setUser(user);
    	    break;

    	case "North Manager":
    	    ((BranchManagerController) controller).setUser(user);
    	    break;
    	    
    	case "South Manager":
    	    ((BranchManagerController) controller).setUser(user);
    	    break;
    	
    	case "Center Manager":
    	    ((BranchManagerController) controller).setUser(user);
    	    break;
	    }
	    
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	
	// disconnect user from server, and exit
	@FXML
	private void getExitBtn(ActionEvent event) throws Exception {
		ClientConsole.disconnectClientFromServer();
		Platform.exit();
		System.exit(0);
	}
}
