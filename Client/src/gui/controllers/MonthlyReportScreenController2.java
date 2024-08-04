package gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;


/*NO NEED FOR THIS CONTROLLER, JUST AN EXAMPLE*/
public class MonthlyReportScreenController2 {
	
	@FXML
	private Button backBtn;
	
	@FXML
	private AnchorPane dashboard;
	
	private MonthlyReportScreenController prevController;
	private HBox wholeScreen;
	
	public MonthlyReportScreenController2(HBox wholeScreen , Object prevController) {
		this.wholeScreen = wholeScreen;
		this.prevController = (MonthlyReportScreenController) prevController;
	}
	
	public void pressBack(ActionEvent event) throws Exception {
		ScreenLoader screenLoader = new ScreenLoader();
		String path = "/gui/view/MonthlyReportScreen.fxml";
		AnchorPane prevDash = screenLoader.loadPreviousDashboard(path, Screen.MONTHLY_REPORT_SCREEN, prevController);
    	
    	dashboard.getChildren().clear();
    	dashboard.getChildren().add(prevDash);
	}
	
}
