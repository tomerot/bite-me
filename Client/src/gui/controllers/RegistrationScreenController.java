package gui.controllers;
import java.sql.Date;
import java.util.List;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.Customer;
import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class RegistrationScreenController {
	@FXML
	private AnchorPane dashboard;
	@FXML
	private Button backBtn;
	@FXML
    private TableView<Customer> CustomersTable;
    @FXML
    private TableColumn<Customer, String> userName;
    @FXML
    private TableColumn<Customer, Integer> Id;
    @FXML
    private TableColumn<Customer, String> type;
    @FXML
    private TableColumn<Customer, Integer> companyId;
    @FXML
    private TableColumn<Customer, String> firstName;
    @FXML
    private TableColumn<Customer, String> lastName;
    @FXML
    private TableColumn<Customer, String> email;
    @FXML
    private TableColumn<Customer, String> phone;
    @FXML
    private TableColumn<Customer, String> homeBranch;
    @FXML
    private TableColumn<Customer, String> creditCardNumber;
    @FXML
    private TableColumn<Customer, String> cvv;
    @FXML
    private TableColumn<Customer, Date> validDate;
    @FXML
    private TableColumn<Customer, Float> walletBallance;
    
	private BranchManagerController prevManagerController;
	private HBox wholeScreen;
	public RegistrationScreenController(HBox wholeScreen , Object prevController) {
		this.prevManagerController=(BranchManagerController)prevController;
		this.wholeScreen = wholeScreen;
		//setupRegistrationTable();
	}
	public void goBack(ActionEvent event) throws Exception {
		ScreenLoader screenLoader = new ScreenLoader();
		String path= "/gui/view/BranchManagerScreen.fxml";
		HBox prevWholeScreen=screenLoader.loadPreviousScreen(path, Screen.MANAGER_SCREEN, prevManagerController);
		prevManagerController.UpdateLabel(prevManagerController.getBranchManager());
		wholeScreen.getChildren().clear();
		wholeScreen.getChildren().add(prevWholeScreen);
	}
	
	public void setupRegistrationTable() {
		// Initialize the columns
		userName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
		Id.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
		type.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerType().toString()));
		companyId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCompanyId()).asObject());
		firstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
		lastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
		email.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
		phone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneNumber()));
		homeBranch.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHomeBranch().toString()));
		creditCardNumber.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCredit()));
		cvv.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCvv()));
		validDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValidDate()));
		walletBallance.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getWalletBalance()).asObject());
		    
//        userName.setCellValueFactory(new PropertyValueFactory<>("customerData.getUserName()"));
  //      Id.setCellValueFactory(new PropertyValueFactory<>("customerData.getId()"));
    //    type.setCellValueFactory(new PropertyValueFactory<>("type"));
     //   companyId.setCellValueFactory(new PropertyValueFactory<>("companyId"));
//        firstName.setCellValueFactory(new PropertyValueFactory<>("customerData.getFirstName()"));
  //      lastName.setCellValueFactory(new PropertyValueFactory<>("customerData.getLastName()"));
    //    email.setCellValueFactory(new PropertyValueFactory<>("customerData.getEmail()"));
      //  homeBranch.setCellValueFactory(new PropertyValueFactory<>("homeBranch"));
 //       creditCardNumber.setCellValueFactory(new PropertyValueFactory<>("credit"));
   //     cvv.setCellValueFactory(new PropertyValueFactory<>("cvv"));
     //   validDate.setCellValueFactory(new PropertyValueFactory<>("validDate"));
       // walletBallance.setCellValueFactory(new PropertyValueFactory<>("walletBalance"));
        
        ClientMainController.requestUnregisteredCustomersData(prevManagerController.getBranchManager());
		ServerResponseDataContainer entityResponse = ClientConsole.responseFromServer;
		@SuppressWarnings("unchecked")
		List<Customer> unRegisteredCustomers=(List<Customer>)entityResponse.getMessage();
		ObservableList<Customer> customerData = FXCollections.observableArrayList(unRegisteredCustomers);
		CustomersTable.setItems(customerData);
    }
	
	/*private void loadCustomerData() {
        // Load data from your database and add to customerData
        // This is just a sample data for demonstration
        customerData.add(new Customer("Data1", "Data2", "Data3", "Data4", "Data5", "Data6", "Data7", "Data8", "Data9", "Data10", "Data11", "Data12"));
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
    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }*/
}
