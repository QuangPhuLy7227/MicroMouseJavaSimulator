package Mouse;

import Maze.Maze;
import Maze.MazeNode;

import java.awt.*;
import java.util.LinkedList;
import java.util.Stack;

// Micromouse class to emulate autonomous robot behavior.
public class Mouse {
    private static final double PROPORTION = 0.3;
    private static final int EVEN = 2;
    public boolean periscopeDisplayCellValues = false;

    public int x, y;
    private int row, column;
    private Maze ref_maze, maze;
    private MouseShape mouse;
    private Point center = new Point();
    private Point origin, start_position;
    private Orientation orientation;
    private Stack<MazeNode> explore_stack = new Stack<MazeNode>();
    private boolean visited[][];

    private int num_of_runs = 0;
    private LinkedList<MazeNode> mousePath = new LinkedList<MazeNode>();
    private LinkedList<MazeNode> previousPath = new LinkedList<MazeNode>();
    private boolean done = false;

    // Creates mouse object on GUI
    public Mouse(int row, int column, Maze ref_maze, Maze maze) {
        this.row = this.y = row;
        this.column = this.x = column;
        this.ref_maze = ref_maze;
        this.maze = maze;
        this.mouse = new MouseShape();
        this.origin = new Point(x, y);
        this.start_position = new Point(x, y);
        this.visited = new boolean[maze.getDimension()][maze.getDimension()];
        start();
    }

    public void setup() {
        clearMazeMemory();
        moveTo(maze.at(15, 0));
        rotateTo(Orientation.NORTH);
    }

    public void loop() {
        explore_stack.push(maze.at(15, 0));
        MazeNode cell = explore_stack.pop();
        rotateTo(cell);
        moveTo(cell);
//        markNeighborWalls(cell, orientation);
    }

    private void start() {
        restart();
    }

    // Erases maze memory and restarts mouse simulation from mouse initial position.
    public void restart() {

    }

    private void clearMazeMemory() {
        if (maze.getDimension() != ref_maze.getDimension()) {
            maze = new Maze( ref_maze.getDimension());
        }
        maze.clearWalls();
        explore_stack.clear();
        mousePath.clear();
        previousPath.clear();
        num_of_runs = 0;
        done = false;
        // Mark manhattan distance of clear maze
        for (MazeNode cell : maze) {
            Point center = getCloserCenter(cell);
            cell.setDistance(Math.abs(center.x - cell.x) + Math.abs(center.y - cell.y));
            cell.setVisited(false);
            setVisited(cell, false);
        }
    }
    
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
    
    //Rotate mouse to face toward the given cell
    void rotateTo(MazeNode cell) {
        if (x == cell.x) {
            //Vertical deviation
            if (y + 1 == cell.y) orientation = Orientation.SOUTH;
            else if (y - 1 == cell.y) orientation = Orientation.NORTH;
        } else if (y == cell.y) {
            //Horizontal deviation
            if (x + 1 == cell.x) orientation = Orientation.EAST;
            else if (x - 1 == cell.x) orientation = Orientation.WEST;
        }
        rotateTo(orientation);
    }

    //Rotate mouse to face the given orientation.
    void rotateTo(Orientation orientation) {
        this.orientation = orientation;
        mouse.rotateTo(orientation);
    }

    private void moveTo(MazeNode cell) {
        moveTo(cell.x, cell.y);
    }

    private void moveTo(Point coord) {
        moveTo(coord.x, coord.y);
    }

    private void moveTo(int x, int y) {
        move(x - column, y - row);
    }

    public void move(int dx, int dy) {
        column = x += dx;
        row = y += dy;
    }

    private void setVisited( MazeNode cell, boolean truthValue ) {
        visited[ cell.row ][ cell.column ] = truthValue;
    }

    // Getters and setters
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Maze getMaze() {
        return maze;
    }
}
