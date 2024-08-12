package gui.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.BranchManager;
import entities.Customer;
import enums.ServerResponse;
import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Controller for the registration screen used by branch managers to register customers.
 * Displays a table of unregistered customers with the option to select and register them.
 */
public class RegistrationScreenController {
	@FXML
	private Label RegistrationPage;
	@FXML
	private AnchorPane dashboard;
	@FXML
	private Button registerBtn;
	@FXML
	private Button backBtn;
	@FXML
    private TableView<CustomerWithSelection> CustomersTable;
	@FXML
    private TableColumn<CustomerWithSelection, Boolean> selectRow;
    @FXML
    private TableColumn<CustomerWithSelection, String> userName;
    @FXML
    private TableColumn<CustomerWithSelection, Integer> Id;
    @FXML
    private TableColumn<CustomerWithSelection, String> type;
    @FXML
    private TableColumn<CustomerWithSelection, Integer> companyId;
    @FXML
    private TableColumn<CustomerWithSelection, String> firstName;
    @FXML
    private TableColumn<CustomerWithSelection, String> lastName;
    @FXML
    private TableColumn<CustomerWithSelection, String> email;
    @FXML
    private TableColumn<CustomerWithSelection, String> phone;
    @FXML
    private TableColumn<CustomerWithSelection, String> homeBranch;
    @FXML
    private TableColumn<CustomerWithSelection, String> creditCardNumber;
    @FXML
    private TableColumn<CustomerWithSelection, String> cvv;
    @FXML
    private TableColumn<CustomerWithSelection, Integer> validYear;
    @FXML
    private TableColumn<CustomerWithSelection, Integer> validMonth;
    @FXML
    private TableColumn<CustomerWithSelection, Float> walletBallance;
    
	private BranchManagerController prevManagerController;
	private HBox wholeScreen;
	
    /**
     * Constructs a new RegistrationScreenController.
     * 
     * @param wholeScreen the HBox container for the current screen
     * @param prevManagerController the previous controller, expected to be an instance of BranchManagerController
     */
	public RegistrationScreenController(HBox wholeScreen , Object prevController) {
		this.prevManagerController=(BranchManagerController)prevController;
		this.wholeScreen = wholeScreen;
		this.UpdateLabel(prevManagerController.getBranchManager());
	}
	
    /**
     * Updates the registration page label with the branch manager's branch type.
     * 
     * @param manager the branch manager whose branch type is used for the label
     */
    public void UpdateLabel(BranchManager manager) {
	    Platform.runLater(() -> {
	    	RegistrationPage.setText(manager.getbranchType()+" Customers Registration Screen");
	    });
    }
	
    /**
     * A class representing a Customer with a selection state.
     * This class is used to wrap a Customer object with an additional property
     * that indicates whether the customer is selected or not.
     */
	private class CustomerWithSelection {
		private final Customer customer;
		private BooleanProperty selected;

		public CustomerWithSelection(Customer customer) {
			this.customer = customer;
			this.selected = new SimpleBooleanProperty(false);
		}

		public Customer getCustomer() {
			return customer;
		}

		public BooleanProperty selectedProperty() {
			return selected;
		}
	}
	
	/**
	 * A custom TableCell that contains a CheckBox for selecting a CustomerWithSelection.
	 * The CheckBox is bound to the selection state of the CustomerWithSelection object.
	 */
	private class CheckBoxTableCell extends TableCell<CustomerWithSelection, Boolean> {
	    private final CheckBox checkBox;

	    public CheckBoxTableCell() {
	        checkBox = new CheckBox();
	        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
	            if (getTableRow() != null) {
	                CustomerWithSelection customerWithSelection = getTableRow().getItem();
	                if (customerWithSelection != null) {
	                    customerWithSelection.selectedProperty().set(isNowSelected);
	                }
	            }
	        });
	    }
	    /**
	     * Updates the content of this TableCell based on the given item.
	     * If the item is not empty or null, the CheckBox is displayed and its
	     * selection state is updated accordingly.
	     *
	     * @param item  the new value for the CheckBox
	     * @param empty whether this cell represents an empty row
	     */
	    @Override
	    protected void updateItem(Boolean item, boolean empty) {
	        super.updateItem(item, empty);

	        if (empty || item == null) {
	            setGraphic(null);
	        } else {
	            setGraphic(checkBox);
	            checkBox.setSelected(item);
	        }
	    }
	}
	
    /**
     * Navigates back to the previous screen.
     * 
     * @param event the action event that triggered the navigation
     * @throws Exception if there is an error loading the previous screen
     */
	public void goBack(ActionEvent event) throws Exception {
		ScreenLoader screenLoader = new ScreenLoader();
		String path= "/gui/view/BranchManagerScreen.fxml";
		HBox prevWholeScreen=screenLoader.loadPreviousScreen(path, Screen.MANAGER_SCREEN, prevManagerController);
		prevManagerController.UpdateLabel(prevManagerController.getBranchManager());
		wholeScreen.getChildren().clear();
		wholeScreen.getChildren().add(prevWholeScreen);
	}
	
    /**
     * Sets up the registration table with customer data.
     * Configures the table columns and populates the table with unregistered customers.
     */
	public void setupRegistrationTable() {
		
		selectRow.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
		selectRow.setCellFactory(column -> new CheckBoxTableCell());
		userName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getUserName()));
		Id.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCustomer().getId()).asObject());
		type.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustomerType().toString()));
		companyId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCustomer().getCompanyId()).asObject());
		firstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getFirstName()));
		lastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getLastName()));
		email.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getEmail()));
		phone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getPhoneNumber()));
		homeBranch.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getHomeBranch().toString()));
		creditCardNumber.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCredit()));
		cvv.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCvv()));
		validYear.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCustomer().getValidYear()));
		validMonth.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCustomer().getValidMonth()));
		walletBallance.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getCustomer().getWalletBalance()).asObject());

        ClientMainController.requestUnregisteredCustomersData(prevManagerController.getBranchManager());
		ServerResponseDataContainer entityResponse = ClientConsole.responseFromServer;
		if(entityResponse.getResponse()==ServerResponse.UNREGISTERED_CUSTOMERS_FOUND) {
			List<Customer> unRegisteredCustomers=(List<Customer>)entityResponse.getMessage();
			ObservableList<CustomerWithSelection> customerData = FXCollections.observableArrayList();
			for (Customer customer : unRegisteredCustomers) {
				customerData.add(new CustomerWithSelection(customer));
			}
			CustomersTable.setItems(customerData);
		}
    }
	
    /**
     * Registers the selected customers.
     * Collects selected customers from the table and sends a registration request to the server.
     * 
     * @param event the action event that triggered the registration
     * @throws Exception if there is an error during the registration process
     */
	public void registerSelectedCustomers(ActionEvent event) throws Exception{
		List<String> userList= new ArrayList<String>();
		ObservableList<CustomerWithSelection> customers = CustomersTable.getItems();
	    for (CustomerWithSelection customerWithSelection : customers) {
	        if (customerWithSelection.selectedProperty().get()) {
	            userList.add(customerWithSelection.getCustomer().getUserName());
	        }
	    }
	    if(customers.isEmpty() || userList.isEmpty()) {
	    	return;
	    }
	    else {
	    	ClientMainController.requestUpdateRegisterCustomers(userList);
		    this.setupRegistrationTable();
	    }
	}
}
