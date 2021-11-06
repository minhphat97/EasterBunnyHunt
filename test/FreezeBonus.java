import java.awt.Image;
import javax.swing.ImageIcon;

public class FreezeBonus extends Environment{

    private static Image image = null;

    FreezeBonus() {
        super();
        if (FreezeBonus.image == null) {
            FreezeBonus.image = new ImageIcon("images/tree.png").getImage();
        }
    }

    @Override
    public Image getImage() { return FreezeBonus.image; }//
}
