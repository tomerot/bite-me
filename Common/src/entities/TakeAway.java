package entities;

import enums.Branch;

public class TakeAway extends Order {
	private SupplyMethod supplyMethod;
	public TakeAway(int orderID, Supplier supplier, RegisteredCustomer customer, Branch branchName, String desiredDate,
            String desiredHour, OrderType type, float totalPrice, String acceptanceHour, String confirmedHour, SupplyMethod TAKEAWAY) {
		super(orderID, supplier, customer, branchName, desiredDate,desiredHour, type, totalPrice, acceptanceHour, confirmedHour);
		this.supplyMethod=TAKEAWAY;
	}
    // supplyMethod getter and setter
    public SupplyMethod getSupplyMethod() { return supplyMethod; }
    public void setSupplyMethod(SupplyMethod supplyMethod) { this.supplyMethod=supplyMethod; }
}
