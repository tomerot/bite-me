package gui.controllers;

import java.awt.ScrollPane;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.AuthorizedEmployee;
import entities.ItemInOrder;
import entities.Order;
import entities.Supplier;
import entities.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class ViewSupplierOrdersScreenController implements Initializable{
	
	private User user;
	private SupplierScreenController prevController;
	private HBox wholeScreen;
	private Supplier supplier;
	
	public ViewSupplierOrdersScreenController(HBox wholeScreen , Object prevController) {
		this.prevController = (SupplierScreenController) prevController;
		this.wholeScreen = wholeScreen;
	}
	
	public void setUser(User user) {
        this.user = user;
    }
	
	public User getUser() {
		return user;
	}
	
	@FXML
	private Button btnRefresh;
	
	@FXML
	private Button btnApprove;
	
	@FXML
	private Accordion awaitingAccordion = new Accordion();
	@FXML
	private Accordion approvedAccordion = new Accordion();
	
	@FXML
	ScrollPane awaitingScrollPane = new ScrollPane();
	
	@FXML
	ScrollPane approvedScrollPane = new ScrollPane();
	
	
	private Map<Order, ArrayList<ItemInOrder>> awaitingOrdersMap = new HashMap<>(); // key is the order object , value is the items list of the order.	
	private Map<Order, ArrayList<ItemInOrder>> ApprovedordersMap = new HashMap<>(); // key is the order object , value is the items list of the order.
 
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		this.supplier = SupplierScreenController.getSupplier();
		//get all the orders of the restaurant.
		ClientMainController.requestOrdersData(supplier.getSupplierID()); //we retrieve just orders with "Awaiting" or "Approved" status.
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		Map<Order, ArrayList<ItemInOrder>> ordersMap = (Map<Order, ArrayList<ItemInOrder>>) response.getMessage();
		System.out.println("in controller: " + ordersMap);
		
		//let's divide the orders to awaiting and approved
		for (Order order : ordersMap.keySet()) { //iterate on the keys of the map
		 // Create a pane for the order details
            FlowPane orderDetailsPane = createOrderDetailsPane(order, ordersMap.get(order)); //send the order and it's items list.
            
            // Create a TitledPane for each order
            TitledPane orderPane = new TitledPane(
                "Order " + order.getOrderID() + " - " + order.getRecipient() + " - " + order.getRequestedDate(), orderDetailsPane);
            
            if("Awaiting".equals(order.getStatus())) {
            	awaitingOrdersMap.put(order, ordersMap.get(order));
            	awaitingAccordion.getPanes().add(orderPane);      // Add the TitledPane to the Accordion
            	System.out.println("wait Order");
            }
		   		
		   	else {
		   		ApprovedordersMap.put(order, ordersMap.get(order));
		   		approvedAccordion.getPanes().add(orderPane);      // Add the TitledPane to the Accordion
		   		System.out.println("approved Order");
		   	}    
		}
		 // Wrap Accordion in a ScrollPane

	}
	
	private FlowPane createOrderDetailsPane(Order order, ArrayList<ItemInOrder> itemsInOrder) {
		System.out.println("createOrderDetailsPane");
        FlowPane pane = new FlowPane();
        pane.setOrientation(Orientation.VERTICAL);
        pane.setVgap(10);
        pane.setHgap(10);

        // Add order details
        pane.getChildren().addAll(
            new Label("Order ID: " + order.getOrderID() + "\n"),
            new Label("Recipient: " + order.getRecipient()),
            new Label("Phone: " + order.getRecipientPhone()),
            new Label("City: " + order.getCity()),
            new Label("Address: " + order.getAddress()),
            new Label("Supply Option: " + order.getSupplyOption()),
            new Label("Type: " + order.getType()),
            new Label("Request Date: " + order.getRequestedDate()),
            new Label("Request Time: " + order.getRequestedTime()),
            new Label("Total Price: " + order.getTotalPrice())
        );

        // Add the items_in_order details
        for (ItemInOrder item : itemsInOrder) {
            pane.getChildren().addAll(
                new Label("Item ID: " + item.getItemID()),
                new Label("Item Name: " + item.getName()),
                new Label("Size: " + item.getSize()),
                new Label("Doneness: " + item.getDonenessDegree()),
                new Label("Restrictions: " + item.getRestrictions()),
                new Label("Quantity: " + item.getQuantity()),
                new Label("Price per unit: " + item.getPrice())
            );
        }
        return pane;
	}
}
