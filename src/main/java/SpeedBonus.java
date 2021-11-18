import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * sets the image of the SpeedBonus egg
 */
public class SpeedBonus extends Environment {
    private static Image image = null;

    SpeedBonus() {
        super();
        if (SpeedBonus.image == null) {
            SpeedBonus.image = new ImageIcon("classes/images/speed_bonus.png").getImage();
        }
    }

    @Override
    public Image getImage() { return SpeedBonus.image; }
}
