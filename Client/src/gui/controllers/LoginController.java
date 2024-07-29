package gui.controllers;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.RegisteredCustomer;
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
		//
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
						ClientMainController.requestUserSpecificData(userData);
						ServerResponseDataContainer entityResponse = ClientConsole.responseFromServer;
						switch(entityResponse.getResponse()) {
							case CEO_FOUND:
								displayWindow(event, "CEO Home Page", "CeoScreen");
								//Need to pass entity to the controller
								//Need to update IsLoggedIn at the DB
								break;
							case MANAGER_FOUND:
								displayWindow(event, "North Manager Home Page", "BranchManagerScreen");
								//Need to pass entity to the controller
								//Need to update IsLoggedIn at the DB
								break;
							case SUPPLIER_FOUND:
								displayWindow(event, "Supplier Home Page", "SupplierScreen");
								//Need to pass entity to the controller
								//Need to update IsLoggedIn at the DB
								break;
							case EMPLOYEE_FOUND:
								displayWindow(event, "Employee Home Page", "EmployeeScreen");
								//Need to pass entity to the controller
								//Need to update IsLoggedIn at the DB
								break;
							case CUSTOMER_FOUND:
								displayWindow(event, "Customer Home Page", "CustomerHomeScreen");
								//Need to pass entity to the controller
								//Need to update IsLoggedIn at the DB
								break;
						}
					}
				}
			}
			else {
				infoLabel.setText("Password is incorrect");
				infoLabel.setStyle("-fx-text-fill: red;");
				return;
			}
		}
	}
	
	public void displayWindow(ActionEvent event, String title, String page) throws Exception {
		String view = "/gui/view/" + page + ".fxml";
		FXMLLoader loader = new FXMLLoader();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource(view).openStream());
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
