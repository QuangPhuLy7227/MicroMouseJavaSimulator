package Maze;

import utility.DisjointSet;
import utility.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class MazeGenerator {
    private final Maze maze;
    private final DisjointSet ds;

    public MazeGenerator(Maze maze) {
        this.maze = maze;
        this.ds = new DisjointSet();
    }

    public void createRandomMaze(int nonTreeEdges) {
        maze.setNonTreeEdges(nonTreeEdges);
        createRandomMaze();
    }

    public void createRandomMaze(File datafile) throws IOException {
        createRandomMaze();
        maze.getMazeSerializer().saveMaze(datafile);
    }

    public void createRandomMaze(int non_tree_edges, File datafile) throws IOException {
        createRandomMaze(non_tree_edges);
        maze.getMazeSerializer().saveMaze(datafile);
    }

    public void createRandomMaze() {
        final int MIN_DIM = 3;
        int dimension = maze.getDimension();
        Random rand = new Random();
        ArrayList<Pair<MazeNode, MazeNode>> walls = new ArrayList<>(dimension * dimension);

        System.err.println( "Generating Random Maze..." );

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
                if (row < dimension - 1) {
                    walls.add(new Pair<>(maze.at(row, column), maze.at(row + 1, column)));
                }
                if (column < dimension - 1) {
                    walls.add(new Pair<>(maze.at(row, column), maze.at(row, column + 1)));
                }
            }
        }

        /* square center solution */
        LinkedList<Pair<MazeNode, MazeNode>> solutionEntry = new LinkedList<Pair<MazeNode, MazeNode>>();
        LinkedList<MazeNode> targetNodes = new LinkedList<MazeNode>();
        int lowerCenter = (maze.getDimension() - 1) / 2;
        int upperCenter = maze.getDimension() / 2;
        int count = 0;

        for( int row = lowerCenter; row <= upperCenter; row++ ) {
            for( int column = lowerCenter; column <= upperCenter; column++ ) {
                if( maze.getDimension() % 2 != 0 ) {
                    /* singular solution cell */
                    solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row, column - 1) ) );
                    solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row, column + 1) ) );
                    solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row - 1, column) ) );
                    solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row + 1, column) ) );
                    break;
                }

                targetNodes.addLast( maze.at(row, column) );
                int dr = ( row == lowerCenter ) ? -1 : +1;
                int dc = ( column == lowerCenter ) ? -1 : + 1;

                /* quad-cell solution */
                solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row + dr, column) ) );
                solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row, column + dc) ) );
            }
        }

        long prevMillis = System.currentTimeMillis();
        int randomIndex = rand.nextInt( solutionEntry.size() );
        Pair<MazeNode, MazeNode> entry_pair = solutionEntry.get( randomIndex );
        ds.union( entry_pair.first, entry_pair.second );
        maze.addEdge( entry_pair.first, entry_pair.second );
        count++;

        /* remove solution entry candidates from walls list */
        while( solutionEntry.size() != 0 ) {
            walls.remove( solutionEntry.pop() );
        }

        /* combine target nodes into one meta node (solution cell(s) )*/
        for( int index = 0, init_size = targetNodes.size(); targetNodes.size() != 0; index++ ) {
            int sign = ( index < init_size / 2 ) ? +1 : -1;
            int dr = ( (index + 1) % init_size < init_size / 2 ) ? 0 : sign * 1;
            int dc = ( (index + 1) % init_size < init_size / 2 ) ? sign * 1: 0;
            MazeNode target = targetNodes.removeFirst();
            MazeNode neighbor = maze.at(target.y + dr, target.x + dc);
            ds.union( target, neighbor );
            maze.addEdge( target, neighbor );
            walls.remove( new Pair<>( target, neighbor) );
            count++;
        }

        // List of non-tree edges
        ArrayList<Pair<MazeNode, MazeNode>> extraWalls = new ArrayList<>(dimension * dimension);

        // Random maze generation using Kruskal's algorithm
        while (walls.size() != 0) {
            randomIndex = rand.nextInt(walls.size());
            Pair<MazeNode, MazeNode> nodePair = walls.get(randomIndex);
            MazeNode vertexA = nodePair.first;
            MazeNode vertexB = nodePair.second;

            if (ds.inSameSet(vertexA, vertexB) == false) {
                ds.union(vertexA, vertexB);
                maze.addEdge(vertexA, vertexB);
                count++;
            } else {
                extraWalls.add(nodePair);
            }
            walls.remove(randomIndex);
        }

        // Create multiple paths to the solution
        int numOfPaths = maze.getNonTreeEdges();
        for (int index = 0; extraWalls.size() != 0 && index < numOfPaths; index++) {
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
        System.err.println("Number of non-tree edges: " + numOfPaths);
        System.err.println( "Time taken for Maze Generation: " + (System.currentTimeMillis() - prevMillis) / 1000.0 + " sec" );
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