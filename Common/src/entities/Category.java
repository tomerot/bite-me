package entities;

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
