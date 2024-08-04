package gui.controllers;

import java.util.Stack;

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
	
	public void UpdateLabel(Ceo ceo) {
	    Platform.runLater(() -> {
	        welcomeLbl.setText("Welcome, " + ceo.getFirstName() + " " + ceo.getLastName());
	    });
	}
	
	public void displayQuarterlyReportScreen() {
		
	}
	
    public void logOut(ActionEvent event) throws Exception{
		user.setisLoggedIn(0);
		ClientMainController.requestUpdateIsLoggedIn(user);
		displayLogin(event);
    }
    
	// Method to display the Client Home Page (HomeClientPage GUI)
	public void displayLogin(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setController(new LoginController());
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/view/LoginScreen.fxml").openStream());
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/view/LoginScreen.css").toExternalForm());
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void displayMonthlyReportScreen(ActionEvent event) throws Exception {
    	ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/MonthlyReportScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.MONTHLY_REPORT_SCREEN, this);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
    
}
