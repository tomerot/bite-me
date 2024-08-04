package enums;

/**
 * Enum representing the different branches available in the system.
 */
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
    
    public String toShortString() {
        String name=null;
        if(this.name.equals("North Branch"))
        	name="North";
        else if(this.name.equals("Center Branch"))
        	name="Center";
        else
        	name="South";
        return name;

    }
}
