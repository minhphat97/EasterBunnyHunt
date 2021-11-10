import java.awt.Image;
import javax.swing.ImageIcon;


public class Hero extends Character {
    private int score = 0;
    private boolean dying;
    public boolean isFast;

    private static Image image_left, image_right, image_up, image_down;

    public Hero(int x, int y) {
        super(x, y);
        if (Hero.image_left == null) {
            Hero.image_left = new ImageIcon("images/48_rabbit_left.gif").getImage();
            Hero.image_right = new ImageIcon("images/48_rabbit_right.gif").getImage();
            Hero.image_up = new ImageIcon("images/48_rabbit_up.gif").getImage();
            Hero.image_down = new ImageIcon("images/48_rabbit_down.gif").getImage();
        }

        this.image_last = Hero.image_right;
    }

    public int getScore() { return this.score; }
    public void setScore(int n) { this.score = n; }
    public void addScore(int n) { this.score += n; }

    @Override
    public Image getImage() {
        if (this.deltaX != 0) {
            this.image_last = this.deltaX < 0 ? Hero.image_left : Hero.image_right;
        } else if (this.deltaY != 0) {
            this.image_last = this.deltaY < 0 ? Hero.image_up : Hero.image_down;
        }

        return this.image_last;
    }
}
