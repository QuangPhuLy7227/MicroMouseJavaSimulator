package GUI;

import Maze.Maze;
import Mouse.Mouse;
import utility.ParsingStrings;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MazeGUI {
    public static final double MAZE_DEFAULT_PROPORTION = 0.50;
    public static final File DATAFILE = new File("../datafile");

    private Maze ref_maze;
    private Maze mouse_maze;
    private Mouse mouse;

    private Timer animationCLK;
    private JPanel northPanel;
    private JPanel southPanel;
    private RenderPanel renderPanel;
    private JPanel northButtonPanel;
    private JPanel southButtonPanel;

    private JButton animateButton;
    private JButton clearButton;
    private JButton mazeButton;
    private JButton nextButton;

    private boolean runDijkstra = false;
    private boolean runDFS = false;
    private boolean outputStats = true;
    private MazeController controller;

    /**
     * Constructor: Creates and sets up MazeGUI
     *
     * @param dimension       number of unit cells per side of square maze.
     * @param non_tree_edges  number of no tree edges in maze graph (adds multiple path solutions).
     * @param dijkstra        color the dijkstra path on the reference maze in DIJKSTRA_PATH_COLOR.
     * @param dfs             color the dfs path on the reference maze in DFS_PATH_COLOR.
     */
    public MazeGUI(int dimension, int non_tree_edges, boolean dijkstra, boolean dfs) {
        if (dimension < 1) dimension = 1;
        ref_maze = new Maze(dimension);
        mouse_maze = new Maze(dimension);
        if( ref_maze.mazeSerializer().loadMaze(ref_maze, DATAFILE) == false ) {
            /* load datafile - otherwise create new random maze if that didn't work */
            ref_maze.mazeGenerator().createRandomMaze( non_tree_edges, DATAFILE );
        }
        mouse = new Mouse(dimension - 1, 0, ref_maze, mouse_maze);
        runDijkstra = dijkstra;
        runDFS = dfs;

        controller = new MazeController(this);
        begin();
    }

    private void begin() {
        JFrame main_frame = new JFrame("MicroMouse Simulator");
        main_frame.setSize( 800, 800 );
        main_frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        main_frame.setBackground( Color.BLACK );
        main_frame.setResizable( true );

        /* main sections of layout, north, south, center */
        northPanel = new JPanel();
        southPanel = new JPanel();
        renderPanel = new RenderPanel(this);
        /* sub-panels for cosmetic needs */
        northButtonPanel = new JPanel();
        southButtonPanel = new JPanel();

        /* set layout for button panels */
        northButtonPanel.setLayout(new BoxLayout(northButtonPanel, BoxLayout.LINE_AXIS));
        southButtonPanel.setLayout(new BoxLayout(southButtonPanel, BoxLayout.LINE_AXIS));

        /* sets names of new buttons */
        animateButton = new JButton("Animate");
        clearButton = new JButton("Clear");
        mazeButton = new JButton("New Maze");
        nextButton = new JButton("Next");

        /* Activates button/comboBox to register state change */
        clearButton.addActionListener(controller);
        animateButton.addActionListener(controller);
        mazeButton.addActionListener(controller);
        nextButton.addActionListener(controller);

        /* add button to panels */
        northButtonPanel.add(animateButton);
        northButtonPanel.add(Box.createHorizontalGlue());
        northButtonPanel.add(Box.createHorizontalGlue());
        northButtonPanel.add(mazeButton);
        /* south button panel buttons */
        southButtonPanel.add(nextButton);
        southButtonPanel.add(Box.createHorizontalGlue());
        southButtonPanel.add(Box.createHorizontalGlue());
        southButtonPanel.add(clearButton);

        /* background color of button panels */
        northButtonPanel.setBackground(Color.BLACK);
        southButtonPanel.setBackground(Color.BLACK);

        /* set up north panel */
        northPanel.setLayout(new GridLayout(0, 1));
        northPanel.add(northButtonPanel);
        /* set up south panel  */
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(southButtonPanel);

        /* add panels with their buttons on the final window */
        Container contentPane = main_frame.getContentPane();
        contentPane.add(northPanel, BorderLayout.NORTH);
        contentPane.add(southPanel, BorderLayout.SOUTH);
        contentPane.add(renderPanel, BorderLayout.CENTER);
        contentPane.validate();

        main_frame.setVisible(true);
        animationCLK = new Timer(controller.ANIMATION_DELAY, controller);
    }

    public Maze getRefMaze() {
        return ref_maze;
    }

    public Maze getMouseMaze() {
        return mouse_maze;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public Timer getAnimationCLK() {
        return animationCLK;
    }

    public JButton getAnimateButton() {
        return animateButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JButton getMazeButton() {
        return mazeButton;
    }

    public JButton getNextButton() {
        return nextButton;
    }

    public boolean isRunDijkstra() {
        return runDijkstra;
    }

    public boolean isRunDFS() {
        return runDFS;
    }

    public boolean isOutputStats() {
        return outputStats;
    }

    public void setOutputStats(boolean outputStats) {
        this.outputStats = outputStats;
    }

    public RenderPanel getRenderPanel() {
        return renderPanel;
    }

    public static void main(String[] args) {
        int dimension = 16;
        int non_tree_edges = 0;
        boolean dijkstra = true;
        boolean dfs = false;

        for (int index = 0; index < args.length; index++) {
            /* parse command line arguments */
            String flag = args[index];
            boolean independent_flag = false;
            boolean invalidFlag = true;

            for (String valid_flag : ParsingStrings.FLAGS) {
                /* search if arg is a valid flag  */
                if (flag.equals(valid_flag)) {
                    invalidFlag = false;
                    break;
                }
            }

            if (invalidFlag) {
                /* no such flag defined */
                System.out.println("Unrecognized Argument: " + args[index] + "\n");
                System.out.println(ParsingStrings.USAGE);
                System.exit(1);
            }

            /* independent args */
            switch (flag) {
                case ParsingStrings.HELP_FLAG_1:
                case ParsingStrings.HELP_FLAG_2:
                    /* program usage */
                    System.out.println(ParsingStrings.USAGE);
                    System.out.println(ParsingStrings.HELP_MSG);
                    System.exit(1);
                    break;
                case ParsingStrings.DIJKSTRA_FLAG:
                    /* run dijkstra */
                    independent_flag = true;
                    dijkstra = true;
                    break;
                case ParsingStrings.DFS_FLAG:
                    /* run dfs */
                    independent_flag = true;
                    dfs = true;
                    break;
            }

            if (independent_flag) continue;
            /* dependent args */
            if (index + 1 == args.length) {
                /* invalid number of args */
                System.out.println("Flag " + args[index] + " is expecting an argument.");
                System.out.println(ParsingStrings.USAGE);
                System.exit(1);
            }
            switch (flag) {
                case ParsingStrings.DIM_FLAG_1:
                case ParsingStrings.DIM_FLAG_2:
                    /* dimension input */
                    try {
                        dimension = Integer.parseInt(args[index + 1]);
                    } catch (NumberFormatException e) {
                        System.out.println("Integer Parsing Error: dimension: " + args[index + 1] + "\n");
                        System.exit(1);
                    }
                    break;
                case ParsingStrings.NUM_PATHS_FLAG_1:
                case ParsingStrings.NUM_PATHS_FLAG_2:
                    /* number of cycles in graph - number of alternative solutions */
                    try {
                        non_tree_edges = Integer.parseInt(args[index + 1]);
                    } catch (NumberFormatException e) {
                        System.out.println("Integer Parsing Error: non_tree_edges: " + args[index + 1] + "\n");
                        System.out.println(ParsingStrings.USAGE);
                        System.exit(1);
                    }
                    if (non_tree_edges < 0) {
                        System.out.println("Max Non-Tree Edges Error: argument must be positive: " + non_tree_edges + "\n");
                        System.out.println(ParsingStrings.USAGE);
                        System.exit(1);
                    }
                    break;
            }
            index++;
        }
        if (dimension <= 0) {
            System.out.println("Dimension Argument Error: not positive\n");
            System.out.println("Example: java MazeGUI -dimension 16\n");
            System.out.println(ParsingStrings.USAGE);
            System.exit(1);
        }
        new MazeGUI(dimension, non_tree_edges, dijkstra, dfs);
    }
}
