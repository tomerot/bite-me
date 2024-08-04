	package gui.controllers;
	
	import java.io.IOException;
import java.util.List;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.Customer;
import entities.Supplier;
import enums.Branch;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
	
	/**
	 * Controller for the new order screen in the customer interface.
	 * Handles displaying a list of restaurants (suppliers) and navigating to the next page.
	 */
	public class ChooseRestaurantScreenController {
	
		private CustomerHomeScreenController prevController;
		private HBox wholeScreen;
	
		@FXML
		private AnchorPane dashboard;
	
		@FXML
		private Button backBtn;
	
		@FXML
		private GridPane gridPane;
		
		private List<Supplier> northSuppliers;
		private List<Supplier> centerSuppliers;
		private List<Supplier> southSuppliers;
	
		public ChooseRestaurantScreenController(HBox wholeScreen, Object prevController) {
			this.prevController = (CustomerHomeScreenController) prevController;
			this.wholeScreen = wholeScreen;
		}
	
//	    /**
//	     * Loads and displays all available restaurants (suppliers) in a TilePane.
//	     * Requests the list of suppliers from the server and populates the TilePane with supplier information.
//	     */
//		public void loadAllRestaurants() {
//			Customer customer = prevCustomerController.getCustomer();
//			ClientMainController.requestRestaurantsByBranch(customer);
//			ServerResponseDataContainer response = ClientConsole.responseFromServer;
//			List<Supplier> suppliersList = (List<Supplier>) response.getMessage();
//	        tilePane.setHgap(10); // Horizontal gap between items
//	        tilePane.setVgap(10); // Vertical gap between items
//	        tilePane.setPrefColumns(3); // Number of columns
//			for (Supplier supplier : suppliersList) {
//				VBox vbox = createSupplierVBox(supplier);
//				tilePane.getChildren().add(vbox);
//			}
//		}
		
	    /**
	     * Loads and displays all available restaurants (suppliers) in a TilePane.
	     * Requests the list of suppliers from the server and populates the TilePane with supplier information.
	     */
		public void loadAllRestaurants() {
			Customer customer = prevController.getCustomer();
			ClientMainController.requestRestaurantsByBranch(customer.getHomeBranch());
			ServerResponseDataContainer response = ClientConsole.responseFromServer;
			switch(customer.getHomeBranch()) {
				case NORTH:
					northSuppliers = (List<Supplier>) response.getMessage();
					break;
				case CENTER:
					centerSuppliers = (List<Supplier>) response.getMessage();
				case SOUTH:
					southSuppliers = (List<Supplier>) response.getMessage();
			}
			
			for (Supplier supplier : centerSuppliers) {
				AnchorPane card = createRestaurantCard(supplier);
				gridPane.add(card, gridPane.getChildren().size() % 2, gridPane.getChildren().size() / 2); // Add to gridPane
			}
			
			gridPane.setHgap(20); // Adjust the horizontal gap as needed
		    gridPane.setVgap(30); // Adjust the vertical gap as needed
		}
		
		private AnchorPane createRestaurantCard(Supplier supplier) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/RestaurantCard.fxml"));
			AnchorPane card = null;
	        try {
	            card = loader.load();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        Label restaurantNameLabel = (Label) card.lookup("#restaurantName");
	        restaurantNameLabel.setText(supplier.getName());
	        
	        card.setOnMouseClicked(event -> {
	        	moveToNextPage(supplier);
	        });
	        
	        return card;
		}
	
	    /**
	     * Creates an AnchorPane for displaying a single restaurant
	     * 
	     * @param supplier the restaurant to display
	     * @return an AnchorPane containing supplier information
	     */
//		private VBox createSupplierVBox(Supplier supplier) {
//			VBox vbox = new VBox();
//			vbox.setPadding(new Insets(35));
//			vbox.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
//	
//			Label nameLabel = new Label(supplier.getName());
//			nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
//	
//			vbox.getChildren().add(nameLabel);
//	
//			vbox.setOnMouseClicked(event -> {
//				// Handle click event, move to the next page
//				moveToNextPage(supplier);
//			});
//	
//			return vbox;
//		}
	
		//when clicking - need to save what he clicked on, move on to next page (this is just to test for now)
		private void moveToNextPage(Supplier supplier) {
			// Implement your logic to move to the next page
			System.out.println("Clicked on supplier: " + supplier.getName());
		}
		
	}