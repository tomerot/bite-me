package client;

import containers.ClientRequestDataContainer;
import entities.User;
import enums.ClientRequest;

public class ClientMainController {

	public static void accept(ClientRequestDataContainer message) {
		ClientConsole.handleMessageFromClientUI(message);
	}
	
	public static void requestUserData(User user) {
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.CHECK_USER_DATA, user);
		ClientMainController.accept(request);
	}
}
