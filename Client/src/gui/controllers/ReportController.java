package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller for handling the report screen
 */
public class ReportController {
	@FXML
	private Button BackBtn;
	private BranchManagerController BranchManagerController;
	private CeoHomeScreenController CeoController;
	
	public ReportController(BranchManagerController BranchManagerController) {
		this.BranchManagerController=BranchManagerController;
		this.CeoController=null;
	}
	public ReportController(CeoHomeScreenController CeoController) {
		this.BranchManagerController=null;
		this.CeoController=CeoController;
	}
	
	
    /**
     * Returns to the appropriate home page based on the type of user.
     * If the controller is for a branch manager, it navigates to the branch manager screen.
     * If the controller is for a CEO, it navigates to the CEO screen.
     * 
     * @param event the action event that triggers the navigation
     * @throws Exception if there is an error loading the FXML files or displaying the screens
     */
	public void returnToManagerPage(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
	    if(CeoController==null) {
	    	loader.setController(BranchManagerController);
	    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
	    	Stage primaryStage = new Stage();
	    	Pane root = loader.load(getClass().getResource("/gui/view/BranchManagerScreen.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle(BranchManagerController.getBranchManager().getUserType()+" Home Page");
			primaryStage.setScene(scene);
			primaryStage.show();
	    }
	    else {
	    	loader.setController(CeoController);
	    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
	    	Stage primaryStage = new Stage();
	    	Pane root = loader.load(getClass().getResource("/gui/view/CeoScreen.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle(CeoController.getCeo().getUserType()+" Home Page");
			primaryStage.setScene(scene);
			primaryStage.show();
	    }
	}
}
