package GUI;

import Maze.Maze;
import Maze.MazeNode;
import Mouse.Mouse;
import Mouse.FloodFillSolver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class RenderPanel extends JPanel {
    public static final Color PERISCOPE_PANEL_COLOR = new Color(43, 50, 56);
    private static final Color LIGHT_BLACK = new Color(32, 32, 32);
    private static final Color NO_WALL_COLOR = new Color(135, 135, 135);
    private static final Color WALL_COLOR = Color.BLACK;
    private static final Color MAZE_BORDER_COLOR = Color.BLACK;
    private static final Color MOUSE_COLOR = Color.YELLOW;
    private static final Color DIJKSTRA_PATH_COLOR = Color.RED;
    private static final Color DFS_PATH_COLOR = Color.BLUE;
    private static final Color MAZE_BACKGROUND_COLOR = Color.GRAY;
    private static final Color NUMBER_COLOR = Color.DARK_GRAY;
    private static final Color MOUSE_PATH_COLOR = Color.YELLOW;
    private static final Color EXCITEMENT_COLOR = Color.BLUE;

    private static BufferedImage image = null;

    private Point leftMazePoint = new Point();
    private Point rightMazePoint = new Point();
    private Point currentPoint = new Point();
    private Point rightPoint = new Point();
    private Point downPoint = new Point();
    private Point center = new Point();

    private boolean periscopeMode = false;
    private boolean done = false;
    private MazeGUI gui;
    private Maze ref_maze;

//    public boolean isDone() {
//        return done;
//    }

    /**
     * Constructor: Creates a JPanel for the maze GUI.
     */
    public RenderPanel(MazeGUI gui) {
        this.gui = gui;
        this.ref_maze = gui.getRefMaze();
        try {
            image = ImageIO.read(new File("../src/utility/images/gannon.png"));
        } catch (IOException e) {
            System.err.println("UCSD logo non-existent");
        }
    }

    /**
     * Double buffered image screen paint on GUI.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);

    }

    /**
     * Renders the main GUI interface - drawing all GUI components.
     *
     * @param g GUI graphics environment.
     */
    private void render(Graphics g) {
//        if (periscopeMode) {
//            renderPeriscope(g);
//        } else {
//            renderDefault(g);
//        }
        renderDefault(g);
    }

    /**
     * Renders Periscope GUI interface with a singular center maze wirelessly
     * communicating with hardware micromouse to display it virtually.
     *
     * @param g GUI graphics environment.
     */
    private void renderPeriscope(Graphics g) {

    }

    /**
     * Renders Standard GUI interface with two mazes which is used to simulate a
     * virtual micromouse and quickly test maze traversal algorithms.
     *
     * @param g GUI graphics environment.
     */
    private void renderDefault(Graphics g) {
        center.setLocation(getWidth() / 2, getHeight() / 2);
        Mouse mouse = gui.getMouse();
        if (mouse == null) {
            System.err.println("Mouse is null.");
            return;
        }

        Maze mouse_maze = mouse.getMaze();
        Maze ref_maze = gui.getRefMaze();
        if (ref_maze == null || mouse_maze == null) {
            System.err.println("Maze is null.");
            return;
        }

        int maze_diameter = (int) (MazeGUI.MAZE_DEFAULT_PROPORTION * Math.min(getHeight(), getWidth()));
        int maze_radius = (int) (0.5 * maze_diameter);
        int maze_offset = (int) (0.25 * (getWidth() - 2 * maze_diameter));
        double cell_unit = (1.0 / ref_maze.getDimension()) * maze_diameter;

        /* draws Gannon Logo - upper left corner */
        int image_diameter = (int)(double)(0.4 * maze_diameter);
        if (image_diameter == 0) image_diameter = 1;
        drawImage(g, image, 0, 0, image_diameter, image_diameter);

        /* draws the 2 square mazes in the center of the frame */
        leftMazePoint.setLocation(maze_offset, center.y - maze_radius);
        rightMazePoint.setLocation(center.x + maze_offset, center.y - maze_radius);
        drawMaze(g, leftMazePoint, maze_diameter, ref_maze, false);
        drawMaze(g, rightMazePoint, maze_diameter, mouse_maze, true);

        mouse.setGraphicsEnvironment(rightMazePoint, maze_diameter);
        mouse.draw(g, MOUSE_COLOR);

        MazeNode endNode = gui.getEndNode();
        if (endNode != null) {
            int endX = (int) (leftMazePoint.x + endNode.x * cell_unit);
            int endY = (int) (leftMazePoint.y + endNode.y * cell_unit);

            // Set the color for the end node
            g.setColor(Color.yellow);

            // Draw the end node as a circle (or any other shape you prefer)
            g.fillOval(endX - 5, endY - 5, 10, 10);
        }

        if (gui.isRunDFS()) {
            /* draw dfs path on ref maze */
            LinkedList<MazeNode> dfsPath = ref_maze.pathFinder.findPathUsingDFS(ref_maze.getBegin(), ref_maze.getEnd());
            if (!dfsPath.isEmpty()) {
                drawDFSPath(g, ref_maze, leftMazePoint, ref_maze.getBegin(), ref_maze.getEnd(), cell_unit, DFS_PATH_COLOR);
                System.out.println("Using DFS......");
            } else {
                System.out.println("Error running DFS....");
            }

        }

        if (gui.isRunDijkstra()) {
            LinkedList<MazeNode> dijkstraPath = ref_maze.pathFinder().findPathUsingDijkstra(ref_maze.getBegin(), ref_maze.getEnd());
            if (!dijkstraPath.isEmpty()) {
                drawDijkstraPath(g, ref_maze, leftMazePoint, ref_maze.getBegin(), ref_maze.getEnd(), cell_unit, DIJKSTRA_PATH_COLOR);
                System.out.println("Using Dijkstra.....");
            } else {
                System.out.println("Error running Dijkstra.....");
            }
        }

        FloodFillSolver mouseSolver = mouse.getMouseSolver();

        if (mouse.getMouseSolver().isDone()) {
            /* draws path found by mouse and checks if path is most optimal */
            drawMousePath(g, mouse_maze, rightMazePoint, cell_unit, MOUSE_PATH_COLOR);
            if (ref_maze.pathFinder().findPathUsingDijkstra(ref_maze.getBegin(), ref_maze.getEnd()).size() == 0)
                ref_maze.pathFinder().findPathUsingDijkstra(ref_maze.getBegin(), ref_maze.getEnd());
            drawSolutionMessage(g, center, leftMazePoint, maze_diameter);
        }

        if (mouseSolver.isDone() && gui.isOutputStats()) {
            /* output statistics about the mouse's run */
            gui.setOutputStats(false);
            int mouse_visited = mouseSolver.getTotalCellsVisited();
            int total = mouse_maze.getDimension() * mouse_maze.getDimension();
            System.err.println("Proportion of cells visited by mouse: " + ((double)(mouse_visited) / total * 100) + "% on a dimension of " + mouse_maze.getDimension() + "x" + mouse_maze.getDimension());
            System.err.println("Total number of mouse runs: " + mouseSolver.getNumberOfRuns());
        }
    }


    private void drawMaze( Graphics g, Point mazePoint, int side, Maze maze, boolean drawFloodFillValues ) {
        double cell_unit = (1.0 / maze.getDimension()) * side;
        /* Maze Background */
        g.setColor( MAZE_BACKGROUND_COLOR );
        g.fillRect( mazePoint.x, mazePoint.y, side, side );
        g.setColor( MAZE_BORDER_COLOR );
        g.drawRect( mazePoint.x, mazePoint.y, side, side );

        /* Maze Foreground - Maze Generation graphics */
        int wall_width  = 2;
        int wall_height = (int) cell_unit;
        Rectangle vertical_wall   = new Rectangle( 0, 0, wall_width, wall_height );
        Rectangle horizontal_wall = new Rectangle( 0, 0, wall_height, wall_width );
        drawGridLines( g, maze, mazePoint, vertical_wall, horizontal_wall, cell_unit );

        if( drawFloodFillValues ) {
            /* draws flood fill values for every cell in maze */
            drawFloodFillCellValues( g, maze, mazePoint, cell_unit );
        }
    }

    private void drawImage( Graphics g, Image image, int x, int y, int width, int height ) {
        if( image == null ) return;
        Image scaled_screen = image.getScaledInstance( width, height, Image.SCALE_SMOOTH );
        BufferedImage scaled_image = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
        Graphics2D g2d = scaled_image.createGraphics();
        g2d.drawImage( scaled_screen, 0, 0, null );
        g2d.dispose();
        g.drawImage( scaled_image, x, y, null );
    }
//Phu
//    private void drawDijkstraPath( Graphics g, Maze maze, Point mazePoint, MazeNode startVertex, MazeNode endVertex, double cell_unit, Color color ) {
////        if( maze.getDijkstraPath().size() == 0 ) maze.dijkstra( startVertex, endVertex );
//        colorPath( g, maze.pathFinder().findPathUsingDijkstra(startVertex, endVertex), color, mazePoint, cell_unit );
//    }
//
//    private void drawDFSPath( Graphics g, Maze maze, Point mazePoint, MazeNode startVertex, MazeNode endVertex, double cell_unit, Color color ) {
////        if( maze.getDFSPath().size() == 0 ) maze.dfs( startVertex, endVertex );
//        colorPath( g, maze.pathFinder.findPathUsingDFS(startVertex, endVertex), color, mazePoint, cell_unit );
//    }

    private void drawDijkstraPath(Graphics g, Maze maze, Point mazePoint, MazeNode startVertex, MazeNode endVertex, double cell_unit, Color color) {
        LinkedList<MazeNode> path = maze.pathFinder().findPathUsingDijkstra(startVertex, endVertex);
        if (path == null) {
            System.err.println("Dijkstra path is null.");
            return; // Do not attempt to draw a null path
        }
        colorPath(g, path, color, mazePoint, cell_unit);
    }

    private void drawDFSPath(Graphics g, Maze maze, Point mazePoint, MazeNode startVertex, MazeNode endVertex, double cell_unit, Color color) {
        LinkedList<MazeNode> path = maze.pathFinder.findPathUsingDFS(startVertex, endVertex);
        if (path == null) {
            System.err.println("DFS path is null.");
            return; // Do not attempt to draw a null path
        }
        colorPath(g, path, color, mazePoint, cell_unit);
    }


    private void drawMousePath( Graphics g, Maze maze, Point mazePoint, double cell_unit, Color color ) {
        FloodFillSolver mouseSolver = gui.getMouse().getMouseSolver();
        /* mouse object should do this on its own when ready */
        if( !mouseSolver.isDone() ) return;
        colorPath( g, (LinkedList<MazeNode>) mouseSolver.getMousePath(), color, mazePoint, cell_unit );
    }

    private void colorPath(Graphics g, LinkedList<MazeNode> path, Color color, Point mazePoint, double cell_unit ) {
        if (path == null || path.size() == 0) {
            System.err.println("Path is null or empty. Cannot draw path.");
            return; // Early exit if path is null or empty
        }
        final double PATH_PROPORTION = 0.1;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor( color );

        if( path.size() == 0 ) return;
        /* set starting location and trail width */
        MazeNode currentNode = path.removeFirst();
        int x = mazePoint.x + (int)(currentNode.getDiagonalX() * cell_unit + 0.5 * (1 - PATH_PROPORTION) * cell_unit);
        int y = mazePoint.y + (int)(currentNode.getDiagonalY() * cell_unit + 0.5 * (1 - PATH_PROPORTION) * cell_unit);
        int sideLength = (int)(PATH_PROPORTION * cell_unit);
        if( sideLength == 0 ) sideLength = 1;
        Rectangle cellBlock = new Rectangle( x, y, sideLength, sideLength );

        while( path.size() != 0 ) {
            /* traverse through path */
            currentNode = path.removeFirst();
            x = mazePoint.x + (int)(currentNode.getDiagonalX() * cell_unit + 0.5 * (1 - PATH_PROPORTION) * cell_unit);
            y = mazePoint.y + (int)(currentNode.getDiagonalY() * cell_unit + 0.5 * (1 - PATH_PROPORTION) * cell_unit);
            int current_x = cellBlock.x;
            int current_y = cellBlock.y;

            while( current_x != x || current_y != y ) {
                /* draw trail */
                int dx = ( x - cellBlock.x == 0 ) ? 0 : Math.abs(x - cellBlock.x) / (x - cellBlock.x);
                int dy = ( y - cellBlock.y == 0 ) ? 0 : Math.abs(y - cellBlock.y) / (y - cellBlock.y);
                current_x += dx;
                current_y += dy;
                cellBlock.setLocation( current_x, current_y );
                g2d.fill( cellBlock );
            }

            cellBlock.setLocation( x, y );
            g2d.fill( cellBlock );
        }
    }

    private void drawGridLines( Graphics g, Maze maze, Point mazePoint, Rectangle vertical_wall, Rectangle horizontal_wall, double cell_unit ) {
        Graphics2D g2d = (Graphics2D) g;

        for( int row = 0; row < maze.getDimension(); row++ ) {
            for( int column = 0; column < maze.getDimension(); column++  ) {
                /* draw walls */
                int x = column;
                int y = row;
                currentPoint.setLocation( x, y );
                rightPoint.setLocation( x + 1, y );
                downPoint.setLocation( x, y + 1 );

                /* vertical wall is present to the right of current cell */
                vertical_wall.setLocation( mazePoint.x + (int)((column + 1) * cell_unit), mazePoint.y + (int)(row * cell_unit) );

                /* horizontal wall is also present below current cell */
                horizontal_wall.setLocation( mazePoint.x + (int)(column * cell_unit), mazePoint.y + (int)((row + 1) * cell_unit) );

                if( column < maze.getDimension() - 1 && maze.wallBetween(currentPoint, rightPoint) ) {
                    g2d.setColor( WALL_COLOR );
                    g2d.fill( vertical_wall );
                }
                else if( column != maze.getDimension() -1 ) {
                    g2d.setColor( NO_WALL_COLOR );
                    g2d.fill( vertical_wall );
                }

                if( row < maze.getDimension() - 1 && maze.wallBetween(currentPoint, downPoint)  ) {
                    g2d.setColor( WALL_COLOR );
                    g2d.fill( horizontal_wall );
                }
                else if( row != maze.getDimension() - 1 ) {
                    g2d.setColor( NO_WALL_COLOR );
                    g2d.fill( horizontal_wall );
                }
            }
        }
    }

    void drawFloodFillCellValues( Graphics g, Maze maze, Point mazePoint, double cell_unit ) {
        final double FONT_PROPORTION = 0.5;
        Mouse mouse = gui.getMouse();

        Font numberFont = new Font( Font.SANS_SERIF, Font.BOLD, (int)(FONT_PROPORTION * cell_unit) );
        g.setFont( numberFont );
        g.setColor( NUMBER_COLOR );

        for( MazeNode cell : maze ) {
            /* draw distance (flood fill) values in all cells of the maze */
            if( cell.x == mouse.x && cell.y == mouse.y ) continue;
            double height_offset = ((1 - FONT_PROPORTION) * cell_unit) / 2.0;
            double width_offset  = (cell_unit - g.getFontMetrics().stringWidth(Integer.toString(cell.distance))) / 2.0;
            g.drawString( Integer.toString(cell.distance), mazePoint.x + (int)(cell.x * cell_unit + width_offset), mazePoint.y + (int)((cell.y + 1) * cell_unit - height_offset));
        }
    }

    private void drawSolutionMessage( Graphics g, Point center, Point mazePoint, int maze_diameter ) {
        String message;
        Mouse mouse = gui.getMouse();
        Maze ref_maze = gui.getRefMaze();
        g.setFont( new Font(Font.SANS_SERIF, Font.BOLD, (int)(0.05 * maze_diameter)) );
        g.setColor( EXCITEMENT_COLOR );

        if( ref_maze.pathFinder().findPathUsingDijkstra(ref_maze.getBegin(), ref_maze.getEnd()).size() == mouse.getMouseSolver().getMousePath().size() ) {
            message = "Most Optimal Solution Found!";
        }
        else {
            message = "Non-optimal. Dijkstra: " + ref_maze.pathFinder().findPathUsingDijkstra(ref_maze.getBegin(), ref_maze.getEnd()).size() + " steps. Flood Fill: " + mouse.getMouseSolver().getMousePath().size() + " steps.";
        }

        double width_offset  = g.getFontMetrics().stringWidth( message ) / 2.0;
        g.drawString( message, (int)(center.x - width_offset), mazePoint.y + maze_diameter + (int)((getHeight() - maze_diameter) / 4.0) );
    }
}
