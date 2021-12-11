import java.awt.Image;

/**
 * Cell is empty game space that player walk on, sets image to null
 */
public class Cell extends Environment
{
	Cell() {
		super();
	}

	@Override
	public Image getImage() { return null; }
}
