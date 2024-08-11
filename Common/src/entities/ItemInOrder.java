package entities;

import java.util.Objects;

/**
 * This class represents an item in an order with additional customization options.
 * It extends the {@link Item} class and includes attributes for size, doneness degree, and restrictions.
 */
public class ItemInOrder extends Item {
	private String size=null;
	private String donenessDegree=null;
	private String restrictions=null;
	private int quantity;
	
	public ItemInOrder(int itemID, int supplierID, String name, Category type, String description,
					   boolean customSize, boolean customDonenessDegree, boolean customRestrictions, 
					   String size, String donenessDegree,String restrictions, float price) {
		super(itemID, supplierID, name, type, description, customSize, customDonenessDegree, customRestrictions, price);
		if(customSize)
			this.size=size;
		if(customDonenessDegree)
			this.donenessDegree=donenessDegree;
		if(customRestrictions)
			this.restrictions=restrictions;
	}
	
	public ItemInOrder(Item item) {
		super(item.getItemID(), item.getSupplierID(), item.getName(), item.getType(), item.getDescription(),
			  item.getCustomSize(), item.getCustomDonenessDegree(), item.getCustomRestrictions(), item.getPrice());
		this.size = "None";
		this.donenessDegree = "None";
		this.restrictions = "None";
	}
	//sagi
	public ItemInOrder(int itemID, String name, String size, String donenessDegree,String restrictions,int quantity, float price) {
		super(itemID,0, name, null ,null, false, false, false, price); // null/false -> we don't need those fields.
		this.size=size;
		this.donenessDegree=donenessDegree;
		this.restrictions=restrictions;
		this.quantity = quantity;
	}
	public boolean equals(Object that) {
		if(this == that) return true;
		if((that == null) || !(that instanceof ItemInOrder)) return false;
		ItemInOrder other = (ItemInOrder) that;
		return this.getItemID() == other.getItemID() &&
			   this.getSize().equals(other.getSize()) &&
			   this.getDonenessDegree().equals(other.getDonenessDegree()) &&
			   this.getRestrictions().equals(other.getRestrictions());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getItemID(), size, donenessDegree, restrictions);
	}
	
    //size getter and setter
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    
    //donenessDegree getter and setter
    public String getDonenessDegree() { return donenessDegree; }
    public void setDonenessDegree(String donenessDegree) { this.donenessDegree = donenessDegree; }
    //restrictions getter and setter
    public String getRestrictions() { return restrictions; }
    public void setRestrictions(String restrictions) { this.restrictions = restrictions; }
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		 this.quantity = quantity;
	}
}
