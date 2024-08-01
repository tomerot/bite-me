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
	
    public void UpdateLabel(BranchManager manager) {
	    Platform.runLater(() -> {
	    	welcomeLbl.setText("Welcome, " + manager.getFirstName()+ " " + manager.getLastName());
	    	homeBranchLbl.setText(manager.getbranchType()+" Manager Screen");
	    });
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
	
	public void displayRegistrationScreen(ActionEvent event) throws Exception {
    	ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/RegistrationScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.REGISTRATION_SCREEN, this);
    	String css = getClass().getResource("/gui/view/RegistrationScreen.css").toExternalForm();
        nextDash.getStylesheets().add(css);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
		//setupRegistrationTable();
	}
	/*
    @FXML
    private TableView<Customer> unregisteredCustomersTable;
    @FXML
    private TableColumn<Customer, Boolean> selectColumn;
    @FXML
    private TableColumn<Customer, String> column1;
    @FXML
    private TableColumn<Customer, String> column2;
    // Add fields for columns 3 to 12
    @FXML
    private TableColumn<Customer, String> column13;
    @FXML
    private Button registerButton;

    private ObservableList<Customer> customerData = FXCollections.observableArrayList();

    // Call this method when you navigate to the registration screen
    public void setupRegistrationTable() {
        // Initialize the columns
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        column1.setCellValueFactory(new PropertyValueFactory<>("column1Data"));
        column2.setCellValueFactory(new PropertyValueFactory<>("column2Data"));
        // Initialize columns 3 to 12 in a similar way
        column13.setCellValueFactory(new PropertyValueFactory<>("column13Data"));

        // Load data from the database and add it to the customerData list
        loadCustomerData();

        unregisteredCustomersTable.setItems(customerData);
    }

    private void loadCustomerData() {
        // Load data from your database and add to customerData
        // This is just a sample data for demonstration
        customerData.add(new Customer("Data1", "Data2", /* other columns data , "Data13"));
    }

    @FXML
    public void registerSelected(ActionEvent event) {
        for (Customer customer : customerData) {
            if (customer.isSelected()) {
                // Update the relevant fields in the database for the selected customers
                registerCustomerInDatabase(customer);
            }
        }
    }

    private void registerCustomerInDatabase(Customer customer) {
        // Implement your database update logic here
    }

    public static class Customer {
        private final SimpleBooleanProperty selected;
        private final SimpleStringProperty column1Data;
        private final SimpleStringProperty column2Data;
        // Add fields for columns 3 to 12
        private final SimpleStringProperty column13Data;

        public Customer(String column1Data, String column2Data, /* other columns data , String column13Data) {
            this.selected = new SimpleBooleanProperty(false);
            this.column1Data = new SimpleStringProperty(column1Data);
            this.column2Data = new SimpleStringProperty(column2Data);
            // Initialize fields for columns 3 to 12
            this.column13Data = new SimpleStringProperty(column13Data);
        }

        public boolean isSelected() {
            return selected.get();
        }

        public SimpleBooleanProperty selectedProperty() {
            return selected;
        }

        public String getColumn1Data() {
            return column1Data.get();
        }

        public String getColumn2Data() {
            return column2Data.get();
        }

        // Add getters for columns 3 to 12

        public String getColumn13Data() {
            return column13Data.get();
        }
    }*/
}