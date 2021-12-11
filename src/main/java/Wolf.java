import java.awt.Image;
import java.util.ArrayList;

/**
 * Wolf is one of the the game enemies, it can track the user movment 
 */
public class Wolf extends Enemy {
    private static Image image_left, image_right;

    /**
     * Initializes Wolf speed and initial position attributes as well as images
     * @param x initial spawn x position in pixels for wolf
     * @param y initial spawn y position in pixels for wolf
     */
    public Wolf(int x, int y) {
        super(x, y);
        if (Wolf.image_left == null) {
            Wolf.image_left = loadImage("images/48_wolf_left.gif");
            Wolf.image_right = loadImage("images/48_wolf_right.gif");
        }
        this.image_last = Wolf.image_right;
    }

    @Override
    public Image getImage() {
        if (this.deltaX != 0)
            this.image_last = this.deltaX < 0 ? Wolf.image_left : Wolf.image_right;
        return this.image_last;
    }

    /**
     * Determines the next direction based on position of hero
     * Wolf will track the main character
     * @param h main character in game
     */
    @Override
    public void nextMove(Hero h) {
        if ((this.deltaX != 0 || this.deltaY != 0) && Math.random() > turnPercentage)
            return;

        if (h.getX() != this.x || h.getY() != this.y) {
            var chaseDirections = new ArrayList<int[]>();

            if (h.getX() < this.x)
                chaseDirections.add(new int[]{-1, 0});
            else if (h.getX() > this.x)
                chaseDirections.add(new int[]{1, 0});
            if (h.getY() < this.y)
                chaseDirections.add(new int[]{0, -1});
            else if (h.getY() > this.y)
                chaseDirections.add(new int[]{0, 1});

            int i = chaseDirections.size() == 2 && Math.random() < 0.5 ? 1 : 0;
            this.deltaX = chaseDirections.get(i)[0];
            this.deltaY = chaseDirections.get(i)[1];

        } else {
            switch ((int) (Math.random() * 4)) {
                case 0: this.deltaX = -1; this.deltaY = 0; break;
                case 1: this.deltaX = 1; this.deltaY = 0; break;
                case 2: this.deltaX = 0; this.deltaY = -1; break;
                case 3: this.deltaX = 0; this.deltaY = 1; break;
            }
        }
    }
}
