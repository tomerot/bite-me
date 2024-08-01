package entities;

import java.sql.Date;

import enums.Branch;
import enums.CustomerType;
import enums.UserType;

public class Customer extends Person {
	private Branch homeBranch;
	private float walletBalance;
	private CustomerType type;
	private int companyId;
	private String credit;
	private String cvv;
	private Date validDate;
	
	public Customer(String userName, int id, CustomerType type, int companyId, String firstName, String lastName, 
			String email, String phoneNumber, Branch homeBranch, String credit, String cvv,
			Date validDate, float walletBalance, int isLoggedIn, int registered, String password) {
		super(id, firstName, lastName, email, phoneNumber, userName, password, UserType.CUSTOMER , isLoggedIn, registered);
		this.homeBranch = homeBranch;
		this.walletBalance = walletBalance;
		this.type = type;
		this.companyId = companyId;
		this.credit = credit;
		this.cvv = cvv;
		this.validDate = validDate;
	}
    // homeBranch getter and setter
    public Branch getHomeBranch() { return homeBranch; }
    public void setHomeBranch(Branch homeBranch) { this.homeBranch=homeBranch; }
    
    // walletBalance getter and setter
    public float getWalletBalance() { return walletBalance; }
    public void setWalletBalance(float walletBalance) { this.walletBalance=walletBalance; }
    
    // CustomerType type  getter and setter
    public CustomerType getCustomerType() { return type; }
    public void setCustomerType(CustomerType type) { this.type=type; }
    
    // companyId getter and setter
    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId=companyId; }
    
    // Credit getter and setter
    public String getCredit() { return credit; }
    public void setCredit(String credit) { this.credit=credit; }
    
    // cvv getter and setter
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv=cvv; }
    
    // validDate getter and setter
    public Date getvalidDate() { return validDate; }
    public void setValidDate(Date validDate) { this.validDate=validDate; }
}
