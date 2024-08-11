package gui.controllers;


import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.Order;
import entities.Supplier;
import entities.User;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SupplierScreenController implements Initializable{
	
	private List<Order> list=new ArrayList<>(); //key is the id order, value is the order instance itself.
	private User user;
	private Supplier sup;
	
	@FXML
	private AnchorPane dashboard;
	
	@FXML
	private HBox screen;
	
	@FXML
	private Button btnRefresh;
	
	@FXML
	private Button btnAccept;
	
	@FXML
    private TableView<Order> tableOrdersToAccept;
	

    @FXML
    private TableColumn<Order, String> colDateToShip;

    @FXML
    private TableColumn<Order, Integer> colID;

    @FXML
    private TableColumn<Order, String> colName; 

    @FXML
    private TableColumn<Order, Double> colPrice;

    @FXML
    private TableColumn<Order, Time> colTimeToShip;

    @FXML
    private TableColumn<Order, String> colType;
	
	@FXML 
	private ListView<String> listOfShippedOrders;
	
	@FXML
	private Label welcomeLbl;
	
	
	
	public void setUser(User user) {
        this.user = user;
        if (user instanceof Supplier) {
            this.sup = (Supplier) user;
            UpdateLabel(sup);
        }
    }
	
	public Supplier getSupplier() {
		return sup;
	}
	
	public User getUser() {
		return user;
	}
	
	public void UpdateLabel(Supplier supplier) {
	    Platform.runLater(() -> {
	        welcomeLbl.setText("Welcome, " + supplier.getName());
	    });
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		
		//get all the orders of the restaurant.
		ClientMainController.requestOrderData(sup.getSupplierID());
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		list = (List<Order >)response.getMessage();
		
		ShowTheOrdersToAccept(); //init the table view for orders to accept
	}
	
	public void ShowTheOrdersToAccept() {
		ObservableList<Order> ordersToAccept = FXCollections.observableArrayList(list);
        colID.setCellValueFactory(new PropertyValueFactory<Order, Integer>("colId"));
        colName.setCellValueFactory(new PropertyValueFactory<Order, String>("colName"));
        colDateToShip.setCellValueFactory(new PropertyValueFactory<Order, String>("colDate"));
        colTimeToShip.setCellValueFactory(new PropertyValueFactory<Order, Time>("colTime"));
        colType.setCellValueFactory(new PropertyValueFactory<Order, String>("colType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<Order, Double>("colPrice"));

        tableOrdersToAccept.setItems(ordersToAccept);
	}
	
	
	
	
	 public void logOut(ActionEvent event) throws Exception{
			user.setisLoggedIn(0);
			ClientMainController.requestUpdateIsLoggedIn(user);
			displayLogin(event);
	    }
	    
		// Method to display login screen
		public void displayLogin(ActionEvent event) throws Exception {
			FXMLLoader loader = new FXMLLoader();
			loader.setController(new LoginController());
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/view/LoginScreen.fxml").openStream());
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/gui/view/LoginScreen.css").toExternalForm());
			primaryStage.setTitle("Main");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		
}
