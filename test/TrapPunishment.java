import java.awt.Image;
import javax.swing.ImageIcon;

public class TrapPunishment extends Environment{
    private static Image imageClose = null;
    private static Image imageOpen = null;  
    private static boolean isOpen = true;

    TrapPunishment() {
        super();
        if (TrapPunishment.imageClose == null) {
            TrapPunishment.imageClose = new ImageIcon("images/48_trap_close.png").getImage();
            TrapPunishment.imageOpen = new ImageIcon("images/48_trap_open.png").getImage();
        }
    }

    @Override
    public Image getImage() {
        return TrapPunishment.isOpen ? TrapPunishment.imageOpen : TrapPunishment.imageClose;
    }

    public static void open() { TrapPunishment.isOpen = true; }
    public static void close() { TrapPunishment.isOpen = false; }
    public static boolean checkOpen() { return TrapPunishment.isOpen; }
}
