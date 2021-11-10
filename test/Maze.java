import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Timer;


public class Maze extends JPanel implements ActionListener
{
    private final Font smallFont = new Font("MV Boli", Font.BOLD, 25);
    private final Color background = new Color(18,125,9);
    private Hero rabbit;
    private ArrayList<Enemy> enemies;

    boolean playing = false;
    boolean pause = false;

    private final int CELL_SIZE = 48 ;
    private final int N_ROW = 9;
    private final int N_COL = 16;
    private final int SCREEN_WIDTH = N_COL * CELL_SIZE;
    private final int SCREEN_HEIGHT = N_ROW * CELL_SIZE;
    private int score;
    private int key_x, key_y;

    private short[][] levelData;
    private Environment[][] screenData;

    private Timer timer;
    private final int DELAY = 40;//added final for delay, used for in game timer
    private double gameTimer = 0;//keeps track of playing time

    //bonus vars
    private boolean enemyFrozen = false;
    private final int BONUSDURATION = 5;//duration in seconds for the bonus effects to last
    private double freezeTimer =0;//timers required for bunus durations
    private double speedTimer = 0;
    private final int BONUSWAIT = 10;//time in seconds that the bonus will remain on screen before hiding
    private double bonusTimer = 0;//timer to count down while bonus is on screen
    private double respawnTimer = 0;//timer to count down till next bonus
    private boolean onScreen = false;//indicate if there is bonus currently on the screen
    private int bonusCol = 0;//index of the bonus
    private int bonusRow = 0;

    private Image introScreen,pauseScreen,winScreen,loseScreen;

    public final short EMPTY = 0;
    public final short WALL = 1;
    public final short EGG = 2;
    public final short DOOR = 3;
    public final short EGGFREEZE = 4;
    public final short EGGPOINTS = 5;
    public final short EGGSPEED = 6;
    public final short[] BONUS = {EGGFREEZE, EGGSPEED,EGGPOINTS};

