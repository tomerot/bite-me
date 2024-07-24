package entities;

import java.util.List;

public class Order {
	private List<ItemInOrder> itemsList;
    private int orderID;
    private Supplier supplier;
    private RegisteredCustomer customer;
    private Branch branchName;
    private String desiredDate;
    private String desiredHour;
    private OrderType type;
    private float totalPrice;
    private String acceptanceHour;
    private String confirmedHour;
    
    public Order(List<ItemInOrder> itemsList, int orderID, Supplier supplier, RegisteredCustomer customer, Branch branchName, String desiredDate,
            String desiredHour, OrderType type, float totalPrice, String acceptanceHour, String confirmedHour) {
    	this.itemsList=itemsList;
    	this.orderID = orderID;
    	this.supplier = supplier;
    	this.customer = customer;
    	this.branchName = branchName;
    	this.desiredDate = desiredDate;
    	this.desiredHour = desiredHour;
    	this.type = type;
    	this.totalPrice = totalPrice;
    	this.acceptanceHour = acceptanceHour;
    	this.confirmedHour = confirmedHour;
    }
    // OrderID getter and setter
    public List<ItemInOrder> getItemsList() { return itemsList; }
    public void setItemsList(List<ItemInOrder> itemsList) { this.itemsList = itemsList; }
    
    // OrderID getter and setter
    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }
    
    //supplier getter and setter
    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }
    
    //RegisteredCustomer getter and setter
    public RegisteredCustomer getCustomer() { return customer; }
    public void setCustomer(RegisteredCustomer customer) { this.customer = customer; }
    
    //Branch getter and setter
    public Branch getBranchName() { return branchName; }
    public void setBranchName(Branch branchName) { this.branchName = branchName; }

    //DesiredDate getter and setter
    public String getDesiredDate() { return desiredDate; }
    public void setDesiredDate(String desiredDate) { this.desiredDate = desiredDate; }

    //DesiredHour getter and setter
    public String getDesiredHour() { return desiredHour; }
    public void setDesiredHour(String desiredHour) { this.desiredHour = desiredHour; }
    
    //type getter and setter
    public OrderType getType() { return type; }
    public void setType(OrderType type) { this.type = type; }

    //TotalPrice getter and setter
    public float getTotalPrice() { return totalPrice; }
    public void setTotalPrice(float totalPrice) { this.totalPrice = totalPrice; }

    //AcceptenceHour getter and setter
    public String getAcceptanceHour() { return acceptanceHour; }
    public void setAcceptanceHour(String acceptanceHour) { this.acceptanceHour = acceptanceHour; }

    //ConfirmedHour getter and setter
    public String getConfirmedHour() { return confirmedHour; }
    public void setConfirmedHour(String confirmedHour) { this.confirmedHour = confirmedHour; }
}