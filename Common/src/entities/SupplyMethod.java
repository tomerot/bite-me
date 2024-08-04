package entities;

/**
 * Enum representing the various supply methods available for deliveries.
 */
public enum SupplyMethod {
	TAKEAWAY("TakeAway"),
    ROBOT("Robot"),
	BASIC("Basic"),
	SHARED("Shared");

    private final String name;
    SupplyMethod(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
