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


/**
 * Controller for the employee home screen in the GUI.
 * This controller handles the interactions and updates for the home screen
 * where employees can manage items and log out.
 */
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
    
	
	/**
     * Sets the user for this controller and updates the welcome label.
     *
     * @param user the user to set
     */
	public void setUser(User user) {
        this.user = user;
        this.employee=(AuthorizedEmployee)user;
        UpdateLabel((AuthorizedEmployee)user);
    }
	
	
	/**
    * Gets the current user of this controller.
    *
    * @return the user
    */
	public User getUser() {
		return user;
	}
	
	/**
    * Gets the currently authorized employee.
    *
    * @return the authorized employee
    */
	public static AuthorizedEmployee getEmployee() {
		return employee;
	}
	
	
	/**
     * Updates the welcome label with the employee's first and last name.
     *
     * @param employee the authorized employee
     */
	public void UpdateLabel(AuthorizedEmployee employee) {
	    Platform.runLater(() -> {
	        welcomeLbl.setText("Welcome, " + employee.getFirstName() + " " + employee.getLastName());
	    });
	}
	
	
	/**
     * Handles the event when the add item button is clicked.
     * Loads the Add Item screen into the dashboard.
     *
     * @param event the action event
     * @throws IOException if loading the FXML file fails
     */
	@FXML
	private void onAddClicked(ActionEvent event) throws IOException {
		ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/AddItemScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.ADD_ITEM_SCREEN, this);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
	
	/**
     * Handles the event when the remove item button is clicked.
     * Loads the Remove Item screen into the dashboard.
     *
     * @param event the action event
     * @throws IOException if loading the FXML file fails
     */
    @FXML
    private void onRemoveClicked(ActionEvent event) throws IOException {
        ScreenLoader screenLoader = new ScreenLoader();
        String path = "/gui/view/RemoveItemScreen.fxml";
        AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.REMOVE_ITEM_SCREEN, this);
        dashboard.getChildren().clear(); // Clear current dashboard
        dashboard.getChildren().add(nextDash); // Assign the new dashboard
    }

    /**
     * Handles the event when the edit item button is clicked.
     * Loads the Edit Item screen into the dashboard.
     *
     * @param event the action event
     * @throws IOException if loading the FXML file fails
     */
    @FXML
    private void onEditClicked(ActionEvent event) throws IOException {
        ScreenLoader screenLoader = new ScreenLoader();
        String path = "/gui/view/EditItemScreen.fxml";
        AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.EDIT_ITEM_SCREEN, this);
        dashboard.getChildren().clear(); // Clear current dashboard
        dashboard.getChildren().add(nextDash); // Assign the new dashboard
    }

    /**
     * Logs out the current user and displays the login screen.
     *
     * @param event the action event
     * @throws Exception if logging out or displaying the login screen fails
     */
    public void logOut(ActionEvent event) throws Exception {
        user.setisLoggedIn(0);
        ClientMainController.requestUpdateIsLoggedIn(user);
        displayLogin(event);
    }

    /**
     * Displays the login screen.
     *
     * @param event the action event
     * @throws Exception if loading the FXML file fails
     */
	public void displayLogin(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setController(new LoginController());
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/view/LoginScreen.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Main");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(closeEvent ->{
            try {
        		ClientConsole.disconnectClientFromServer();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        });
		primaryStage.setResizable(false);
		primaryStage.show();
	} 
	
	
	
	
}