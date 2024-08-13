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
	
    /**
     * Constructs an {@code ItemInOrder} with specified details and customizations.
     *
     * @param itemID the item id
     * @param supplierID The unique identifier for the supplier of the item.
     * @param name The name of the item.
     * @param type The category of the item, represented by an instance of {@link Category}.
     * @param description A brief description of the item.
     * @param customSize A boolean indicating whether the item has a custom size option.
     * @param customDonenessDegree A boolean indicating whether the item has a custom doneness degree option.
     * @param customRestrictions A boolean indicating whether the item has custom restrictions.
     * @param size The size of the item if customization is allowed.
     * @param donenessDegree The doneness degree of the item if customization is allowed.
     * @param restrictions Any restrictions applied to the item if customization is allowed.
     * @param price The price of the item.
     */
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
	
	 /**
     * Constructs an {@code ItemInOrder} from an existing {@link Item} object.
     *
     * @param item The {@link Item} object from which to create the {@code ItemInOrder}.
     */
	public ItemInOrder(Item item) {
		super(item.getItemID(), item.getSupplierID(), item.getName(), item.getType(), item.getDescription(),
			  item.getCustomSize(), item.getCustomDonenessDegree(), item.getCustomRestrictions(), item.getPrice());
		super.setImageURL(item.getImageURL());
		this.size = "None";
		this.donenessDegree = "None";
		this.restrictions = "None";
	}
	
	//sagi
    /**
     * Constructs an {@code ItemInOrder} with specified details, ignoring supplier and category fields.
     *
     * @param itemID the item id
     * @param name The name of the item.
     * @param size The size of the item.
     * @param donenessDegree The doneness degree of the item.
     * @param restrictions Any restrictions applied to the item.
     * @param quantity The quantity of the item in the order.
     * @param price The price of the item.
     */
	public ItemInOrder(int itemID, String name, String size, String donenessDegree,String restrictions,int quantity, float price) {
		super(itemID,0, name, null ,null, false, false, false, price); // null/false -> we don't need those fields.
		this.size=size;
		this.donenessDegree=donenessDegree;
		this.restrictions=restrictions;
		this.quantity = quantity;
	}
	
	   /**
     * Checks if this {@code ItemInOrder} is equal to another object.
     * Two {@code ItemInOrder} objects are considered equal if they have the same item ID, size, doneness degree, and restrictions.
     *
     * @param that The object to compare this against.
     * @return ture if the specified object is equal to this ItemInOrder; false otherwise.
     */
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
