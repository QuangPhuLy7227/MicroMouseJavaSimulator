package Maze;

import utility.DisjointSet;
import utility.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class MazeGenerator {
    private final Maze maze;
    private final DisjointSet ds;
    private int nonTreeEdges;
    private static Maze cachedMaze = null;

    public MazeGenerator(Maze maze) {
        this.maze = maze;
        this.ds = new DisjointSet();
    }

    public void createRandomMaze(int nonTreeEdges) {
        this.nonTreeEdges = nonTreeEdges;
        createRandomMaze();
    }

    public void createRandomMaze(File datafile) {
        createRandomMaze();
        MazeSerializer serializer = new MazeSerializer();
        serializer.saveMaze(maze, datafile);
    }

    public void createRandomMaze(int non_tree_edges, File datafile) {
        createRandomMaze(non_tree_edges);
        MazeSerializer serializer = new MazeSerializer();
        serializer.saveMaze(maze, datafile);
    }

    public void createRandomMaze() {
        final int MIN_DIM = 3;
        int dimension = maze.getDimension();
        Random rand = new Random();
        ArrayList<Pair<MazeNode, MazeNode>> walls = new ArrayList<>(dimension * dimension);

        if (dimension < MIN_DIM) {
            System.err.println("Invalid Dimension for Maze Generation. Valid dimension >= 3.");
            return;
        }

        // Initialize disjoint sets
        for (MazeNode node : maze) {
            ds.makeSet(node);
        }

        // Create list of all walls
        for (int row = 0; row < dimension; row++) {
            for (int column = 0; column < dimension; column++) {
                MazeNode node = maze.at(row, column);
                if (row < dimension - 1) {
                    walls.add(new Pair<>(node, maze.at(row + 1, column)));
                }
                if (column < dimension - 1) {
                    walls.add(new Pair<>(node, maze.at(row, column + 1)));
                }
            }
        }

        // List of non-tree edges
        ArrayList<Pair<MazeNode, MazeNode>> extraWalls = new ArrayList<>(dimension * dimension);

        // Random maze generation using Kruskal's algorithm
        int randomIndex;
        while (!walls.isEmpty()) {
            randomIndex = rand.nextInt(walls.size());
            Pair<MazeNode, MazeNode> nodePair = walls.get(randomIndex);
            MazeNode vertexA = nodePair.first;
            MazeNode vertexB = nodePair.second;

            if (!ds.inSameSet(vertexA, vertexB)) {
                ds.union(vertexA, vertexB);
                maze.addEdge(vertexA, vertexB);
            } else {
                extraWalls.add(nodePair);
            }
            walls.remove(randomIndex);
        }

        // Create multiple paths to the solution
        int numOfPath = nonTreeEdges;
        for (int index = 0; !extraWalls.isEmpty() && index < numOfPath; index++) {
            randomIndex = rand.nextInt(extraWalls.size());
            Pair<MazeNode, MazeNode> nodePair = extraWalls.get(randomIndex);
            MazeNode vertexA = nodePair.first;
            MazeNode vertexB = nodePair.second;

            // Add cycle: alternate path
            maze.addEdge(vertexA, vertexB);

            // Remove non-tree edge picked
            extraWalls.remove(randomIndex);
        }
        extraWalls.clear();
        System.err.println("Number of non-tree edges: " + numOfPath);
    }

//    public void createRandomMaze() {
//        final int MIN_DIM = 3;
//        int dimension = maze.getDimension();
//
//        // Check if the maze has already been generated and cached
//        if (cachedMaze != null && cachedMaze.getDimension() == dimension) {
//            System.err.println("Using cached maze...");
//            copyMaze(cachedMaze, maze);
//            return;
//        }
//
//        Random rand = new Random();
//        ArrayList<Pair<MazeNode, MazeNode>> walls = new ArrayList<>(dimension * dimension);
//
//        if (dimension < MIN_DIM) {
//            System.err.println("Invalid Dimension for Maze Generation. Valid dimension >= 3.");
//            return;
//        }
//
//        // Initialize disjoint sets
//        for (MazeNode node : maze) {
//            ds.makeSet(node);
//        }
//
//        // Create list of all walls
//        for (int row = 0; row < dimension; row++) {
//            for (int column = 0; column < dimension; column++) {
//                MazeNode node = maze.at(row, column);
//                if (row < dimension - 1) {
//                    walls.add(new Pair<>(node, maze.at(row + 1, column)));
//                }
//                if (column < dimension - 1) {
//                    walls.add(new Pair<>(node, maze.at(row, column + 1)));
//                }
//            }
//        }
//
//        // List of non-tree edges
//        ArrayList<Pair<MazeNode, MazeNode>> extraWalls = new ArrayList<>(dimension * dimension);
//
//        // Random maze generation using Kruskal's algorithm
//        int randomIndex;
//        while (!walls.isEmpty()) {
//            randomIndex = rand.nextInt(walls.size());
//            Pair<MazeNode, MazeNode> nodePair = walls.get(randomIndex);
//            MazeNode vertexA = nodePair.first;
//            MazeNode vertexB = nodePair.second;
//
//            if (!ds.inSameSet(vertexA, vertexB)) {
//                ds.union(vertexA, vertexB);
//                maze.addEdge(vertexA, vertexB);
//            } else {
//                extraWalls.add(nodePair);
//            }
//            walls.remove(randomIndex);
//        }
//
//        // Create multiple paths to the solution
//        int numOfPath = nonTreeEdges;
//        for (int index = 0; !extraWalls.isEmpty() && index < numOfPath; index++) {
//            randomIndex = rand.nextInt(extraWalls.size());
//            Pair<MazeNode, MazeNode> nodePair = extraWalls.get(randomIndex);
//            MazeNode vertexA = nodePair.first;
//            MazeNode vertexB = nodePair.second;
//
//            // Add cycle: alternate path
//            maze.addEdge(vertexA, vertexB);
//
//            // Remove non-tree edge picked
//            extraWalls.remove(randomIndex);
//        }
//        extraWalls.clear();
//
//        System.err.println("Number of non-tree edges: " + numOfPath);
//
//        // Cache the generated maze
//        cachedMaze = new Maze(dimension);
//        copyMaze(maze, cachedMaze);
//    }
//
//    private void copyMaze(Maze source, Maze destination) {
//        destination.clearWalls();
//        for (MazeNode node : source) {
//            MazeNode destNode = destination.at(node.row, node.column);
//            if (node.down != null) {
//                destination.addEdge(destNode, destination.at(node.row + 1, node.column));
//            }
//            if (node.right != null) {
//                destination.addEdge(destNode, destination.at(node.row, node.column + 1));
//            }
//        }
//    }

}