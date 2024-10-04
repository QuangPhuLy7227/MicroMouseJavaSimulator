package Maze;

import utility.DisjointSet;

public class MazeGenerator {
    private Maze maze;
    private DisjointSet ds;
    private int nonTreeEdges;

    public MazeGenerator(Maze maze) {
        this.maze = maze;
        this.ds = new DisjointSet();
    }

    public void createRandomMaze(int nonTreeEdges) {
        this.nonTreeEdges = nonTreeEdges;
        createRandomMaze();
    }

    public void createRandomMaze() {
        int dimension = maze.getDimension();
    }
}
