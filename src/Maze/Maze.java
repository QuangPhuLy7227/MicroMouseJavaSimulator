package Maze;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Maze implements Iterable<MazeNode> {
    private static final int EVEN = 2;
    private final int dimension;
    private int non_tree_edges = 0;
    private MazeNode[][] maze;
    private LinkedList<MazeNode> dijkstraPath = new LinkedList<MazeNode>();
    private LinkedList<MazeNode> dfsPath = new LinkedList<MazeNode>();

    public Maze(int dimension) {
        this.dimension = dimension;
        this.non_tree_edges = 0;
        maze = new MazeNode[dimension][dimension];
        for (int row = 0; row < maze.length; row++){
            for (int column = 0; column < maze[0].length; column++) {
                maze[row][column] = new MazeNode(row, column);
            }
        }
    }

    //Clear all data of the node except the coordination
    public void clear() {
        for (MazeNode node : this) {
            node.clearData();
        }
    }

    public int getDimension() {
        return dimension;
    }

    public boolean outOfBounds(int index) {
        return (index < 0 || index >= dimension);
    }

    public MazeNode at(int row, int column) {
        if (outOfBounds(row) || outOfBounds(column)) {
            System.err.println( "Maze:at() out of bounds (" + row + ", " + column + ")" );
            return null;
        }
        return maze[row][column];
    }

    public MazeNode at(Point a) {
        // y is row and x is column
        return at(a.y, a.x);
    }

    public MazeNode getBegin() {
        return at(dimension -1, 0);
    }

    public MazeNode getEnd() {
        MazeNode end = at(getDimension() / EVEN, getDimension() / EVEN);
        if (getDimension() % EVEN == 0) {
            //quad-cell solution set
            int lowerBound = getDimension() / EVEN -1;
            for (int delta = 0; delta < EVEN; delta++) {
                MazeNode topNode = at(lowerBound, lowerBound + delta);
                MazeNode lowerNode = at( lowerBound + 1, lowerBound + delta );
                if (topNode.getNeighborList().size() > EVEN) {
                    end = topNode;
                    break;
                }
                if( lowerNode.getNeighborList().size() > EVEN ) {
                    end = lowerNode;
                    break;
                }
            }
        }
        return end;
    }

    //Create an undirected edge connect 2 vertex
    public void addEdge(MazeNode a, MazeNode b) {
        if (a == null || b == null) return;
        a.addNeighbor(b);
        b.addNeighbor(a);
    }

    public void removeEdge(MazeNode a, MazeNode b) {
        if (a == null || b ==null) return;
        a.removeNeighbor(b);
        b.removeNeighbor(a);
    }

    @Override
    public Iterator<MazeNode> iterator() {
        return new MazeIterator();
    }

    private class MazeIterator implements Iterator<MazeNode> {
        private int currentRow;
        private int currentColumn;

        public MazeIterator() {
            this.currentRow = 0;
            this.currentColumn = 0;
        }

        @Override
        public boolean hasNext() {
            if (currentColumn == maze[0].length) {
                if (currentRow == maze.length) {
                    return false;
                }
            }
            return (currentRow < maze.length && currentColumn < maze[0].length);
        }

        @Override
        public MazeNode next() {
            if( !hasNext() ) {
                throw new NoSuchElementException();
            }
            MazeNode nextNode = maze[currentRow][currentColumn];
            if (currentColumn + 1 == maze[0].length){
                currentColumn = 0;
                currentRow++;
            } else {
                currentColumn++;
            }
            return nextNode;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

