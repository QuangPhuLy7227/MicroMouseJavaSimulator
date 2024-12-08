package Maze;

import GUI.MazeController;
import GUI.MazeGUI;
import utility.ParsingStrings;

public class Main {
    public static void main(String[] args) {
        MazeGUI gui = null;
        MazeController controller;

        int dimension = 16;
        int non_tree_edges = 0;
        boolean dijkstra = false;
        boolean dfs = false;
        boolean astar = false;

        // Initialize the MazeGUI object
        if (gui == null) {
            gui = new MazeGUI(dimension, non_tree_edges, false, false, false);
        }
        System.out.println("Initial state: DFS=" + false + ", Dijkstra=" + false);

        controller = new MazeController(gui);

        // Command-line argument parsing
        for (int index = 0; index < args.length; index++) {
            String flag = args[index];
            boolean independent_flag = false;
            boolean invalidFlag = true;

            for (String valid_flag : ParsingStrings.FLAGS) {
                if (flag.equals(valid_flag)) {
                    invalidFlag = false;
                    break;
                }
            }

            if (invalidFlag) {
                System.out.println("Unrecognized Argument: " + args[index] + "\n");
                System.out.println(ParsingStrings.USAGE);
                System.exit(1);
            }

            // Independent arguments
            switch (flag) {
                case ParsingStrings.HELP_FLAG_1:
                case ParsingStrings.HELP_FLAG_2:
                    System.out.println(ParsingStrings.USAGE);
                    System.out.println(ParsingStrings.HELP_MSG);
                    System.exit(1);
                    break;
                case ParsingStrings.DIJKSTRA_FLAG:
                    independent_flag = true;
                    dijkstra = true;
                    dfs = false;
                    astar = false;
                    System.out.println("Command line selected: Dijkstra");
                    break;
                case ParsingStrings.DFS_FLAG:
                    independent_flag = true;
                    dfs = true;
                    dijkstra = false;
                    astar = false;
                    System.out.println("Command line selected: DFS");
                    break;
                case ParsingStrings.ASTAR_FLAG:
                    independent_flag = true;
                    dfs = false;
                    dijkstra = false;
                    astar = true;
                    System.out.println("Command line selected: AStar");
                    break;
            }

            if (independent_flag) continue;

            // Dependent arguments
            if (index + 1 == args.length) {
                System.out.println("Flag " + args[index] + " is expecting an argument.");
                System.out.println(ParsingStrings.USAGE);
                System.exit(1);
            }
            switch (flag) {
                case ParsingStrings.DIM_FLAG_1:
                case ParsingStrings.DIM_FLAG_2:
                    try {
                        dimension = Integer.parseInt(args[index + 1]);
                    } catch (NumberFormatException e) {
                        System.out.println("Integer Parsing Error: dimension: " + args[index + 1] + "\n");
                        System.exit(1);
                    }
                    break;
                case ParsingStrings.NUM_PATHS_FLAG_1:
                case ParsingStrings.NUM_PATHS_FLAG_2:
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

        // Debug: Final state before creating GUI
        System.out.println("Final state before GUI initialization: DFS=" + dfs + ", Dijkstra=" + dijkstra + ", AStar=" + astar);

        // Reinitialize with updated parameters if needed
//        new MazeGUI(dimension, non_tree_edges, dijkstra, dfs, astar);
    }
}
