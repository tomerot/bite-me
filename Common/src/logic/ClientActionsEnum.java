package logic;

//ENUM for Client Actions - for comfort
public enum ClientActionsEnum {
	FETCH_ORDERS, //call server and ask bring all order from database
	UPDATE_ORDER, //call server to update specific order
	DISCONNECT; //call server to disconnect client from server 
}
