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

import client.ClientMainController;
import entities.Customer;
import entities.ItemInOrder;
import entities.Order;
import entities.Supplier;
import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class OrderSummaryScreenController {
	
	private Map<ItemInOrder, Integer> cart = new HashMap<>();
	private Customer customer;
	private Supplier supplier;
	private Order order;
	private float walletUsedAmount;
	private int peopleInOrder;
	
	
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
    private Label refundWalletLbl;

    @FXML
    private Label totalPriceLbl;
	
	public OrderSummaryScreenController(HBox wholeScreen, Object prevController) {
		this.wholeScreen = wholeScreen;
		this.prevController = (CheckoutScreenController) prevController;
		this.cart = this.prevController.getCart();
		this.customer = this.prevController.getCustomer();
		this.supplier = this.prevController.getSupplier();
		this.order = this.prevController.getOrder();
		this.walletUsedAmount = this.prevController.getWalletAmount();
		this.peopleInOrder = this.prevController.getPeopleInOrder();
	}
	
	
	@FXML
	private void initialize() {
		//initialize all the labels
		nameLbl.setText(order.getRecipient());
		phoneLbl.setText(order.getRecipientPhone());
		cityLbl.setText(order.getCity());
		addressLbl.setText(order.getAddress());
		supplyMethodLbl.setText(String.format("%s + participents: %d",order.getSupplyOption(),peopleInOrder));
		requestedDateLbl.setText(order.getRequestedDate());
		requestedTimeLbl.setText(order.getRequestedTime());
		orderTypeLbl.setText(order.getType().toString());
		refundWalletLbl.setText(String.format("%.2f₪", walletUsedAmount));
		totalPriceLbl.setText(String.format("%.2f₪", order.getTotalPrice()));
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
    void approveOrderClicked(ActionEvent event) {
    	LocalTime currentTime = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        
    	System.out.println(formattedTime);
    	order.setApprovalTimer((formattedTime));
    	
	    //CALL SERVER UPDATE ORDER AND ITEMS_IN_ORDER
	    List<Object> list = new ArrayList<>();
	    list.add(order);
	    list.add(cart);
	    ClientMainController.updateOrderAndItems(list); //update the order and items_in_order DB rows
	    
	    //update here the customer wallet in the if stetmant
	    if (walletUsedAmount != 0 ) {
	    	
	    }
	    
	    
	    
	    
	    
	    
    }
	
}
	
