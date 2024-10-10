package Mouse;

import Maze.Maze;

/**
 *Handles the communication protocols (periscope functionality) for interacting with the virtual environment.
 */
public class PeriscopeHandler {
    private Mouse mouse;
    private Maze maze;
    private boolean periscopeDisplayCellValues = false;

    public PeriscopeHandler(Mouse mouse, Maze maze) {
        this.mouse = mouse;
        this.maze = maze;
    }

    //Communication protocol to display micromouse data in virtual environment.
    public void periscopeProtocol(String data) {
        String textPreamble = "Periscope:";
        byte[] bytePreamble = { (byte) 0xBE, (byte) 0xCA };

        if (data.startsWith(textPreamble)) {
            // text-based protocol
            periscopeDisplayCellValues = false;
            String payload = data.substring(textPreamble.length());
            periscopeTextProtocol(payload);
        } else if (data.startsWith(new String(bytePreamble))) {
            // Byte-based protocol
            periscopeDisplayCellValues = true;
            String payload = data.substring(bytePreamble.length);
            periscopeByteProtocol(payload);
        }
    }

    /**
     * Periscope Text Protocol handling.
     * Preamble: "Periscope: "
     * Example payload: "(3x3)(0,0)(north)(up)"
     * @param payload current data sent from micromouse.
     * @return Nothing.
     */
    private void periscopeTextProtocol(String payload) {
        System.err.println("Will Be Implemented");
    }

    private void periscopeByteProtocol(String payload) {
        System.err.println("Will Be Implemented");
    }
}
