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

//    public void createRandomMaze() {
//        final int MIN_DIM = 3;
//        int dimension = maze.getDimension();
//        Random rand = new Random();
//        ArrayList<Pair<MazeNode, MazeNode>> walls = new ArrayList<>(dimension * dimension);
//
//        System.err.println( "Generating Random Maze..." );
//
//        if (dimension < MIN_DIM) {
//            System.err.println("Invalid Dimension for Maze Generation. Valid dimension >= 3.");
//            return;
//        }
//        // Initialize disjoint sets
//        for (MazeNode node : maze) {
//            ds.makeSet(node);
//        }
//
//        // Create list of all walls
//        for (int row = 0; row < dimension; row++) {
//            for (int column = 0; column < dimension; column++) {
//                if (row < dimension - 1) {
//                    walls.add(new Pair<>(maze.at(row, column), maze.at(row + 1, column)));
//                }
//                if (column < dimension - 1) {
//                    walls.add(new Pair<>(maze.at(row, column), maze.at(row, column + 1)));
//                }
//            }
//        }
//
//        /* square center solution */
//        LinkedList<Pair<MazeNode, MazeNode>> solutionEntry = new LinkedList<Pair<MazeNode, MazeNode>>();
//        LinkedList<MazeNode> targetNodes = new LinkedList<MazeNode>();
//        int lowerCenter = (maze.getDimension() - 1) / 2;
//        int upperCenter = maze.getDimension() / 2;
//        int count = 0;
//
//        for( int row = lowerCenter; row <= upperCenter; row++ ) {
//            for( int column = lowerCenter; column <= upperCenter; column++ ) {
//                if( maze.getDimension() % 2 != 0 ) {
//                    /* singular solution cell */
//                    solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row, column - 1) ) );
//                    solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row, column + 1) ) );
//                    solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row - 1, column) ) );
//                    solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row + 1, column) ) );
//                    break;
//                }
//
//                targetNodes.addLast( maze.at(row, column) );
//                int dr = ( row == lowerCenter ) ? -1 : +1;
//                int dc = ( column == lowerCenter ) ? -1 : + 1;
//
//                /* quad-cell solution */
//                solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row + dr, column) ) );
//                solutionEntry.add( new Pair<>( maze.at(row, column), maze.at(row, column + dc) ) );
//            }
//        }
//
//        long prevMillis = System.currentTimeMillis();
//        int randomIndex = rand.nextInt( solutionEntry.size() );
//        Pair<MazeNode, MazeNode> entry_pair = solutionEntry.get( randomIndex );
//        ds.union( entry_pair.first, entry_pair.second );
//        maze.addEdge( entry_pair.first, entry_pair.second );
//        count++;
//
//        /* remove solution entry candidates from walls list */
//        while( solutionEntry.size() != 0 ) {
//            walls.remove( solutionEntry.pop() );
//        }
//
//        /* combine target nodes into one meta node (solution cell(s) )*/
//        for( int index = 0, init_size = targetNodes.size(); targetNodes.size() != 0; index++ ) {
//            int sign = ( index < init_size / 2 ) ? +1 : -1;
//            int dr = ( (index + 1) % init_size < init_size / 2 ) ? 0 : sign * 1;
//            int dc = ( (index + 1) % init_size < init_size / 2 ) ? sign * 1: 0;
//            MazeNode target = targetNodes.removeFirst();
//            MazeNode neighbor = maze.at(target.y + dr, target.x + dc);
//            ds.union( target, neighbor );
//            maze.addEdge( target, neighbor );
//            walls.remove( new Pair<>( target, neighbor) );
//            count++;
//        }
//
//        // List of non-tree edges
//        ArrayList<Pair<MazeNode, MazeNode>> extraWalls = new ArrayList<>(dimension * dimension);
//
//        // Random maze generation using Kruskal's algorithm
//        while (walls.size() != 0) {
//            randomIndex = rand.nextInt(walls.size());
//            Pair<MazeNode, MazeNode> nodePair = walls.get(randomIndex);
//            MazeNode vertexA = nodePair.first;
//            MazeNode vertexB = nodePair.second;
//
//            if (ds.inSameSet(vertexA, vertexB) == false) {
//                ds.union(vertexA, vertexB);
//                maze.addEdge(vertexA, vertexB);
//                count++;
//            } else {
//                extraWalls.add(nodePair);
//            }
//            walls.remove(randomIndex);
//        }
//
//        // Create multiple paths to the solution
//        int numOfPaths = maze.getNonTreeEdges();
//        for (int index = 0; extraWalls.size() != 0 && index < numOfPaths; index++) {
//            randomIndex = rand.nextInt(extraWalls.size());
//            Pair<MazeNode, MazeNode> nodePair = extraWalls.get(randomIndex);
//            MazeNode vertexA = nodePair.first;
//            MazeNode vertexB = nodePair.second;
//
//            // Add cycle: alternate path
//            maze.addEdge(vertexA, vertexB);
//            count++;
//
//            // Remove non-tree edge picked
//            extraWalls.remove(randomIndex);
//        }
//        extraWalls.clear();
//        System.err.println("Number of non-tree edges: " + numOfPaths);
//        System.err.println( "Time taken for Maze Generation: " + (System.currentTimeMillis() - prevMillis) / 1000.0 + " sec" );
//    }

    public void createRandomMaze() {
        final int MIN_DIM = 3;
        final int BIG_DIM = 16;
        final int MAX_DIM = 32;

        int dimension = maze.getDimension();
        Random rand = new Random();

        if (dimension < MIN_DIM) {
            System.err.println("Invalid Dimension for Maze Generation. Minimum dimension is 3.");
            return;
        }

        if (dimension > MAX_DIM) {
            System.err.println("Invalid Dimension for Maze Generation. Maximum dimension is 32.");
            return;
        }

        if (dimension <= BIG_DIM) {
            // Normal maze generation for dimensions <= 16
            generateMaze(rand, 0, 0, dimension);
        } else {
            if (dimension % 2 == 0) {
                // For dimensions > 16, divide into quadrants
                int halfDim = dimension / 2;

                // Generate each quadrant
                generateForBigMaze(rand, 0, 0, halfDim);                  // Top-left
                generateForBigMaze(rand, 0, halfDim, halfDim);           // Top-right
                generateForBigMaze(rand, halfDim, 0, halfDim);           // Bottom-left
                generateForBigMaze(rand, halfDim, halfDim, halfDim);     // Bottom-right

                // Connect quadrants
                connectQuadrants(rand, halfDim);
            } else {
                generateMaze(rand, 0, 0, dimension);
            }
        }
    }

    private void generateForBigMaze(Random rand, int startRow, int startCol, int size) {
        ArrayList<Pair<MazeNode, MazeNode>> walls = new ArrayList<>();
        DisjointSet localDs = new DisjointSet();

        // Initialize disjoint sets for the sub-maze
        for (int row = startRow; row < startRow + size; row++) {
            for (int col = startCol; col < startCol + size; col++) {
                MazeNode node = maze.at(row, col);
                if (node != null) {
                    localDs.makeSet(node);
                }
            }
        }

        // Create list of all walls for the sub-maze
        for (int row = startRow; row < startRow + size; row++) {
            for (int col = startCol; col < startCol + size; col++) {
                if (row < startRow + size - 1) {
                    walls.add(new Pair<>(maze.at(row, col), maze.at(row + 1, col)));
                }
                if (col < startCol + size - 1) {
                    walls.add(new Pair<>(maze.at(row, col), maze.at(row, col + 1)));
                }
            }
        }

        // Generate the quad-cell solution for this sub-maze
        int lowerCenter = startRow + (size - 1) / 2;
        int upperCenter = startRow + size / 2;
        LinkedList<Pair<MazeNode, MazeNode>> solutionEntry = new LinkedList<>();
        LinkedList<MazeNode> targetNodes = new LinkedList<>();

        for (int row = lowerCenter; row <= upperCenter; row++) {
            for (int column = startCol + (size - 1) / 2; column <= startCol + size / 2; column++) {
                MazeNode node = maze.at(row, column);
                if (node == null) continue;

                if (size % 2 != 0) {
                    /* Singular solution cell */
                    if (maze.at(row, column - 1) != null) {
                        solutionEntry.add(new Pair<>(node, maze.at(row, column - 1)));
                    }
                    if (maze.at(row, column + 1) != null) {
                        solutionEntry.add(new Pair<>(node, maze.at(row, column + 1)));
                    }
                    if (maze.at(row - 1, column) != null) {
                        solutionEntry.add(new Pair<>(node, maze.at(row - 1, column)));
                    }
                    if (maze.at(row + 1, column) != null) {
                        solutionEntry.add(new Pair<>(node, maze.at(row + 1, column)));
                    }
                    break;
                }

                targetNodes.addLast(node);
                int dr = (row == lowerCenter) ? -1 : +1;
                int dc = (column == startCol + (size - 1) / 2) ? -1 : +1;

                /* Quad-cell solution */
                if (maze.at(row + dr, column) != null) {
                    solutionEntry.add(new Pair<>(node, maze.at(row + dr, column)));
                }
                if (maze.at(row, column + dc) != null) {
                    solutionEntry.add(new Pair<>(node, maze.at(row, column + dc)));
                }
            }
        }

        // Add the initial solution entry edge
        if (!solutionEntry.isEmpty()) {
            int randomIndex = rand.nextInt(solutionEntry.size());
            Pair<MazeNode, MazeNode> entryPair = solutionEntry.get(randomIndex);
            if (entryPair.first != null && entryPair.second != null) {
                localDs.union(entryPair.first, entryPair.second);
                maze.addEdge(entryPair.first, entryPair.second);
            }
        }

        // Remove used edges from walls
        solutionEntry.forEach(walls::remove);

        // Combine target nodes into one meta node (solution cell)
        while (!targetNodes.isEmpty()) {
            MazeNode target = targetNodes.removeFirst();
            LinkedList<MazeNode> neighbors = maze.getAdjacentCellsList(target);
            for (MazeNode neighbor : neighbors) {
                if (!localDs.inSameSet(target, neighbor)) {
                    localDs.union(target, neighbor);
                    maze.addEdge(target, neighbor);
                    walls.remove(new Pair<>(target, neighbor));
                }
            }
        }

        // Perform Kruskal's algorithm to generate the maze
        ArrayList<Pair<MazeNode, MazeNode>> extraWalls = new ArrayList<>();
        while (!walls.isEmpty()) {
            int randomIndex = rand.nextInt(walls.size());
            Pair<MazeNode, MazeNode> wall = walls.remove(randomIndex);
            MazeNode vertexA = wall.first;
            MazeNode vertexB = wall.second;

            if (!localDs.inSameSet(vertexA, vertexB)) {
                localDs.union(vertexA, vertexB);
                maze.addEdge(vertexA, vertexB);
            } else {
                extraWalls.add(wall);
            }
        }

        // Add additional paths to the maze
        int numOfPaths = maze.getNonTreeEdges();
        for (int i = 0; i < numOfPaths && !extraWalls.isEmpty(); i++) {
            int randomIndex = rand.nextInt(extraWalls.size());
            Pair<MazeNode, MazeNode> wall = extraWalls.remove(randomIndex);
            maze.addEdge(wall.first, wall.second);
        }
    }

    private void generateMaze(Random rand, int startRow, int startCol, int size) {
        ArrayList<Pair<MazeNode, MazeNode>> walls = new ArrayList<>();
        DisjointSet localDs = new DisjointSet();

        // Initialize disjoint sets for the sub-maze
        for (int row = startRow; row < startRow + size; row++) {
            for (int col = startCol; col < startCol + size; col++) {
                MazeNode node = maze.at(row, col);
                if (node != null) {
                    localDs.makeSet(node);
                }
            }
        }

        // Create list of all walls for the sub-maze
        for (int row = startRow; row < startRow + size; row++) {
            for (int col = startCol; col < startCol + size; col++) {
                if (row < startRow + size - 1) {
                    walls.add(new Pair<>(maze.at(row, col), maze.at(row + 1, col)));
                }
                if (col < startCol + size - 1) {
                    walls.add(new Pair<>(maze.at(row, col), maze.at(row, col + 1)));
                }
            }
        }

        // Generate the quad-cell solution for this sub-maze
        int lowerCenter = startRow + (size - 1) / 2;
        int upperCenter = startRow + size / 2;
        LinkedList<Pair<MazeNode, MazeNode>> solutionEntry = new LinkedList<>();
        LinkedList<MazeNode> targetNodes = new LinkedList<>();

        for (int row = lowerCenter; row <= upperCenter; row++) {
            for (int column = startCol + (size - 1) / 2; column <= startCol + size / 2; column++) {
                MazeNode node = maze.at(row, column);
                if (node == null) continue;

                if (size % 2 != 0) {
                    /* Singular solution cell */
                    if (maze.at(row, column - 1) != null) {
                        solutionEntry.add(new Pair<>(node, maze.at(row, column - 1)));
                    }
                    if (maze.at(row, column + 1) != null) {
                        solutionEntry.add(new Pair<>(node, maze.at(row, column + 1)));
                    }
                    if (maze.at(row - 1, column) != null) {
                        solutionEntry.add(new Pair<>(node, maze.at(row - 1, column)));
                    }
                    if (maze.at(row + 1, column) != null) {
                        solutionEntry.add(new Pair<>(node, maze.at(row + 1, column)));
                    }
                    break;
                }

                targetNodes.addLast(node);
                int dr = (row == lowerCenter) ? -1 : +1;
                int dc = (column == startCol + (size - 1) / 2) ? -1 : +1;

                /* Quad-cell solution */
                if (maze.at(row + dr, column) != null) {
                    solutionEntry.add(new Pair<>(node, maze.at(row + dr, column)));
                }
                if (maze.at(row, column + dc) != null) {
                    solutionEntry.add(new Pair<>(node, maze.at(row, column + dc)));
                }
            }
        }

        int randomIndex = rand.nextInt( solutionEntry.size() );
        Pair<MazeNode, MazeNode> entry_pair = solutionEntry.get( randomIndex );
        ds.union( entry_pair.first, entry_pair.second );
        maze.addEdge( entry_pair.first, entry_pair.second );

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
        }

        // Perform Kruskal's algorithm to generate the maze
        ArrayList<Pair<MazeNode, MazeNode>> extraWalls = new ArrayList<>();
        while (!walls.isEmpty()) {
            randomIndex = rand.nextInt(walls.size());
            Pair<MazeNode, MazeNode> wall = walls.remove(randomIndex);
            MazeNode vertexA = wall.first;
            MazeNode vertexB = wall.second;

            if (!localDs.inSameSet(vertexA, vertexB)) {
                localDs.union(vertexA, vertexB);
                maze.addEdge(vertexA, vertexB);
            } else {
                extraWalls.add(wall);
            }
        }

        // Add additional paths to the maze
        int numOfPaths = maze.getNonTreeEdges();
        for (int i = 0; i < numOfPaths && !extraWalls.isEmpty(); i++) {
            randomIndex = rand.nextInt(extraWalls.size());
            Pair<MazeNode, MazeNode> wall = extraWalls.remove(randomIndex);
            maze.addEdge(wall.first, wall.second);
        }
    }

    private void connectQuadrants(Random rand, int halfDim) {
        int centerRow = halfDim - 1;
        int centerCol = halfDim - 1;

        // Connect top-left and top-right quadrants
        for (int row = centerRow; row <= centerRow + 1; row++) {
            MazeNode left = maze.at(row, centerCol);
            MazeNode right = maze.at(row, centerCol + 1);
            if (isValidEdge(left, right)) {
                maze.addEdge(left, right);
            }
        }

        // Connect top-left and bottom-left quadrants
        for (int col = centerCol; col <= centerCol + 1; col++) {
            MazeNode top = maze.at(centerRow, col);
            MazeNode bottom = maze.at(centerRow + 1, col);
            if (isValidEdge(top, bottom)) {
                maze.addEdge(top, bottom);
            }
        }

        // Connect top-right and bottom-right quadrants
        for (int col = centerCol + 1; col < centerCol + 2; col++) {
            MazeNode top = maze.at(centerRow, col);
            MazeNode bottom = maze.at(centerRow + 1, col);
            if (isValidEdge(top, bottom)) {
                maze.addEdge(top, bottom);
            }
        }

        // Connect bottom-left and bottom-right quadrants
        for (int row = centerRow + 1; row < centerRow + 2; row++) {
            MazeNode left = maze.at(row, centerCol);
            MazeNode right = maze.at(row, centerCol + 1);
            if (isValidEdge(left, right)) {
                maze.addEdge(left, right);
            }
        }
    }

    private void addValidWallsToSolution(LinkedList<Pair<MazeNode, MazeNode>> solutionEntry, MazeNode node) {
        LinkedList<MazeNode> neighbors = maze.getAdjacentCellsList(node);
        for (MazeNode neighbor : neighbors) {
            solutionEntry.add(new Pair<>(node, neighbor));
        }
    }

    private boolean isAdjacent(int row, int col) {
        return !maze.outOfBounds(row) && !maze.outOfBounds(col);
    }

    private boolean isValidEdge(MazeNode a, MazeNode b) {
        if (a == null || b == null) return false;

        // Check adjacency based on coordinates
        int dr = Math.abs(a.row - b.row);
        int dc = Math.abs(a.column - b.column);

        // Adjacent if one difference is exactly 1 and the other is 0
        return (dr == 1 && dc == 0) || (dr == 0 && dc == 1);
    }

}