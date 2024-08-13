package gui.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

import Server.Server;
import db.DBConnectionDetails;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller for managing server port configurations and server status in the GUI.
 * Handles user interactions for starting, stopping, and managing server connections.
 */
public class ServerPortController {
	@FXML
	private Button importCustomerBtn;
	@FXML
	private Button connectBtn;
	@FXML
	private Button exitBtn;
	@FXML
	private Button disconnectBtn;
	@FXML
	private TextField ipField;
	@FXML
	private TextField portField;
	@FXML
	private TextField dbNameField;
	@FXML
	private TextField dbUsernameField;
	@FXML
	private TextField dbPasswordField;
	@FXML
	private TextField csvFilePath;
	@FXML
	private Label lblServerStatus;
	@FXML
	private ListView lvConnectedClients;
	@FXML
	
	private ObservableList<String> connectedClientsList = FXCollections.observableArrayList();

	// empty constructor
	public ServerPortController() {
	}

    @FXML
    void initialize() {
        lvConnectedClients.setItems(connectedClientsList); 
    }
    
    
    /**
     * Handles the action event when the connect button is clicked.
     * Starts the server using the provided configuration details.
     * 
     * @param event the action event
     */
	@FXML
	private void onConnectServerClicked(ActionEvent event) {
		DBConnectionDetails database = new DBConnectionDetails();
		Integer portNumber;
		boolean serverStatus;
		String ipv4 = "";

		try {
			portNumber = Integer.parseInt(portField.getText());
		} catch (Exception ex) {
			System.out.println("Port must be a number");
			return;
		}
		try {
			database.setIp(ipField.getText());
			database.setName(dbNameField.getText());
			database.setUsername(dbUsernameField.getText());
			database.setPassword(dbPasswordField.getText());

			// Check if any field is empty
			if (database.getName().isEmpty() || database.getUsername().isEmpty() || database.getPassword().isEmpty()) {
				throw new IllegalArgumentException("All fields must be filled");
			}

		} catch (IllegalArgumentException e) {
			System.err.println("Error: " + e.getMessage());
			return;
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			return;
		}

		// Start the server
		serverStatus = Server.startServer(database, portNumber, this);
		
		try {
			ipv4 = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		if (serverStatus) {
			updateServerStatus("Server successfully started.\n ip to connect is: " + ipv4 + "\n " + "on Port: " + portNumber);
			connectBtn.setDisable(true);
			disconnectBtn.setDisable(false);
			importCustomerBtn.setVisible(true);
			csvFilePath.setVisible(true);
		} else {
			updateServerStatus("Failed to start server.");
		}
	}
	/**
	 * Simulates importing customer data and hides the import button upon completion.
	 * 
	 * This method calls a static method `importCustomerSimulation` from the `Server` class
	 * to perform the customer import operation. If a `SQLException` is thrown during
	 * the operation, it is caught and its stack trace is printed. After the import process
	 * is complete, the import button is set to be invisible.
	 * 
	 * @throws SQLException If an SQL error occurs during the import operation. 
	 *                       (This is caught internally and does not propagate.)
	 */
	public void importCustomerSimulation() {
		try {
			Server.importCustomerSimulation(csvFilePath.getText());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		importCustomerBtn.setVisible(false);
		csvFilePath.setVisible(false);
	}
	//add connected client to the list
    public void addConnectedClient(String clientInfo) {
        Platform.runLater(() -> connectedClientsList.add(clientInfo));
    }

    //remove disconnected client from list
    public void removeConnectedClient(String clientInfo) {
        Platform.runLater(() -> connectedClientsList.remove(clientInfo));
    }

	// show server status on the status label
	private void updateServerStatus(String message) {
		Platform.runLater(() -> lblServerStatus.setText(message));
	}

	
    /**
     * Handles the action event when the exit button is clicked.
     * Stops the server and exits the application.
     * 
     * @param event the action event
     * @throws Exception if an error occurs while stopping the server or exiting
     */
	@FXML
	private void getExitBtn(ActionEvent event) throws Exception {
		Server.stopServer();
		Platform.exit();
		System.exit(0);
	}
	
    /**
     * Handles the action event when the disconnect button is clicked.
     * Stops the server and clears the list of connected clients.
     * 
     * @param event the action event
     */
	@FXML
	private void onDisconnectClicked(ActionEvent event) {
		System.out.println("Server Disconnected from Db");
		lblServerStatus.setText("Server Disconnected successfully");
		connectedClientsList.clear();
		Server.stopServer();
		connectBtn.setDisable(false);
		disconnectBtn.setDisable(true);
	}

	
    /**
     * Initializes and displays the Server Port GUI.
     * 
     * @param primaryStage the primary stage for the GUI
     * @throws Exception if an error occurs while loading the FXML or CSS files
     */
	public void start(Stage primaryStage) throws Exception {
		Pane root = FXMLLoader.load(getClass().getResource("/gui/view/ServerPort.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/view/ServerPort.css").toExternalForm());
		primaryStage.setTitle("ServerPort");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
