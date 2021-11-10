import java.awt.Image;
import javax.swing.ImageIcon;

public class ThornBushPunishment extends Environment{
    private static Image image = null;

    ThornBushPunishment() {
        super();
        if (ThornBushPunishment.image == null) {
            ThornBushPunishment.image = new ImageIcon("images/48_thorn_bush.png").getImage();
        }
    }


    public Image getImage() { return ThornBushPunishment.image; }
}
