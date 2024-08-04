
package client;

import gui.controllers.ClientConnectFormController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The entry point for the client-side JavaFX application. This class extends
 * {@link Application} and initializes the client GUI.
 */

public class ClientUI extends Application {

	/**
	 * The main method that launches the JavaFX application.
	 *
	 * @param args command-line arguments (this is not really used)
	 * @throws Exception if an error occurs while launching the application
	 */
	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	/**
	 * Initializes and starts the client-side GUI.
	 *
	 * @param primaryStage the primary stage for this application, onto which the
	 *                     application scene will be set
	 * @throws Exception if an error occurs during the initialization of the GUI
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientConnectFormController aFrame = new ClientConnectFormController();
		aFrame.start(primaryStage); // start the ClientConnect gui (happening inside ClientGuiController start
									// method)
	}

}
