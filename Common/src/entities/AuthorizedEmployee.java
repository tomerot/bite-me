package entities;

import enums.Branch;
import enums.UserType;

public class AuthorizedEmployee extends Person {
	private int supplierId;
	public AuthorizedEmployee(int id, String firstName, String lastName, String email, String phoneNumber, String userName, String password, int supplierId, int isLoggedIn, int registered) {
		super(id, firstName, lastName, email, phoneNumber, userName, password, UserType.EMPLOYEE, isLoggedIn, registered);
		this.supplierId=supplierId;
	}
    
    // supplierId getter and setter
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId=supplierId; }
}

