import java.awt.Image;
import javax.swing.ImageIcon;

public class FreezeBonus extends Environment {
    private static Image image = null;

    FreezeBonus() {
        super();
        if (FreezeBonus.image == null) {
            FreezeBonus.image = new ImageIcon("images/48_bonus_egg.png").getImage();
        }
    }

    @Override
    public Image getImage() { return FreezeBonus.image; }
}
