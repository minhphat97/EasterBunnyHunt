import java.awt.Image;
import javax.swing.ImageIcon;

public class Hero extends Character
{
    //adding score variable for our hero
    private int score = 0;
    private boolean dying;
    public boolean isFast;

    private static Image image;

    public Hero(int x, int y) {
        super(x, y);
        if (Hero.image == null) {
            Hero.image = new ImageIcon("images/hero.gif").getImage();
        }
    }
    public int getScore(){return score;}

    public void setScore(int val){score = val;}



    @Override
    public Image getImage() { return Hero.image; }



    //public int checkCollsion();

}
