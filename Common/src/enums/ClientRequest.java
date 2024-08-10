package enums;

/**
 * Enum representing various client actions or requests that can be sent to the server.
 */
public enum ClientRequest {
	DISCONNECT, //call server to disconnect client from server 
	CHECK_USER_DATA, // Request to check user data
	UPDATE_USER_DATA, // Request to update user data
	FETCH_BRANCH_MANAGER_DATA,
	FETCH_CEO_DATA,
	GET_USER_DATA, // Request to get general user data
	GET_SPECIFIC_USER_DATA, // Request to get data for a specific user
	UPDATE_IS_LOGGED_IN, // Request to update the login status of a user
	GET_ORDER_DATA,
	ADD_ITEM_DATA,
	GET_ITEMS_LIST,
	GET_FULL_ITEMS_LIST,
	REMOVE_ITEM,
	UPDATE_ITEM,
	FETCH_CUSTOMERS_DATA,
	UPDATE_CUSTOMERS_REGISTER, // Request to update customer registration
	FETCH_BRANCH_RESTAURANTS, // Request to fetch restaurant data by branch
	FETCH_CUSTOMER_WAITING_ORDERS,
	FETCH_CUSTOMER_HISTORY_ORDERS,
	UPDATE_ORDER_STATUS_AND_TIME,
	GET_SUPPLIER_ITEMS,
	GET_RELEVANT_CITIES,
	UPDATE_ORDER_AND_ITEMS
	;
}
