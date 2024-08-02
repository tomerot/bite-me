package gui.loader;

import java.io.IOException;

import gui.controllers.BranchManagerController;
import gui.controllers.CeoHomeScreenController;
import gui.controllers.MonthlyReportScreenController;
import gui.controllers.MonthlyReportScreenController2;
import gui.controllers.RegistrationScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class ScreenLoader {
	
	private AnchorPane dashboard; //Requested dashboard
	private HBox wholeScreen;
	
	//HBox - the object that contains the whole screen (the root in the FXML file)
	//path - the path to the FXML
	//view - what screen do we need to load
	//currController - in case it's needed to pass the current controller (for example for "Back")
	//data - contains entities we need to pass to the next screen, MAYBE WE CAN USE LIST INSTEAD?
	
	public AnchorPane loadOnDashboard(HBox wholeScreen, String path, Screen toLoad, Object currController) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path)); //The FXML is actually an AnchorPane
		switch(toLoad) {
			case MONTHLY_REPORT_SCREEN: {
				MonthlyReportScreenController controller = new MonthlyReportScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				break;
			}
			case MONTHLY_REPORT_SCREEN_TWO: {
				MonthlyReportScreenController2 controller = new MonthlyReportScreenController2(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				break;
			}
			case REGISTRATION_SCREEN: {
				RegistrationScreenController controller = new RegistrationScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				controller.setupRegistrationTable();
				break;
			}
			default:
				break;
		}
		
		dashboard = loader.getRoot();
		return dashboard;
	}
	
	public HBox loadPreviousScreen(String path, Screen toLoad, Object prevController) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
		switch(toLoad) {
			case CEO_SCREEN:
				loader.setController((CeoHomeScreenController) prevController);
				break;
			case MANAGER_SCREEN:
				loader.setController((BranchManagerController) prevController);
				break;
			default:
				break;
		}
		loader.load();
		wholeScreen = loader.getRoot();
		return wholeScreen;
	}
	
	public AnchorPane loadPreviousDashboard(String path, Screen toLoad, Object prevController) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
		switch(toLoad) {
			case MONTHLY_REPORT_SCREEN:
				loader.setController((MonthlyReportScreenController) prevController);
				break;
			default:
				break;
		}
		loader.load();
		dashboard = loader.getRoot();
		return dashboard;
	}
}
