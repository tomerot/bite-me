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
//	
//	public static void requestManagerData(User user) {
//		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.GET_MANAGER_DATA, user);
//		ClientMainController.accept(request);
//	}
//	
//	public static void requestSupplierData(User user) {
//		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.GET_SUPPLIER_DATA, user);
//		ClientMainController.accept(request);
//	}
//	
//	public static void requestEmployeeData(User user) {
//		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.GET_EMPLOYEE_DATA, user);
//		ClientMainController.accept(request);
//	}
//	
//	public static void requestCustomerData(User user) {
//		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.GET_CUSTOMER_DATA, user);
//		ClientMainController.accept(request);
//	}
}
