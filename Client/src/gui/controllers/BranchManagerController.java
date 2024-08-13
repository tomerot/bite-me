package gui.controllers;
import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
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
 * The controller for the branch manager's user interface.
 * It manages interactions for branch manager-specific actions and UI updates.
 */
public class BranchManagerController {
	@FXML
	private Button logOutButton;
	@FXML
	private Button RegisterCustomerButton;
	@FXML
    private Label welcomeLbl;
    @FXML
    private Label homeBranchLbl;
	@FXML
	private AnchorPane dashboard;
	@FXML
	private HBox screen;
    
	private User user;
	private BranchManager manager;
    
    /**
     * Constructs a BranchManagerController with the given user.
     * 
     * @param user the user for whom this controller is created, expected to be a BranchManager
     */
	public BranchManagerController(User user) {
		this.user = user;
		this.manager=(BranchManager)user;
        UpdateLabel((BranchManager)user);
	}
	
	public BranchManager getBranchManager() {
		return manager;
	}
	
	public User getUser() {
		return user;
	}
	
    /**
     * Updates the labels on the UI with the branch manager's information.
     * This method is run on the JavaFX Application Thread.
     * 
     * @param manager the branch manager whose details are to be displayed
     */
    public void UpdateLabel(BranchManager manager) {
	    Platform.runLater(() -> {
	    	welcomeLbl.setText("Welcome, " + manager.getFirstName()+ " " + manager.getLastName());
	    	homeBranchLbl.setText(manager.getbranchType()+" Manager Screen");
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
     * Displays the registration screen on the dashboard.
     * 
     * @param event the action event triggered by the corresponding button
     * @throws Exception if an error occurs while loading the registration screen
     */
	public void displayRegistrationScreen(ActionEvent event) throws Exception {
    	ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/RegistrationScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.REGISTRATION_SCREEN, this);
    	String css = getClass().getResource("/gui/view/RegistrationScreen.css").toExternalForm();
        nextDash.getStylesheets().add(css);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
}