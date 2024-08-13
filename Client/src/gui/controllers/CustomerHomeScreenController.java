package gui.controllers;

import client.ClientConsole;
import client.ClientMainController;
import entities.Ceo;
import entities.Customer;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The controller for the customer home screen user interface.
 * It manages the display of the customer home screen, user logout, and transitions to other screens.
 */
public class CustomerHomeScreenController {

	private User user;
	private Customer customer;
	
	@FXML
	private Label welcomeLbl;
	
	@FXML
	private AnchorPane dashboard;
	
	@FXML
	private HBox screen;
	
    /**
     * Constructs a CustomerHomeScreenController with the specified user.
     * Initializes the customer and updates the label with the user's information.
     * 
     * @param user the user associated with this controller
     */
	public CustomerHomeScreenController(User user) {
		this.user = user;
		this.customer = (Customer)user;
		UpdateLabel(this.customer);
	}
	
	public User getUser() {
		return user;
	}
	
	public Customer getCustomer() {
		return customer;
	}

    /**
     * Updates the welcome label with the customer's first and last name.
     * This method is run on the JavaFX Application Thread.
     * 
     * @param customer the customer whose information will be displayed
     */
	public void UpdateLabel(Customer customer) {
	    Platform.runLater(() -> {
	        welcomeLbl.setText("Welcome, " + customer.getFirstName() +" " + customer.getLastName());
	    });
	}
	
	
    /**
     * Logs out the current user and transitions to the login screen.
     * 
     * @param event the action event triggered by the logout button
     * @throws Exception if an error occurs while transitioning to the login screen
     */
    public void logOut(ActionEvent event) throws Exception{
		user.setisLoggedIn(0);
		ClientMainController.requestUpdateIsLoggedIn(user);
		ClientMainController.customerLogout();
		displayLogin(event);
    }
    
    /**
     * Displays the login screen.
     * Hides the current window and shows a new stage with the login UI.
     * 
     * @param event the action event triggered by the logout button
     * @throws Exception if an error occurs while loading the login screen
     */
	public void displayLogin(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setController(new LoginController());
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/view/LoginScreen.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Login");
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
	
    /**
     * Displays the restaurant options screen.
     * Loads and shows the restaurant options screen within the current dashboard.
     * 
     * @param event the action event triggered by the button to display the screen
     * @throws Exception if an error occurs while loading the screen
     */
	public void displayChooseRestaurantScreen(ActionEvent event) throws Exception {
    	ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/ChooseRestaurantScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.CHOOSE_RESTAURANT_SCREEN, this);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
	
	public void displayMyOrderScreen(ActionEvent event) throws Exception{
    	ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/MyOrdersScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.MY_ORDERS_SCREEN, this);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
}

