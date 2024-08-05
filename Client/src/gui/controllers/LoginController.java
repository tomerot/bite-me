package gui.controllers;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.AuthorizedEmployee;
import entities.BranchManager;
import entities.Ceo;
import entities.Customer;
import entities.Supplier;
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

/**
 * Controller for handling user login interactions.
 * Manages the login process, validates user credentials, and navigates to the appropriate user-specific home page.
 */
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
	
    /**
     * Handles the event when the connect button is clicked.
     * Validates user credentials, requests user data from the server, and navigates to the appropriate home page.
     * 
     * @param event the action event triggered by clicking the connect button
     * @throws Exception if an error occurs during the connection or screen transition
     */
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
						userData.setisLoggedIn(1);
						ClientMainController.requestUpdateIsLoggedIn(userData);
						ClientMainController.requestUserSpecificData(userData);
						System.out.println("after91");
						ServerResponseDataContainer entityResponse = ClientConsole.responseFromServer;
						System.out.println(entityResponse.getResponse());
						switch(entityResponse.getResponse()) {
							case CEO_FOUND:
								Ceo ceo = (Ceo) entityResponse.getMessage();
								displayWindow(event, "CEO Home Page", "CeoScreen", ceo);
								break;
							case MANAGER_FOUND:
								BranchManager manager = (BranchManager) entityResponse.getMessage();
								displayWindow(event, "Branch Manager Home Page", "BranchManagerScreen", manager);
								break;
							case SUPPLIER_FOUND:
								Supplier supplier = (Supplier) entityResponse.getMessage();
								System.out.println("Im here1");
								displayWindow(event, "Supplier Home Page", "SupplierScreen", supplier);
								break;
							case EMPLOYEE_FOUND:
								AuthorizedEmployee employee = (AuthorizedEmployee) entityResponse.getMessage();
								displayWindow(event, "Employee Home Page", "EmployeeHomeScreen", employee);
								break;
							case CUSTOMER_FOUND:
								Customer customer = (Customer) entityResponse.getMessage();
								displayWindow(event, "Customer Home Page", "CustomerHomeScreen", customer);
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
		default:
			break;
		}
	}
	
    /**
     * Displays the appropriate user-specific home page based on the user's type.
     * 
     * @param event the action event that triggered this method
     * @param title the title of the new stage
     * @param page the FXML file name for the user-specific home page
     * @param user the user for whom the home page is to be displayed
     * @throws Exception if an error occurs while loading the FXML file or creating the new stage
     */
	public void displayWindow(ActionEvent event, String title, String page, User user) throws Exception {
		String view = "/gui/view/" + page + ".fxml";
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
	    switch(user.getUserType()) {
	    case CEO:{
	    	CeoHomeScreenController controller = new CeoHomeScreenController(user);
	    	loader.setController(controller);
    	    break;
	    }
    	case MANAGER:{
    		BranchManagerController bController = new BranchManagerController(user);
    		loader.setController(bController);
    		break;
    	}
    	case SUPPLIER:{
    		SupplierScreenController supController=new SupplierScreenController();
    		loader.setController(supController);
    		supController.setUser(user);
    		break;
    	}
	    case EMPLOYEE:{
	    	EmployeeHomeScreenController controller = new EmployeeHomeScreenController();
	    	loader.setController(controller);
	    	controller.setUser(user);
    		break;
	    }
	    case CUSTOMER:{
	    	//((SupplierController) controller).setUser(user);
	    	CustomerHomeScreenController customerContoller = new CustomerHomeScreenController(user);
	    	loader.setController(customerContoller);
//	    	controller.setUser(user);
    		break;
	    }
	   }
	    
	    Pane root = loader.load();   
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	
    /**
     * Handles the event when the exit button is clicked.
     * Disconnects the client from the server and exits the application.
     * 
     * @param event the action event triggered by clicking the exit button
     * @throws Exception if an error occurs during the disconnection process
     */
	@FXML
	private void getExitBtn(ActionEvent event) throws Exception {
		ClientConsole.disconnectClientFromServer();
		Platform.exit();
		System.exit(0);
	}
}
