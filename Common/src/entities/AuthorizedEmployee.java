package entities;

public class AuthorizedEmployee extends Person {
	private Branch branchName;
	private int supplierId;
	public AuthorizedEmployee(int id, String firstName, String lastName, String email, String phoneNumber, String userName, String password, Branch branchName, int supplierId) {
		super(id, firstName, lastName, email, phoneNumber, userName, password);
		this.branchName=branchName;
		this.supplierId=supplierId;
	}
    // branchName getter and setter
    public Branch getbranchName() { return branchName; }
    public void setBranchName(Branch branchName) { this.branchName=branchName; }
    
    // supplierId getter and setter
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId=supplierId; }
}

