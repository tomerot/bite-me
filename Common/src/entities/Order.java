package entities;

import java.io.Serializable;
import java.util.List;

import enums.Branch;

/**
 * This class represents an order in the system.
 * It includes details about the items in the order, the supplier, the customer, and various order attributes.
 */
public class Order implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private List<ItemInOrder> itemsList;
    private int orderID; //orderid
    private String recipient; //recipient
    private String recipientPhone; //recipientPhone
    private SupplyMethod supplyOption; //supplyOption 
    private Supplier supplier; // can get id,city,address
    private Customer customer; 
    private Branch branchName;
    private String requestedDate; //requested date?
    private String requestedTime; //requested time?
    private OrderType type;
    private float totalPrice;
    private String approvalTimer; //approval time?
    private String arrivalTime; //arrival time?
    private String status;
    
    public Order(List<ItemInOrder> itemsList, int orderID,String recipient,String recipientPhone,SupplyMethod supplyOption, Supplier supplier, Customer customer, Branch branchName, String requestedDate,
            String requestedTime, OrderType type, float totalPrice, String approvalTimer, String arrivalTime, String status) {
    	this.itemsList=itemsList;
    	this.orderID = orderID;
    	this.recipient = recipient;
    	this.recipientPhone = recipientPhone;
    	this.supplyOption = supplyOption;
    	this.supplier = supplier;
    	this.customer = customer;
    	this.branchName = branchName;
    	this.requestedDate = requestedDate;
    	this.requestedTime = requestedTime;
    	this.type = type;
    	this.totalPrice = totalPrice;
    	this.approvalTimer = approvalTimer;
    	this.arrivalTime = arrivalTime;
    	this.status = status;
    }

	public List<ItemInOrder> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<ItemInOrder> itemsList) {
		this.itemsList = itemsList;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getRecipientPhone() {
		return recipientPhone;
	}

	public void setRecipientPhone(String recipientPhone) {
		this.recipientPhone = recipientPhone;
	}

	public SupplyMethod getSupplyOption() {
		return supplyOption;
	}

	public void setSupplyOption(SupplyMethod supplyOption) {
		this.supplyOption = supplyOption;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Branch getBranchName() {
		return branchName;
	}

	public void setBranchName(Branch branchName) {
		this.branchName = branchName;
	}

	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getRequestedTime() {
		return requestedTime;
	}

	public void setRequestedTime(String requestedTime) {
		this.requestedTime = requestedTime;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getApprovalTimer() {
		return approvalTimer;
	}

	public void setApprovalTimer(String approvalTimer) {
		this.approvalTimer = approvalTimer;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    
}