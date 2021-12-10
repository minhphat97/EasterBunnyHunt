import java.awt.Image;

/**
 * Trap is one of the puunihsment class, if the user walk upon it, it decreases user score
 */
public class ThornBushPunishment extends Punishment{
    
    private static Image image = null;

    ThornBushPunishment() {
        super();
        if (ThornBushPunishment.image == null) {
            ThornBushPunishment.image = loadImage("images/48_thorn_bush.png");
        }
    }


    public Image getImage() { return ThornBushPunishment.image; }
}
