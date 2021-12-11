package easter_bunny_hunt;
import java.awt.Image;

/**
 * Hunter is part of the enemy class, it have fast speed
 */
public class Hunter extends Enemy {
    private static Image image_left, image_right, image_up, image_down;

    /**
     * Initializes the hunters position, speed, images, and turn percentage used for nextMove
     * @param x initial spawn x position in pixels of hunter
     * @param y initial spwan y position in pixels of hunter
     */
    public Hunter(int x, int y) {
        super(x, y);
        if (Hunter.image_left == null) {
            Hunter.image_left = loadImage("images/48_hunter_left.gif");
            Hunter.image_right = loadImage("images/48_hunter_right.gif");
            Hunter.image_up = loadImage("images/48_hunter_up.gif");
            Hunter.image_down = loadImage("images/48_hunter_down.gif");
        }

        this.image_last = Hunter.image_left;
        this.turnPercentage = 0.02;
        this.defaultSpeed = 3;
        this.speed = this.defaultSpeed;
    }

    @Override
    public Image getImage() {
        if (this.deltaX != 0) {//returns image based on hunters direction
            this.image_last = this.deltaX < 0 ? Hunter.image_left : Hunter.image_right;
        } else if (this.deltaY != 0) {
            this.image_last = this.deltaY < 0 ? Hunter.image_up : Hunter.image_down;
        }

        return this.image_last;
    }
}
