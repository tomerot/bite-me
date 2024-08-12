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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;


/**
 * Controller for the screen where a supplier can view and manage orders.
 * This includes refreshing the list of awaiting orders, approving orders,
 * and updating orders to "Ready".
 */
public class ViewSupplierOrdersScreenController implements Initializable{
	
	private User user;
	private SupplierScreenController prevController;
	private HBox wholeScreen;
	private Supplier supplier;
	private Order selectedOrder;
	
	
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
	
    @FXML
    private TextField arrivalTimeField;
	
	private Map<Order, ArrayList<ItemInOrder>> awaitingOrdersMap = new HashMap<>(); // key is the order object , value is the items list of the order.	
	private Map<Order, ArrayList<ItemInOrder>> approvedOrdersMap = new HashMap<>(); // key is the order object , value is the items list of the order.
 
	
	 /**
     * Constructs a ViewSupplierOrdersScreenController with the specified parameters.
     *
     * @param wholeScreen the HBox representing the whole screen
     * @param prevController the previous controller (SupplierScreenController)
     */
	public ViewSupplierOrdersScreenController(HBox wholeScreen , Object prevController) {
		this.prevController = (SupplierScreenController) prevController;
		this.wholeScreen = wholeScreen;
	}
	
	 /**
     * Sets the user for this controller.
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
    
    /**
     * Gets the current user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }
    
	
    /**
     * Initializes the controller. Retrieves orders and sets up the UI components.
     *
     * @param location the location used to resolve relative paths for the root object, or null
     * @param resources the resources used to localize the root object, or null
     */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		this.supplier = SupplierScreenController.getSupplier();
		
		// Set the TextArea to be read-only
		awaitingOrderTextArea.setEditable(false);
		approvedOrderTextArea.setEditable(false);
		
		//get all the orders with "Awaiting" or "Approved" status of the restaurant.
		ClientMainController.requestOrdersData(supplier.getSupplierID());
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		
		Map<Order, ArrayList<ItemInOrder>> ordersMap =  (Map<Order, ArrayList<ItemInOrder>>) response.getMessage();
		
		//let's divide the orders to awaiting and approved
		initMaps(ordersMap);
		
		//initializeListsView
		initListView( awaitingOrdersList, awaitingOrdersMap);
		initListView( approvedOrdersList, approvedOrdersMap);
		
