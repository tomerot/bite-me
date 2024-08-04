package gui.controllers;

import java.io.IOException;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.AuthorizedEmployee;
import entities.BranchManager;
import entities.Ceo;
import entities.User;
import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class EmployeeHomeScreenController {

	
	@FXML
    private AnchorPane dashboard;

    @FXML
    private HBox screen;

    @FXML
    private Label welcomeLbl;
    
    @FXML
    private Button btnAddItem;
    
    @FXML
    private Button btnRemoveItem;
    
    @FXML
    private Button EditAddItem;

    @FXML
    private Button btnLogOut;
    
    
    
    private User user;
	private static AuthorizedEmployee employee;
    
	public void setUser(User user) {
        this.user = user;
        ClientMainController.requestBranchManagerData(user);
        ServerResponseDataContainer response = ClientConsole.responseFromServer;
        AuthorizedEmployee employee =(AuthorizedEmployee) response.getMessage();
        this.employee=employee;
        UpdateLabel(employee);
    }
	public User getUser() {
		return user;
	}
	public static AuthorizedEmployee getEmployee() {
		return employee;
	}
	
	public void UpdateLabel(AuthorizedEmployee employee) {
	    Platform.runLater(() -> {
	        welcomeLbl.setText("Welcome, " + employee.getFirstName() + " " + employee.getLastName());
	    });
	}
	
	@FXML
	private void onAddClicked(ActionEvent event) throws IOException {
		ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/AddItemScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.ADD_ITEM_SCREEN, this);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
	
	@FXML
	private void onRemoveClicked(ActionEvent event) throws IOException {
		ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/RemoveItemScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.REMOVE_ITEM_SCREEN, this);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
	
	@FXML
	private void onEditClicked(ActionEvent event) throws IOException {
		ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/EditItemScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.EDIT_ITEM_SCREEN, this);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
	
	public void logOut(ActionEvent event) throws Exception{
		user.setisLoggedIn(0);
		ClientMainController.requestUpdateIsLoggedIn(user);
		displayLogin(event);
    }
	
	public void displayLogin(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setController(new LoginController());
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/view/LoginScreen.fxml").openStream());
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/view/LoginScreen.css").toExternalForm());
		primaryStage.setTitle("Main");
		primaryStage.setScene(scene);
		primaryStage.show();
	} 
	
	
	
	
}
