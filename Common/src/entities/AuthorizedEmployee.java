package entities;

import enums.Branch;
import enums.UserType;

/**
 * This class represents an authorized employee in the system.
 * It extends the {@link Person} class and adds the supplier ID attribute.
 */
public class AuthorizedEmployee extends Person {
	private int supplierId;
	public AuthorizedEmployee(int id, String firstName, String lastName, String email, String phoneNumber, String userName, String password, int supplierId) {
		super(id, firstName, lastName, email, phoneNumber, userName, password, UserType.EMPLOYEE);
		this.supplierId=supplierId;
	}
    
    // supplierId getter and setter
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId=supplierId; }
}

