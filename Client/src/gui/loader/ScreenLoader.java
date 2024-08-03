package gui.loader;

import java.io.IOException;

import gui.controllers.BranchManagerController;
import gui.controllers.CeoHomeScreenController;
import gui.controllers.MonthlyReportScreenController;
import gui.controllers.MonthlyReportScreenController2;
import gui.controllers.MyOrdersScreenController;
import gui.controllers.NewOrderScreenController;
import gui.controllers.RegistrationScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Utility class for loading different screens in the application.
 */
public class ScreenLoader {
	
	private AnchorPane dashboard; //Requested dashboard
	private HBox wholeScreen;
	
	//view - what screen do we need to load
	//data - contains entities we need to pass to the next screen, MAYBE WE CAN USE LIST INSTEAD?
	
    /**
     * Loads the specified screen into an AnchorPane.
     *
     * @param wholeScreen , HBox - the object that contains the whole screen (the root in the FXML file)
     * @param path The path to the FXML file.
     * @param toLoad The type of screen to load.
     * @param currController The current controller to pass to the new screen, in case it's needed to pass the current controller (for example for "Back")
     * @return The loaded AnchorPane.
     * @throws IOException If loading the FXML file fails.
     */
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
			case NEW_ORDER_SCREEN:{
				NewOrderScreenController controller = new NewOrderScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				controller.loadAllRestaurants();
				break;
			}
			case MY_ORDERS_SCREEN:{
				MyOrdersScreenController controller = new MyOrdersScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				controller.loadAllOrders();
				break;
			}
			default:
				break;
		}
		
		dashboard = loader.getRoot();
		return dashboard;
	}
	
    /**
     * Loads a previous screen into an HBox.
     *
     * @param path The path to the FXML file.
     * @param toLoad The type of screen to load.
     * @param prevController The previous controller to pass to the new screen.
     * @return The loaded HBox.
     * @throws IOException If loading the FXML file fails.
     */
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
	
    /**
     * Loads a previous dashboard into an AnchorPane.
     *
     * @param path The path to the FXML file.
     * @param toLoad The type of screen to load.
     * @param prevController The previous controller to pass to the new screen.
     * @return The loaded AnchorPane.
     * @throws IOException If loading the FXML file fails.
     */
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
