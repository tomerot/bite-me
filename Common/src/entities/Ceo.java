package entities;

import enums.UserType;

/**
 * This class represents a CEO in the system.
 * It extends the {@link Person} class.
 */
public class Ceo extends Person {
	public Ceo(int id, String firstName, String lastName, String email, String phoneNumber, String userName, String password, int isLoggedIn, int registered) {
		super(id, firstName, lastName, email, phoneNumber, userName, password, UserType.CEO, isLoggedIn, registered);
	}
}
