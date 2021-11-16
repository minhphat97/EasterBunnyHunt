import java.awt.Image;
import javax.swing.ImageIcon;


public class Egg extends Environment {
	private static Image image_1 = null;
    private static Image image_2 = null;
    private static Image image_3 = null;

    // This is the image that is actually displayed.
    private Image image = null;
    private static int count = 0;

	Egg() {
		super();
		if (Egg.image_1 == null) {
			Egg.image_1 = new ImageIcon("cmpt276Group9ImplementationMaven/src/main/resources/images/48_egg_1.png").getImage();
            Egg.image_2 = new ImageIcon("cmpt276Group9ImplementationMaven/src/main/resources/images/48_egg_2.png").getImage();
            Egg.image_3 = new ImageIcon("cmpt276Group9ImplementationMaven/src/main/resources/images/48_egg_3.png").getImage();
        }
        double percentage = Math.random();
        if (percentage<0.3){
        	this.image = Egg.image_1;
        }
        else if (percentage>0.3 && percentage<0.6){
        	this.image = Egg.image_2;
        }
        else{
        	this.image = Egg.image_3;
        }    
		count++;
	}

	@Override
	public Image getImage() { return this.image; }

	public static int getCount() { return Egg.count; }
	public static void decCount() { Egg.count--; }
    public static void resetCount() { Egg.count = 0; }
}
