	package gui.controllers;
	
	import java.sql.Date;
	import java.util.List;
	
	import client.ClientConsole;
	import client.ClientMainController;
	import containers.ServerResponseDataContainer;
	import entities.Customer;
	import entities.Supplier;
	import enums.Branch;
	import enums.CustomerType;
	import javafx.fxml.FXML;
	import javafx.geometry.Insets;
	import javafx.scene.control.Button;
	import javafx.scene.control.Label;
	import javafx.scene.layout.AnchorPane;
	import javafx.scene.layout.HBox;
	import javafx.scene.layout.TilePane;
	import javafx.scene.layout.VBox;
	
	/**
	 * Controller for the new order screen in the customer interface.
	 * Handles displaying a list of restaurants (suppliers) and navigating to the next page.
	 */
	public class NewOrderScreenController {
	
		private CustomerHomeScreenController prevManagerController;
		private HBox wholeScreen;
	
		@FXML
		private AnchorPane dashboard;
	
		@FXML
		private Button backBtn;
	
		@FXML
		private TilePane tilePane;
		
	
		public NewOrderScreenController(HBox wholeScreen, Object prevController) {
			this.prevManagerController = (CustomerHomeScreenController) prevController;
			this.wholeScreen = wholeScreen;
		}
	
	    /**
	     * Loads and displays all available restaurants (suppliers) in a TilePane.
	     * Requests the list of suppliers from the server and populates the TilePane with supplier information.
	     */
		public void loadAllRestaurants() {
			Customer t = new Customer(null, 0, null, 0, null, null, null, null, Branch.NORTH, null, null, null, 0, 0, 0,
					null); // THIS IS FOR TEST!! HOW I BRING ENTETIE FROM OTHER CONTROLLER
			ClientMainController.requestAllRestaurants(t);
			ServerResponseDataContainer response = ClientConsole.responseFromServer;
			List<Supplier> l = (List<Supplier>) response.getMessage();
	        tilePane.setHgap(10); // Horizontal gap between items
	        tilePane.setVgap(10); // Vertical gap between items
	        tilePane.setPrefColumns(3); // Number of columns
			for (Supplier supplier : l) {
				VBox vbox = createSupplierVBox(supplier);
				tilePane.getChildren().add(vbox);
			}
		}
	
	    /**
	     * Creates a VBox for displaying a single supplier.
	     * 
	     * @param supplier the supplier to display
	     * @return a VBox containing supplier information
	     */
		private VBox createSupplierVBox(Supplier supplier) {
			VBox vbox = new VBox();
			vbox.setPadding(new Insets(35));
			vbox.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
	
			Label nameLabel = new Label(supplier.getName());
			nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	
			vbox.getChildren().add(nameLabel);
	
			vbox.setOnMouseClicked(event -> {
				// Handle click event, move to the next page
				moveToNextPage(supplier);
			});
	
			return vbox;
		}
	
		private void moveToNextPage(Supplier supplier) {
			// Implement your logic to move to the next page
			System.out.println("Clicked on supplier: " + supplier.getName());
		}
	}