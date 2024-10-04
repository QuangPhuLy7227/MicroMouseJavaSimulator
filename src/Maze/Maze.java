package Maze;

import java.util.Iterator;
import java.util.LinkedList;

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



    @Override
    public Iterator<MazeNode> iterator() {
        return null;
    }
}

