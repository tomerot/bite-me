package gui.controllers;

import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class MonthlyReportScreenController {
	
	@FXML
	private Button nextPageBtn;
	
	@FXML
	private AnchorPane dashboard;
	
	private CeoHomeScreenController prevController;
	private HBox wholeScreen;
	
	public MonthlyReportScreenController(HBox wholeScreen , Object prevController) {
		this.prevController = (CeoHomeScreenController) prevController;
		this.wholeScreen = wholeScreen;
	}
	
	public void displayNextPage(ActionEvent event) throws Exception {
		ScreenLoader screenLoader = new ScreenLoader();
		AnchorPane nextDash = screenLoader.loadOnDashboard(wholeScreen, "/gui/view/MonthlyReportScreen2.fxml", Screen.MONTHLY_REPORT_SCREEN_TWO, this, null);
		dashboard.getChildren().clear();
		dashboard.getChildren().add(nextDash);
	}
	
	public void goBack(ActionEvent event) throws Exception {
		ScreenLoader screenLoader = new ScreenLoader();
		String path = "/gui/view/CeoScreen.fxml";
		HBox prevWholeScreen = screenLoader.loadPreviousScreen(path, Screen.CEO_SCREEN, prevController);
		wholeScreen.getChildren().clear();
		wholeScreen.getChildren().add(prevWholeScreen);
	}
	
}
