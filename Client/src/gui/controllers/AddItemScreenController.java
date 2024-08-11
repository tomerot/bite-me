package gui.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.AuthorizedEmployee;
import entities.Category;
import entities.Item;
import entities.User;
import enums.ServerResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;


/**
 * Controller for the Add Item screen in the GUI.
 * This controller manages the user interface for adding a new item
 * and interacting with the server.
 */
public class AddItemScreenController implements Initializable{

	private EmployeeHomeScreenController prevController;
	private HBox wholeScreen;
	private AuthorizedEmployee employee;
	
	
	/**
     * Constructs an instance of the AddItemScreenController.
     *
     * @param wholeScreen the HBox representing the whole screen
     * @param prevController the previous main controller (EmployeeHomeScreenController)
     */
	public AddItemScreenController(HBox wholeScreen , Object prevController) {
		this.prevController = (EmployeeHomeScreenController) prevController;
		this.wholeScreen = wholeScreen;
	}
	
	@FXML
	private ComboBox<Category> categoryField;
	
	ObservableList<Category> list;
	
	@FXML
	private Button btnAdd;
	
	@FXML
	private Button btnBack;

	@FXML
	private TextField itemNameField;

	@FXML
	private TextField priceField;
	
	@FXML
	private RadioButton donenessField;
	
	@FXML
	private RadioButton sizeField;
	
	@FXML
	private RadioButton restrictionsField;
	
	@FXML
	private TextArea descriptionField; 
	
	@FXML
	private Label resultMessage; 
	
	
	
	private User user;
   
	 /**
     * Sets the user for this controller.
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the current user of this controller.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Initializes the controller class.
     * This method is called after the FXML file has been loaded.
     *
     * @param location the location used to resolve relative paths for the root object, or null
     * @param resources the resources used to localize the root object, or null
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCategoryComboBox();
        this.employee = EmployeeHomeScreenController.getEmployee();
    }

    /**
     * Sets up the category combo box with predefined categories.
     */
    private void setCategoryComboBox() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(Category.SALAD);
        categories.add(Category.FIRSTCOURSE);
        categories.add(Category.MAINCOURSE);
        categories.add(Category.DESSERT);
        categories.add(Category.BEVERAGE);

        list = FXCollections.observableArrayList(categories);
        categoryField.setItems(list);
    }

    /**
     * Handles the event when the Add Item button is clicked.
     * Validates the input, creates an Item object, and sends it to the server.
     *
     * @param event the action event
     * @throws Exception if the request to the server fails
     */
	@FXML
	private void onAddItemClicked(ActionEvent event) throws Exception{
		resultMessage.setStyle("-fx-text-fill: red;");
		
		String itemName = itemNameField.getText();
		String itemPriceString = priceField.getText();
		Category itemCategory = categoryField.getSelectionModel().getSelectedItem();
		
		//check all the relevant fields are inserted
		if(itemName.isEmpty() || itemPriceString.isEmpty() || itemCategory==null) {
			resultMessage.setText("item name, price and category cannot be empty.");
			return;
		}
		
		float price;
		
		try {
	          price = Float.parseFloat(itemPriceString);
	          
	        } catch (NumberFormatException e) {
	        	resultMessage.setText("Invalid price");
				return; 
	    }
		
		if(price <= 0.0) {
			resultMessage.setText("Invalid price");
			return; 
		}
		
		//from here the input is legal, lets send it to the server

		String itemDescription = descriptionField.getText();

		boolean customSize= sizeField.isSelected() ?  true : false;
		boolean customDoneness= donenessField.isSelected() ?  true : false;
		boolean customRestrictions= restrictionsField.isSelected() ?  true : false;    
		
		//id is default 0, we don't really insert it to the database.
		Item item = new Item(0,employee.getSupplierId() ,itemName, itemCategory, itemDescription, customSize, customDoneness, customRestrictions, price);
		
		
		ClientMainController.requestAddItemData(item);
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		System.out.println(response.getResponse());
		resultMessage.setText((String)response.getMessage());
		if(response.getResponse().equals(ServerResponse.ADD_SUCCESS)) 
			resultMessage.setStyle("-fx-text-fill: black;");
	}
	
	
}