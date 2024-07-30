package entities;

import enums.Branch;
import enums.CustomerType;

public class RegisteredCustomer extends Person {
	private Branch homeBranch;
	private float walletBalance;
	private CustomerType type;
	
	public RegisteredCustomer(int id, String firstName, String lastName, String email, String phoneNumber, String userName, String password, Branch homeBranch, float walletBalance, CustomerType type) {
		super(id, firstName, lastName, email, phoneNumber, userName, password);
		this.homeBranch=homeBranch;
		this.walletBalance=walletBalance;
		this.type = type;
	}
    // homeBranch getter and setter
    public Branch getHomeBranch() { return homeBranch; }
    public void setHomeBranch(Branch homeBranch) { this.homeBranch=homeBranch; }
    
    // walletBalance getter and setter
    public float getWalletBalance() { return walletBalance; }
    public void setWalletBalance(float walletBalance) { this.walletBalance=walletBalance; }
}
