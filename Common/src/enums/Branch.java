package enums;

public enum Branch {
	NORTH("North Branch"),
    CENTER("Center Branch"),
	SOUTH("South Branch");
    private final String name;
    Branch(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
