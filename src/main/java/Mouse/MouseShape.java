package Mouse;

import java.awt.*;

public class MouseShape {
    private static final int EVEN = 2;
    private final double HEAD_PROPORTION = 1;
    private Rectangle body;
    private Rectangle head;

    /**
     * Constructor for mouse shape.
     * @param x Top left corner x-coordinate of shape.
     * @param y Top left corner y-coordinate of shape.
     * @param width Pixel width of mouse body.
     * @param height Pixel height of mouse body.
     * @param orientation Initial orientation of the mouse.
     */
    public MouseShape(int x, int y, int width, int height, Orientation orientation) {
        body = new Rectangle(x, y, width, height);
        head = new Rectangle(x, y, (int)(double)(HEAD_PROPORTION * body.width), (int)(double)(HEAD_PROPORTION * body.height));
        this.rotateTo(orientation); // Set head location based on orientation
    }

    /**
     * Default constructor for empty mouse shape.
     */
    public MouseShape() {
        this(0, 0, 0, 0, Orientation.NORTH);
    }

    /**
     * Draws the mouse shape on the GUI.
     * @param g Graphics object to draw with.
     * @param color Color of the mouse shape.
     */
    public void draw(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect(body.x, body.y, body.width, body.height); // Draw mouse body
        g.fillOval(head.x, head.y, head.width, head.height); // Draw mouse head
    }

    /**
     * Rotates the mouse shape to a given orientation on the GUI.
     * @param orientation Compass value that the mouse head points to.
     */
    public void rotateTo(Orientation orientation) {
        // Calculate dx and dy based on the orientation
//        int dx = (orientation.ordinal() % EVEN == 0) ? 0 : -1 * (orientation.ordinal() - 2);
//        int dy = (orientation.ordinal() % EVEN == 0) ? orientation.ordinal() - 1 : 0;
//
//        // Calculate head position based on orientation
//        int head_center_x = (int) (body.x + ((1.0 - HEAD_PROPORTION) / 3.0) * body.width);
//        int head_center_y = (int) (body.y + ((1.0 - HEAD_PROPORTION) / 3.0) * body.height);
//        int head_x = (orientation.ordinal() % EVEN == 0) ? head_center_x : head_center_x + dx * (head.width / 2);
//        int head_y = (orientation.ordinal() % EVEN == 0) ? head_center_y + dy * (head.height / 2) : head_center_y;
//
//        head.setLocation(head_x, head_y);

        int dx = 0, dy = 0;

        switch (orientation) {
            case NORTH -> dy = -1;
            case EAST -> dx = 1;
            case SOUTH -> dy = 1;
            case WEST -> dx = -1;
            case NORTHEAST -> { dx = 1; dy = -1; }
            case SOUTHEAST -> { dx = 1; dy = 1; }
            case SOUTHWEST -> { dx = -1; dy = 1; }
            case NORTHWEST -> { dx = -1; dy = -1; }
        }

        // Calculate the new head position
        int headX = body.x + (int) (dx * body.width * HEAD_PROPORTION / 2);
        int headY = body.y + (int) (dy * body.height * HEAD_PROPORTION / 2);

        head.setLocation(headX, headY);
    }

    /**
     * Sets new dimensions for the mouse shape.
     * @param width Width of the mouse.
     * @param height Height of the mouse.
     */
    public void setDimension(int width, int height) {
        body.setSize(width, height);
        head.setSize((int)(double)(HEAD_PROPORTION * body.width), (int)(double)(HEAD_PROPORTION * body.height));
    }

    /**
     * Sets a new location for the mouse.
     * @param x New x-coordinate.
     * @param y New y-coordinate.
     */
    public void setLocation(int x, int y) {
        head.translate(x - body.x, y - body.y);
        body.setLocation(x, y);
    }

    /**
     * Shifts the mouse shape by dx and dy pixels.
     * @param dx Pixel differential in the x-axis.
     * @param dy Pixel differential in the y-axis.
     */
    public void translate(int dx, int dy) {
        body.translate(dx, dy);
        head.translate(dx, dy);
    }
}
