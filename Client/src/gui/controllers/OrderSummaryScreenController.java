package gui.controllers;

import javafx.scene.layout.HBox;

public class OrderSummaryScreenController {

	private HBox wholeScreen;
	private CheckoutScreenController prevController;
	
	public OrderSummaryScreenController(HBox wholeScreen, Object prevController) {
		this.wholeScreen = wholeScreen;
		this.prevController = (CheckoutScreenController) prevController;
	}
	
}
