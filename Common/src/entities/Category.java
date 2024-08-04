package entities;

/**
 * This enum represents the different categories of items.
 * Each category has a name associated with it.
 */
public enum Category {
	MAINCOURSE("Main Course"),
    FIRSTCOURSE("First Course"),
	SALAD("Salad"),
	DESSERT("Dessert"),
	BEVERAGE("Beverage");
    private final String name;
    Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
