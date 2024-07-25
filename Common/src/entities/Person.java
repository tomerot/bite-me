package entities;

public class Person extends User {
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;

    // Constructor
    public Person(int id, String firstName, String lastName, String email, String phoneNumber, String userName, String password) {
        super(userName, password);
    	this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Id getter and setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // firstName getter and setter
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    // lastName getter and setter
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    // Email getter and setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // phoneNumber getter and setter
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
