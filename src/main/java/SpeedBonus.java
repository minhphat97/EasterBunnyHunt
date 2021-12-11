package easter_bunny_hunt;
import java.awt.Image;

/**
 * One of the Bonus Eggs, it will speed up user for a few second when player collect it
 */
public class SpeedBonus extends Environment {
    private static Image image = null;
    /**Duration of the speed up effect*/
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
