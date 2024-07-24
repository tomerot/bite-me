package entities;

public class Item {
	private int itemID;
    private String name;
    private Category type;
    private float price;
    private boolean customSize;
    private boolean customDonenessDegree;
    private boolean customRestrictions;
    public Item(int itemID, String name, Category type, float price, boolean customSize, boolean customDonenessDegree, boolean customRestrictions) {
    	this.itemID=itemID;
    	this.name=name;
    	this.type=type;
    	this.price=price;
    	this.customSize=customSize;
    	this.customDonenessDegree=customDonenessDegree;
    	this.customRestrictions=customRestrictions;
    }
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
