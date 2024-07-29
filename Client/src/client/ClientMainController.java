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
		System.out.println("ClientMainController" + request.getRequest());
		ClientMainController.accept(request);
	}
	public static void requestUpdateUserData(User user) {
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.UPDATE_USER_DATA, user);
		ClientMainController.accept(request);
	}
	public static void requestBranchManagerData(User user) {
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.FETCH_BRANCH_MANAGER_DATA, user);
		ClientMainController.accept(request);
	}
	
	public static void requestCeoData(User user) {
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.FETCH_CEO_DATA, user);
		ClientMainController.accept(request);
	}
}
