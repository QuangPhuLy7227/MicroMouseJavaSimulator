package Mouse;

/**
 * Enum class defining the possible orientations of the mouse.
 */
public enum Orientation {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    NORTHEAST,
    SOUTHEAST,
    SOUTHWEST,
    NORTHWEST;

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
//        int length = size();
//        return values()[(ordinal() + (length / 2)) % length];
        switch (this) {
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            default:
                return this; // For diagonal, don't rotate.
        }
    }

    //Moves orientation clockwise.
    public Orientation next() {
//        return relativeRight();
        switch (this) {
            case NORTH:
                return EAST;
            case EAST:
                return SOUTH;
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
            default:
                return this; // For diagonal, don't rotate.
        }
    }

    // Checks if the orientation is a diagonal direction.
    public boolean isDiagonal() {
        return this == NORTHEAST || this == NORTHWEST || this == SOUTHEAST || this == SOUTHWEST;
    }

    // Checks if the orientation is a cardinal direction.
    public boolean isCardinal() {
        return this == NORTH || this == EAST || this == SOUTH || this == WEST;
    }
}
