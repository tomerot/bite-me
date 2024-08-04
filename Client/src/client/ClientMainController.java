package client;

import java.util.List;

import containers.ClientRequestDataContainer;
import entities.BranchManager;
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
    public static void requestUnregisteredCustomersData(BranchManager manager) {
        ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.FETCH_CUSTOMERS_DATA, manager);
        ClientMainController.accept(request);
    }
    public static void requestUpdateRegisterCustomers(List<String> userList) {
        ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.UPDATE_CUSTOMERS_REGISTER, userList);
        ClientMainController.accept(request);
    }
    
}
