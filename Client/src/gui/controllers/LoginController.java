package gui.controllers;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.AuthorizedEmployee;
import entities.BranchManager;
import entities.Ceo;
import entities.RegisteredCustomer;
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
						/*UPDATE ISLOGGEDIN, REMOVE THIS COMMENT IN THE END*/
						//userData.setisLoggedIn(1);
						ClientMainController.requestUpdateIsLoggedIn(userData);
						ClientMainController.requestUserSpecificData(userData);
						ServerResponseDataContainer entityResponse = ClientConsole.responseFromServer;
						switch(entityResponse.getResponse()) {
							case CEO_FOUND:
								Ceo ceo = (Ceo) entityResponse.getMessage();
								System.out.println(ceo.getRegistered());
								displayWindow(event, "CEO Home Page", "CeoScreen", ceo);
								break;
							case MANAGER_FOUND:
								BranchManager manager = (BranchManager) entityResponse.getMessage();
								displayWindow(event, "North Manager Home Page", "BranchManagerScreen", manager);
								break;
							case SUPPLIER_FOUND:
								Supplier supplier = (Supplier) entityResponse.getMessage();
								displayWindow(event, "Supplier Home Page", "SupplierScreen", supplier);
								break;
							case EMPLOYEE_FOUND:
								AuthorizedEmployee employee = (AuthorizedEmployee) entityResponse.getMessage();
								displayWindow(event, "Employee Home Page", "EmployeeScreen", employee);
								break;
							case CUSTOMER_FOUND:
								RegisteredCustomer customer = (RegisteredCustomer) entityResponse.getMessage();
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
	
	public void displayWindow(ActionEvent event, String title, String page, User user) throws Exception {
		String view = "/gui/view/" + page + ".fxml";
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
	    switch(user.getUserType()) {
	    case CEO:
	    	CeoHomeScreenController controller = new CeoHomeScreenController();
	    	loader.setController(controller);
	    	controller.setUser(user);
    	    break;

    	case MANAGER:
    	    //((BranchManagerController) controller).setUser(user);
//    		CeoHomeScreenController controller = new CeoHomeScreenController();
//	    	loader.setController(controller);
//	    	controller.setUser(user);
    	    break;
    	    
    	case SUPPLIER:
    		//((SupplierController) controller).setUser(user);
//    		CeoHomeScreenController controller = new CeoHomeScreenController();
//	    	loader.setController(controller);
//	    	controller.setUser(user);
    		break;
	    
	    case EMPLOYEE:
	    	//((SupplierController) controller).setUser(user);
//	    	CeoHomeScreenController controller = new CeoHomeScreenController();
//	    	loader.setController(controller);
//	    	controller.setUser(user);
    		break;
    		
	    case CUSTOMER:
	    	//((SupplierController) controller).setUser(user);
//	    	CeoHomeScreenController controller = new CeoHomeScreenController();
//	    	loader.setController(controller);
//	    	controller.setUser(user);
    		break;
	    }
	    
	    Pane root = loader.load();
	    
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
