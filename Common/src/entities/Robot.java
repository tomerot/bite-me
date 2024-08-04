package entities;

import enums.Branch;

/**
 * Represents a robot delivery for an order. Extends the {@link Delivery} class to include
 * additional attributes and behaviors specific to robot deliveries.
 */
public class Robot extends Delivery {
	private float robotFee=0;
	public Robot(int orderID, Supplier supplier, Customer customer, Branch branchName, String desiredDate,
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
