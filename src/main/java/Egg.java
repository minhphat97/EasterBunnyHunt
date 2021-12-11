import java.awt.Image;

/**
 * Mandatory eggs to collect, eggs may have different images/looks
 * count determines required eggs remaining in game
 */
public class Egg extends Environment {
	private static Image image_1 = null;
    private static Image image_2 = null;
    private static Image image_3 = null;

    // This is the image that is actually displayed.
    private Image image = null;
    private static int count = 0;

    /**
     * setes images of egg randomly, and increments the required egg count
     */
    Egg() {
		super();
		if (Egg.image_1 == null) {
			Egg.image_1 = loadImage("images/48_egg_1.png");
            Egg.image_2 = loadImage("images/48_egg_2.png");
            Egg.image_3 = loadImage("images/48_egg_3.png");
        }
        double percentage = Math.random();//pick random egg image
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
