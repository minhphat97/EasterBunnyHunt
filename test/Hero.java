import java.awt.Image;
import javax.swing.ImageIcon;

public class Hero extends Character
{
    private boolean dying;

    private static Image image;

    public Hero(int x, int y) {
        super(x, y);
        if (Hero.image == null) {
            Hero.image = new ImageIcon("images/hero.gif").getImage();
        }

    }

    @Override
    public Image getImage() { return Hero.image; }



    //public int checkCollsion();

}