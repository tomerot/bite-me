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
	
	public ThankYouScreenController(HBox wholeScreen, Object prevController) {
		this.wholeScreen = wholeScreen;
		this.prevController = (OrderSummaryScreenController) prevController;
		this.orderID = this.prevController.getOrderID();
	}
	
	@FXML
	private void initialize() {
		orderIDLbl.setText(String.format("Order ID: #%s",orderID));
	}
}
