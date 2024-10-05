package Mouse;

import java.awt.*;

/**
 * MouseShape constitutes the generic mouse shape.
 */
public class MouseShape {
    private static final double HEAD_PROPORTION = 1;
    private Rectangle body;
    private Rectangle head;
    private Orientation orientation;

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
        head = new Rectangle(x, y, (int)(double)(HEAD_PROPORTION*body.width), (int)(double)(HEAD_PROPORTION*body.height));
        this.orientation = orientation;
        this.rotateTo(orientation); //set head location
    }

    public MouseShape() {
        this(0, 0, 0, 0, Orientation.NORTH);
    }

    //Draw mouse shape in GUI
    public void draw(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect(body.x, body.y, body.width, body.height);
        g.fillOval(head.x, head.y, head.width, head.height);
    }

    // Rotates the mouse shape on given orientation on GUI.
    public void rotateTo(Orientation orientation) {
        this.orientation = orientation;
        int dx = (orientation.ordinal() % 2 == 0) ? 0 : -1 * (orientation.ordinal() - 2);
        int dy = (orientation.ordinal() % 2 == 0) ? orientation.ordinal() - 1 : 0;
        int head_center_x = (int) (body.x + ((1.0 - HEAD_PROPORTION) / 3.0) * body.width);
        int head_center_y = (int) (body.y + ((1.0 - HEAD_PROPORTION) / 3.0) * body.height);
        int head_x = (orientation.ordinal() % 2 == 0) ? head_center_x : head_center_x + dx * (head.width / 2);
        int head_y = (orientation.ordinal() % 2 == 0) ? head_center_y + dy * (head.height / 2) : head_center_y;
        //set head location
        head.setLocation(head_x, head_y);
    }

    // Sets new dimension of mouse shape
    public void setDimension(int width, int height) {
        body.setSize(width, height);
        head.setSize((int) (HEAD_PROPORTION * body.width), (int) (HEAD_PROPORTION * body.height));
    }

    // Set new location for mouse
    public void setLocation(int x, int y) {
        head.translate(x - body.x, y - body.y);
        body.setLocation(x, y);
    }

    // Shifts the mouse shape by dx and dy pixels.
    public void translate(int dx, int dy) {
        body.translate(dx, dy);
        head.translate(dx, dy);
    }
}
