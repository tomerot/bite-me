package enums;

/**
 * Enum representing different types of users in the system.
 */
public enum UserType {
    CUSTOMER("Customer"),
    CEO("CEO"),
    MANAGER("Manager"),
    SUPPLIER("Supplier"),
    EMPLOYEE("Employee");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
