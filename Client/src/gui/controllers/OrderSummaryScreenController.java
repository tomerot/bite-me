package gui.controllers;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class OrderSummaryScreenController {
	
	private ChooseRestaurantScreenController restaurantController;
	
	private Map<ItemInOrder, Integer> cart = new HashMap<>();
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
	}
	
	
	
	@FXML
	private void goBack() throws IOException {
		ScreenLoader screenLoader = new ScreenLoader();
		String path = "/gui/view/CheckoutScreen.fxml";
		AnchorPane prevDash = screenLoader.loadPreviousDashboard(path, Screen.CHECKOUT_SCREEN, prevController);
		dashboard.getChildren().clear();
		dashboard.getChildren().add(prevDash);
	}
	
	
	
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
    
    
	@FXML
	private void ThankYouScreen() throws IOException {
		ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/ThankYouScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(wholeScreen, path, Screen.THANK_YOU_SCREEN, this);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
	
	
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
	
