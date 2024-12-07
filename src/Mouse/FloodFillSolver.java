package Mouse;

import Maze.Maze;
import Maze.MazeNode;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class FloodFillSolver {
    private final int EVEN = 2;
    private Mouse mouse;
    private Maze maze;
    private Maze refMaze;
    private Orientation orientation;
    private Point center = new Point();
    private Stack<MazeNode> exploreStack = new Stack<MazeNode>();
    private LinkedList<MazeNode> mousePath = new LinkedList<>();
    private LinkedList<MazeNode> previousPath = new LinkedList<>();
    private boolean done = false;
    private int numOfRuns = 0;

//    public FloodFillSolver(Mouse mouse, Maze maze, Maze refMaze) {
//        this.mouse = mouse;
//        this.maze = maze;
//        this.refMaze = refMaze;
//        exploreStack.push(maze.at(mouse.getRow(), mouse.getColumn()));
//    }

    public FloodFillSolver(Mouse mouse, Maze maze, Maze refMaze) {
        this.mouse = mouse;
        this.maze = maze;
        this.refMaze = refMaze;
        this.orientation = Orientation.NORTH; // Assign a default orientation
        exploreStack.push(maze.at(mouse.getRow(), mouse.getColumn()));
    }


    public void setup() {
        clearMazeMemory();
        mouse.moveTo(maze.at(15, 0));
        mouse.rotateTo(Orientation.NORTH);
    }

    public void loop() {
        exploreStack.push(maze.at(15, 0));
        MazeNode cell = exploreStack.pop();
        mouse.rotateTo(cell);
        mouse.moveTo(cell);
        markNeighborWalls(cell, orientation);
    }

    /**
     * Flood Fill Algorithm iteration.
     * @return true if mouse is in progress to get to target; false if
     *         mouse is at target.
     */
    public boolean exploreNextCell() {
        if (exploreStack.isEmpty()) {
            // Target reached
            done = true;
            trackSteps();
            if (mousePath.size() == previousPath.size() && isCompletePath(mousePath)) return false;
            done = false;
            retreat();
            setPreviousPath(mousePath);
            return false;
        }

        MazeNode currentCell = exploreStack.pop();
        mouse.rotateTo(currentCell);
        mouse.moveTo(currentCell);
        mouse.setVisited(currentCell, true);
        markNeighborWalls(currentCell, orientation);

        calibrateDistance(currentCell);

        // Find the next cell to move to
        MazeNode nextCell = null;
        for (MazeNode neighbor : currentCell.getNeighborList()) {
            if (!mouse.visited(neighbor) && neighbor.distance == currentCell.distance - 1) {
                nextCell = neighbor;
                break;
            }
        }

        if (nextCell != null) {
            exploreStack.push(nextCell);
            return true;
        }

        return false;
    }


    /**
     * Floods current cell such that there exist an "open" neighbor with a
     * distance of cell.distance - 1.
     * @param cell a cell in need of distance validation.
     * @return Nothing.
     */
//    private void calibrate(MazeNode cell) {
//        int minDistance = Integer.MAX_VALUE;
//        for (MazeNode openNeighbor : cell.getNeighborList()) {
//            // Validate cell's need for calibration
//            if (openNeighbor.distance == cell.distance - 1) return;
//            if (openNeighbor.distance < minDistance) minDistance = openNeighbor.distance;
//        }
//
//        //Update non target cell to a higher elevation
//        if (cell.distance != 0) cell.distance = minDistance;
//        for (MazeNode globalNeighbor : maze.getAdjacentCellsList(cell)) {
//            //calibrate all global neighbors except for the target cells
//            if (globalNeighbor.distance == 0) continue;
//            calibrate(globalNeighbor);
//        }
//    }

    private void calibrate(MazeNode cell) {
        // Base case: If the cell has already been visited, return immediately
        if (cell.getVisited()) {
            return;
        }

        // Mark the cell as visited to avoid recalibrating it
        cell.setVisited(true);

        int minDistance = Integer.MAX_VALUE;
        for (MazeNode openNeighbor : cell.getNeighborList()) {
            if (openNeighbor.distance == cell.distance - 1) {
                // If a valid neighbor with the correct distance is found, stop further calibration
                return;
            }
            if (openNeighbor.distance < minDistance) {
                minDistance = openNeighbor.distance;
            }
        }

        // Update the distance only if the cell is not the target
        if (cell.distance != 0) {
            cell.distance = minDistance;
        }

        // Recursively calibrate all adjacent cells, excluding target cells
        for (MazeNode globalNeighbor : maze.getAdjacentCellsList(cell)) {
            if (globalNeighbor.distance == 0) {
                continue; // Skip target cells
            }
            calibrate(globalNeighbor); // Recursive call
        }
    }


    /**
     * Floods the current cell and its adjacent cell distance value towards the target;
     * This function delegates to calibrate.
     * @param cell is current positional cell.
     * @return Nothing.
     */
    private void calibrateDistance(MazeNode cell) {
        calibrate(cell);
        for( MazeNode globalNeighbor : maze.getAdjacentCellsList( cell ) ) {
            if( globalNeighbor.distance == 0 ) continue;
            calibrate( globalNeighbor );
        }
    }

    /**
     * Continue exploring maze by retreating to the starting position.
     * @return Nothing.
     */
    private void retreat() {
        Point start_position = mouse.getStartPosition();
        MazeNode newTargetCell = maze.at(start_position);
        start_position.setLocation(mouse.getX(), mouse.getY());
        updateMazeDistance(newTargetCell);
        exploreStack.push(maze.at(mouse.getRow(), mouse.getColumn()));
        numOfRuns++;
    }

    /**
     * Update distance values for each cell in the maze given the target.
     * @param target target cell that will have a distance of 0.
     * @return Nothing.
     */
    private void updateMazeDistance(MazeNode target) {
        Queue<MazeNode> q = new LinkedList<MazeNode>();
        for (MazeNode cell : maze) {
            //reset visited values of all cells in the maze
            cell.setVisited( false );
        }
        q.add(target);
        target.setVisited(true);
        target.distance = 0;
        while (!q.isEmpty()) {
            //BFS Traversal
            MazeNode cell = q.remove();
            for (MazeNode openNeighbor : cell.getNeighborList()) {
                q.add(openNeighbor);
                openNeighbor.setVisited(true);
                openNeighbor.distance = cell.distance + 1;
            }
        }
    }

    /**
     * Tracks the mouse's next maze traversal from starting point to target point.
     * @return Nothing.
     */
    public void trackSteps() {
        mousePath.clear();
        updateMousePath(maze.at(mouse.getStartPosition()), maze.at(mouse.getRow(), mouse.getColumn()));
    }

    /**
     * Appends path traversal to mousePath linked list.
     * @param start beginning of path.
     * @param end terminating cell of path.
     * @return Nothing.
     */
    private void updateMousePath(MazeNode start, MazeNode end) {
        mousePath.push(start);
        // current node is the destination
        if (start.equals(end)) return;
        //Move to the next least expensive cell
        for (MazeNode neighbor : start.getNeighborList()) {
            //if mouse did not visit neighbor do not consider it
            if (!mouse.visited(neighbor)) continue;
            // otherwise least
            if (neighbor.distance == start.distance - 1) {
                updateMousePath(neighbor, end);
                return;
            }
        }
    }

    /**
     * Emulate sensor data of mouse to mark surrounding maze walls;
     * Physical Constraint: There are only sensors on the front, left, and right
     *                      faces of the mouse.
     * @param cell Location in maze.
     * @param orientation mouse front face direction.\
     * @return Nothing.
     */
//    private void markNeighborWalls(MazeNode cell, Orientation orientation) {
//        MazeNode refCell = refMaze.at(cell.row, cell.column);
//        MazeNode[] refNeighbors = { refCell.up, refCell.right, refCell.down, refCell.left };
//        MazeNode[] neighbors = { cell.up, cell.right, cell.down, cell.left };
//
//        Orientation point = orientation.relativeLeft();
//        while (point != orientation.relativeBack()) {
//            /* sweep across the left wall, up wall, and right wall */
//            if (refNeighbors[point.ordinal()] == null) {
//                /* wall found in reference maze */
//                maze.removeEdge(cell, neighbors[point.ordinal()]);
//            }
//            point = point.next();
//        }
//    }

    private void markNeighborWalls(MazeNode cell, Orientation orientation) {
        if (orientation == null) {
            orientation = Orientation.NORTH; // Default to NORTH
        }

        MazeNode refCell = refMaze.at(cell.row, cell.column);
        MazeNode[] refNeighbors = { refCell.up, refCell.right, refCell.down, refCell.left };
        MazeNode[] neighbors = { cell.up, cell.right, cell.down, cell.left };

        Orientation point = orientation.relativeLeft();
        while (point != orientation.relativeBack()) {
            /* sweep across the left wall, up wall, and right wall */
            if (refNeighbors[point.ordinal()] == null) {
                /* wall found in reference maze */
                maze.removeEdge(cell, neighbors[point.ordinal()]);
            }
            point = point.next();
        }
    }


    /**
     * Used in periscope mode, this function marks the neighboring wall indicated
     * by the code argument and current orientation of the mouse.
     * Example: mouse is facing south and wants to mark a code="right" wall,
     *          then cell.left is a wall.
     * @param cell current location of mouse in maze.
     * @param code Relative wall detected. See local variable code_list.
     * @return Nothing.
     */
    private void markNeighborWall(MazeNode cell, String code) {
        String[] codeList = {"up", "right", "down", "left"};
        MazeNode[] neighbors = { cell.up, cell.right, cell.down, cell.left };
        Orientation point = orientation;
        do {
            // mark relative wall with respect to current orientation
            int index = (point.ordinal() - orientation.ordinal() + orientation.size()) % orientation.size();
            if (codeList[index].equals(code)) {
                maze.addWall(cell, neighbors[point.ordinal()]);
                return;
            }
            point = point.next();
        } while (point != orientation);
    }

    //Setter to set previous path to be a shallow copy of list.
    private void setPreviousPath(LinkedList<MazeNode> list) {
        previousPath.clear();
        previousPath.addAll( list );
    }

    public boolean isDone() {
        return done;
    }

    private boolean isCompletePath(LinkedList<MazeNode> path) {
        if( path == null || path.size() == 0 ) {
            /* invlaid argument */
            System.err.println( "Mouse.java:isCompletePath parameter is invalid: path: " + path );
            return false;
        }
        MazeNode mouseCell = maze.at(mouse.getRow(), mouse.getColumn());
        MazeNode startCell = maze.at(mouse.getStartPosition());
        boolean pathContainStart = path.getFirst().equals(startCell) || path.getLast().equals(startCell);
        boolean pathContainMouse = path.getFirst().equals(mouseCell) || path.getLast().equals(mouseCell);
        if (pathContainStart && pathContainMouse) {
            // path contains both start and end points
            return true;
        }
        return false;
    }

    /**
     * Getter for the most optimal path the mouse found.
     * @return a linked list that starts from the starting cell to the target
     *         cell.
     */
    public LinkedList<MazeNode> getMousePath() {
        return new LinkedList<MazeNode>( mousePath );
    }

    /**
     * Statistic that gets the total number of cells the mouse visited on the maze.
     * @return total cells the mouse visited.
     */
    public int getTotalCellsVisited() {
        int cellsVisited = 0;
        for( MazeNode cell : maze ) if( mouse.visited(cell) ) cellsVisited++;
        return cellsVisited;
    }

    /**
     * Statistic that measure how many times the mouse ran from a starting point
     * to a target, e.g 2 runs is counted when the mouse runs to the middle and back.
     * @return number of runs the mouse took to get the optimal solution.
     */
    public int getNumberOfRuns() {
        return (this.isDone()) ? numOfRuns + 1: numOfRuns;
    }

    public void start() {
        restart();
    }

    // Erases maze memory and restarts mouse simulation from mouse initial position.
    public void restart() {

    }

    /**
     * Erases all memory about the maze configuration.
     * @return Nothing.
     */
    private void clearMazeMemory() {
        if (maze.getDimension() != refMaze.getDimension()) {
            maze = new Maze(refMaze.getDimension());
        }
        maze.clearWalls();
        exploreStack.clear();
        mousePath.clear();
        previousPath.clear();
        numOfRuns = 0;
        done = false;
        // Mark manhattan distance of clear maze
        for (MazeNode cell : maze) {
            Point center = getCloserCenter(cell);
            cell.setDistance(Math.abs(center.x - cell.x) + Math.abs(center.y - cell.y));
            cell.setVisited(false);
            mouse.setVisited(cell, false);
        }
    }

    /**
     * Retrieves the closest target location relative to the passed cell location;
     * This is needed when the target is a quad-cell solution set.
     * @param cell relative cell location in maze.
     * @return updated global variable "center" with the closest target location.
     */
    private Point getCloserCenter(MazeNode cell) {
        int dimension = maze.getDimension();
        int centerX = dimension / EVEN;
        int centerY = dimension / EVEN;

        //Singular solution cell
        if (dimension % EVEN == 1) {
            center.setLocation(centerX, centerY);
            return center;
        }

        //Quad-cells solution
        if (cell.x < dimension / EVEN) {
            centerX = dimension / EVEN - 1;
        }
        if (cell.y < dimension / EVEN) {
            centerY = dimension / EVEN - 1;
        }
        center.setLocation(centerX, centerY);
        return center;
    }
}
