import java.awt.Image;

/**
 * sets the image of the score bonus egg
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
