package GUI;

import Maze.Maze;
import Mouse.FloodFillSolver;
import Maze.PathFinder;
import Mouse.Mouse;
import utility.ParsingStrings;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.PrintStream;

public class MazeController implements ActionListener, KeyListener, PopupMenuListener {
    public static final int ANIMATION_DELAY = 250;
    private static final File DEVICE_CONNECTED_LOG = new File("/tmp/device_connected.log");
    private static final PrintStream stdoutStream = System.out;

    private MazeGUI gui;
    private PathFinder pathFinder;
    public MazeController(MazeGUI gui) {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == gui.getClearButton()) {
            handleClearButtonEvent();
        } else if (evt.getSource() == gui.getAnimateButton()) {
            handleAnimateButtonEvent();
        } else if (evt.getSource() == gui.getMazeButton()) {
            handleMazeButtonEvent();
        } else if (evt.getSource() == gui.getNextButton() || evt.getSource() == gui.getAnimationCLK()) {
            handleNextButtonEvent();
        }
//        else if (evt.getSource() == gui.getSelectAlgoButton()) {
//            handleSelectAlgoButtonEvent();
//        }
    }

    private void handleClearButtonEvent() {
        gui.getMouse().getMouseSolver().restart();
        gui.setOutputStats(true);
        gui.getRenderPanel().repaint();
    }

    private void handleAnimateButtonEvent() {
        if (!gui.getAnimationCLK().isRunning()) {
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
        System.err.println("\nnew maze");
        gui.getAnimateButton().setText("Animate");
        if (gui.getAnimationCLK().isRunning()) gui.getAnimationCLK().stop();
        gui.getNextButton().setEnabled(true);
        gui.getRefMaze().clear();
        gui.getRefMaze().mazeGenerator().createRandomMaze(MazeGUI.DATAFILE);
        gui.getMouse().getMouseSolver().restart();
        gui.setOutputStats(true);
        gui.getRenderPanel().repaint();
    }

//    private void handleSelectAlgoButtonEvent() {
//        if (gui.isRunDFS()) {
//            System.out.println("Running DFS in MazeController");
//            pathFinder.findPathUsingDFS(gui.getRefMaze().getBegin(), gui.getEndNode());
//        } else if (gui.isRunDijkstra()) {
//            System.out.println("Running Dijkstra in MazeController");
//            pathFinder.findPathUsingDijkstra(gui.getRefMaze().getBegin(), gui.getEndNode());
//        }
//    }


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

    private void handlePortComboBoxEvent() {

    }

    private void handlePeriscopeButtonEvent() {
    }

    private void handleSerialCommEvent(ActionEvent evt) {
    }
}








