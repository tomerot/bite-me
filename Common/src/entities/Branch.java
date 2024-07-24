package entities;

public enum Branch {
	NORTHBRANCH("North Branch"),
    CENTERBRANCH("Center Branch"),
	SOUTHBRANCH("South Branch");
    private final String name;
    Branch(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
