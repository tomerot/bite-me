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
	private Button RegisterCustomerButton;
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
	public User getUser() {
		return user;
	}
	public BranchManager getBranchManager() {
		return manager;
	}

    public void updateUI(BranchManager manager) {
        WelcomeMessageLabel.setText("Welcome, " + manager.getFirstName()+ " " + manager.getLastName());
        homeBranchLabel.setText(manager.getUserType());
    }
    
    public void logOut(ActionEvent event) throws Exception{
		user.setisLoggedIn(0);
		ClientMainController.requestUpdateUserData(user);
		displayLogin(event);
    }
    
	// Method to display the Client Home Page (HomeClientPage GUI)
	public void displayLogin(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
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
    	FXMLLoader loader = new FXMLLoader();
    	ReportController ReportController=new ReportController(this);//this is a mistake- should init a ReportController with appropriate ceo\branchmanager controller, and use it
	    loader.setController(ReportController);
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/view/MonthlyReportScreen.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Monthly Reports");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void displayRegistrationScreen(ActionEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader();
	    loader.setController(this);
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/view/RegistrationScreen.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Register Customers");
		primaryStage.setScene(scene);
		primaryStage.show();
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