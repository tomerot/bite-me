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
import enums.ServerResponse;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
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
	private Button btnUpdateReady;
	
	@FXML
	private ListView<Integer> awaitingOrdersList;
	
	@FXML
	private ListView<Integer> approvedOrdersList;
	
	@FXML
	private TextArea awaitingOrderTextArea;
	
	@FXML
	private TextArea approvedOrderTextArea;
	
	@FXML
	private Label resultMessage;
	
	private Map<Order, ArrayList<ItemInOrder>> awaitingOrdersMap = new HashMap<>(); // key is the order object , value is the items list of the order.	
	private Map<Order, ArrayList<ItemInOrder>> approvedordersMap = new HashMap<>(); // key is the order object , value is the items list of the order.
 
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		this.supplier = SupplierScreenController.getSupplier();
		
		// Set the TextArea to be read-only
		awaitingOrderTextArea.setEditable(false);
		approvedOrderTextArea.setEditable(false);
		
		//get all the orders of the restaurant.
		ClientMainController.requestOrdersData(supplier.getSupplierID()); //we retrieve just orders with "Awaiting" or "Approved" status.
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		Map<Order, ArrayList<ItemInOrder>> ordersMap = (Map<Order, ArrayList<ItemInOrder>>) response.getMessage();
		System.out.println("in controller: " + ordersMap);
		
		//let's divide the orders to awaiting and approved
		initMaps(ordersMap);
		
		//initializeListsView
		initListView( awaitingOrdersList, awaitingOrdersMap);
		initListView( approvedOrdersList, approvedordersMap);
		
		// Set up selection listeners
        setupSelectionListener(awaitingOrdersList, awaitingOrdersMap, awaitingOrderTextArea);
        setupSelectionListener(approvedOrdersList, approvedordersMap, approvedOrderTextArea);
	}

	//when user select row from the list we want to show the list details.
	private void setupSelectionListener(ListView<Integer> listView, Map<Order, ArrayList<ItemInOrder>> ordersMap, TextArea textArea) {
		System.out.println("In setup selection listener");
			listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {  // When the user selects an OrderID in the ListView, the listener is triggered.
				if(listView.getItems().isEmpty())
					// Clear the TextArea if the ListView is empty
		            textArea.clear();
				
				else if (newValue != null) {
					for (Order order : ordersMap.keySet()) {
						if (order.getOrderID() == newValue) {
							displayOrderDetails(order, ordersMap.get(order), textArea);
							break;
						}
					}
				}
			});
	}
	
	private void displayOrderDetails(Order order, ArrayList<ItemInOrder> items, TextArea textArea) {
		System.out.println("inside Display Order details");
        StringBuilder details = new StringBuilder();
        details.append("Order ID: ").append(order.getOrderID()).append("\n")
        	   .append("Customer ID: ").append(order.getCustomerID()).append("\n")
               .append("Recipient Name: ").append(order.getRecipient()).append("\n")
               .append("Recipient Phone: ").append(order.getRecipientPhone()).append("\n")
               .append("Recipient Email: ").append(order.getRecipientEmail()).append("\n")
               .append("City: ").append(order.getCity()).append("\n")
               .append("Address: ").append(order.getAddress()).append("\n")
               .append("Supply Option: ").append(order.getSupplyOption()).append("\n")
               .append("Order Type: ").append(order.getType()).append("\n")
               .append("Requested Date: ").append(order.getRequestedDate()).append("\n")
               .append("Requested Time: ").append(order.getRequestedTime()).append("\n");
               if("Approved".equals(order.getStatus()))
       			details.append("Approval Time: ").append(order.getApprovalTime()).append("\n");
               details.append("Status: ").append(order.getStatus()).append("\n")
               .append("Total Price: ").append(order.getTotalPrice()).append("\n\n");

        details.append("Items:\n");
        for (ItemInOrder item : items) {
            details.append("\tItem Name: ").append(item.getName()).append("\n")
                   .append("\tSize: ").append(item.getSize()).append("\n")
                   .append("\tDoneness Degree: ").append(item.getDonenessDegree()).append("\n")
                   .append("\tRestrictions: ").append(item.getRestrictions()).append("\n")
                   .append("\tPrice Per Unit: ").append(item.getPrice()).append("\n")
                   .append("\tQuantity: ").append(item.getQuantity()).append("\n\n");
        }
        System.out.println("before set text area");
        System.out.println(details.toString());
        textArea.setText(details.toString());
    }
	
	void initListView(ListView<Integer> ordersList, Map<Order, ArrayList<ItemInOrder>> ordersMap) {
		ObservableList<Integer> oList = FXCollections.observableArrayList();
		
		for (Order order : ordersMap.keySet())
			oList.add(order.getOrderID());
		
		// update the listView and set its items
		ordersList.setItems(oList); 
	}

	public void initMaps(Map<Order, ArrayList<ItemInOrder>> ordersMap) {
		for (Order order : ordersMap.keySet()) { //iterate on the keys of the map
            
            if("Awaiting".equals(order.getStatus())) 
            	awaitingOrdersMap.put(order, ordersMap.get(order));

		   	else 
		   		approvedordersMap.put(order, ordersMap.get(order)); 
		}
		
		System.out.println("awaiting map: " + awaitingOrdersMap);
		System.out.println("approved map: " + approvedordersMap);
	}
	
	@FXML
	private void onApproveClicked(ActionEvent event) throws Exception{
		Integer selectedOrderID = awaitingOrdersList.getSelectionModel().getSelectedItem();
		
		// Check if any order is selected
		if(selectedOrderID == null) {
			resultMessage.setText("No item selected.");	
        	return;
		}

		 // Update the database status to 'Approved' and set the approval time
	    int[] orderInfo = {selectedOrderID, 0}; // 0 indicates transition from Awaiting to Approved
		ClientMainController.requestSuppleriUpdateOrderStatus(orderInfo);
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		
		
		if(ServerResponse.SUPPLIER_UPDATE_ORDER_STATUS_SUCCESS.equals(response.getResponse())) {
			//let's move the order to the approvedOrders map and list view.
			for (Order order : awaitingOrdersMap.keySet()) { //iterate on the keys of the map
				if(order.getOrderID() == selectedOrderID) {

					order.setApprovalTime(response.getMessage().toString());
					order.setStatus("Approved");
					System.out.println("updated order:" +order.getApprovalTime() + order.getStatus());
					
					//update maps
					approvedordersMap.put(order, awaitingOrdersMap.get(order)); //add to approved map
					awaitingOrdersMap.remove(order); //remove from awaiting map
					
					//update lists view
					awaitingOrdersList.getItems().remove(selectedOrderID);
					approvedOrdersList.getItems().add(selectedOrderID);
					
					resultMessage.setText("The order was updated to 'Approved' successfully\n" +
                            "An SMS was sent to the phone number: " + order.getRecipientPhone() + "\n" +
                            "An email was sent to: " + order.getRecipientEmail());
					resultMessage.setStyle("-fx-text-fill: black;");
					break;
				}
			}
		}
		else {
			resultMessage.setText("failed to update order because error in db");	
			resultMessage.setStyle("-fx-text-fill: red;");
		}
	}
	
	@FXML
	private void onRefreshClicked(ActionEvent event) throws Exception{ //we want to fetch from the database all the orders with status 'Awaiting' and to update the awaiting map and list view.
		ClientMainController.requestSupplerRefreshAwaitingOrders(supplier.getSupplierID());
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		awaitingOrdersMap = (Map<Order, ArrayList<ItemInOrder>>) response.getMessage();
		System.out.println("After refresh, awaiting orders: " + awaitingOrdersMap);
		
		//update the awaiting list view
		initListView( awaitingOrdersList, awaitingOrdersMap);
		
		// Reattach the selection listener to the updated ListView
	    setupSelectionListener(awaitingOrdersList, awaitingOrdersMap, awaitingOrderTextArea);
	}
	
	@FXML
	private void onUpdateReadyClicked(ActionEvent event) throws Exception{
		Integer selectedOrderID = approvedOrdersList.getSelectionModel().getSelectedItem();
		
		// Check if any order is selected
		if(selectedOrderID == null) {
			resultMessage.setText("No item selected.");	
        	return;
		}
		
		 // Update the database status to 'Ready'.
	    int[] orderInfo = {selectedOrderID, 1}; // 1 indicates transition from 'Approved' to 'Ready'
		ClientMainController.requestSuppleriUpdateOrderStatus(orderInfo);
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		
		if(ServerResponse.SUPPLIER_UPDATE_ORDER_STATUS_SUCCESS.equals(response.getResponse())) {
			//let's remove the order from the approvedOrders map and list view.
			for (Order order : approvedordersMap.keySet()) { //iterate on the keys of the map
				if(order.getOrderID() == selectedOrderID) {	
					//update map
					approvedordersMap.remove(order); 
					
					//update lists view
					approvedOrdersList.getItems().remove(selectedOrderID);
					
					resultMessage.setText("The order was updated to 'Ready' successfully\n" +
                            "An SMS was sent to the phone number: " + order.getRecipientPhone() + "\n" +
                            "An email was sent to: " + order.getRecipientEmail());
					resultMessage.setStyle("-fx-text-fill: black;");
					break;
				}
			}
		}
		else {
			resultMessage.setText("failed to update order because error in db");	
			resultMessage.setStyle("-fx-text-fill: red;");
		}
	}
}
