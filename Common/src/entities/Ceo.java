package entities;


public class Ceo extends Person {
	
	private String firstName;
	private String lastName;
	
	public Ceo(int id, String firstName, String lastName, String email, String phoneNumber, String userName, String password) {
		super(id, firstName, lastName, email, phoneNumber, userName, password);
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String GetFirstName() {
		return this.firstName;
	}
	
	public String GetLastName() {
		return this.lastName;
	}
}
