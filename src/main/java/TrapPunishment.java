import java.awt.Image;

/**
 * sets the image of the trap
 */
public class TrapPunishment extends Punishment{
    private static Image image = null;
    public final int TRAPDURATION = 2;
    TrapPunishment() {
        super();
        if (TrapPunishment.image == null) {
            TrapPunishment.image = loadImage("images/48_trap.gif");
        }
    }


    public Image getImage() { return TrapPunishment.image; }
}
