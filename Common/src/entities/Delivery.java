package entities;

import java.util.List;

import enums.Branch;

/**
 * This class represents a delivery order in the system.
 * It extends the {@link Order} class and adds attributes specific to delivery.
 */
public class Delivery extends Order {

	private String city;
	private String address;
	
	public Delivery(List<ItemInOrder> itemsList, int orderID,String recipient,String recipientPhone,SupplyMethod supplyOption, Supplier supplier, Customer customer, Branch branchName, String requestedDate,
            String requestedTime, OrderType type, float totalPrice, String approvalTimer, String arrivalTime, String status, String city, String address) {
		super(itemsList, orderID, recipient, recipientPhone, supplyOption, supplier, customer, branchName, requestedDate,
	            requestedTime, type, totalPrice, approvalTimer, arrivalTime, status);
		this.city = city;
		this.address = address;
	}
	
    // address getter and setter
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address=address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
