package entities;

import enums.Branch;

/**
 * This class represents a delivery order in the system.
 * It extends the {@link Order} class and adds attributes specific to delivery.
 */
public class Delivery extends Order {
	private SupplyMethod supplyMethod;
	private String address;
	public Delivery(int orderID, Supplier supplier, Customer customer, Branch branchName, String desiredDate,
            String desiredHour, OrderType type, float totalPrice, String acceptanceHour, String confirmedHour, SupplyMethod supplyMethod, String address) {
		super(orderID, supplier, customer, branchName, desiredDate,desiredHour, type, totalPrice, acceptanceHour, confirmedHour);
		this.supplyMethod=supplyMethod;
		this.address=address;
	}
    // supplyMethod getter and setter
    public SupplyMethod getSupplyMethod() { return supplyMethod; }
    public void setSupplyMethod(SupplyMethod supplyMethod) { this.supplyMethod=supplyMethod; }
    // address getter and setter
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address=address; }
}
