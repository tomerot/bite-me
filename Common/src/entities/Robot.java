package entities;

public class Robot extends Delivery {
	private float robotFee=0;
	public Robot(int orderID, Supplier supplier, RegisteredCustomer customer, Branch branchName, String desiredDate,
            String desiredHour, OrderType type, float totalPrice, String acceptanceHour, String confirmedHour, SupplyMethod ROBOT, String address) {
		super(orderID, supplier, customer, branchName, desiredDate,desiredHour, type, totalPrice, acceptanceHour, confirmedHour, ROBOT, address);
	}
	// robotFee getter and setter
    public float getRobotFee() { return robotFee; }
    public void setRobotFee(float robotFee) { this.robotFee=robotFee; }
    //addFee for Robot delivery
    public void addFee() {
    	this.setTotalPrice(this.getTotalPrice()+robotFee);
    }
}
