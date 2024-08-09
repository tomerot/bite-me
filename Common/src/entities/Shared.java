package entities;

import java.util.List;

import enums.Branch;

/**
 * Represents a shared delivery for an order. Extends the {@link Delivery} class to include
 * additional attributes and behaviors specific to shared deliveries.
 */
public class Shared extends Delivery{
	private float initialSharedFee=25;
	private float minSharedFee=15;
	private List<Customer> customers;

	public Shared(List<ItemInOrder> itemsList, int orderID,String recipient,String recipientPhone,SupplyMethod supplyOption, Supplier supplier, Customer customer, Branch branchName, String requestedDate,
            String requestedTime, OrderType type, float totalPrice, String approvalTimer, String arrivalTime, String status, String city, String address, List<Customer> customers) {
		super(itemsList, orderID, recipient, recipientPhone, SupplyMethod.SHARED, supplier, null,  branchName,  requestedDate,
	             requestedTime, type, totalPrice, approvalTimer, arrivalTime, status, city, address);
		this.customers = customers;
	}
    // MinSharedFee getter and setter
	public float getMinSharedFee() { return minSharedFee; }
    public void setMinSharedFee(float minSharedFee) { this.minSharedFee=minSharedFee; }
    // initialSharedFee getter and setter
	public float getInitialSharedFee() { return initialSharedFee; }
    public void setInitialSharedFee(float initialSharedFee) { this.initialSharedFee=initialSharedFee; }
    public void addFee() {}//unimplemented yet
}
