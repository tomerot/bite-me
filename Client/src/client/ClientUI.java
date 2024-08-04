
package client;

import gui.controllers.ClientConnectFormController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientUI extends Application {

	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientConnectFormController aFrame = new ClientConnectFormController();
		aFrame.start(primaryStage); // start the ClientConnect gui (happening inside ClientGuiController start
									// method)
	}

}
