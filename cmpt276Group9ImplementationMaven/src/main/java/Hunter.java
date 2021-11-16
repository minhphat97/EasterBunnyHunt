import java.awt.Image;
import javax.swing.ImageIcon;


public class Hunter extends Enemy {
    private static Image image_left, image_right, image_up, image_down;

    public Hunter(int x, int y) {
        super(x, y);
        if (Hunter.image_left == null) {
            Hunter.image_left = new ImageIcon("cmpt276Group9ImplementationMaven/src/main/resources/images/48_hunter_left.gif").getImage();
            Hunter.image_right = new ImageIcon("cmpt276Group9ImplementationMaven/src/main/resources/images/48_hunter_right.gif").getImage();
            Hunter.image_up = new ImageIcon("cmpt276Group9ImplementationMaven/src/main/resources/images/48_hunter_up.gif").getImage();
            Hunter.image_down = new ImageIcon("cmpt276Group9ImplementationMaven/src/main/resources/images/48_hunter_down.gif").getImage();
        }

        this.image_last = Hunter.image_left;

        this.turnPercentage = 0.02;
        this.defaultSpeed = 3;
        this.speed = this.defaultSpeed;
    }

    @Override
    public Image getImage() {
        if (this.deltaX != 0) {
            this.image_last = this.deltaX < 0 ? Hunter.image_left : Hunter.image_right;
        } else if (this.deltaY != 0) {
            this.image_last = this.deltaY < 0 ? Hunter.image_up : Hunter.image_down;
        }

        return this.image_last;
    }
}
