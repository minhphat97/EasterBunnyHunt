package easter_bunny_hunt;
import java.awt.Image;

/**
 * The main charater of the game, controlled by the user
 */
public class Hero extends Character {
    private int score = 0;
    private boolean dead = false;
    public boolean isFast = false;
    public boolean isTrap = false;
    /** Image for different direction*/
    private static Image image_left, image_right, image_up, image_down;

    /**
     * Initializes hero's spawn location, speed, and directional images
     * @param x initial x spawn position of hero in pixels
     * @param y initial y spawn position of hero in pixels
     */
    public Hero(int x, int y) {
        super(x, y);
        if (Hero.image_left == null) {
            Hero.image_left = loadImage("images/48_rabbit_left.gif");
            Hero.image_right = loadImage("images/48_rabbit_right.gif");
            Hero.image_up = loadImage("images/48_rabbit_up.gif");
            Hero.image_down = loadImage("images/48_rabbit_down.gif");
        }

        this.image_last = Hero.image_right;
    }

    public int getScore() { return this.score; }
    public void setScore(int n) { this.score = n; }
    public void addScore(int n) { this.score += n; }
    public void setDead() { this.dead = true; }
    public boolean isDead() {
        return (this.dead = this.dead || this.score < 0);
    }

    @Override
    public Image getImage() {
        if (this.deltaX != 0) {//returns different image depending on direction
            this.image_last = this.deltaX < 0 ? Hero.image_left : Hero.image_right;
        } else if (this.deltaY != 0) {
            this.image_last = this.deltaY < 0 ? Hero.image_up : Hero.image_down;
        }

        return this.image_last;
    }
}
