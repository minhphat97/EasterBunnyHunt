package easter_bunny_hunt;
import java.awt.Image;

/**
 * One of the Bonus eggs, it will increser player score when player collect it
 */
public class ScoreBonus extends Environment {
    private static Image image = null;

    ScoreBonus() {
        super();
        if (ScoreBonus.image == null) {
            ScoreBonus.image = loadImage("images/48_bonus_egg.png");
        }
    }

    @Override
    public Image getImage() { return ScoreBonus.image; }
}
