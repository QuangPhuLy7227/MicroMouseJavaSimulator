package GUI;

import Maze.Maze;
import Mouse.FloodFillSolver;
import Maze.MazeSerializer;
import Mouse.Mouse;
import Maze.MazeNode;
import Maze.PathFinder;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MazeController implements ActionListener, KeyListener, PopupMenuListener {
    private final MazeGUI gui;
    MazeSerializer mazeSerializer;
    public static final int ANIMATION_DELAY = 250;

    private int saveCount = 0;
    private static final int MAX_SAVES = 3;
    private final File[] savedMazeFiles = {
            new File("saveMazeFiles1.dat"),
            new File("saveMazeFiles2.dat"),
            new File("saveMazeFiles3.dat")
    };

    public File[] getSavedMazeFiles() {
        return savedMazeFiles;
    }

    public MazeController(MazeGUI gui) {
        this.gui = gui;
        mazeSerializer = gui.getRefMaze().mazeSerializer;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == gui.getLoadMazeButton()) {
            handleLoadMazeButtonEvent();
        } else if (evt.getSource() == gui.getAnimateButton()) {
            handleAnimateButtonEvent();
        } else if (evt.getSource() == gui.getNewMazeButton()) {
            handleNewMazeButtonEvent();
        } else if (evt.getSource() == gui.getNextButton() || evt.getSource() == gui.getAnimationCLK()) {
            handleNextButtonEvent();
        } else if (evt.getSource() == gui.getSelectAlgoButton()) {
            toggleAlgoDropDown();
        } else if (evt.getSource() == gui.getAlgoComboBox()) {
            handleSelectAlgoButtonEvent();
        } else if (evt.getSource() == gui.getLoadSaveMazeButton()) {
            toggleLoadSaveMazeDropDown();
        } else if (evt.getSource() == gui.getLoadMazeComboBox()) {
            handleLoadSaveMazeButtonEvent();
        } else if (evt.getSource() == gui.getSaveMazeButton()) {
            handleSaveMazeButtonEvent();
        }
    }

    private void handleLoadMazeButtonEvent() {
        gui.getMouse().restart();
        gui.setOutputStats(true);
        gui.getRenderPanel().repaint();
    }

    private void handleNewMazeButtonEvent() {
        // Regenerate the maze
        gui.setRefMaze(new Maze(gui.getRefMaze().getDimension()));
        gui.setMouseMaze(new Maze(gui.getRefMaze().getDimension()));
        gui.getRefMaze().getMazeGenerator().createRandomMaze(3); // I want a number between 2 and 5

        gui.setEndNode(gui.getRefMaze().getEnd());
        System.out.println("New End Node: " + gui.getEndNode());

        gui.setMouse(new Mouse(gui.getRefMaze().getDimension() - 1, 0, gui.getRefMaze(), gui.getMouseMaze()));

        // Trigger any additional updates like redrawing the maze or resetting the UI if needed
        gui.getRenderPanel().repaint();
    }

    public String handleSelectAlgoButtonEvent() {
        String selectedAlgorithm = (String) gui.getAlgoComboBox().getSelectedItem();
        gui.setRunDFS(false);
        gui.setRunDijkstra(false);
        gui.setRunAStar(false);

        gui.getSelectAlgoButton().setText(selectedAlgorithm);

        switch (Objects.requireNonNull(selectedAlgorithm)) {
            case "DFS":
                gui.setRunDFS(true);
                break;
            case "Dijkstra":
                gui.setRunDijkstra(true);
                break;
            case "A*":
                gui.setRunAStar(true);
                break;
        }

        System.out.println("DFS: " + gui.isRunDFS());
        System.out.println("Dijkstra: " + gui.isRunDijkstra());
        System.out.println("A*: " + gui.isRunAStar());

        gui.getAlgoComboBox().setVisible(false);

        return selectedAlgorithm;
    }


    private void handleSelectSaveMazeButtonEvent() {
    }

    private void handleSaveMazeButtonEvent() {
        // Create a file chooser dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Maze");

        File defaultDirectory = new File("C:\\Users\\Owner\\Downloads\\MicroMouse\\JavaSimulatorMMS\\MazeFiles");
        if (defaultDirectory.exists()) {
            fileChooser.setCurrentDirectory(defaultDirectory);
        }

        // Set the file chooser to select files only
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Show the save dialog
        int userSelection = fileChooser.showSaveDialog(gui);

        if(userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String fileName = fileToSave.getName();
            // Get the maze dimension
            int mazeDimension = gui.getRefMaze().getDimension();
            // Append the maze dimension at the beginning of the file name
            if (!fileName.endsWith(".dat")) {
                fileName += ".dat";
            }
            fileName = mazeDimension + "_" + fileName;
            File updatedFileToSave = new File(fileChooser.getCurrentDirectory(), fileName);
            if (mazeSerializer != null) {
                mazeSerializer.saveMaze(updatedFileToSave);
                System.out.println("Maze saved to: " + updatedFileToSave.getAbsolutePath());
            } else {
                System.out.println("Maze serializer is null");
            }
        } else {
            System.out.println("Save operation cancelled by the user.");
        }
    }

    public void handleLoadSaveMazeButtonEvent() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Maze");

        File defaultDirectory = new File("C:\\Users\\Owner\\Downloads\\MicroMouse\\JavaSimulatorMMS\\MazeFiles");
        if (defaultDirectory.exists()) {
            fileChooser.setCurrentDirectory(defaultDirectory);
        }
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int userSelection = fileChooser.showOpenDialog(gui);
        if(userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            if (!fileToLoad.exists() || !fileToLoad.isFile()) {
                System.out.println("Invalid file selected. Please choose a valid maze file.");
                return;
            }

            if (mazeSerializer != null) {
                boolean loadSuccess = mazeSerializer.loadMaze(fileToLoad);
                if (loadSuccess) {
                    System.out.println("Maze loaded successfully from: " + fileToLoad.getAbsolutePath());
                    gui.getRenderPanel().repaint(); // Update the UI with the loaded maze
                } else {
                    System.out.println("Failed to load maze. Please check the file format.");
                }
            } else {
                System.out.println("Maze serializer is null");
            }
        } else {
            System.out.println("Load operation cancelled by the user.");
        }
    }


    private void handleAnimateButtonEvent() {
        if (gui.getAnimationCLK().isRunning() == false) {
            /* start animation */
            gui.getAnimationCLK().start();
            gui.getAnimateButton().setText("Stop");
            gui.getNextButton().setEnabled(false);
        } else {
            /* stop animation */
            gui.getAnimationCLK().stop();
            gui.getAnimateButton().setText("Animate");
            gui.getNextButton().setEnabled(true);
        }
        gui.getRenderPanel().repaint();
    }

    private void handleMazeButtonEvent() {
        System.err.println( "\nnew maze" );
        gui.getAnimateButton().setText( "Animate" );
        if( gui.getAnimationCLK().isRunning() == true ) gui.getAnimationCLK().stop();
        gui.getNextButton().setEnabled( true );
        gui.getRefMaze().clear();
        gui.getRefMaze().getMazeGenerator().createRandomMaze( gui.DATAFILE );
        gui.getMouse().restart();
        gui.setOutputStats(true);
        gui.getRenderPanel().repaint();
    }

    private void handleNextButtonEvent() {
        FloodFillSolver mouseSolver = gui.getMouse().getMouseSolver();
        if (mouseSolver.exploreNextCell() || gui.isOutputStats()) {
            /* mouse is exploring maze or display mouse statistics after its run */
            gui.getRenderPanel().repaint();
        } else if (mouseSolver.isDone()) {
            /* mouse is done running. */
            System.err.println("Mouse is done running.");
            if (gui.getAnimationCLK().isRunning()) gui.getAnimationCLK().stop();
            gui.getAnimateButton().setText("Animate");
            gui.getAnimateButton().setEnabled(true);
            gui.getNextButton().setEnabled(true);
        }
    }

    private void toggleAlgoDropDown() {
        gui.getAlgoComboBox().setVisible(!gui.getAlgoComboBox().isVisible());
    }

    private void toggleLoadSaveMazeDropDown() {
        gui.getLoadMazeComboBox().setVisible(!gui.getLoadMazeComboBox().isVisible());
    }

    @Override
    public void keyPressed(KeyEvent evt) {
    }
    @Override
    public void keyReleased(KeyEvent evt) {}
    @Override
    public void keyTyped(KeyEvent evt) {}

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent evt) {
    }
    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent evt) {}
    @Override
    public void popupMenuCanceled(PopupMenuEvent evt) {}
}








