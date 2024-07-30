package gui.controllers;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.BranchManager;
import entities.Ceo;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CeoHomeScreenController {
	
	@FXML
	private Button QuarterlyReports;
	
	@FXML
	private Label WelcomeLabel;
	
	private User user;
	private Ceo ceo;
	
	public void setUser(User user) {
        this.user = user;
        ClientMainController.requestCeoData(user);
        ServerResponseDataContainer response = ClientConsole.responseFromServer;
        Ceo ceo = (Ceo)response.getMessage();
        this.ceo = ceo;
        UpdateLabel(ceo);
    }
	
	public void UpdateLabel(Ceo ceo) {
		WelcomeLabel.setText("Welcome, " + ceo.getFirstName() + " " + ceo.getLastName());
	}
	
	public void ShowQuarterlyReports() {
		
	}
    public void logOut(ActionEvent event) throws Exception{
		user.setisLoggedIn(0);
		ClientMainController.requestUpdateUserData(user);
		displayWindow(event);
    }
    
	// Method to display the Client Home Page (HomeClientPage GUI)
	public void displayWindow(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
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
