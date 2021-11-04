import java.awt.Image;
import javax.swing.ImageIcon;

public class Door extends Environment
{
	private static Image image = null;
	private static Image imageOpen = null;	
	private static boolean isOpen = false;

	Door() {
		super();
		if (Door.image == null)
			Door.image = new ImageIcon("images/egg.png").getImage();
		if (Door.imageOpen == null)
			Door.imageOpen = new ImageIcon("images/tree.png").getImage();
	}

	@Override
	public Image getImage() 
	{
		if (isOpen)
			return Door.imageOpen;
		else
			return Door.image;
	}

	public static void open() { Door.isOpen = true; }
	public static boolean checkOpen() { return isOpen; }
}