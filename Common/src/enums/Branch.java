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
    
    /**
     * Returns a short representation of the branch name.
     * 
     * @return A shortened string representing the branch name.
     */
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
    
    /**
     * Returns a short representation of the branch based on its enum value.
     * 
     * @return A shortened string representing the branch name.
     * @throws IllegalArgumentException if the enum constant does not match any of the predefined values.
     */
    public String toShortStringTwo() {
        switch (this) {
            case NORTH:
                return "North";
            case CENTER:
                return "Center";
            case SOUTH:
                return "South";
            default:
                throw new IllegalArgumentException("Unknown branch: " + this);
        }
    }
}
