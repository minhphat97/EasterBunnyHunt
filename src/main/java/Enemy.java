/**
 * Parent class for enemies (bat, hunter, wolf)
 */
public abstract class Enemy extends Character {
    protected double turnPercentage = 0.05;

    /**
     * Initializes enemy position and speed
     * @param x initial spwan x position in pixels
     * @param y initial spwan y postion in pixels
     */
    public Enemy(int x, int y) {
        super(x, y);

        this.defaultSpeed = 2;
        this.speed = this.defaultSpeed;
    }

    /**
     * Generates random next direction for enemy to move in, based on enemies direction
     * @param h main character hero in game
     */
    public void nextMove(Hero h) {
        if ((this.deltaX != 0 || this.deltaY != 0) && Math.random() > this.turnPercentage)
            return;

        switch ((int) (Math.random() * 4)) {
            case 0: this.deltaX = -1; this.deltaY = 0; break;
            case 1: this.deltaX = 1; this.deltaY = 0; break;
            case 2: this.deltaX = 0; this.deltaY = -1; break;
            case 3: this.deltaX = 0; this.deltaY = 1; break;
        }
    }
}
