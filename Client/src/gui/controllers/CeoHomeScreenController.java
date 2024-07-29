package gui.controllers;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.BranchManager;
import entities.Ceo;
import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
        System.out.println("CeoHomeScreenController" + (User)response.getMessage());
        Ceo ceo = (Ceo)response.getMessage();
        this.ceo = ceo;
        UpdateLabel(ceo);
    }
	
	public void UpdateLabel(Ceo ceo) {
		WelcomeLabel.setText("Welcome" + " " + ceo.GetFirstName() + " " + ceo.GetLastName());
	}
	
	public void ShowQuarterlyReports() {
		
	}
}
