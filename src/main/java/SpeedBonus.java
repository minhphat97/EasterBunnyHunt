import java.awt.Image;

/**
 * sets the image of the SpeedBonus egg
 */
public class SpeedBonus extends Environment {
    private static Image image = null;
    public final int SPEEDDURATION = 5;
    SpeedBonus() {
        super();
        if (SpeedBonus.image == null) {
            SpeedBonus.image = loadImage("images/speed_bonus.png");
        }
    }

    @Override
    public Image getImage() { return SpeedBonus.image; }
}
