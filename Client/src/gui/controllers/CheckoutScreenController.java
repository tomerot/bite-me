package gui.controllers;

import java.io.IOException;

import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class CheckoutScreenController {
	
	@FXML
	private AnchorPane dashboard;
	
	private HBox wholeScreen;
	RestaurantMenuScreenController prevController;
	
	public CheckoutScreenController(HBox wholeScreen, Object prevController) {
		this.wholeScreen = wholeScreen;
		this.prevController = (RestaurantMenuScreenController) prevController;
	}
	
	@FXML
	private void pressBack() throws IOException {
		ScreenLoader screenLoader = new ScreenLoader();
		String path = "/gui/view/RestaurantMenuScreen.fxml";
		AnchorPane prevDash = screenLoader.loadPreviousDashboard(path, Screen.RESTAURANT_MENU_SCREEN, prevController);
		dashboard.getChildren().clear();
    	dashboard.getChildren().add(prevDash);
	}
}
