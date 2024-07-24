package entities;

public class User {
	private String userName;
	private String password;

    // Constructor
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
}
