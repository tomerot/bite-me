package entities;

public class Basic extends Delivery {
	private float basicFee=0;
	public Basic(int orderID, Supplier supplier, RegisteredCustomer customer, Branch branchName, String desiredDate,
            String desiredHour, OrderType type, float totalPrice, String acceptanceHour, String confirmedHour, SupplyMethod BASIC, String address) {
		super(orderID, supplier, customer, branchName, desiredDate,desiredHour, type, totalPrice, acceptanceHour, confirmedHour, BASIC, address);
	}
    // BasicFee getter and setter
	public float getBasicFee() { return basicFee; }
    public void setBasicFee(float basicFee) { this.basicFee=basicFee; }
    //addFee for Robot delivery
    public void addFee() {
    	this.setTotalPrice(this.getTotalPrice()+basicFee);
    }
}
