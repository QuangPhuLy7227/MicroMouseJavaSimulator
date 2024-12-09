package Mouse;

import Maze.Maze;
import Maze.MazeNode;

import java.awt.*;
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
    private Point origin, start_position;
    private Orientation orientation;
    private boolean visited[][];
    private FloodFillSolver mouseSolver;
    private MouseShape mouseShape;

    public Mouse(int row, int column, Maze ref_maze, Maze maze) {
        this.row = this.y = row;
        this.column = this.x = column;
        this.ref_maze = ref_maze;
        this.maze = maze;
        this.mouse = new MouseShape();
        this.origin = new Point(x, y);
        this.start_position = new Point(x, y);
        this.visited = new boolean[maze.getDimension()][maze.getDimension()];

        // Initialize the mouseSolver object before starting it
        this.mouseSolver = new FloodFillSolver(this);
        this.mouseShape = new MouseShape();
        start();
    }


    // Rotate mouse to face toward the given cell
    void rotateTo(MazeNode cell) {
        int dx = cell.x - x;
        int dy = cell.y - y;

        if (dx == 0 && dy == -1) orientation = Orientation.NORTH;
        else if (dx == 1 && dy == -1) orientation = Orientation.NORTHEAST;
        else if (dx == 1 && dy == 0) orientation = Orientation.EAST;
        else if (dx == 1 && dy == 1) orientation = Orientation.SOUTHEAST;
        else if (dx == 0 && dy == 1) orientation = Orientation.SOUTH;
        else if (dx == -1 && dy == 1) orientation = Orientation.SOUTHWEST;
        else if (dx == -1 && dy == 0) orientation = Orientation.WEST;
        else if (dx == -1 && dy == -1) orientation = Orientation.NORTHWEST;

        rotateTo(orientation);
    }

    // Rotate mouse to face the given orientation
    void rotateTo(Orientation orientation) {
        this.orientation = orientation;
        mouseShape.rotateTo(orientation);
    }

    public void moveTo(MazeNode cell) {
        int dx = cell.column - column;
        int dy = cell.row - row;

        if (dx != 0 && dy != 0) {
            // Diagonal move
            rotateTo(cell);
        } else {
            // Non-diagonal move
            rotateTo(cell);
        }

        move(dx, dy);
    }

    public void moveTo(Point coord) {
        moveTo(coord.x, coord.y);
    }

    public void moveTo(int x, int y) {
        move(x - column, y - row);
    }

    public void move(int dx, int dy) {
        column = x += dx;
        row = y += dy;
    }

    public void setVisited(MazeNode cell, boolean truthValue) {
        visited[cell.row][cell.column] = truthValue;
    }

    public boolean visited(MazeNode cell) {
        return visited[cell.row][cell.column];
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
    public void setMaze(Maze maze) {this.maze = maze;}
    public Maze getRefMaze() {
        return ref_maze;
    }

    public Point getStartPosition() {
        return start_position;
    }
    public Point getOrigin() {return origin;}

    public void setStartPosition(int x, int y) {
        this.start_position.setLocation(x, y);
    }

    public void draw(Graphics g, Color color) {
        mouseShape.draw(g, color);
    }

    public void setGraphicsEnvironment(Point maze_draw_point, int maze_diameter) {
        double UNIT = (1.0 / maze.getDimension()) * maze_diameter;
        double unitCenterX = maze_draw_point.x + column * UNIT + (UNIT / 2.0);
        double unitCenterY = maze_draw_point.y + row * UNIT + (UNIT / 2.0);
        double width = UNIT * PROPORTION;
        double height = UNIT * PROPORTION;
        double x = unitCenterX - UNIT * PROPORTION / 2.0;
        double y = unitCenterY - UNIT * PROPORTION / 2.0;

        mouseShape.setDimension((int) width, (int) height);
        mouseShape.setLocation((int) x, (int) y);
        mouseShape.rotateTo(orientation);
    }

    public FloodFillSolver getMouseSolver() {
        return mouseSolver;
    }

    public void start() {
        restart();
    }

    // Erases maze memory and restarts mouse simulation from mouse initial position.
    public void restart() {
        mouseSolver.clearMazeMemory();
        getStartPosition().setLocation(origin.x, origin.y);
        orientation = Orientation.NORTH;
        moveTo( start_position );
        rotateTo( orientation );
        mouseSolver.getExploreStack().clear();
        mouseSolver.getExploreStack().push( maze.at(row, column) );
    }

    @Override
    public String toString() {
        return super.toString() + "-(" + row + "," + column + ")-" + orientation;
    }

}
