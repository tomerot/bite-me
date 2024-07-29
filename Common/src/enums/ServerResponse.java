package enums;

//ENUM for Server Actions - for comfort
public enum ServerResponse {
	UPDATE_CONNECTION_INFO, //call client with STRING containing his IP + HOST + STATUS
	USER_FOUND,
	USER_NOT_FOUND,
	BRANCH_MANAGER_DATA,
	CEO_DATA
}
