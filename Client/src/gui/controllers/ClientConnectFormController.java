package gui.controllers;

import client.ClientConsole;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClientConnectFormController {
	//private HomeClientPageController sfc;
	//private static int itemIndex = 3;

	@FXML
	private Button btnConnect = null;
	
	@FXML
	private Button exitBtn;

	@FXML
	private TextField serverIpField;

	@FXML
	private TextField serverPortField;
	
	@FXML
	private Label lblClientStatus;

	// empty constructor
	public ClientConnectFormController() {
	}

	//getter for IpField
	public String getServerIpField() {
		return serverIpField.getText();
	}

	//getter for PortField
	public String getServerPortField() {
		return serverPortField.getText();
	}

	// Method to display the client connection form/Gui
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/view/ClientConnectForm.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/view/ClientConnectForm.css").toExternalForm());
		primaryStage.setTitle("Client Connection");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// Method gets string and print into console - for future comfort
	public void printToConsole(String msg) {
		System.out.println(msg);
	}

	// Method to connect the client to the server
	public void connectToServer(ActionEvent event) {
		boolean isConnected = ClientConsole.connectClientToServer(serverIpField.getText(), serverPortField.getText(),
				this);
		if (isConnected) {
			System.out.println("connected succesfully.");
			try {
				this.displayClientHomeWindow(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Failed to connected the server!");
			updateClientStatus("Failed to connected the server!");
		}
	}
	
	// show server status on the label
	private void updateClientStatus(String message) {
		Platform.runLater(() -> lblClientStatus.setText(message));
	}

	
	// Method to display the Client Home Page (HomeClientPage GUI)
	public void displayClientHomeWindow(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/view/LoginPageController.fxml").openStream());
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/view/LoginPageController.css").toExternalForm());
		primaryStage.setTitle("Main");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void getExitBtn() {
		System.out.println("Exiting...");
		Platform.exit();
		System.exit(1);
	}
}
