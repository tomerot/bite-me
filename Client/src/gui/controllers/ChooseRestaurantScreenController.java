package gui.controllers;
	
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.Customer;
import entities.Supplier;
import enums.Branch;
import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
	
	/**
	 * Controller for the new order screen in the customer interface.
	 * Handles displaying a list of restaurants (suppliers) and navigating to the next page.
	 * @author Tomer Rotman
	 */
	public class ChooseRestaurantScreenController {
	
		private CustomerHomeScreenController prevController;
		private HBox wholeScreen;
		private Customer customer;
		private Supplier preferredSupplier;
	
		@FXML
		private AnchorPane dashboard;
	
		@FXML
		private Button backBtn;
	
		@FXML
		private GridPane gridPane;
		
		@FXML
		private Button northBtn;
		
		@FXML
		private Button centerBtn;
		
		@FXML
		private Button southBtn;
		
		private List<Supplier> northSuppliers = new ArrayList<>();
		private List<Supplier> centerSuppliers = new ArrayList<>();
		private List<Supplier> southSuppliers = new ArrayList<>();
		
		public ChooseRestaurantScreenController(HBox wholeScreen, Object prevController) {
			this.prevController = (CustomerHomeScreenController) prevController;
			this.wholeScreen = wholeScreen;
			customer = ((CustomerHomeScreenController) prevController).getCustomer();
		}
		
		public void initialize() {
		    //Simulate button click based on customer home branch
		    switch(customer.getHomeBranch()) {
		        case NORTH:
		            northBtn.fire();
		            break;
		        case CENTER:
		            centerBtn.fire();
		            break;
		        case SOUTH:
		            southBtn.fire();
		            break;
		    }
		}
		
		public Supplier getPreferredSupplier() {
			return preferredSupplier;
		}
		
		public Customer getCustomer() {
			return customer;
		}
		
		public void pressNorth(ActionEvent event) {
			loadAllRestaurants(Branch.NORTH, northSuppliers);
		}
		
		public void pressCenter(ActionEvent event) {
			loadAllRestaurants(Branch.CENTER, centerSuppliers);
		}
		
		public void pressSouth(ActionEvent event) {
			loadAllRestaurants(Branch.SOUTH, southSuppliers);
		}
		
		/**
	     * Loads and displays all available restaurants (suppliers) in a GridPane.
	     * Requests the list of restaurants of a given branch from the server and populates the GridPane with restaurants buttons.
	     * @param branchName the branch to load it's restaurants
	     * @param suppliers the list of restaurants to insert to the GridPane
	     */
		public void loadAllRestaurants(Branch branchName, List<Supplier> suppliers) {
			gridPane.getChildren().clear();
			if(suppliers.isEmpty()) { //No need to create query if suppliers already fetched
				ClientMainController.requestRestaurantsByBranch(branchName);
				ServerResponseDataContainer response = ClientConsole.responseFromServer;
				suppliers.addAll((ArrayList<Supplier>) response.getMessage());
			}
			
			for (Supplier supplier : suppliers) {
				AnchorPane card = createRestaurantCard(supplier);
				gridPane.add(card, gridPane.getChildren().size() % 3, gridPane.getChildren().size() / 3); // Add to gridPane
			}
			
			gridPane.setHgap(10); //Need to be fixed somehow
		    gridPane.setVgap(30);
			
		}
		
		/**
	     * Creates an AnchorPane button for a single restaurant
	     * 
	     * @param supplier the restaurant to display
	     * @return an AnchorPane containing supplier's picture, city and address
	     */
		private AnchorPane createRestaurantCard(Supplier supplier) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/RestaurantCard.fxml"));
			AnchorPane card = null;
	        try {
	            card = loader.load();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        //Labels adjustments
	        Label restaurantNameLabel = (Label) card.lookup("#restaurantName");
	        Label restaurantAddressLabel = (Label) card.lookup("#restaurantAddress");
	        restaurantNameLabel.setText(supplier.getName());
	        restaurantAddressLabel.setText(supplier.getCity() + " | " + supplier.getAddress());
	        
	        //Image adjustments
	        ImageView restaurantImage = (ImageView) card.lookup("#restaurantImage");
	        String imgPath = "/gui/resource/" + supplier.getSupplierID() + ".png";
	        try {
	        	Image image = new Image(imgPath);
	        	restaurantImage.setImage(image);
	        } catch(IllegalArgumentException e) { //Image not found
	        	Image no_image = new Image("gui/resource/No_image.png");
	        	restaurantImage.setImage(no_image);
	        }
	        
	        card.setOnMouseClicked(event -> {
	        		try {
						displayRestaurantMenuScreen(supplier);
					} catch (Exception e) {
						e.printStackTrace();
					}
	        });
	        
	        return card;
		}
	
		private void displayRestaurantMenuScreen(Supplier supplier) throws Exception {
			preferredSupplier = supplier; //The supplier that the customer has chosen
			ScreenLoader screenLoader = new ScreenLoader();
	    	String path = "/gui/view/RestaurantMenuScreen.fxml";
	    	AnchorPane nextDash = screenLoader.loadOnDashboard(wholeScreen, path, Screen.RESTAURANT_MENU_SCREEN, this);
	    	dashboard.getChildren().clear(); //Clear current dashboard
	    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
		}
		
		public void pressBack(ActionEvent event) throws IOException {
			ScreenLoader screenLoader = new ScreenLoader();
			String path = "/gui/view/CustomerHomeScreen.fxml";
			HBox prevWholeScreen = screenLoader.loadPreviousScreen(path, Screen.CUSTOMER_SCREEN, prevController);
			wholeScreen.getChildren().clear();
			wholeScreen.getChildren().add(prevWholeScreen);
		}
		
	}