import java.awt.Image;
import javax.swing.ImageIcon;


public class ScoreBonus extends Environment {
    private static Image image = null;

    // Number range for maximum score bonus (0 to RANGE).
    private final int RANGE = 5;

    ScoreBonus() {
        super();
        if (ScoreBonus.image == null) {
            ScoreBonus.image = new ImageIcon("classes/images/48_bonus_egg.png").getImage();
        }
    }

    @Override
    public Image getImage() { return ScoreBonus.image; }
}
