package enums;

/**
 * Enum representing various server responses to client requests.
 */
public enum ServerResponse {
	UPDATE_CONNECTION_INFO, //call client with STRING containing his IP + HOST + STATUS
	USER_FOUND, // Indicates that the user was found
	USER_NOT_FOUND, // Indicates that the user was not found
	BRANCH_MANAGER_DATA, // Provides branch manager data
	CEO_DATA, // Provides CEO data
	CEO_FOUND, // Indicates that the CEO was found
	MANAGER_FOUND, // Indicates that a manager was found
	CUSTOMER_FOUND, // Indicates that a customer was found
	SUPPLIER_FOUND, // Indicates that a supplier was found
	EMPLOYEE_FOUND, // Indicates that an employee was found
	UNREGISTERED_CUSTOMERS_FOUND, // Indicates that unregistered customers were found
	SUPPLIER_DATA_BY_BRANCH // Provides supplier data by branch
	;
}
