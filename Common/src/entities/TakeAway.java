package entities;

import enums.Branch;

/**
 * Represents a takeaway order. Extends the {@link Order} class to include
 * additional attributes and behaviors specific to takeaway orders.
 */
public class TakeAway extends Order {
	private SupplyMethod supplyMethod;
	public TakeAway(int orderID, Supplier supplier, Customer customer, Branch branchName, String desiredDate,
            String desiredHour, OrderType type, float totalPrice, String acceptanceHour, String confirmedHour, SupplyMethod TAKEAWAY) {
		super(orderID, supplier, customer, branchName, desiredDate,desiredHour, type, totalPrice, acceptanceHour, confirmedHour);
		this.supplyMethod=TAKEAWAY;
	}
    // supplyMethod getter and setter
    public SupplyMethod getSupplyMethod() { return supplyMethod; }
    public void setSupplyMethod(SupplyMethod supplyMethod) { this.supplyMethod=supplyMethod; }
}
