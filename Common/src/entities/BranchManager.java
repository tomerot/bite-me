package entities;

import enums.Branch;
import enums.UserType;

public class BranchManager extends Person{
	
	private Branch branchType;
	
	public BranchManager(int id, String firstName, String lastName, String email, String phoneNumber, String userName, String password, Branch branchType,int isLoggedIn, int registered) {
		super(id, firstName, lastName, email, phoneNumber, userName, password, UserType.MANAGER ,isLoggedIn, registered);
		this.branchType = branchType;
	}
	
	public Branch getbranchType() {return branchType;}
	
	public void setBranch(Branch branchType) {this.branchType=branchType;}
}
