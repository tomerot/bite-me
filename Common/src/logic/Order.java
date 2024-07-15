package logic;

import java.io.Serializable;

// class which contain all the required details about Order
// made it Serializable in order to be able to use it with the OCSF framework.
public class Order implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int orderNumber;
	private String restaurant;
	private double totalPrice;
	private String orderListNumber;
	private String orderAddress;
	public Order(String restaurant, int orderNumber, double totalPrice, String orderListNum,String orderAddress) {
		this.restaurant = restaurant;
		this.orderNumber = orderNumber;
		this.totalPrice = totalPrice;
		this.orderListNumber = orderListNum;
		this.orderAddress = orderAddress;
	}

	//getter for OrderNumber
	public int getOrderNumber() {
		return orderNumber;
	}

	//getter for Restaurant
	public String getRestaurant() {
		return restaurant;
	}

	//getter for TotalPrice
	public double getTotalPrice() {
		return totalPrice;
	}

	//getter for OrderListNumber
	public String getOrderListNumber() {
		return orderListNumber;
	}

	//getter for OrderAddress
	public String getOrderAddress() {
		return orderAddress;
	}
}
