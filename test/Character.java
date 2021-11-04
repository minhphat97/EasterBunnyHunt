import java.awt.Image;
import javax.swing.ImageIcon;
public abstract class Character extends GameObject
{
    protected int x,y;
    protected int deltaX, deltaY;
    protected int speed = 3;

    public Character(int initialX, int initialY) {
        this.x = initialX;
        this.y = initialY;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int num) { x = num; }
    public void setY(int num) { y = num; }
    public int getDeltaX() { return deltaX; }
    public int getDeltaY() { return deltaY; }
    public void setDeltaX(int num) { deltaX = num; }
    public void setDeltaY(int num) { deltaY = num; }
    public int getSpeed() { return speed; }
    public void setSpeed(int num) { speed = num; }
    public void move() {
        x += speed * deltaX; //change pacman x and y according to it speed and the direction that it just updated
        y += speed * deltaY;
    };


    public abstract Image getImage();
}