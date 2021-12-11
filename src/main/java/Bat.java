package easter_bunny_hunt;
import java.awt.Image;

/**
 * Bat is one of the game enemie, it change direction frequently
 */
public class Bat extends Enemy {
    private static Image image_left, image_right;

    /**
     * Initializes bat enemy's speed, images, and turn radius used for nextMove function
     * @param x initial spwan x position of bat in pixels
     * @param y initial spwan y position of bat in pixels
     */
    public Bat(int x, int y) {
        super(x, y);
        if (Bat.image_left == null) {
            Bat.image_left = loadImage("images/48_bat_left.gif");
            Bat.image_right = loadImage("images/48_bat_right.gif");
        }

        this.image_last = Bat.image_left;

        this.turnPercentage = 0.3;
        this.defaultSpeed = 4;
        this.speed = this.defaultSpeed;
    }

    @Override
    public Image getImage() {
        if (this.deltaX != 0)//change image depending on direction
            this.image_last = this.deltaX < 0 ? Bat.image_left : Bat.image_right;
        return this.image_last;
    }
}
