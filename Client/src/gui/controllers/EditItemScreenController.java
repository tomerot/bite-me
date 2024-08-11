package gui.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.AuthorizedEmployee;
import entities.Category;
import entities.Item;
import enums.ServerResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;


/**
 * Controller for the edit item screen in the GUI.
 * This controller manages the interactions for editing items in a restaurant's menu.
 */
public class EditItemScreenController implements Initializable{

	private EmployeeHomeScreenController prevController;
	private HBox wholeScreen;


	private AuthorizedEmployee employee;

	private Map<String, Item> itemsMap = new HashMap<>(); // key is the item name, value is the item instance itself.

	@FXML
	private Button btnEdit;

	@FXML
	private Button btnBack;

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

	@FXML
	private ListView<String> listOfItems;

	@FXML
	private ComboBox<Category> categoryField;

	ObservableList<Category> categoryList;
	
	
	 /**
     * Constructs an instance of EditItemScreenController.
     *
     * @param wholeScreen the HBox containing the entire screen
     * @param prevController the previous main controller (EmployeeHomeScreenController)
     */
	public EditItemScreenController(HBox wholeScreen, Object prevController) {
		this.prevController = (EmployeeHomeScreenController) prevController;
		this.wholeScreen = wholeScreen;
	}
	
	
	
	 /**
     * Initializes the controller class.
     * This method is called after the FXML file has been loaded.
     * It sets up the category combo box, populates the item list, and sets up a listener for item selection.
     *
     * @param location  the location used to resolve relative paths for the root object, or null
     * @param resources the resources used to localize the root object, or null
     */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.employee=EmployeeHomeScreenController.getEmployee();
		setCategoryComboBox();

		// get all the items of the restaurant this employee works for.
		ClientMainController.requestFullItemsList(employee.getSupplierId());
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		itemsMap = (Map<String, Item>) response.getMessage();
		
		
		// listener to ListView for item selection
		listOfItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue != null) {
	            Item selectedItem = itemsMap.get(newValue);
	            populateFieldsWithItemDetails(selectedItem);
	        }
	    });
	}
	
	
	/**
     * Sets up the category combo box with predefined categories.
     */
	private void setCategoryComboBox() {
		ArrayList<Category> al = new ArrayList<Category>();	
		al.add(Category.SALAD);
		al.add(Category.FIRSTCOURSE);
		al.add(Category.MAINCOURSE);
		al.add(Category.DESSERT);
		al.add(Category.BEVERAGE);
		categoryList = FXCollections.observableArrayList(al);
		categoryField.setItems(categoryList);
	}
	
	
	 /**
     * Handles the event when a category is selected from the combo box.
     * Updates the list of items based on the selected category.
     *
     * @param event the action event
     * @throws Exception if updating the list fails
     */
	@FXML
	private void onCategoryClicked(ActionEvent event) throws Exception{
		
		Category category = categoryField.getSelectionModel().getSelectedItem();
		ObservableList<String> itemsInCategory = FXCollections.observableArrayList();
		
		 // Iterate over keys(names of items)
        for (String itemName : itemsMap.keySet()) {
            Item item = itemsMap.get(itemName);
            if(item.getType().equals(category))
            	itemsInCategory.add(item.getName());
        }
         // update the listView and set its items
        listOfItems.setItems(itemsInCategory);       
	}
	
	
	/**
     * Handles the event when the edit button is clicked.
     * Updates the selected item in the database with new details.
     *
     * @param event the action event
     * @throws Exception if updating the item fails
     */
	@FXML
	private void onEditClicked(ActionEvent event) throws Exception{
		resultMessage.setStyle("-fx-text-fill: red;");
		
		 String itemName = listOfItems.getSelectionModel().getSelectedItem();	
		 // Check if any item is selected
        if (itemName==null) {
        	resultMessage.setText("No item selected.");	
        	return;
        }

		String itemPriceString = priceField.getText();
		float itemPrice;
		
		try {
	          itemPrice = Float.parseFloat(itemPriceString);
	          
	        } catch (NumberFormatException e) {
	        	resultMessage.setText("Invalid price");
				return; 
	    }
		
		if(itemPrice <= 0.0) {
			resultMessage.setText("Invalid price");
			return; 
		}
		
		Category itemCategory = categoryField.getSelectionModel().getSelectedItem();
		String itemDescription = descriptionField.getText();
		
		boolean customSize= sizeField.isSelected() ?  true : false;
		boolean customDoneness= donenessField.isSelected() ?  true : false;
		boolean customRestrictions= restrictionsField.isSelected() ?  true : false;
		
		//let's find out if the employee really changed something. if not, we will not go to the database for nothing.
		Item selectedItem = itemsMap.get(itemName);	
		
		if(selectedItem.getPrice() != itemPrice || !itemDescription.equals(selectedItem.getDescription()) || 
				customSize != selectedItem.getCustomSize() || customDoneness!= selectedItem.getCustomDonenessDegree() || 
				customRestrictions != selectedItem.getCustomRestrictions()) 
		{
			Item updatedItem = new Item(selectedItem.getItemID(), selectedItem.getSupplierID(), itemName, itemCategory, itemDescription, customSize, customDoneness, customRestrictions, itemPrice);
			 //let's update the item in the database.
	        ClientMainController.requestUpdateItem(updatedItem);
			ServerResponseDataContainer response = ClientConsole.responseFromServer;
			
			resultMessage.setText((String)response.getMessage());
			if(response.getResponse().equals(ServerResponse.UPDATE_ITEM_SUCCESS)) {
				resultMessage.setStyle("-fx-text-fill: black;");
				itemsMap.put(itemName, updatedItem);   //let's update the selected item in the map
			}				
		}		
		else 
			resultMessage.setText("Nothing changed");
	}
	
	  /**
     * Populates the input fields with the details of the selected item.
     *
     * @param item the item whose details are to be displayed
     */
	private void populateFieldsWithItemDetails(Item item) {
	    if (item != null) {
	        priceField.setText(String.valueOf(item.getPrice()));
	        descriptionField.setText(item.getDescription());
	        sizeField.setSelected(item.getCustomSize());
	        donenessField.setSelected(item.getCustomDonenessDegree());
	        restrictionsField.setSelected(item.getCustomRestrictions());
	        categoryField.setValue(item.getType());
	    }
	}
}