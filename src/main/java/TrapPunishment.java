import java.awt.Image;

/**
 * Trap is one of the puunihsment class, if the user walk upon it, it freeze the user for a few second
 */
public class TrapPunishment extends Punishment{
    private static Image image = null;
    /**Duration of the trap effect*/
    public final int TRAPDURATION = 2;
    TrapPunishment() {
        super();
        if (TrapPunishment.image == null) {
            TrapPunishment.image = loadImage("images/48_trap.gif");
        }
    }


    public Image getImage() { return TrapPunishment.image; }
}
