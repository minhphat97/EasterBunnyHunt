import java.awt.Image;

/**
 * Character onjects are movable entities and have x, y coordinates (in pixels)
 * They also have a direction and speed
 */
public abstract class Character extends GameObject {
    /*
    x,y are the horixontal and vertical position of the object
    Delta X,Y are the changes to x,y in every tick
    Speed are multiplier to Delta X,Y, the higher the speed, the bigger the change to x,y in each tick;
    */
    protected int x, y;
    protected int deltaX, deltaY; 
    protected int defaultSpeed = 3, speed;

    /*All characters have multiple images depending on which way they're
      facing (based on movement). This variable saves the last such image to
      use if there is no current movement command.*/
    protected Image image_last;

    /**
     * Constructor that initializes character with a poistion and speed
     * @param initialX initial x position (spawn) of character
     * @param initialY initial y position (spawn) of character
     */
    public Character(int initialX, int initialY) {
        this.x = initialX;
        this.y = initialY;
        this.speed = defaultSpeed;
    }

    //Various getter and setter;
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public void setX(int n) { this.x = n; }
    public void setY(int n) { this.y = n; }
    public int getDeltaX() { return this.deltaX; }
    public int getDeltaY() { return this.deltaY; }
    public void setDeltaX(int n) { this.deltaX = n; }
    public void setDeltaY(int n) { this.deltaY = n; }
    public int getSpeed() { return this.speed; }
    public void setSpeed(int n) { this.speed = n; }
    public void setDefaultSpeed() { this.speed = this.defaultSpeed; }

    /**
     * updates the character to the next x and y based on direction and speed
     */
    public void move() {
        this.x += this.speed * this.deltaX;
        this.y += this.speed * this.deltaY;
    };

    // To be overwritten by subclasses.
    public abstract Image getImage();
}
