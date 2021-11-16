import java.awt.Image;
import javax.swing.ImageIcon;


public class Bat extends Enemy {
    private static Image image_left, image_right;

    public Bat(int x, int y) {
        super(x, y);
        if (Bat.image_left == null) {
            Bat.image_left = new ImageIcon("classes/images/48_bat_left.gif").getImage();
            Bat.image_right = new ImageIcon("classes/images/48_bat_right.gif").getImage();
        }

        this.image_last = Bat.image_left;

        this.turnPercentage = 0.3;
        this.defaultSpeed = 4;
        this.speed = this.defaultSpeed;
    }

    @Override
    public Image getImage() {
        if (this.deltaX != 0)
            this.image_last = this.deltaX < 0 ? Bat.image_left : Bat.image_right;
        return this.image_last;
    }
}
