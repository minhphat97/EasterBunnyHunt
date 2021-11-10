public abstract class Enemy extends Character {
    protected static double turnPercentage = 0.05;

    public Enemy(int x, int y) {
        super(x, y);

        this.defaultSpeed = 2;
        this.speed = this.defaultSpeed;
    }

    public void nextMove(Hero h) {
        if ((this.deltaX != 0 || this.deltaY != 0) && Math.random() > turnPercentage)
            return;

        switch ((int) (Math.random() * 4)) {
            case 0: this.deltaX = -1; this.deltaY = 0; break;
            case 1: this.deltaX = 1; this.deltaY = 0; break;
            case 2: this.deltaX = 0; this.deltaY = -1; break;
            case 3: this.deltaX = 0; this.deltaY = 1; break;
        }
    }
}
