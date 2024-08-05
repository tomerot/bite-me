package gui.controllers;

import entities.Customer;
import entities.Supplier;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class RestaurantMenuScreenController {
	private ChooseRestaurantScreenController prevController;
	private HBox wholeScreen;
	private Customer customer;
	private Supplier supplier;
	
	@FXML
	private AnchorPane dashboard;
	
	public RestaurantMenuScreenController(HBox wholeScreen, Object prevController) {
		this.wholeScreen = wholeScreen;
		this.prevController = (ChooseRestaurantScreenController) prevController;
		customer = this.prevController.getCustomer();
		supplier = this.prevController.getPreferredSupplier();
	}
	
	public void initialize() { //Just making sure everything passed
		System.out.println(customer.getId());
		System.out.println(supplier.getName());
	}
}
