import java.awt.Image;
import javax.swing.ImageIcon;

public class Wall extends Environment
{
	private static Image image = null;

	Wall() {
		super();
		if (Wall.image == null) {
			Wall.image = new ImageIcon("images/tree.png").getImage();
		}
	}

	@Override
	public Image getImage() { return Wall.image; }
}