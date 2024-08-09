package gui.controllers;

import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Controller for managing the monthly report screen.
 * Handles navigation between different pages of the monthly report and returning to the previous screen.
 */
public class MonthlyReportScreenController {
	
	@FXML
	private Button nextPageBtn;
	
	@FXML
	private Button backBtn;
	
	@FXML
	private AnchorPane dashboard;
	
	private CeoHomeScreenController prevCeoController;
	private BranchManagerController prevManagerController;
	private HBox wholeScreen;
	
    /**
     * Constructs a new MonthlyReportScreenController.
     * 
     * @param wholeScreen the HBox container for the current screen
     * @param prevController the previous controller, either CeoHomeScreenController or BranchManagerController
     */
	public MonthlyReportScreenController(HBox wholeScreen , Object prevController) {
		if(prevController instanceof CeoHomeScreenController) {
			this.prevCeoController = (CeoHomeScreenController) prevController;
			this.prevManagerController=null;
		}
		if(prevController instanceof BranchManagerController) {
			this.prevCeoController = null;
			this.prevManagerController=(BranchManagerController) prevController;
		}
		this.wholeScreen = wholeScreen;
	}
	
    /**
     * Navigates to the next page of the monthly report.
     * 
     * @param event the action event triggered by clicking the next page button
     * @throws Exception if an error occurs during the page transition
     */
//	public void displayNextPage(ActionEvent event) throws Exception {
//		ScreenLoader screenLoader = new ScreenLoader();
//		AnchorPane nextDash = screenLoader.loadOnDashboard(wholeScreen, "/gui/view/MonthlyReportScreen2.fxml", Screen.MONTHLY_REPORT_SCREEN_TWO, this);
//		dashboard.getChildren().clear();
//		dashboard.getChildren().add(nextDash);
//	}
	
    /**
     * Returns to the previous screen based on the previous controller.
     * 
     * @param event the action event triggered by clicking the back button
     * @throws Exception if an error occurs during the screen transition
     */
	public void goBack(ActionEvent event) throws Exception {
		ScreenLoader screenLoader = new ScreenLoader();
		HBox prevWholeScreen=null;
		String path="";
		if(prevCeoController!=null && prevManagerController==null) {
			path = "/gui/view/CeoScreen.fxml";
			prevWholeScreen = screenLoader.loadPreviousScreen(path, Screen.CEO_SCREEN, prevCeoController);
			prevCeoController.UpdateLabel(prevCeoController.getCeo());
		}
		if(prevCeoController==null && prevManagerController!=null) {
			path = "/gui/view/BranchManagerScreen.fxml";
			prevWholeScreen = screenLoader.loadPreviousScreen(path, Screen.MANAGER_SCREEN, prevManagerController);
			prevManagerController.UpdateLabel(prevManagerController.getBranchManager());
		}

		wholeScreen.getChildren().clear();
		wholeScreen.getChildren().add(prevWholeScreen);
	}
	
}
