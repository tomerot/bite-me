package entities;

public class Shared extends Delivery{
	private float initialSharedFee=25;
	private float minSharedFee=15;
	private int numOfParticipants;

	public Shared(int orderID, Supplier supplier, RegisteredCustomer customer, Branch branchName, String desiredDate,
            String desiredHour, OrderType type, float totalPrice, String acceptanceHour, String confirmedHour, SupplyMethod SHARED, String address, int numOfParticipants) {
		super(orderID, supplier, customer, branchName, desiredDate,desiredHour, type, totalPrice, acceptanceHour, confirmedHour, SHARED, address);
		this.numOfParticipants=numOfParticipants;
	}
    // MinSharedFee getter and setter
	public float getMinSharedFee() { return minSharedFee; }
    public void setMinSharedFee(float minSharedFee) { this.minSharedFee=minSharedFee; }
    // initialSharedFee getter and setter
	public float getInitialSharedFee() { return initialSharedFee; }
    public void setInitialSharedFee(float initialSharedFee) { this.initialSharedFee=initialSharedFee; }
    // numOfParticipants getter and setter
	public float getNumOfParticipants() { return numOfParticipants; }
    public void setNumOfParticipants(int numOfParticipants) { this.numOfParticipants=numOfParticipants; }

    public void addFee() {}//unimplemented yet
}
