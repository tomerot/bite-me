package client;

import containers.ClientRequestDataContainer;
import entities.User;
import enums.ClientRequest;

public class ClientMainController {

    public static void accept(ClientRequestDataContainer message) {
        ClientConsole.handleMessageFromClientUI(message);
    }

    public static void requestUserData(User user) {
        ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.GET_USER_DATA, user);
        ClientMainController.accept(request);
    }

    public static void requestUserSpecificData(User user) {
        ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.GET_SPECIFIC_USER_DATA, user);
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
    
    public static void requestUpdateIsLoggedIn(User user) {
        ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.UPDATE_IS_LOGGED_IN, user);
        ClientMainController.accept(request);
    }
}
