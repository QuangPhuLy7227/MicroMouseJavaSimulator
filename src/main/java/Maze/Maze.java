package Maze;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Maze implements Iterable<MazeNode> {
    private static final int EVEN = 2;
    private final int dimension;
    private int nonTreeEdges;
    private final MazeNode[][] maze;
    public MazeGenerator mazeGenerator;
    public MazeSerializer mazeSerializer;
    public PathFinder pathFinder;

    public Maze(int dimension) {
        this.dimension = dimension;
        this.nonTreeEdges = 0;
        maze = new MazeNode[dimension][dimension];
        for (int row = 0; row < maze.length; row++){
            for (int column = 0; column < maze[0].length; column++) {
                maze[row][column] = new MazeNode(row, column);
            }
        }
        mazeGenerator = new MazeGenerator(this);
        mazeSerializer = new MazeSerializer(this);
        pathFinder = new PathFinder(this);
    }

    //Clear all data of the node except the coordination
    public void clear() {
        for (MazeNode node : this) {
            node.clearData();
        }
        pathFinder.getDijkstraPath().clear();
        pathFinder.getDfsPath().clear();
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
                /* find target node with 3 children in quad-cell solution */
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
        /* undirected edge added */
        a.addNeighbor(b);
        b.addNeighbor(a);
    }

    public void removeEdge(MazeNode a, MazeNode b) {
        if (a == null || b ==null) return;
        /* removing undirected edge */
        a.removeNeighbor(b);
        b.removeNeighbor(a);
    }

    public boolean wallBetween(Point alpha, Point beta) {
        return wallBetween(at(alpha), at(beta));
    }

    public boolean wallBetween(MazeNode vertexA, MazeNode vertexB) {
        if (vertexA == null || vertexB == null) return false;
        LinkedList<MazeNode> neighborsOfA = vertexA.getNeighborList();
        for (MazeNode neighbor : neighborsOfA) {
            if (neighbor == vertexB) {
                // There is a path directly connecting A and B, therefore no wall
                return false;
            }
        }
        return true;
    }

    //Removes all walls in the maze, connecting every cell to its adjacent cells.
    public void clearWalls() {
        for (int row = 0; row < maze.length; row++) {
            for (int column = 0; column < maze[0].length; column++) {
                MazeNode currentNode = maze[row][column];
                if (!outOfBounds(row + 1)) {
                    //Vertical neighbor
                    addEdge(currentNode, maze[row+1][column]);
                }
                if (!outOfBounds(column + 1)) {
                    //Horizontal neighbor
                    addEdge(currentNode, maze[row][column + 1]);
                }
            }
        }
    }

    //Adds a wall between two adjacent cells by removing the edge connecting them.
    public void addWall(MazeNode vertex_A, MazeNode vertex_B) {
        removeEdge(vertex_A, vertex_B);
    }

    //Retrieves a list of all adjacent cells to a given cell, regardless of walls.
    public LinkedList<MazeNode> getAdjacentCellsList(MazeNode vertex) {
        int MAX_CELLS = 4;
        LinkedList<MazeNode> list = new LinkedList<MazeNode>();

        for (int index = 0; index < MAX_CELLS; index++) {
            //Append all adjacent neighbors to list
            int deviation = (index < EVEN) ? +1 : -1;
            int dr = (index % EVEN == 0) ? deviation : 0;
            int dc = (index % EVEN == 1) ? deviation : 0;
            if (!outOfBounds(vertex.row + dr) && !outOfBounds(vertex.column + dc)) {
                list.add(maze[vertex.row + dr][vertex.column + dc]);
            }
        }

        return list;
    }

    @Override
    @NotNull
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

    public MazeSerializer getMazeSerializer() {
        return mazeSerializer;
    }

    public MazeGenerator getMazeGenerator() {
        return mazeGenerator;
    }

    public PathFinder getPathFinder() {
        return pathFinder;
    }
    public int getNonTreeEdges() {
        return nonTreeEdges;
    }

    public void setNonTreeEdges(int nte) {
        nonTreeEdges = nte;
    }

    public LinkedList<MazeNode> getDijkstraPath() {
        return new LinkedList<MazeNode>( pathFinder.getDijkstraPath() );
    }

    public LinkedList<MazeNode> getDFSPath() {
        return new LinkedList<MazeNode>( pathFinder.getDfsPath() );
    }

    public int getTotalNonTreeEdges() {
        return nonTreeEdges;
    }
}

