import java.awt.Image;

/**
 * The wall will stop player if playing walk upon it, it have 2 differen version of image
 */
public class Wall extends Environment {
	private static Image imageNorm = null;
    private static Image imageAlt = null;

    /** This is the image that is actually displayed.*/
    private Image image = null;
    /** Probaility of diaplaying the alternative version of Wall image*/
    private static double altPercentage = 0.2;

	Wall() {
		super();
		if (Wall.imageNorm == null) {
			Wall.imageNorm = loadImage("images/48_tree_2.png");
            Wall.imageAlt = loadImage("images/48_tree_1.png");
        }
        this.image = Math.random() < Wall.altPercentage ? Wall.imageAlt : Wall.imageNorm;
	}

	@Override
	public Image getImage() { return this.image; }
}
