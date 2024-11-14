package GUI;

import Maze.Maze;
import Mouse.Mouse;
import Maze.MazeNode;
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
    private JButton selectAlgoButton;

    private JComboBox<String> algoComboBox;
    private JPanel algoPanel;

    private boolean runDijkstra = false;
    private boolean runDFS = false;
    private boolean runAStar = false;
    private boolean outputStats = true;
    private MazeController controller;
    private MazeNode endNode;

    /**
     * Constructor: Creates and sets up MazeGUI
     *
     * @param dimension       number of unit cells per side of square maze.
     * @param non_tree_edges  number of no tree edges in maze graph (adds multiple path solutions).
     * @param dijkstra        color the dijkstra path on the reference maze in DIJKSTRA_PATH_COLOR.
     * @param dfs             color the dfs path on the reference maze in DFS_PATH_COLOR.
     * @param astar           color the astar path on the reference maze in ASTAR_PATH_COLOR.
     */
    public MazeGUI(int dimension, int non_tree_edges, boolean dijkstra, boolean dfs, boolean astar) {
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
        runAStar = astar;

        controller = new MazeController(this);
        begin();

        endNode = ref_maze.getEnd();
        System.out.println(endNode);
    }

    private void begin() {
        JFrame main_frame = new JFrame("MicroMouse Simulator");
        main_frame.setSize(800, 800);
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main_frame.setBackground(Color.BLACK);
        main_frame.setResizable(true);

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

        /* set names of new buttons */
        animateButton = new JButton("Animate");
        selectAlgoButton = new JButton("Select Algo");
        mazeButton = new JButton("New Maze");
        nextButton = new JButton("Next");
        clearButton = new JButton("Clear");

        /* JComboBox for algorithm selection */
        String[] algorithms = { "DFS", "Dijkstra", "A*" };
        algoComboBox = new JComboBox<>(algorithms);
        algoComboBox.setVisible(false);
        algoComboBox.addActionListener(e -> handleAlgorithmSelection());

        /* Panel to hold the JComboBox */
        algoPanel = new JPanel();
        algoPanel.add(algoComboBox);

        /* Activates button/comboBox to register state change */
        clearButton.addActionListener(controller);
        animateButton.addActionListener(controller);
        mazeButton.addActionListener(e -> handleNewMazeAction());
        nextButton.addActionListener(controller);
        selectAlgoButton.addActionListener(e -> toggleAlgoDropdown());

        /* Set layout for the northButtonPanel to center the Select Algo button */
        northButtonPanel.add(animateButton);

        // Add horizontal glue to center the Select Algo button
        northButtonPanel.add(Box.createHorizontalGlue());

        // Add the Select Algo button between Animate and New Maze
        northButtonPanel.add(selectAlgoButton);

        // Add more horizontal glue after the Select Algo button
        northButtonPanel.add(Box.createHorizontalGlue());

        // Add the New Maze button
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
        northPanel.add(algoPanel);

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

    private void toggleAlgoDropdown() {
        // Toggle the visibility of the JComboBox (the dropdown)
        algoComboBox.setVisible(!algoComboBox.isVisible());
    }

//    private void handleAlgorithmSelection() {
//        String selectedAlgorithm = (String) algoComboBox.getSelectedItem();
//
//        switch (selectedAlgorithm) {
//            case "DFS":
//                runDFS = true;
//                runDijkstra = false;
//                runAStar = false;
//                break;
//            case "Dijkstra":
//                runDFS = false;
//                runDijkstra = true;
//                runAStar = false;
//                break;
//            case "A*":
//                runDFS = false;
//                runDijkstra = false;
//                runAStar = true;
//                break;
//        }
//
//        algoComboBox.setVisible(false);
//    }

    public String handleAlgorithmSelection() {
        String selectedAlgorithm = (String) algoComboBox.getSelectedItem();

        runDFS = false;
        runDijkstra = false;
        runAStar = false;

        selectAlgoButton.setText(selectedAlgorithm);

        switch (selectedAlgorithm) {
            case "DFS":
                runDFS = true;
                break;
            case "Dijkstra":
                runDijkstra = true;
                break;
            case "A*":
                runAStar = true;
                break;
        }

        algoComboBox.setVisible(false);

        return selectedAlgorithm;
    }


    private void handleNewMazeAction() {
        // Regenerate the maze
        ref_maze = new Maze(ref_maze.getDimension());
        mouse_maze = new Maze(ref_maze.getDimension());
        ref_maze.mazeGenerator().createRandomMaze(0, DATAFILE);

        endNode = ref_maze.getEnd();
        System.out.println("New End Node: " + endNode);

        mouse = new Mouse(ref_maze.getDimension() - 1, 0, ref_maze, mouse_maze);

        // Trigger any additional updates like redrawing the maze or resetting the UI if needed
        renderPanel.repaint(); // This will force the RenderPanel to redraw the maze
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

    public JButton getSelectAlgoButton() {
        return selectAlgoButton;
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

    public boolean isRunAStar() {
        return runAStar;
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

    public MazeNode getEndNode() {
        return endNode;
    }


}
