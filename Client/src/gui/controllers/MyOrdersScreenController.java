package gui.controllers;

import java.util.ArrayList;
import java.util.List;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.Customer;
import entities.Order;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller class for the "My Orders" screen in the application.
 * This class handles the display and interaction with the user's orders.
 */
public class MyOrdersScreenController {

	private CustomerHomeScreenController prevManagerController;
	private HBox wholeScreen;
	private Customer customer;
	private List<Order> waitingOrders; // all ready orders
    private List<Order> historyOrders; // all history orders
	
	@FXML
	private AnchorPane dashboard;
	
    @FXML
    private Accordion approvalAccordion;

    @FXML
    private Accordion historyAccordion;

	
    /**
     * Constructor for MyOrdersScreenController.
     *
     * @param wholeScreen The HBox containing the entire screen layout.
     * @param prevController The previous controller
     */
	public MyOrdersScreenController(HBox wholeScreen, Object prevController) {
		this.prevManagerController = (CustomerHomeScreenController) prevController;
		this.wholeScreen = wholeScreen;
	}
	
    /**
     * Loads all orders (waiting and history) for the current customer.
     */
	public void loadAllOrders() {
		customer = prevManagerController.getCustomer();

		ClientMainController.requestAllCustomerWaitingOrders(customer);
		ServerResponseDataContainer waitingOrdersResponse = ClientConsole.responseFromServer;
		waitingOrders = (List<Order>) waitingOrdersResponse.getMessage();
		
		
		ClientMainController.requestAllCustomerHistoryOrders(customer);
		ServerResponseDataContainer historyOrdersResponse = ClientConsole.responseFromServer;
		historyOrders = (List<Order>) historyOrdersResponse.getMessage();
		
        updateAccordions();
	}
	
    /**
     * Updates the approval and history accordions with the latest orders.
     */
    private void updateAccordions() {
        approvalAccordion.getPanes().clear();
        historyAccordion.getPanes().clear();

        if (waitingOrders != null) {
            for (Order order : waitingOrders) {
                TitledPane titledPane = createApprovalTitledPane(order);
                approvalAccordion.getPanes().add(titledPane);
            }
        }
        
        if (historyOrders != null) {
            for (Order order : historyOrders) {
                TitledPane titledPane = createHistoryTitledPane(order);
                historyAccordion.getPanes().add(titledPane);
            }
        }
    }
	
    /**
     * Creates a TitledPane for a waiting order with approval options.
     *
     * @param order The order to create the pane for.
     * @return A TitledPane with order details and an approval button.
     */
    private TitledPane createApprovalTitledPane(Order order) {
        String title = String.format("Order ID: %d   from: %s - %s", order.getOrderID(), order.getSupplier().getName(), order.getBranchName().toString());
         
        VBox content = new VBox(10);

        content.getChildren().addAll(
            createStyledLabel("Customer fullname: " + customer.getFirstName() + " " + customer.getLastName()),
            createStyledLabel("Recipient info: " + order.getRecipient() + ", " + order.getRecipientPhone()),
            createStyledLabel("Supply Method: " + order.getSupplyOption()),
            createStyledLabel("Status: " + order.getStatus(), order.getStatus()),
            createStyledLabel("Total Price: ₪" + order.getTotalPrice())
        );

        // we only add the approve button if the order status is Ready or Approved
        if (!order.getStatus().equals("Awaiting") && !order.getStatus().equals("Approved")) {
            Button approveButton = new Button("Approve");
            approveButton.setOnAction(e -> approveOrder(order));
            approveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            content.getChildren().add(approveButton);
        }

        return new TitledPane(title, content);
    }

    /**
     * Creates a TitledPane for a historical order.
     *
     * @param order The order to create the pane for.
     * @return A TitledPane with order details.
     */
    private TitledPane createHistoryTitledPane(Order order) {
        String title = String.format("Order ID: %d   from: %s - %s", order.getOrderID(), order.getSupplier().getName(), order.getBranchName().toString());

        VBox content = new VBox(10);
        content.getChildren().addAll(
            createStyledLabel("Customer name: " + customer.getFirstName() + ", " + customer.getLastName()),
            createStyledLabel("Recipient info: " + order.getRecipient() + ", " + order.getRecipientPhone()),
            createStyledLabel("Order Requested Date and time: " + order.getRequestedDate() + " " + order.getRequestedTime()),
            createStyledLabel("Order Approval Date and time: " + order.getApprovalDate() + " " + order.getApprovalTimer()),
            createStyledLabel("Order Arrival Date and time: " + order.getArrivalDate() + ", " + order.getArrivalTime()),
            createStyledLabel("Supply Method and type: " + order.getSupplyOption() + ", " + order.getType()),
            createStyledLabel("Status: " + order.getStatus(), order.getStatus()),
            createStyledLabel("Total Price: ₪" + order.getTotalPrice())
        );

        return new TitledPane(title, content);
    }
    
    /**
     * Creates a styled Label based on the text and optional status.
     *
     * @param text The text to display in the label.
     * @param status Optional status to style the label if provided.
     * @return A styled Label.
     */
    private Label createStyledLabel(String text, String... status) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        if (status.length > 0 && "Awaiting".equals(status[0])) {
            label.setStyle(label.getStyle() + " -fx-text-fill: #FF0000;"); //red
        }
        if (status.length > 0 && "Approved".equals(status[0])) {
        	label.setStyle(label.getStyle() + " -fx-text-fill: #FFD700;"); //gold
        }
        if (status.length > 0 && "Ready".equals(status[0])) {
        	label.setStyle(label.getStyle() + " -fx-text-fill: #90EE90;"); //green
        }

        return label;
    }
    
    
    /**
     * Handles the approval of an order when customer click approve.
     *
     * @param order The order to approve.
     */
    private void approveOrder(Order order) {
    	ClientMainController.requestToUpdateOrder(order);
    	loadAllOrders();
    }
    
    
}
