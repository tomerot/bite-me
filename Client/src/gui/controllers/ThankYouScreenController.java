package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ThankYouScreenController {

	
	private HBox wholeScreen;
	private OrderSummaryScreenController prevController;
	private int orderID;
	
    @FXML
    private Label orderIDLbl;
	
    /**
     * Constructor for ThankYouScreenController.
     * Initializes the thank you screen with the whole screen container and the previous screen's controller.
     *
     * @param wholeScreen The main container (HBox) where the thank you screen will be displayed.
     * @param prevController The controller of the previous screen (OrderSummaryScreenController).
     */
	public ThankYouScreenController(HBox wholeScreen, Object prevController) {
		this.wholeScreen = wholeScreen;
		this.prevController = (OrderSummaryScreenController) prevController;
		this.orderID = this.prevController.getOrderID();
	}
	
	/**
	 * Sets the order ID label text with the order ID retrieved from the previous screen.
	 */
	@FXML
	private void initialize() {
		orderIDLbl.setText(String.format("Order ID: #%s",orderID));
	}
}
