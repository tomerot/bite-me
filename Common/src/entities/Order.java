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
    private String customerID;
    private String recipientEmail;
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
    private String approvalTime; //approval time?
    private String arrivalTime; //arrival time?
    private String status;
	private String city;
    private String address;
    private String approvalDate;
    private String arrivalDate;
    
    
    /**
     * Constructs an {@code Order} with specified details.
     *
     * @param itemsList A list of {@link ItemInOrder} objects representing the items in the order.
     * @param orderID The unique identifier for the order.
     * @param recipient The name of the recipient of the order.
     * @param recipientPhone The phone number of the recipient.
     * @param supplyOption The supply method for the order, represented by an instance of {@link SupplyMethod}.
     * @param supplier The {@link Supplier} who provides the items in the order.
     * @param customer The {@link Customer} who placed the order.
     * @param branchName The {@link Branch} where the order is placed.
     * @param requestedDate The date when the order is requested
     * @param requestedTime The time when the order is requested
     * @param type The type of the order, represented by an instance of {@link OrderType}.
     * @param totalPrice The total price of the order.
     * @param approvalTimer The time order is approved
     * @param arrivalTime The arrival time of the order.
     * @param status The current status of the order.
     */
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
    	this.approvalTime = approvalTimer;
    	this.arrivalTime = arrivalTime;
    	this.status = status;
    }
    
    
    /**
     * Constructs an {@code Order} with the specified details.
     *
     * @param itemsList A list of {@link ItemInOrder} objects representing the items in the order.
     * @param orderID The unique identifier for the order.
     * @param recipient The name of the recipient of the order.
     * @param recipientPhone The phone number of the recipient.
     * @param supplyOption The supply method for the order, represented by an instance of {@link SupplyMethod}.
     * @param supplier The {@link Supplier} who provides the items in the order.
     * @param customer The {@link Customer} who placed the order.
     * @param branchName The {@link Branch} 
     * @param requestedDate The date when the order is requested
     * @param requestedTime The time when the order is requested
     * @param type The type of the order, represented by an instance of {@link OrderType}.
     * @param totalPrice The total price of the order.
     * @param approvalTimer The approval time
     * @param arrivalTime The arrival time .
     * @param status The current status of the order.
     * @param city The city where the order is to be delivered or picked up.
     * @param address The detailed address for the order delivery or pickup.
     */
    public Order(List<ItemInOrder> itemsList, int orderID,String recipient,String recipientPhone,SupplyMethod supplyOption, Supplier supplier, Customer customer, Branch branchName, String requestedDate,
            String requestedTime, OrderType type, float totalPrice, String approvalTimer, String arrivalTime, String status, String city, String address) {
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
    	this.approvalTime = approvalTimer;
    	this.arrivalTime = arrivalTime;
    	this.status = status;
    	this.city = city;
    	this.address = address;
    }
    
    /**
     * Constructs an {@code Order} with the specified details.
     *
     * @param itemsList A list of {@link ItemInOrder} objects representing the items in the order.
     * @param orderID The unique identifier for the order.
     * @param recipient The name of the recipient of the order.
     * @param recipientPhone The phone number of the recipient.
     * @param supplyOption The supply method for the order, represented by an instance of {@link SupplyMethod}.
     * @param supplier The {@link Supplier} who provides the items in the order.
     * @param customer The {@link Customer} who placed the order.
     * @param branchName The {@link Branch}
     * @param requestedDate The date when the order is requested
     * @param requestedTime The time when the order is requested.
     * @param type The type of the order, represented by an instance of {@link OrderType}.
     * @param totalPrice The total price of the order.
     * @param approvalTimer the approval time
     * @param arrivalTime The arrival time.
     * @param status The current status of the order.
     * @param city The city where the order is to be delivered or picked up.
     * @param address The detailed address for the order delivery or pickup.
     * @param approvalDate The date of approval.
     * @param arrivalDate the date of arrival.
     */
    public Order(List<ItemInOrder> itemsList, int orderID,String recipient,String recipientPhone,SupplyMethod supplyOption, Supplier supplier, Customer customer, Branch branchName, String requestedDate,
            String requestedTime, OrderType type, float totalPrice, String approvalTimer, String arrivalTime, String status, String city, String address, String approvalDate, String arrivalDate) {
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
    	this.approvalTime = approvalTimer;
    	this.arrivalTime = arrivalTime;
    	this.status = status;
    	this.city = city;
    	this.address = address;
    	this.approvalDate = approvalDate;
    	this.arrivalDate = arrivalDate;
    }
    
    
    //sagi
    /**
     * Constructs an {@code Order} with the specified details.
     *
     * @param orderID The unique identifier for the order.
     * @param customerID The ID of the customer who placed the order.
     * @param recipient The name of the recipient of the order.
     * @param recipientPhone The phone number of the recipient.
     * @param recipientEmail The email address of the recipient.
     * @param city The city where the order is to be delivered or picked up.
     * @param address The detailed address for the order delivery or pickup.
     * @param supplyOption The supply method for the order, represented by an instance of {@link SupplyMethod}.
     * @param type The type of the order, represented by an instance of {@link OrderType}.
     * @param requestedDate The date when the order is requested
     * @param requestedTime The time when the order is requested
     * @param approvalTimer the approval time.
     * @param arrivalTime the arrival time
     * @param totalPrice The total price of the order.
     * @param status The current status of the order.
     */
    public Order(int orderID,String customerID, String recipient,String recipientPhone,String recipientEmail, String city,String address,SupplyMethod supplyOption, OrderType type,
    		String requestedDate, String requestedTime, String approvalTimer, String arrivalTime, float totalPrice, String status) {
    	this.orderID = orderID;
    	this.customerID = customerID;
    	this.recipient = recipient;
    	this.recipientPhone = recipientPhone;
    	this.recipientEmail = recipientEmail;
    	this.city = city;
    	this.address = address;
    	this.supplyOption = supplyOption;
    	this.type = type;
    	this.requestedDate = requestedDate;
    	this.requestedTime = requestedTime;
    	this.totalPrice = totalPrice;
    	this.approvalTime = approvalTimer;
    	this.arrivalTime = arrivalTime;
    	this.status = status;
    }
   
    


    //from here getters setters
    
	public List<ItemInOrder> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<ItemInOrder> itemsList) {
		this.itemsList = itemsList;
	}

    public String getCustomerID() {
    	return customerID;
    }
    
    public void setCustomerID(String customerID) {
    	this.customerID = customerID;
    }
    
    public String getApprovalTime() {
		return approvalTime;
	}

	public void setApprovalTime(String approvalTime) {
		this.approvalTime = approvalTime;
	}

    public String getRecipientEmail() {
    	return recipientEmail;
    }
    
    public void setRecipientEmail(String recipientEmail) {
    	this.recipientEmail = recipientEmail;
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
		return approvalTime;
	}

	public void setApprovalTimer(String approvalTimer) {
		this.approvalTime = approvalTimer;
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

    public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
    
}