    public Maze()
    {
        screenData = new Environment[N_ROW][N_COL];
        levelData = new short[N_ROW][N_COL];
        timer = new Timer(DELAY, this);
        timer.start();
        addKeyListener(new Key());
        setFocusable(true);
        setBackground(Color.green);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT+175));//extra height for the score and timer
        rabbit = new Hero(50, 50);
        enemies = new ArrayList<Enemy>();
        enemies.add(new Bat(100, 100));
        enemies.add(new Hunter(200, 100));

        for (int r = 0; r < N_ROW; ++r) {
            for (int c = 0; c < N_COL; ++c) {
                if (r == 0 || r == N_ROW - 1 || c == 0 || c == N_COL - 1) {
                    levelData[r][c] = WALL;
                } else {
                    levelData[r][c] = EMPTY;
                }
            }
        }
        levelData[4][7] = EGG;
        levelData[5][15] = DOOR;

        initLevel();

    }
    class Key extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            if (key == KeyEvent.VK_A)
            {
                rabbit.setDeltaX(-1);
                rabbit.setDeltaY(0);
            }
            else if (key == KeyEvent.VK_D)
            {
                rabbit.setDeltaX(1);
                rabbit.setDeltaY(0);
            }
            else if (key == KeyEvent.VK_W)
            {
                rabbit.setDeltaX(0);
                rabbit.setDeltaY(-1);
            }
            else if (key == KeyEvent.VK_S)
            {
                rabbit.setDeltaX(0);
                rabbit.setDeltaY(1);
            }

        }
        @Override
        public void keyReleased(KeyEvent e)
        {
            //Uncomment the below code to make pacman stop after releasing key
            rabbit.setDeltaX(0);
            rabbit.setDeltaY(0);
        }
    }



    private void initLevel()
    {
        for (int r = 0; r < N_ROW; ++r) {
            for (int c = 0; c < N_COL; ++c) {
                switch (levelData[r][c]) {
                    case EMPTY: screenData[r][c] = new Cell(); break;
                    case WALL: screenData[r][c] = new Wall(); break;
                    case EGG: screenData[r][c] = new Egg();break;
                    case DOOR: screenData[r][c] = new Door(); break;
                }
            }
        }
    }
    //find a random cell for a bonus to appear in, insert in random bonus in screendata cell
    private void insertBonus() {
        //choose a random bonus
        short bonus = BONUS[(int)(Math.round((Math.random() * 2)))];
        //find empty cell
        int c = (int) (Math.random() * (N_COL - 2)) + 1;//random column not including edge (guaranteed wall)
        int r = (int) (Math.random() * (N_ROW - 2)) + 1;//random row not including edge (guaranteed wall)
        //iterate until empty cell found
        while (!(screenData[r][c].getClass().getName() == "Cell")) {
            c = (int) (Math.random() * (N_COL - 2)) + 1;
            r = (int) (Math.random() * (N_ROW - 2)) + 1;
        }
        switch (bonus) {
            case EGGFREEZE:
                screenData[r][c] = new FreezeBonus();
                break;
            case EGGPOINTS:
                screenData[r][c] = new ScoreBonus();
                break;
            case EGGSPEED:
                screenData[r][c] = new SpeedBonus();
                break;
        }
        onScreen = true;
        bonusTimer = BONUSWAIT;//start the waiting timer
        bonusCol = c;
        bonusRow = r;//keep track of index of bonus in case it needs to be deleted
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        startDrawing(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) //determine next moves, collisions, and draw screen, called each clock cycle
    {
        checkBonus();//check if bonus should be inserted or taken off screen
        repaint();
    }

    //function called each clock cycle
    //determines if bonus should be taken of the screen or add a new bonus onto screen
    private void checkBonus()
    {//check if bonus should be taken off screen
        if(onScreen)
        {
            if((int)bonusTimer == 0)
            {
                screenData[bonusRow][bonusCol] = new Cell();//remove from screen
                onScreen = false;
                respawnTimer = 5+ (int)(Math.random()*10);//wait up to 10 secs for next bonus
            }
            else
            {
                bonusTimer -= ((double) DELAY)/1000;//decrement timer
            }
        }
        else
        {
            //check if it should be shown on the screen
            if((int)respawnTimer == 0)
            {
                //add a bonus onto the screen
                insertBonus();
            }
            else
            {
                respawnTimer -= ((double)DELAY)/1000;
            }
        }
    }
    private void startDrawing(Graphics g)
    {
        drawMaze(g);
        drawHero(g);
        drawEnemy(g);
        drawTimer(g);
        drawScore(g);
        drawPauseInfo(g);
        if(enemyFrozen)//draw bonus effect durations if applicable
        {
            drawFreezeTimer(g);
        }
        if(rabbit.isFast)
        {
            drawSpeedTimer(g);
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }


    private void drawMaze(Graphics g)
    {

        for (int r = 0; r < N_ROW; ++r) {
            for (int c = 0; c < N_COL; ++c) {
                g.drawImage(screenData[r][c].getImage(), c * CELL_SIZE, r * CELL_SIZE, this);
            }
        }
    }

    private void drawHero(Graphics g)
    {
        if(rabbit.isFast)
        {
            checkSpeedBonus();
        }
        checkCollision(rabbit);
        checkHero();
        rabbit.move();
        g.drawImage(rabbit.getImage(), rabbit.getX(), rabbit.getY(), this);
    }

    private void drawEnemy(Graphics g)
    {
        if(enemyFrozen) {//check if enemies should resume moving
            checkFrozen();
        }
        for (var e : enemies) {
            e.nextMove();
            checkCollision(e);
            e.move();
            g.drawImage(e.getImage(), e.getX(), e.getY(), this);
        }
    }

    //added function to display in game timer
    private void drawTimer(Graphics g)
    {
        int hour, min, sec, gameTimerS;
        gameTimer += (double)DELAY/1000;//keep track of game time in seconds
        gameTimerS = (int)gameTimer;
        sec = gameTimerS % 60;
        min = (gameTimerS / 60) % 60;
        hour = gameTimerS / 3600;
        g.setFont(smallFont);
        //display in hour:min:sec format
        g.drawString("Time: "+ hour + " : " + min + " : " + sec, 100,475);
    }

    //functoin to draw score, score depends on bonuses, regular eggs, and traps.
    private void drawScore(Graphics g)
    {
        g.setFont(smallFont);
        g.drawString("Score: " + rabbit.getScore(), 400,475);
    }

    //draws a little message to remind user how to pause
    private void drawPauseInfo(Graphics g)
    {

        g.setFont(new Font("MV Boli", Font.PLAIN, 12));
        //display in hour:min:sec format
        g.drawString("Press \"Esc\" to pause!", 650,475);
    }

    //draw bonus effect timers
    private void drawFreezeTimer(Graphics g)
    {
        g.setFont(smallFont);
        g.drawString("Freeze boost time: " + (int)freezeTimer, 50,575);
    }
    private void drawSpeedTimer(Graphics g)
    {
        g.setFont(smallFont);
        g.drawString("Speed boost time: " + (int)speedTimer, 400,575);
    }

    private void checkCollision(Character c) {
        /**
         * Checks a character for collisions with environment objects.
         * It is assumed that the character is currently not in collision with
         * any environment objects.
         * It is also assumed that the character only moves in the four
         * cardinal directions.
         * <p>
         * Currently handles collisions with the following classes:
         *   - Wall
         *   - Egg
         *   - Door
         *   - ScoreBonus
         *   - FreezeBonus
         *   - SpeedBonus
         *
         * @param  c  The character for which to check collisions.
         */

        int currRow = c.getY() / CELL_SIZE;
        int currCol = c.getX() / CELL_SIZE;
        int nextRow = (c.getY() + c.getSpeed() * c.getDeltaY()) / CELL_SIZE;
        int nextCol = (c.getX() + c.getSpeed() * c.getDeltaX()) / CELL_SIZE;

        // items: { row , col , edgeX , edgeY }
        var checkCells = new ArrayList<int[]>();

        if (c.getDeltaX() != 0) {
            if (c.getDeltaX() < 0) {  // check left collision
                checkCells.add(new int[]{currRow, nextCol, CELL_SIZE * currCol, c.getY()});
                if (c.getY() % CELL_SIZE != 0)
                    checkCells.add(new int[]{currRow + 1, nextCol, CELL_SIZE * currCol, c.getY()});
            } else {  // check right collision
                checkCells.add(new int[]{currRow, nextCol + 1, CELL_SIZE * nextCol, c.getY()});
                if (c.getY() % CELL_SIZE != 0)
                    checkCells.add(new int[]{currRow + 1, nextCol + 1, CELL_SIZE * nextCol, c.getY()});
            }
        } else if (c.getDeltaY() != 0) {
            if (c.getDeltaY() < 0) {  // check up collision
                checkCells.add(new int[]{nextRow, currCol, c.getX(), CELL_SIZE * currRow});
                if (c.getX() % CELL_SIZE != 0)
                    checkCells.add(new int[]{nextRow, currCol + 1, c.getX(), CELL_SIZE * currRow});
            } else {  //check down collision
                checkCells.add(new int[]{nextRow + 1, currCol, c.getX(), CELL_SIZE * nextRow});
                if (c.getX() % CELL_SIZE != 0)
                    checkCells.add(new int[]{nextRow + 1, currCol + 1, c.getX(), CELL_SIZE * nextRow});
            }
        }

        for (var data : checkCells) {
            var nextEnv = screenData[data[0]][data[1]];
            if (nextEnv instanceof Wall || (nextEnv instanceof Door && !Door.checkOpen())) {
                // If wall or closed door.
                c.setDeltaX(0);
                c.setDeltaY(0);
                c.setX(data[2]);
                c.setY(data[3]);
            } else if (c != rabbit) {
                // Don't check the rest of the collisions if this character is
                // not the rabbit.
            } else if (nextEnv instanceof Egg) {
                screenData[data[0]][data[1]] = new Cell();
                Egg.decCount();
                rabbit.setScore(rabbit.getScore() + 1);  // increment score when received egg
                if (Egg.getCount() == 0)
                    Door.open();
            } else if (nextEnv instanceof Door) {
                System.out.println("Win!");
            } else if (nextEnv instanceof ScoreBonus) {  // check for bonuses
                screenData[data[0]][data[1]] = new Cell();
                int bonus = (int) (Math.random() * 5);
                rabbit.setScore(rabbit.getScore() + bonus);
                onScreen = false;
                respawnTimer = 5 + (int) (Math.random() * 10);  // wait up to 10 secs for next bonus
            } else if (nextEnv instanceof FreezeBonus) {
                screenData[data[0]][data[1]] = new Cell();
                enemyFrozen = true;
                freezeEnemies();
                freezeTimer += BONUSDURATION;  // set the freeze timer to begin
                onScreen = false;  // bonus no longer on screen
                respawnTimer = 5 + (int) (Math.random() * 10);  // wait up to 10 secs for next bonus
            } else if (nextEnv instanceof SpeedBonus) {
                screenData[data[0]][data[1]] = new Cell();
                rabbit.setSpeed(10);
                rabbit.isFast = true;
                speedTimer += BONUSDURATION;
                onScreen = false;
                respawnTimer = 5 + (int) (Math.random() * 10);  // wait up to 10 secs for next bonus
            }
        }
    }

    private void checkHero() {
        /**
         * Checks the hero for collisions with any enemies.
         * This differs from ``checkCollision()'' in that this holds the
         * additional checks meant for the hero only whereas the other one is
         * more general and works for any ``Character'' object.
         */

        double dx, dy;
        for (var e : this.enemies) {
            dx = (double) e.getX() - this.rabbit.getX();
            dy = (double) e.getY() - this.rabbit.getY();
            if (Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) <= CELL_SIZE) {
                this.rabbit.hit(3);
                break;
            }
        }
    }

    //classes for frozen egg, checks if enemies should still be frozen
    private void checkFrozen()
    {
        if((int)freezeTimer == 0)
        {
            enemyFrozen = false;//unfreeze the enemies
            unFreezeEnemies();
        }
        else
        {
            freezeTimer = freezeTimer - (double)DELAY/1000;
        }
    }
    private void unFreezeEnemies()
    {
        for (var e : enemies) {
            e.setSpeed(3);
        }
    }
    private void freezeEnemies()
    {
        for (var e : enemies) {
            e.setSpeed(0);
        }
    }
    //function checks if hero should still have speed boost
    private void checkSpeedBonus()
    {
        if((int)speedTimer == 0)
        {
            //return hero to normal speed
            rabbit.isFast = false;
            rabbit.setSpeed(3);
        }
        else
        {
            speedTimer = speedTimer - (double)DELAY/1000;
        }
    }

}
