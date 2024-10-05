package Maze;

import utility.DisjointSet;
import utility.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

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

    public void createRandomMaze(File datafile) {
        createRandomMaze();
        MazeSerializer serializer = new MazeSerializer();
        serializer.saveMaze(maze, datafile);
    }

    public void createRandomMaze( int non_tree_edges, File datafile ) {
        createRandomMaze( non_tree_edges );
        MazeSerializer serializer = new MazeSerializer();
        serializer.saveMaze(maze, datafile);
    }

    public void createRandomMaze() {
        final int MIN_DIM = 3;
        int dimension = maze.getDimension();
        Random rand = new Random();
        ArrayList<Pair<MazeNode, MazeNode>> walls = new ArrayList<Pair<MazeNode, MazeNode>>(dimension * dimension);

        if( dimension < MIN_DIM ) {
            /* invalid dimension for random maze generation */
            System.err.println( "Invalid Dimension for Maze Generation. Valid dimension >= 3." );
            return;
        }

        // Initialize disjoint sets
        for (MazeNode node : maze) {
            ds.makeSet(node);
        }

        //Create list of all walls
        for (int row = 0; row < dimension; row++) {
            for (int column = 0; column < dimension; column++) {
                MazeNode node = maze.at(row, column);
                if (row < dimension - 1) {
                    /* insert wall below the current cell */
                    walls.add(new Pair<>(node, maze.at(row + 1, column)));
                }
                if (column < dimension -1) {
                    /* insert wall to the right current cell */
                    walls.add(new Pair<>(node, maze.at(row, column + 1)));
                }
            }
        }

        // square center solution
        LinkedList<Pair<MazeNode, MazeNode>> solutionEntry = new LinkedList<Pair<MazeNode, MazeNode>>();
        LinkedList<MazeNode> targetNodes = new LinkedList<MazeNode>();
        int lowerCenter = (dimension - 1) / 2;
        int upperCenter = dimension / 2;
        int count = 0;
        for (int row = lowerCenter; row < upperCenter; row++) {
            for (int column = lowerCenter; column < upperCenter; column++) {
                if (dimension % 2 != 0) {
                    solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row, column - 1) ) );
                    solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row, column + 1) ) );
                    solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row -1, column) ) );
                    solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row + 1, column) ) );
                    break;
                }
                targetNodes.addLast(maze.at(row, column));
                int dr = ( row == lowerCenter ) ? -1 : +1; //d-row
                int dc = ( column == lowerCenter ) ? -1 : + 1; //d-column
                solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row + dr, column ) ) );
                solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row, column + dc) ) );
            }
        }
        long prevMillis = System.currentTimeMillis();

        //Create entry point for target
        //Opens up one path to the center, and updates the disjoint sets to reflect the new connection.
        int randomIndex = rand.nextInt(solutionEntry.size());
        Pair<MazeNode, MazeNode> entryPair = solutionEntry.get(randomIndex);
        ds.union(entryPair.first, entryPair.second);
        maze.addEdge(entryPair.first, entryPair.second);
        count++;

        //Remove solution entry candidates from wall list
        //Ensures these walls are not considered again in the maze generation.
        while (!solutionEntry.isEmpty()) {
            walls.remove(solutionEntry.pop());
        }

        // Combine target nodes into one meta node (solution cells)
        //For even dimensions, combines the center cells into one set, ensuring the center is reachable.
        int initSize = targetNodes.size();
        for (int index = 0; !targetNodes.isEmpty(); index++) {
            int sign = (index < initSize / 2) ? +1 : -1;
            int dr = ( (index + 1) % initSize < initSize / 2 ) ? 0 : sign * 1;
            int dc = ( (index + 1) % initSize < initSize / 2 ) ? sign * 1: 0;
            MazeNode target = targetNodes.removeFirst();
            MazeNode neighbor = maze.at(target.y + dr, target.x + dc);
            ds.union(target, neighbor);
            maze.addEdge(target, neighbor);
            walls.remove(new Pair<>(target, neighbor));
            count++;
        }

        // List of non-tree edges
        ArrayList<Pair<MazeNode, MazeNode>> extraWalls = new ArrayList<>(dimension * dimension);

        //Random maze generation using Kruskal's algorithm
        while (!walls.isEmpty()) {
            //Choose a random wall from the maze
            randomIndex = rand.nextInt(walls.size());
            Pair<MazeNode, MazeNode> nodePair = walls.get(randomIndex);
            MazeNode vertexA = nodePair.first;
            MazeNode vertexB = nodePair.second;

            //Randomly removes walls to connect cells, ensuring cells are only connected if they're in different sets (avoiding cycles).
            if (!ds.inSameSet(vertexA, vertexB)) {
                //Combine disjoint sets and create new edge
                ds.union(vertexA, vertexB);
                maze.addEdge(vertexA, vertexB);
                count++;
            } else {
                extraWalls.add(nodePair);
            }
            walls.remove(randomIndex);
        }

        //Create multiple paths to the solution
        //By adding back some walls, we introduce cycles, creating multiple paths and increasing maze complexity.
        int numOfPath = nonTreeEdges;
        for (int index = 0; !extraWalls.isEmpty() && index < numOfPath; index++) {
            randomIndex = rand.nextInt(extraWalls.size());
            Pair<MazeNode, MazeNode> nodePair = extraWalls.get(randomIndex);
            MazeNode vertexA = nodePair.first;
            MazeNode vertexB = nodePair.second;

            // Add cycle: alternate path
            maze.addEdge(vertexA, vertexB);
            count++;

            // Remove non-tree edge picked
            extraWalls.remove(randomIndex);
        }
        extraWalls.clear();
        System.err.println("Number of non-tree edges: " + numOfPath);
        System.err.println("Time taken for Maze Generation: " + (System.currentTimeMillis() - prevMillis) / 1000.0 + " sec");
    }

    //Getter for the number of non-tree edges (cycles) used in maze generation.
    public int getTotalNonTreeEdges() {
        return nonTreeEdges;
    }
}
