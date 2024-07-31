package enums;

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
