package entities;

/**
 * This enum represents the types of orders.
 */
public enum OrderType {
    PRE_ORDER("PreOrder"),
    REGULAR("Regular");

    private final String value;

    OrderType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
