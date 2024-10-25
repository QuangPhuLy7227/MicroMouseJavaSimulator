package Maze;

import java.util.LinkedList;

public class MazeNode {
    private final String ADD_EDGE_ERROR = "Error: Fail to add edge to a pair on non-adjacent nodes. ";
    private final String REMOVE_EDGE_ERROR = "Error: Fail to remove edge to a non-adjacent node. ";
    public static final int MAX_NEIGHBORS = 4;

    /* begin - maze generation data */
    public MazeNode parent = null;
    public int rank = 0;

    public final int column, x;
    public final int row, y;

    /* begin - attributes for path optimization */
    public final double diagonal_x;
    public final double diagonal_y;

    public MazeNode up = null;
    public MazeNode down = null;
    public MazeNode left = null;
    public MazeNode right = null;

    /* begin - graph search data */
    public boolean visited = false;
    public MazeNode prev = null;
    public int distance = 0;

    public MazeNode(double row, double column) {
        this.column = x = (int) column;
        this.row = y = (int) row;
        diagonal_x = column;
        diagonal_y = row;
    }

    public void addNeighbor(MazeNode vertex) {
        if (vertex == null) return;
        if (x == vertex.x) {
            //y-axis is inverted (start 0,0 and y-axis going down)
            if (y + 1 == vertex.y) down = vertex;
            else if (y - 1 == vertex.y) up = vertex;
        } else if (y == vertex.y) {
            if (x + 1 == vertex.x) right = vertex;
            else if (x - 1 == vertex.x) left = vertex;
        }
        else {
            /* vertex is not adjacent */
            System.err.println( ADD_EDGE_ERROR + this + " <-> " + vertex );
        }
    }

    public void removeNeighbor(MazeNode vertex) {
        if (vertex == null) return;
        //Vertical neighbor
        if (x == vertex.x) {
            if (y + 1 == vertex.y) down = null;
            else if (y - 1 == vertex.y) up = null;
        }
        //Horizontal neighbor
        else if (y == vertex.y) {
            if (x + 1 == vertex.x) right = null;
            else if (x - 1 == vertex.x) left = null;
        }
        else {
            /* vertex is not adjacent */
            System.err.println( REMOVE_EDGE_ERROR + this + "<->" + vertex );
        }
    }

    public void clearData() {
        up = down = left = right = null;
        prev = null;
        parent = null;
        visited = false;
        distance = 0;
        rank = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if( !(o instanceof MazeNode) ) return false;
        MazeNode node = (MazeNode) o;
        if (x == node.x && y == node.y) return true;
        return false;
    }

    @Override
    public String toString() {
        /* (row, column) */
        return "(" + this.row + ", " + this.column + ")";
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    public boolean getVisited() {
        return visited;
    }

    public void setPrev(MazeNode prev) {
        this.prev = prev;
    }
    public MazeNode getPrev() {
        return prev;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
    public int getDistance() {
        return distance;
    }

    public LinkedList<MazeNode> getNeighborList() {
        LinkedList<MazeNode> neighbor_list = new LinkedList<MazeNode>();
        if (up != null) neighbor_list.addLast(up);
        if( down != null ) neighbor_list.addLast( down );
        if( left != null ) neighbor_list.addLast( left );
        if( right != null ) neighbor_list.addLast( right );
        return neighbor_list;
    }

    public double getDiagonalX() {
        return diagonal_x;
    }

    public double getDiagonalY() {
        return diagonal_y;
    }
}
