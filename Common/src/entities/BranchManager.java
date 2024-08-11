package entities;

import enums.Branch;
import enums.UserType;

/**
 * This class represents a branch manager in the system.
 * It extends the {@link Person} class and adds the branch type attribute.
 */
public class BranchManager extends Person{
	
	private Branch branchType;
	/**
	 * Constructs a {@link BranchManager} instance with the specified parameters.
	 * 
	 * This constructor initializes a {@link BranchManager} by calling the constructor of the superclass {@link Person} to set common 
	 * fields such as ID, first name, last name, email, phone number, username, password, user type, login status, 
	 * and registration status. It also sets the additional field {@code branchType} specific to {@link BranchManager}.
	 * @param id the unique identifier of the branch manager
	 * @param firstName the first name of the branch manager
	 * @param lastName the last name of the branch manager
	 * @param email the email address of the branch manager
	 * @param phoneNumber the phone number of the branch manager
	 * @param userName the username of the branch manager
	 * @param password the password of the branch manager
	 * @param branchType the branch type associated with the branch manager
	 * @param isLoggedIn the login status of the branch manager
	 * @param registered the registration status of the branch manager
	 */
	public BranchManager(int id, String firstName, String lastName, String email, String phoneNumber, String userName, String password, Branch branchType,int isLoggedIn, int registered) {
		super(id, firstName, lastName, email, phoneNumber, userName, password, UserType.MANAGER ,isLoggedIn, registered);
		this.branchType = branchType;
	}
	
	public Branch getbranchType() {return branchType;}
	
	public void setBranch(Branch branchType) {this.branchType=branchType;}
}
