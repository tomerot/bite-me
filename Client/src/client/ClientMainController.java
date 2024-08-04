package client;

import java.util.Map;

import containers.ClientRequestDataContainer;
import entities.Item;
import java.util.List;
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

    
    public static void requestOrderData(int supplierID) {
    	ClientRequestDataContainer request=new ClientRequestDataContainer(ClientRequest.GET_ORDER_DATA, supplierID);
    	ClientMainController.accept(request);
    }
	
	public static void requestAddItemData(Item item) {
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.ADD_ITEM_DATA, item);
		ClientMainController.accept(request);
	}
	
	public static void requestItemsList(int supplierID) {
		
		Integer suppID = supplierID;
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.GET_ITEMS_LIST, suppID);
		ClientMainController.accept(request);
	}
	
	public static void requestFullItemsList(int supplierID) {
		
		Integer suppID = supplierID;
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.GET_FULL_ITEMS_LIST, suppID);
		ClientMainController.accept(request);
	}
	
	public static void requestRemoveItem(Map<String,Integer> itemData) {
		
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.REMOVE_ITEM, itemData);
		ClientMainController.accept(request);
	}
	
	public static void requestUpdateItem(Item item) {
		
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.UPDATE_ITEM, item);
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
