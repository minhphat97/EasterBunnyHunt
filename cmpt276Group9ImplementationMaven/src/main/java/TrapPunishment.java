import java.awt.Image;
import javax.swing.ImageIcon;

public class TrapPunishment extends Punishment{
    private static Image image = null;

    TrapPunishment() {
        super();
        if (TrapPunishment.image == null) {
            TrapPunishment.image = new ImageIcon("cmpt276Group9ImplementationMaven/src/main/resources/images/48_trap.gif").getImage();
        }
    }


    public Image getImage() { return TrapPunishment.image; }
}
