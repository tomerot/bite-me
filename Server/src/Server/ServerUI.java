package Server;

import gui.ServerPortController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;

	public static void main(String args[]) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// create ServerPortController
		ServerPortController aFrame = new ServerPortController();
		// start the ServerPort GUI (happening inside ClientGuiController start method)
		aFrame.start(primaryStage);
	}
}
