package Mouse;

/**
 * Enum class defining the possible orientations of the mouse.
 */
public enum Orientation {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    // Checks if the string equivalent enum exists in orientation.
    public static boolean contains(String value) {
        for (Orientation orientation : Orientation.values()) {
            if (orientation.name().equals(value)) {
                return true;
            }
        }
        return false;
    }

    // Number of orientation possibilities
    public int size() {
        return values().length;
    }

    // Relative right to the current orientation.
    public Orientation relativeRight() {
        int length = size();
        return values()[(ordinal() + 1) % length];
    }

    // Relative left to the current orientation
    public Orientation relativeLeft() {
        int length = size();
        return values()[(ordinal() + (length - 1)) % length];
    }

    // Relative back to the current orientation
    public Orientation relativeBack() {
        int length = size();
        return values()[(ordinal() + (length / 2)) % length];
    }

    //Moves orientation clockwise.
    public Orientation next() {
        return relativeRight();
    }
}
