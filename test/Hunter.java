import java.awt.Image;
import javax.swing.ImageIcon;

public class Hunter extends Enemy
{
    private static Image image;

    public Hunter(int x, int y) {
        super(x, y);
        if (Hunter.image == null) {
            Hunter.image = new ImageIcon("images/tree.png").getImage();
        }
    }


    @Override
    public Image getImage() { return Hunter.image; }

}
