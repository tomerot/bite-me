package gui.controllers;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.BranchManager;
import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BranchManagerHomeScreenController {

    @FXML
    private Label WelcomeMessageLabel;
    @FXML
    private Label homeBranchLabel;
    @SuppressWarnings("unused")
	private User user;
    
    public void setUser(User user) {
        this.user = user;
        ClientMainController.requestBranchManagerData(user);
        ServerResponseDataContainer response = ClientConsole.responseFromServer;
        BranchManager manager=(BranchManager) response.getMessage();
        updateUI(manager);
    }

    private void updateUI(BranchManager manager) {
        WelcomeMessageLabel.setText("Welcome, " + manager.getFirstName());
        homeBranchLabel.setText(manager.getbranchType().toString());
    }
}