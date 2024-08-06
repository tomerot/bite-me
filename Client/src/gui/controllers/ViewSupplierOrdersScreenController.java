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
import javafx.scene.control.ListView;
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
	private ListView<String> awaitingOrdersList;
	@FXML
	private ListView<String> approvedOrdersList;
	
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
            
            if("Awaiting".equals(order.getStatus())) 
            	awaitingOrdersMap.put(order, ordersMap.get(order));

		   	else 
		   		ApprovedordersMap.put(order, ordersMap.get(order)); 
		}
	}
	
	
}
