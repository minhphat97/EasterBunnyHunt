import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;


public class Wolf extends Enemy {
    private static Image image_left, image_right;

    public Wolf(int x, int y) {
        super(x, y);
        if (Wolf.image_left == null) {
            Wolf.image_left = new ImageIcon("images/48_wolf_left.gif").getImage();
            Wolf.image_right = new ImageIcon("images/48_wolf_right.gif").getImage();
        }

        this.image_last = Wolf.image_left;
        this.speed = 2;
    }

    @Override
    public Image getImage() {
        if (this.deltaX != 0)
            this.image_last = this.deltaX < 0 ? Wolf.image_left : Wolf.image_right;
        return this.image_last;
    }

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
