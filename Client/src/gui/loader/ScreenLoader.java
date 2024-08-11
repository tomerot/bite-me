package gui.loader;

import java.io.IOException;

import gui.controllers.AddItemScreenController;
import gui.controllers.BranchManagerController;
import gui.controllers.CeoHomeScreenController;
import gui.controllers.CheckoutScreenController;
import gui.controllers.ChooseRestaurantScreenController;
import gui.controllers.CustomerHomeScreenController;
import gui.controllers.MonthlyReportScreenController;
import gui.controllers.MyOrdersScreenController;
import gui.controllers.QuarterlyReportScreenController;
import gui.controllers.EditItemScreenController;
import gui.controllers.EmployeeHomeScreenController;
import gui.controllers.MonthlyReportScreenController;
import gui.controllers.MyOrdersScreenController;
import gui.controllers.OrderSummaryScreenController;
import gui.controllers.RegistrationScreenController;
import gui.controllers.RemoveItemScreenController;
import gui.controllers.RestaurantMenuScreenController;
import gui.controllers.SupplierScreenController;
import gui.controllers.ThankYouScreenController;
import gui.controllers.ViewSupplierOrdersScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Utility class for loading different screens in the application.
 */
public class ScreenLoader {
	
	private AnchorPane dashboard; //Requested dashboard
	private HBox wholeScreen;
	
    /**
     * Loads the specified screen into an AnchorPane.
     *
     * @param wholeScreen , HBox - the object that contains the whole screen (the root in the FXML file)
     * @param path The path to the FXML file.
     * @param toLoad The name of screen to load.
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
				controller.setScreen();
				break;
			}
			case QUARTERLY_REPORT_SCREEN: {
				QuarterlyReportScreenController controller = new QuarterlyReportScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				controller.setScreen();
				break;
			}
			
			case REGISTRATION_SCREEN: {
				RegistrationScreenController controller = new RegistrationScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				controller.setupRegistrationTable();
				break;
			}
			case CHOOSE_RESTAURANT_SCREEN:{
				ChooseRestaurantScreenController controller = new ChooseRestaurantScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				break;
			}
			case RESTAURANT_MENU_SCREEN:{
				RestaurantMenuScreenController controller = new RestaurantMenuScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				break;
			}
			case CHECKOUT_SCREEN:{
				CheckoutScreenController controller = new CheckoutScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				break;
			}
			case ORDER_SUMMARY_SCREEN:{
				OrderSummaryScreenController controller = new OrderSummaryScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				break;
			}
			case MY_ORDERS_SCREEN:{
				MyOrdersScreenController controller = new MyOrdersScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				controller.loadAllOrders();
				break;
			}
			case ADD_ITEM_SCREEN:{
				AddItemScreenController controller=new AddItemScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				break;
			}
				
			case REMOVE_ITEM_SCREEN:{
				RemoveItemScreenController controller=new RemoveItemScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				break;
			}
			case EDIT_ITEM_SCREEN:{
				EditItemScreenController controller=new EditItemScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				break;
			}
			case VIEW_SUPPLIER_ORDERS_SCREEN:{
				ViewSupplierOrdersScreenController controller=new ViewSupplierOrdersScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
				break;
			}
			case THANK_YOU_SCREEN:{
				ThankYouScreenController controller=new ThankYouScreenController(wholeScreen, currController);
				loader.setController(controller);
				loader.load();
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
			case SUPPLIER_SCREEN:
				loader.setController((SupplierScreenController) prevController);
				break;
			case EMPLOYEE_SCREEN:
				loader.setController((EmployeeHomeScreenController)prevController);

			case MANAGER_SCREEN:
				loader.setController((BranchManagerController) prevController);
				break;
			case CUSTOMER_SCREEN:
				loader.setController((CustomerHomeScreenController)prevController);
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
			case RESTAURANT_MENU_SCREEN:
				loader.setController((RestaurantMenuScreenController) prevController);
				break;
				
			case  CHECKOUT_SCREEN:
				loader.setController((CheckoutScreenController) prevController);
				break;
				
			default:
				break;
		}
		AnchorPane dashboard = loader.load();
		//dashboard = loader.getRoot();
		return dashboard;
	}
}
