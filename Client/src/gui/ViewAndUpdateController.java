package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Order;
import logic.OrderingClient;

public class ViewAndUpdateController implements Initializable {

	private static ViewAndUpdateController instance;

	// FXML annotated fields for UI components
	@FXML
	private TableView<Order> ordersTable;

	@FXML
	private TableColumn<Order, Integer> orderNumberColumn;

	@FXML
	private TableColumn<Order, String> restaurantColumn;

	@FXML
	private TableColumn<Order, Double> totalPriceColumn;

	@FXML
	private TableColumn<Order, String> orderListNumberColumn;

	@FXML
	private TableColumn<Order, String> orderAddressColumn;

	@FXML
	private Button btnUpdate;

	@FXML
	private Button btnBack;

	@FXML
	private TextField OrderNumberField;

	@FXML
	private TextField TotalPriceField;

	@FXML
	private TextField OrderAddressField;

	@FXML
	private Label messageLabel;

	// Observable list to hold order data for the table
	private ObservableList<Order> orderData = FXCollections.observableArrayList();

	@Override
	// initialize - first thing to happen when oppening this page
	public void initialize(URL arg0, ResourceBundle arg1) {
		OrderingClient.setViewController(this);
		// Set up cell value factories for each table column
		restaurantColumn.setCellValueFactory(new PropertyValueFactory<>("restaurant"));
		orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
		totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
		orderListNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderListNumber"));
		orderAddressColumn.setCellValueFactory(new PropertyValueFactory<>("orderAddress"));
		setInstance(this); // Set this instance
		updateGuiTable(); // Initial update of the GUI table
	}

	// Method to request a fetch of all orders from the server
	public void updateGuiTable() {
		OrderingClient.requestToFetchAllOrders();
	}

	// Method to update the orders in the table
	// This is called from the OrderingClient
	public static void updateOrdersInTable(List<Order> orders) {
		if (instance != null) {
			Platform.runLater(() -> {
				instance.orderData.clear(); // Clear existing data
				instance.orderData.addAll(orders); // Add new data
				instance.ordersTable.setItems(instance.orderData); // Set items to TableView
			});
		} else {
			System.out.println("ViewAndUpdateController instance is not set");
		}
	}

	// Method to handle updating an order's price and address by given id
	public void updateOrderPriceAndAddress() {
		//make sure all field are entered
		if (OrderNumberField.getText() == "" || TotalPriceField.getText()== "" || OrderAddressField.getText() == "") {
			messageLabel.setText("Make Sure all fiels entered!");
			return;
		}
        // make sure OrderNumberField is an integer
        if (!isInteger(OrderNumberField.getText())) {
            messageLabel.setText("Order number must be an integer!");
            return;
        }

        // make sure TotalPriceField is a double
        if (!isDouble(TotalPriceField.getText())) {
            messageLabel.setText("Total price must be a valid number!");
            return;
        }
        
		int orderNumber = Integer.parseInt(OrderNumberField.getText());
		double totalPrice = Double.parseDouble(TotalPriceField.getText());
		String orderAddress = OrderAddressField.getText();
		Order updatedOrder = new Order(null, orderNumber, totalPrice, null, orderAddress);
		OrderingClient.requestToUpdateOrder(updatedOrder);
	}

	// When clicking on back button - go back into the Main home client page
	public void displayHomeClientPage(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/HomeClientPage.fxml").openStream());
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/HomeClientPage.css").toExternalForm());
		primaryStage.setTitle("Main");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// show updated massage on the screen inside the label
	public void updateMessage(String message) {
		Platform.runLater(() -> messageLabel.setText(message));
	}
	
    // check if a string is an integer
    private boolean isInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // check if a string is a double
    private boolean isDouble(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

	
	public static void setInstance(ViewAndUpdateController controller) {
		instance = controller;
	}
}
