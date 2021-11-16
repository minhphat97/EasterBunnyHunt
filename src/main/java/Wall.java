import java.awt.Image;
import javax.swing.ImageIcon;


public class Wall extends Environment {
	private static Image imageNorm = null;
    private static Image imageAlt = null;

    // This is the image that is actually displayed.
    private Image image = null;
    private static double altPercentage = 0.2;

	Wall() {
		super();
		if (Wall.imageNorm == null) {
			Wall.imageNorm = new ImageIcon("classes/images/48_tree_2.png").getImage();
            Wall.imageAlt = new ImageIcon("classes/images/48_tree_1.png").getImage();
        }
        this.image = Math.random() < Wall.altPercentage ? Wall.imageAlt : Wall.imageNorm;
	}

	@Override
	public Image getImage() { return this.image; }
}
