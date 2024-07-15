package logic;

//ENUM for Server Actions - for comfort
public enum ServerActionsEnum {
	ALL_ORDERS, // call client with LIST<ORDER> containing  all orders from database
	UPDATE_ORDER_RESULT, // call client with boolean if update not/worked
	UPDATE_CONNECTION_INFO; //call client with STRING containing his IP + HOST + STATUS
}
