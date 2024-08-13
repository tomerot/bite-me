package gui.controllers;

import client.ClientConsole;
import client.ClientMainController;
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

/**
 * The controller for the client connection form user interface.
 * It manages the connection to the server and transitions to the login screen.
 */
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

    /**
     * Displays the client connection form GUI.
     * 
     * @param primaryStage the primary stage for this application
     * @throws Exception if an error occurs while loading the FXML file
     */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/view/ClientConnectForm.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/view/ClientConnectForm.css").toExternalForm());
		primaryStage.setTitle("Client Connection");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

    /**
     * Prints a message to the console.
     * This method is provided for future debugging and convenience.
     * 
     * @param msg the message to print
     */
	public void printToConsole(String msg) {
		System.out.println(msg);
	}

    /**
     * Attempts to connect the client to the server using the IP and port from the input fields.
     * If successful, transitions to the login screen.
     * 
     * @param event the action event triggered by the connect button
     */
	public void connectToServer(ActionEvent event) {
		boolean isConnected = ClientConsole.connectClientToServer(serverIpField.getText(), serverPortField.getText(),
				this);
		if (isConnected) {
			System.out.println("connected succesfully.");
			try {
				this.displayLoginScreen(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Failed to connected the server!");
			updateClientStatus("Failed to connected the server!");
		}
	}
	
    /**
     * Updates the client status label with the given message.
     * 
     * @param message the status message to display
     */
	private void updateClientStatus(String message) {
		Platform.runLater(() -> lblClientStatus.setText(message));
	}

	
    /**
     * Displays the login screen.
     * Hides the current window and shows a new stage with the login UI.
     * 
     * @param event the action event triggered by the connect button
     * @throws Exception if an error occurs while loading the login screen
     */
	public void displayLoginScreen(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setController(new LoginController());
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/view/LoginScreen.fxml").openStream());
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/view/LoginScreen.css").toExternalForm());
		primaryStage.setTitle("Main");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(closeEvent ->{
            try {
        		ClientConsole.disconnectClientFromServer();
                this.getExitBtn();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        });
		primaryStage.show();
	}

    /**
     * Exits the application.
     * Prints a message to the console and then exiting the application.
     */
	public void getExitBtn() {
		System.out.println("Exiting...");
		Platform.exit();
		System.exit(1);
	}
}
