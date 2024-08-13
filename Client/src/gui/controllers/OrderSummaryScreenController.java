package gui.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.Customer;
import entities.ItemInOrder;
import entities.Order;
import entities.Supplier;
import entities.SupplyMethod;
import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class OrderSummaryScreenController {
	
	private ChooseRestaurantScreenController restaurantController;
	
	private Map<ItemInOrder, Integer> cart;
	private Customer customer;
	private Supplier supplier;
	private Order order;
	private float walletUsedAmount;
	private int peopleInOrder;
	private int orderID;
	private float deliveryPrice;
	private float itemsPrice;
	
	
	private HBox wholeScreen;
	private CheckoutScreenController prevController;
	
	
    @FXML
    private AnchorPane dashboard;

    @FXML
    private Button backBtn;

    @FXML
    private Button approveBtn;

    @FXML
    private Label nameLbl;

    @FXML
    private Label phoneLbl;

    @FXML
    private Label cityLbl;

    @FXML
    private Label addressLbl;

    @FXML
    private Label supplyMethodLbl;

    @FXML
    private Label requestedDateLbl;

    @FXML
    private Label requestedTimeLbl;

    @FXML
    private Label orderTypeLbl;

    @FXML
    private Label priceInfoLbl;

    @FXML
    private Label totalPriceLbl;
    
    @FXML
    private GridPane itemsGrid;
    
    @FXML
    private ScrollPane itemsScroll;
	
    /**
     * Initializes the `OrderSummaryScreenController` with the given parameters.
     * 
     * @param wholeScreen is for the screen to be displayed
     * @param prevController the previous controller from which data is retrieved
     */
	public OrderSummaryScreenController(HBox wholeScreen, Object prevController) {
		this.wholeScreen = wholeScreen;
		this.prevController = (CheckoutScreenController) prevController;
		this.restaurantController = this.prevController.getRestaurantController();
		this.cart = this.prevController.getCart();
		this.customer = this.prevController.getCustomer();
		this.supplier = this.prevController.getSupplier();
		this.order = this.prevController.getOrder();
		this.walletUsedAmount = this.prevController.getWalletAmount();
		this.peopleInOrder = this.prevController.getPeopleInOrder();
		this.deliveryPrice = this.prevController.getTotalDeliveryPrice();
		this.itemsPrice = this.prevController.getItemsPrice();
	}
	
	
	/**
	 * Initializes the order summary screen by setting up labels and displaying order details.
	 */
	@FXML
	private void initialize() {
		//initialize all the labels
		nameLbl.setText(order.getRecipient());
		phoneLbl.setText(order.getRecipientPhone());
		cityLbl.setText(order.getCity());
		addressLbl.setText(order.getAddress());
		supplyMethodLbl.setText(String.format("%s, participents: %d",order.getSupplyOption(),peopleInOrder));
		requestedDateLbl.setText(order.getRequestedDate());
		requestedTimeLbl.setText(order.getRequestedTime());
		orderTypeLbl.setText(order.getType().toString());
		priceInfoLbl.setText(String.format("items: %.2f₪, delivery: %.2f₪, ,\n%.2f₪ is gonna be used from wallet",itemsPrice,deliveryPrice,walletUsedAmount));
		totalPriceLbl.setText(String.format("%.2f₪", order.getTotalPrice()));
		
		if ("PreOrder".equals(order.getType().toString())) {
			orderTypeLbl.setText(String.format("%s, (10%% discount)", order.getType().toString()));
			totalPriceLbl.setText(String.format("(%.2f₪ + %.2f₪) * 0.9 =  %.2f₪", itemsPrice, deliveryPrice, order.getTotalPrice()));
		}
		if (!order.getSupplyOption().equals(SupplyMethod.SHARED)) {
			supplyMethodLbl.setText(String.format("%s",order.getSupplyOption()));
		}
		
		int row = 0;
		for(ItemInOrder item : cart.keySet()) {
			AnchorPane card;
			if(row % 2 == 0) 
				card = createCartItemCard(item, Parity.EVEN);
			else
				card = createCartItemCard(item, Parity.ODD);
			itemsGrid.add(card, 0, row);
			row++;
		}
		
		itemsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
	}
	
	private enum Parity{
		ODD,
		EVEN;
	}
	
	
	/**
	 * Creates a cart item card for a given item and card placement parity.
	 *
	 * @param item The item to be displayed on the card.
	 * @param cardPlaceParity The parity of the card placement (even or odd).
	 * @return The populated AnchorPane representing the cart item card.
	 */
	private AnchorPane createCartItemCard(ItemInOrder item, Parity cardPlaceParity) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/CartItemCard.fxml"));
		AnchorPane card = null;
        try {
            card = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if(cardPlaceParity == Parity.ODD) {
        	/*NEED TO CHANGE BACKGROUND COLOR*/
        }
        
        //Labels
        Label nameAndPrice = (Label) card.lookup("#nameAndPrice");
        Label qty = (Label) card.lookup("#qty");
        Label choices = (Label) card.lookup("#choices");
        Label restrictions = (Label) card.lookup("#restrictions");
        
        //Labels adjustments
        nameAndPrice.setText(item.getName() + " (" + item.getPrice() + "₪)");
        qty.setText("Qty: 1");
        if(item.getCustomSize() && item.getCustomDonenessDegree())
        	choices.setText("Size: " + item.getSize().charAt(0) + ", " + "Doneness: " + item.getDonenessDegree());
        else if(item.getCustomSize()) 
        	choices.setText("Size: " + item.getSize().charAt(0));
        else if(item.getCustomDonenessDegree())
        	choices.setText("Doneness: " + item.getDonenessDegree());
        else
        	choices.setText("Size: N/A, Doneness: N/A");
        if(item.getCustomRestrictions())
        	restrictions.setText("Extras/Removals: " + item.getRestrictions());
        else
        	restrictions.setText("Extras/Removals: N/A");
        
        ImageView add = (ImageView) card.lookup("#add");
        ImageView reduce = (ImageView) card.lookup("#reduce");
        ImageView edit = (ImageView) card.lookup("#edit");
        
        add.setVisible(false);
        reduce.setVisible(false);
        edit.setVisible(false);
        return card;
	}
	
	
	/**
	 * Navigates back to the Checkout Screen.
	 *
	 * @throws IOException If there is an issue loading the FXML file for the Checkout Screen.
	 */
	@FXML
	private void goBack() throws IOException {
		ScreenLoader screenLoader = new ScreenLoader();
		String path = "/gui/view/CheckoutScreen.fxml";
		AnchorPane prevDash = screenLoader.loadPreviousDashboard(path, Screen.CHECKOUT_SCREEN, prevController);
		dashboard.getChildren().clear();
		dashboard.getChildren().add(prevDash);
	}
	
	
	
	/**
	 * Handles the event when the "Approve Order" button is clicked.
	 *
	 * Updates the server with the current order and items in the cart.
	 * Checks the server response to handle any errors or success messages.
	 * If the order is successfully updated, updates the customer's wallet balance
	 * Displays a "Thank You" screen to the user if the order is approved.
	 * 
	 * @param event The action event triggered by clicking the "Approve Order" button.
	 * @throws IOException If there is an issue communicating with the server or loading the Thank You screen.
	 */
    @FXML
	void approveOrderClicked(ActionEvent event) throws IOException {
//    	LocalTime currentTime = LocalTime.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//        String formattedTime = currentTime.format(formatter);
//    	order.setApprovalTimer((formattedTime));

		// CALL SERVER UPDATE ORDER AND ITEMS_IN_ORDER
		List<Object> list = new ArrayList<>();
		list.add(order);
		list.add(cart);
		ClientMainController.updateOrderAndItems(list); // update the order and items_in_order DB rows
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		switch (response.getResponse()) {
		case ITEM_WAS_DELETED:
			String msg = (String) response.getMessage();
			showAlert("Error", msg);
			backToRestaurant();
			break;

		case UPDATED_ORDER_ID:
			int orderId = (int) response.getMessage();
			order.setOrderID(orderId);
			this.orderID = orderId;

			// update here the customer wallet in the if stetmant
			if (walletUsedAmount != 0) {
				List<Object> orderAndWallet = new ArrayList<>();
				orderAndWallet.add(order);
				orderAndWallet.add(walletUsedAmount);
				ClientMainController.updateCustomerWallet(orderAndWallet);
				float updatedWalletBalance = customer.getWalletBalance() - walletUsedAmount;
				customer.setWalletBalance(updatedWalletBalance);
			}

			ThankYouScreen();
			break;
		}
	}
    
    /**
     * Displays the "Thank You" screen.
     * 
     * @throws IOException If there is an issue loading the "Thank You" screen FXML file or accessing the dashboard.
     */
	@FXML
	private void ThankYouScreen() throws IOException {
		ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/ThankYouScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(wholeScreen, path, Screen.THANK_YOU_SCREEN, this);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
	
	
	/**
	 * Navigates back to the "Restaurant Menu" screen.
	 *
	 * @throws IOException If there is an issue loading the "Restaurant Menu" screen FXML file or accessing the dashboard.
	 */
	@FXML
	private void backToRestaurant() throws IOException {
		ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/RestaurantMenuScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(wholeScreen, path, Screen.RESTAURANT_MENU_SCREEN, this.restaurantController);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
	
	/**
	 * Shows an alert with the specified title and message to provide feedback to the user
	 * regarding errors or important information.
	 *
	 * @param title   the title of the alert.
	 * @param message the content of the alert message.
	 */
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public int getOrderID() {
		return orderID;
	}
}
	