		// Set up selection listeners
        setupSelectionListener(awaitingOrdersList, awaitingOrdersMap, awaitingOrderTextArea);
        setupSelectionListener(approvedOrdersList, approvedOrdersMap, approvedOrderTextArea);
	}

	
	
	/**
     * Sets up a selection listener for the given ListView. Displays order details in the TextArea when an item is selected.
     *
     * @param listView the ListView to set up the listener for
     * @param ordersMap the map of orders
     * @param textArea the TextArea to display order details
     */
	private void setupSelectionListener(ListView<Integer> listView, Map<Order, ArrayList<ItemInOrder>> ordersMap, TextArea textArea) {
			listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {  // When the user selects an OrderID in the ListView, the listener is triggered.
				if(listView.getItems().isEmpty())
					// Clear the TextArea if the ListView is empty
		            textArea.clear();
				
				else if (newValue != null) {
					for (Order order : ordersMap.keySet()) {
						if (order.getOrderID() == newValue) {
							selectedOrder = order;
							displayOrderDetails(order, ordersMap.get(order), textArea);
							break;
						}
					}
				}
			});
	}
	
	 /**
     * Displays details of the specified order in the TextArea.
     *
     * @param order the order to display
     * @param items the items in the order
     * @param textArea the TextArea to display the details
     */
	private void displayOrderDetails(Order order, ArrayList<ItemInOrder> items, TextArea textArea) {
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
        textArea.setText(details.toString());
    }
	
	 /**
     * Initializes the ListView with the provided orders map.
     *
     * @param ordersList the ListView to initialize
     * @param ordersMap the map of orders
     */
	void initListView(ListView<Integer> ordersList, Map<Order, ArrayList<ItemInOrder>> ordersMap) {
		ObservableList<Integer> oList = FXCollections.observableArrayList();
		
		for (Order order : ordersMap.keySet())
			oList.add(order.getOrderID());
		
		// update the listView and set its items
		ordersList.setItems(oList); 
	}

	/**
     * Initializes the awaiting and approved orders maps.
     *
     * @param ordersMap the map of orders
     */
	public void initMaps(Map<Order, ArrayList<ItemInOrder>> ordersMap) {
		for (Order order : ordersMap.keySet()) { //iterate on the keys of the map
            
            if("Awaiting".equals(order.getStatus())) 
            	awaitingOrdersMap.put(order, ordersMap.get(order));

		   	else 
		   		approvedOrdersMap.put(order, ordersMap.get(order)); 
		}
		
	}
	
	
	/**
     * Handles the event when the approve button is clicked. Updates the status of the selected order to "Approved".
     *
     * @param event the action event
     * @throws Exception if updating the order status fails
     */
	@FXML
	private void onApproveClicked(ActionEvent event) throws Exception{
		Integer selectedOrderID = awaitingOrdersList.getSelectionModel().getSelectedItem();
		
		// Check if any order is selected
		if(selectedOrderID == null) {
			resultMessage.setText("No item selected.");	
        	return;
		}

		 // Update the database status to 'Approved' and set the approval time
	    int[] orderInfo = {selectedOrderID, 0 , 0}; // 0 indicates transition from Awaiting to Approved
		ClientMainController.requestSupplierUpdateOrderStatus(orderInfo);
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		
		
		if(ServerResponse.SUPPLIER_UPDATE_ORDER_STATUS_SUCCESS.equals(response.getResponse())) {
			//let's move the order to the approvedOrders map and list view.
			for (Order order : awaitingOrdersMap.keySet()) { //iterate on the keys of the map
				if(order.getOrderID() == selectedOrderID) {

					order.setApprovalTime(response.getMessage().toString());
					order.setStatus("Approved");
					
					//update maps
					approvedOrdersMap.put(order, awaitingOrdersMap.get(order)); //add to approved map
					awaitingOrdersMap.remove(order); //remove from awaiting map
					
					//update lists view
					awaitingOrdersList.getItems().remove(selectedOrderID);
					approvedOrdersList.getItems().add(selectedOrderID);
					
					resultMessage.setText("The order was updated to 'Approved' successfully\n");
					resultMessage.setStyle("-fx-text-fill: black;");
					break;
				}
			}
		}
		else {
			resultMessage.setText("failed to update order because error in db");	
			resultMessage.setStyle("-fx-text-fill: red;");
		}
		
		String customerIDString = selectedOrder.getCustomerID();
	    String msg = "Email: " + selectedOrder.getRecipientEmail() + "\nPhone: " + selectedOrder.getRecipientPhone() + "\norder was approved!";
	    sendOrderStatusUpdate(customerIDString, msg, "Order Approved Simulation", "Order Approved");
	}
	
	/**
     * Handles the event when the refresh button is clicked. Fetches the latest awaiting orders and updates the list.
     *
     * @param event the action event
     * @throws Exception if refreshing the orders fails
     */
	@FXML
	private void onRefreshClicked(ActionEvent event) throws Exception{ //we want to fetch from the database all the orders with status 'Awaiting' and to update the awaiting map and list view.
		ClientMainController.requestSupplierRefreshAwaitingOrders(supplier.getSupplierID());
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		awaitingOrdersMap = (Map<Order, ArrayList<ItemInOrder>>) response.getMessage();
		
		//update the awaiting list view
		initListView( awaitingOrdersList, awaitingOrdersMap);
		
		// Reattach the selection listener to the updated ListView
	    setupSelectionListener(awaitingOrdersList, awaitingOrdersMap, awaitingOrderTextArea);
	}
	
	/**
     * Handles the event when the update ready button is clicked. Updates the status of the selected order to "Ready".
     *
     * @param event the action event
     * @throws Exception if updating the order status fails
     */
	@FXML
	private void onUpdateReadyClicked(ActionEvent event) throws Exception {
		boolean isTakeAway = false;
		String msg;
		String timeInput = arrivalTimeField.getText().trim();
		Integer selectedOrderID = approvedOrdersList.getSelectionModel().getSelectedItem();
		
		// Check if any order is selected
		if (selectedOrderID == null) {
			resultMessage.setText("No item selected.");
			return;
		}

		// Update the database status to 'Ready'.
		int[] orderInfo = { selectedOrderID, 1, 0 }; // 1 indicates transition from 'Approved' to 'Ready'

		for (Order specificOrder : approvedOrdersMap.keySet()) { // iterate on the keys of the map
			if (specificOrder.getOrderID() == selectedOrderID) {
				if (specificOrder.getSupplyOption().toString().equals("TakeAway")) {
					isTakeAway = true;
					orderInfo[2] = 1; // indicates takeaway
					break;
				} else {
					orderInfo[2] = 0; // indicates not takeaway
					break;
				}
			}
		}
		
		if (!isTakeAway) {
			if (!isValidTimeFormat(timeInput)) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Invalid Time Format");
				alert.setHeaderText(null);
				alert.setContentText("Please enter a valid time in HH:mm format.");
				alert.showAndWait();

				arrivalTimeField.clear();
				arrivalTimeField.requestFocus();
				return;
			}
		}
		

		ClientMainController.requestSupplierUpdateOrderStatus(orderInfo);
		ServerResponseDataContainer response = ClientConsole.responseFromServer;

		if (ServerResponse.SUPPLIER_UPDATE_ORDER_STATUS_SUCCESS.equals(response.getResponse())) {
			// let's remove the order from the approvedOrders map and list view.
			for (Order order : approvedOrdersMap.keySet()) { // iterate on the keys of the map
				if (order.getOrderID() == selectedOrderID) {
					// update map
					approvedOrdersMap.remove(order);

					// update lists view
					approvedOrdersList.getItems().remove(selectedOrderID);

					resultMessage.setText("The order was updated to 'Ready' successfully\n");
					resultMessage.setStyle("-fx-text-fill: black;");
					break;
				}
			}
		} else {
			resultMessage.setText("failed to update order because error in db");
			resultMessage.setStyle("-fx-text-fill: red;");
		}
		
		String customerIDString = selectedOrder.getCustomerID();
		if (isTakeAway) {
			msg = "Email: " + selectedOrder.getRecipientEmail() + "\nPhone: " + selectedOrder.getRecipientPhone()
            + "\nYour order is on the way!";
		}
		else {
		    msg = "Email: " + selectedOrder.getRecipientEmail() + "\nPhone: " + selectedOrder.getRecipientPhone()
            + "\nYour order is on the way!\nEstimated arrival time: " + timeInput;
		}
	    sendOrderStatusUpdate(customerIDString, msg, "Order Ready Simulation", "Order is Ready and on the way");
	}
	
	private void sendOrderStatusUpdate(String customerIDString, String message, String title, String header) {
	    int customerID = Integer.parseInt(customerIDString);
	    ArrayList<Object> data = new ArrayList<>();
	    data.add(customerID);
	    data.add(message);
	    
	    ClientMainController.sendApproveReadyOrderToClient(data);
	    ServerResponseDataContainer res = ClientConsole.responseFromServer;
	    
	    if (ServerResponse.MSG_WAS_SENT.equals(res.getResponse())) {
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle(title);
	        alert.setHeaderText(header);
	        alert.setContentText(message);
	        alert.showAndWait();
	    } else {
	        problemAlert();
	    }
	}
	
	private void problemAlert() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Order Simulation");
		alert.setHeaderText("Simulation");
		alert.setContentText("there was a problem");
		alert.showAndWait();
	}
	
	private boolean isValidTimeFormat(String time) {
	    // Check if the string is exactly 5 characters long
	    if (time.length() != 5) {
	        return false;
	    }

	    // Check each character
	    for (int i = 0; i < 5; i++) {
	        char c = time.charAt(i);
	        if (i == 2) {
	            // Check for a colon at position 2
	            if (c != ':') {
	                return false;
	            }
	        } else {
	            // Check for digits at all other positions
	            if (!Character.isDigit(c)) {
	                return false;
	            }
	        }
	    }

	    // Extract the hour and minute values
	    int hours = Integer.parseInt(time.substring(0, 2));
	    int minutes = Integer.parseInt(time.substring(3, 5));

	    // Check if hours and minutes are within valid ranges
	    if (hours < 0 || hours > 23) {
	        return false;
	    }
	    if (minutes < 0 || minutes > 59) {
	        return false;
	    }

	    // If we've made it this far, the format is correct and the values are valid
	    return true;
	}
}