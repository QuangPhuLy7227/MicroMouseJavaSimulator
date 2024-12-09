package Maze;

import java.util.LinkedList;

public class MazeNode {
    /* begin - maze generation data */
    private final String ADD_EDGE_ERROR = "Error: attempt to add edge to a pair on non-adjacent nodes. ";
    private final String REMOVE_EDGE_ERROR = "Error: attempt to remove edge to a non-adjacent node. ";
    public static final int MAX_NEIGHBORS = 4;
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

    public MazeNode upLeft = null;
    public MazeNode upRight = null;
    public MazeNode downLeft = null;
    public MazeNode downRight = null;

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

    public void addDiagonalNeighbor(MazeNode vertex) {
        if (vertex == null) return;

        if (x - 1 == vertex.x && y - 1 == vertex.y) {
            upLeft = vertex;
        } else if (x + 1 == vertex.x && y - 1 == vertex.y) {
            upRight = vertex;
        } else if (x - 1 == vertex.x && y + 1 == vertex.y) {
            downLeft = vertex;
        } else if (x + 1 == vertex.x && y + 1 == vertex.y) {
            downRight = vertex;
        } else {
            System.err.println("Invalid diagonal neighbor addition for " + this + " -> " + vertex);
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

    public void removeDiagonalNeighbor(MazeNode vertex) {
        if (vertex == null) return;

        if (vertex == upLeft) {
            upLeft = null;
        } else if (vertex == upRight) {
            upRight = null;
        } else if (vertex == downLeft) {
            downLeft = null;
        } else if (vertex == downRight) {
            downRight = null;
        } else {
            System.err.println("Attempt to remove non-diagonal neighbor from " + this + " -> " + vertex);
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
        if( x ==  node.x && y == node.y ) return true;
        else return false;
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
        LinkedList<MazeNode> neighbor_list = new LinkedList<>();
        if (up != null) neighbor_list.push(up);
        if( down != null ) neighbor_list.push( down );
        if( left != null ) neighbor_list.push( left );
        if( right != null ) neighbor_list.push( right );
        return neighbor_list;
    }

    public LinkedList<MazeNode> getDiagonalNeighborList() {
        LinkedList<MazeNode> diagonals = new LinkedList<>();
        if (upLeft != null) diagonals.add(upLeft);
        if (upRight != null) diagonals.add(upRight);
        if (downLeft != null) diagonals.add(downLeft);
        if (downRight != null) diagonals.add(downRight);
        return diagonals;
    }

    public double getDiagonalX() {
        return diagonal_x;
    }

    public double getDiagonalY() {
        return diagonal_y;
    }
}
