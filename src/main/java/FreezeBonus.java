import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * sets the image of the freeze bonus
 */
public class FreezeBonus extends Environment {
    private static Image image = null;

    FreezeBonus() {
        super();
        if (FreezeBonus.image == null) {
            FreezeBonus.image = loadImage("images/freeze_bonus.png");
        }
    }

    @Override
    public Image getImage() { return FreezeBonus.image; }
}
