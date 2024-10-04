package utility;

/*
Priority Queue node, small weight has higher priority
Using for min binary heap later
 */

import java.lang.Comparable;

public class PQNode<T> implements Comparable<PQNode<T>> {
    public final int weight;
    public final T data;

    public PQNode(int weight, T data) {
        this.weight = weight;
        this.data = data;
    }

    @Override
    public int compareTo(PQNode<T> node) {
        return weight - node.weight;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if( !(o instanceof PQNode<?>) ) {
            return false;
        }
        PQNode<?> pq_node = (PQNode<?>) o;
        if (data.equals(pq_node.data) && weight == pq_node.weight) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "{" + data + ", " + weight + "}";
    }

    public T getData() {
        return data;
    }

    public int getWeight() {
        return weight;
    }
}
