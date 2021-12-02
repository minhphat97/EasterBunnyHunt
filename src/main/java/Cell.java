import java.awt.Image;

/**
 * cell is empty game space, sets image to null
 */
public class Cell extends Environment
{
	Cell() {
		super();
	}

	@Override
	public Image getImage() { return null; }
}
