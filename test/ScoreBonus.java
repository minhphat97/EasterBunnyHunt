import java.awt.Image;
import javax.swing.ImageIcon;
public class ScoreBonus extends Environment{

    private static Image image = null;
    private final int RANGE = 5;//number range for maximum score bonus (0 to RANGE)
    ScoreBonus() {
        super();
        if (ScoreBonus.image == null) {
            ScoreBonus.image = new ImageIcon("images/tree.png").getImage();
        }
    }

    @Override
    public Image getImage() { return ScoreBonus.image; }
}
