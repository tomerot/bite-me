package gui.controllers;


import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.ItemInOrder;
import entities.Order;
import entities.Supplier;
import entities.User;
import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SupplierScreenController{
	
	private User user;
	private static Supplier sup;

	@FXML
	private HBox screen;

	@FXML
	private AnchorPane dashboard;
	
	@FXML
	private Button btnViewOrders;

	@FXML
	private Label welcomeLbl;
	
	public void setUser(User user) {
        this.user = user;
        if (user instanceof Supplier) {
            this.sup = (Supplier) user;
            UpdateLabel(sup);
        }
    }
	
	public static Supplier getSupplier() {
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
		
		@FXML
		private void onViewOrdersClicked(ActionEvent event) throws IOException {
			ScreenLoader screenLoader = new ScreenLoader();
	    	String path = "/gui/view/ViewSupplierOrdersScreen.fxml";
	    	AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.VIEW_SUPPLIER_ORDERS_SCREEN, this);
	    	dashboard.getChildren().clear(); //Clear current dashboard
	    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
		}
}
