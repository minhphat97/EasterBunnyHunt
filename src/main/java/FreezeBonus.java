import java.awt.Image;

/**
 * sets the image of the freeze bonus
 */
public class FreezeBonus extends Environment {
    private static Image image = null;
    public final int FREEZEDURATION = 7;
    FreezeBonus() {
        super();
        if (FreezeBonus.image == null) {
            FreezeBonus.image = loadImage("images/freeze_bonus.png");
        }
    }


    @Override
    public Image getImage() { return FreezeBonus.image; }
}
