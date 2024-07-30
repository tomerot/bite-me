package gui.controllers;
import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.BranchManager;
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

public class BranchManagerController {
	@FXML
	private Button logOutButton;
	@FXML
    private Label WelcomeMessageLabel;
    @FXML
    private Label homeBranchLabel;

	private User user;
	private BranchManager manager;
    
	public void setUser(User user) {
        this.user = user;
        ClientMainController.requestBranchManagerData(user);
        ServerResponseDataContainer response = ClientConsole.responseFromServer;
        BranchManager manager=(BranchManager) response.getMessage();
        this.manager=manager;
        updateUI(manager);
    }

    public void updateUI(BranchManager manager) {
        WelcomeMessageLabel.setText("Welcome, " + manager.getFirstName()+ " " + manager.getLastName());
        homeBranchLabel.setText(manager.getUserType());
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