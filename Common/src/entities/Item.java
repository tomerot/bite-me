package entities;
/**
 * This class represents an item in the system.
 * It includes details such as the item's ID, name, type, price, and customization options.
 */

import java.io.Serializable;

public class Item implements Serializable{
	private int itemID;
	private int supplierID;
    private String name;
    private Category type;
    private String description;
    private float price;
    private boolean customSize;
    private boolean customDonenessDegree;
    private boolean customRestrictions;
    
    public Item(int itemID,int supplierID ,String name, Category type, String description, boolean customSize, 
    		    boolean customDonenessDegree, boolean customRestrictions, float price) {
    	this.itemID=itemID;
    	this.supplierID=supplierID;
    	this.name=name;
    	this.type=type;
    	this.description=description;
    	this.price=price;
    	this.customSize=customSize;
    	this.customDonenessDegree=customDonenessDegree;
    	this.customRestrictions=customRestrictions;
    }
    
    
 // description getter and setter
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

	// supplierID getter and setter
    public int getSupplierID() {return supplierID;}
	public void setSupplierID(int supplierID) {this.supplierID = supplierID;}
	
	// itemID getter and setter
    public int getItemID() { return itemID; }
    public void setItemID(int itemID) { this.itemID = itemID; }
    
    //name getter and setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    //type getter and setter
    public Category getType() { return type; }
    public void setType(Category type) { this.type = type; }
    
    //Price getter and setter
    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }
    
    //customSize getter and setter
    public boolean getCustomSize() { return customSize; }
    public void setCustomSize(boolean customSize) { this.customSize = customSize; }

    //customDonenessDegree getter and setter
    public boolean getCustomDonenessDegree() { return customDonenessDegree; }
    public void setCustomDonenessDegree(boolean customDonenessDegree) { this.customDonenessDegree = customDonenessDegree; }
    
    //customRestrictions getter and setter
    public boolean getCustomRestrictions() { return customRestrictions; }
    public void setCustomRestrictions(boolean customRestrictions) { this.customRestrictions = customRestrictions; }
    
}
