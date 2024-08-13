package entities;

import enums.Branch;
import enums.UserType;

/**
 * Represents a supplier in the system. This class extends {@link User} and includes additional
 * attributes specific to suppliers, such as their ID, name, city, address, and home branch.
 */
public class Supplier extends User {
	private int supplierID;
	private String name;
	private String city;
	private String address;
	private Branch homeBranch;
	private String email;
	private String phone;
	private String imageURL;
  
	/**
	 * Constructs a {@code Supplier} with the specified details.
	 *
	 * @param supplierID The unique identifier for the supplier.
	 * @param name The name of the supplier/ name of restaurant.
	 * @param city The city where the supplier is located.
	 * @param address The address of the supplier.
	 * @param homeBranch The {@link Branch} where the supplier's home branch is located.
	 * @param userName The username of the supplier for authentication purposes.
	 * @param password The password of the supplier for authentication purposes.
	 */
    public Supplier(int supplierID, String name, String city, String address, Branch homeBranch, String userName, String password) {
        super(userName, password,1, UserType.SUPPLIER,1);
    	this.supplierID = supplierID;
        this.name = name;
        this.city = city;
        this.address = address;
        this.homeBranch = homeBranch;
        this.email = email;
        this.phone = phone;
    }
    
	/**
	 * Constructs a {@code Supplier} with the specified details.
	 *
	 * @param supplierID The unique identifier for the supplier.
	 * @param name The name of the supplier/ name of restaurant.
	 * @param city The city where the supplier is located.
	 * @param address The address of the supplier.
	 * @param homeBranch The {@link Branch} where the supplier's home branch is located.
	 */
    public Supplier(int supplierID, String name, String city, String address, Branch homeBranch) {
        super(null, null);
    	this.supplierID = supplierID;
        this.name = name;
        this.city = city;
        this.address = address;
        this.homeBranch = homeBranch;
    }
    
	/**
	 * Constructs a {@code Supplier} with the specified details.
	 *
	 * @param supplierID The unique identifier for the supplier.
	 * @param name The name of the supplier/ name of restaurant.
	 * @param city The city where the supplier is located.
	 * @param address The address of the supplier.
	 * @param homeBranch The {@link Branch} where the supplier's home branch is located.
	 * @param email is the supplier email
	 * @param phone is the supplier phone number
	 * @param imageURL is image attached to the supplier
	 */
    public Supplier(int supplierID, String name, String city, String address, Branch homeBranch, String email, String phone, String imageURL) {
    	super(null, null);
    	this.supplierID = supplierID;
        this.name = name;
        this.city = city;
        this.address = address;
        this.homeBranch = homeBranch;
        this.email = email;
        this.phone = phone;
        this.imageURL = imageURL;
    }

    // SupplierID getter and setter
    public int getSupplierID() { return supplierID; }
    public void setSupplierID(int supplierID) { this.supplierID = supplierID; }

    // Name getter and setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // City getter and setter
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    // Address getter and setter
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // HomeBranch getter and setter
    public Branch getHomeBranch() { return homeBranch; }
    public void setHomeBranch(Branch homeBranch) { this.homeBranch = homeBranch; }
    
    public String getImgURL() { return imageURL; }
}