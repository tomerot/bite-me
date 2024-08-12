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


/**
 * Controller for the supplier screen in the GUI.
 * This controller manages the interactions and updates for the supplier's home screen,
 * including moving to viewing orders and logging out.
 */
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
	
	/**
     * Sets the user for this controller and updates the welcome label.
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
        if (user instanceof Supplier) {
            this.sup = (Supplier) user;
            UpdateLabel(sup);
        }
    }

    /**
     * Gets the current supplier.
     *
     * @return the supplier
     */
    public static Supplier getSupplier() {
        return sup;
    }

    /**
     * Gets the current user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Updates the welcome label with the supplier's name.
     *
     * @param supplier the supplier
     */
    public void UpdateLabel(Supplier supplier) {
        Platform.runLater(() -> {
            welcomeLbl.setText("Welcome, " + supplier.getName());
        });
    }

    /**
     * Logs out the current user and displays the login screen.
     *
     * @param event the action event
     * @throws Exception if logging out or displaying the login screen fails
     */
    public void logOut(ActionEvent event) throws Exception {
        user.setisLoggedIn(0);
        ClientMainController.requestUpdateIsLoggedIn(user);
        displayLogin(event);
    }

    /**
     * Displays the login screen.
     *
     * @param event the action event
     * @throws Exception if loading the FXML file fails
     */
    public void displayLogin(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new LoginController());
        ((Node) event.getSource()).getScene().getWindow().hide(); // Hides the primary window
        Stage primaryStage = new Stage();
        Pane root = loader.load(getClass().getResource("/gui/view/LoginScreen.fxml").openStream());
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/view/LoginScreen.css").toExternalForm());
        primaryStage.setTitle("Main");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Handles the event when the view orders button is clicked.
     * Loads the View Supplier Orders screen into the dashboard.
     *
     * @param event the action event
     * @throws IOException if loading the FXML file fails
     */
    @FXML
    private void onViewOrdersClicked(ActionEvent event) throws IOException {
        ScreenLoader screenLoader = new ScreenLoader();
        String path = "/gui/view/ViewSupplierOrdersScreen.fxml";
        AnchorPane nextDash = screenLoader.loadOnDashboard(screen, path, Screen.VIEW_SUPPLIER_ORDERS_SCREEN, this);
        dashboard.getChildren().clear(); // Clear current dashboard
        dashboard.getChildren().add(nextDash); // Assign the new dashboard
    }
}
