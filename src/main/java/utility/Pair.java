package utility;

// Generic pair

public class Pair<T, K> {
    public final T first;
    public final K second;

    public Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if( !(o instanceof Pair<?, ?> pair) ) {
            return false;
        }
        boolean elementOneExists = pair.first.equals( first ) || pair.first.equals( second );
        boolean elementTwoExists = pair.second.equals( first ) || pair.second.equals( second );

        return elementOneExists && elementTwoExists;
    }

    @Override
    public String toString() {
        return "( " + first + ", " + second + " )";
    }
}
