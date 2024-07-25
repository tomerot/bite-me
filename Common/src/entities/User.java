package entities;

import java.io.Serializable;

import enums.UserType;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	private int isLoggedIn;
	private String userType;
	private int registered;

    // Constructor
    public User(String userName, String password, int isLoggedIn, String userType,int registered) {
        this.userName = userName;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
        this.userType = userType;
        this.registered = registered;
    }

    public User(String userName, String password) {
    	this.userName = userName;
        this.password = password;
    }
    
    // userName getter and setter
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    // password getter and setter
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public int getisLoggedIn() {return isLoggedIn;}
    public void setisLoggedIn(int isLoggedIn) {this.isLoggedIn = isLoggedIn;}
    
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    
    public int getRegistered() { return registered; }
    public void setRegistered(int registered) { this.registered = registered; }
    
}
