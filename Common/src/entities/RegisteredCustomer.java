package entities;

public class RegisteredCustomer extends Person {
	private Branch homeBranch;
	private float walletBalance;
	public RegisteredCustomer(int id, String firstName, String lastName, String email, String phoneNumber, String userName, String password, Branch homeBranch, float walletBalance) {
		super(id, firstName, lastName, email, phoneNumber, userName, password);
		this.homeBranch=homeBranch;
		this.walletBalance=walletBalance;
	}
    // homeBranch getter and setter
    public Branch getHomeBranch() { return homeBranch; }
    public void setHomeBranch(Branch homeBranch) { this.homeBranch=homeBranch; }
    
    // walletBalance getter and setter
    public float getWalletBalance() { return walletBalance; }
    public void setWalletBalance(float walletBalance) { this.walletBalance=walletBalance; }
}
