package gui.controllers;

import client.ClientConsole;
import client.ClientMainController;
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
 * The controller for the CEO's home screen user interface.
 * It manages interactions for CEO-specific actions and UI updates.
 */

public class CeoHomeScreenController {
	
	@FXML
	private Button QuarterlyReports;
	
	@FXML
	private Label welcomeLbl;
	
	@FXML
	private AnchorPane dashboard;
	
	@FXML
	private HBox screen;
	
	private User user;
	private Ceo ceo;
	
    /**
     * Constructs a CeoHomeScreenController with the given user.
     * 
     * @param user the user for whom this controller is created, expected to be a Ceo
     */
	public CeoHomeScreenController(User user) {
		this.user = user;
		this.ceo=(Ceo)user;
        UpdateLabel((Ceo)user);
	}
	
	public Ceo getCeo() {
		return ceo;
	}
	
	public User getUser() {
		return user;
	}
	
    /**
     * Updates the welcome label on the UI with the CEO's information.
     * This method is run on the JavaFX Application Thread.
     * 
     * @param ceo the CEO whose details are to be displayed
     */
	public void UpdateLabel(Ceo ceo) {
	    Platform.runLater(() -> {
	        welcomeLbl.setText("Welcome, " + ceo.getFirstName() + " " + ceo.getLastName());
	    });
	}
	
    /**
     * Handles the logout action, updates the user's login status, and displays the login screen.
     * 
     * @param event the action event triggered by the logout button
     * @throws Exception if an error occurs while updating the login status or displaying the login screen
     */
    public void logOut(ActionEvent event) throws Exception{
		user.setisLoggedIn(0);
		ClientMainController.requestUpdateIsLoggedIn(user);
		displayLogin(event);
    }
    
    /**
     * Displays the login screen by hiding the current window and showing a new stage with the login UI.
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
     * Displays the monthly report screen on the dashboard.
     * 
     * @param event the action event triggered by the corresponding button
     * @throws Exception if an error occurs while loading the monthly report screen
     */
	public void displayMonthlyReportScreen(ActionEvent event) throws Exception {
    	ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/MonthlyReportScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.MONTHLY_REPORT_SCREEN, this);
    	String css = getClass().getResource("/gui/view/MonthlyReportScreenGraph.css").toExternalForm();
        nextDash.getStylesheets().add(css);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
	
    /**
     * Displays the Quarterly report screen on the dashboard.
     * 
     * @param event the action event triggered by the corresponding button
     * @throws Exception if an error occurs while loading the monthly report screen
     */
	public void displayQuarterlyReportScreen(ActionEvent event) throws Exception {
    	ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/QuarterlyReportScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.QUARTERLY_REPORT_SCREEN, this);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
    
}
