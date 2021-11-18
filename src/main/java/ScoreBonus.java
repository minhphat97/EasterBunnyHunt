import java.awt.Image;
import javax.swing.ImageIcon;


/**
 * sets the image of the score bonus egg
 */
public class ScoreBonus extends Environment {
    private static Image image = null;

    ScoreBonus() {
        super();
        if (ScoreBonus.image == null) {
            ScoreBonus.image = new ImageIcon("classes/images/48_bonus_egg.png").getImage();
        }
    }

    @Override
    public Image getImage() { return ScoreBonus.image; }
}
