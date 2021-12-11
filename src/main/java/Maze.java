import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


/**
 * Panel of game to be shown in window, Handles all game object creation
 * <p>
 * Displays all objects on window at desired locations based on next moves and user inputs
 * <p>
 * Handles game logic flow, bonus effects, collision, timers, and image screens
 */
public class Maze extends JPanel implements ActionListener {
    // Constants describing the look of the game window.
    private final Font smallFont = new Font("MV Boli", Font.BOLD, 25);
    private final Color background = new Color(18, 125, 9);

    // Class variables describing the hero and the array of enemies.
    private Hero rabbit;
    private ArrayList<Enemy> enemies;

    /** List of boolean flags describing the current state of the game.
     * Everything starts as false and we first show the start screen.
     * <p>
     * Once the user interacts, sawStart becomes true and the rules screen is shown.
     * Once the user interacts, sawRule becomes true and the main game starts showing.
     * <p>
     * During them main game loop the following can happen
     * <p>
     * - If the user hits the pause key, pause becomes true and the pause screen is shown.
     * <p>
     * - If the user dies or wins, finish becomes true and the screen shown depends on the win flag.
     * <p>
     * - If the user wins, win becomes true.
     */
    private boolean pause=false,sawStart=false,sawRule=false,finish=false,win=false;

    // Height of the bottom info panel.
    private final int INFO_HEIGHT = 72;
    private final int CELL_SIZE = 48;
    private final int N_ROW = 12;
    private final int N_COL = 24;
    private final int SCREEN_WIDTH = N_COL * CELL_SIZE;
    private final int SCREEN_HEIGHT = N_ROW * CELL_SIZE + INFO_HEIGHT;

    /** Pixels of leeway for the horizontal and vertical collision hitboxes respectively.
    * Leeway represents the number of pixels that don't count in the hitbox but are in the image.
    * These values are in pixels.
    */
    private final int H_MARGIN = 20;
    /** Pixels of leeway for the horizontal and vertical collision hitboxes respectively.
    * Leeway represents the number of pixels that don't count in the hitbox but are in the image.
    * These values are in pixels.
    */
    private final int V_MARGIN =  6;

    /**
     *Call repaint at every tick
     */
    private Timer timer;
    /**
     * Time interval between each tick
     */
     private final int DELAY = 40;  

    // Bonus variables.
    /** boolean flag describing whether the enemies are frozen*/
    private boolean enemyFrozen = false; 
    /** time in seconds that the bonus will remain on screen before hiding*/
    private final int BONUSWAIT = 10; 
    /** boolean flag for if there is bonus currently on the screen*/
    private boolean onScreen = false; 
    /** column of the bonus in the maze array*/ 
    private int bonusCol;  
    /** row of the bonus in the maze array*/
    private int bonusRow;  

    /** Class image variables for the multiple static screens.*/
    private Image introScreen, ruleScreen, pauseScreen, winScreen, loseScreen, bgImage;

    private final short EGGFREEZE = 4;
    private final short EGGPOINTS = 5;
    private final short EGGSPEED = 6;
    private final short[] BONUS = { EGGFREEZE, EGGSPEED, EGGPOINTS };

    /** game map, can still access private screenData array to use*/
    private Map map;  
    /** used to handle all in game timers, such as bonus duration and play time clock*/
    private GameTimer gameTime;  

    /**
     * Initializes games images, size, key listener, game map layout of objects
     * Starts games Swing timer to begin performing game "ticks"
     */
    public Maze() {
        introScreen = loadImage("images/intro.png");    
        pauseScreen = loadImage("images/rule.png");
        winScreen = loadImage("images/win.png");
        loseScreen = loadImage("images/lose.png");
        ruleScreen = loadImage("images/rule.png");
        bgImage = loadImage("images/Background.png");

        timer = new Timer(DELAY, this);
        addKeyListener(new Key());

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(background);
        setForeground(Color.WHITE);
        setFocusable(true);

        gameTime = new GameTimer();//create new timer for game time display
        createLevel();
        timer.start();
    }

    private Image loadImage(String input){
        URL stream = this.getClass().getResource(input);
        Image image = null;
        try{
            image = new ImageIcon(ImageIO.read(stream)).getImage();
        }
        catch(IOException e) {
            System.out.println("An error occurred loading image from file.");
        }
        return image;
    }
    /**
     * Handles user input from keyboard, processes character direction
     * Processes game state
     */
    private class Key extends KeyAdapter {
        // This cannot be static because inner classes are lame.
        private int[] allowedKeys = {
                KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D
        };

        // Stack of Integers to keep track of which direction we're moving.
        private Stack<Integer> keyStack = new Stack<Integer>();

        /**
         * depending on keys presssed/released determines Main characters direction
         */
        private void processDirection() {
            rabbit.setDeltaX(0);
            rabbit.setDeltaY(0);

            if (this.keyStack.isEmpty())
                return;

            switch (this.keyStack.peek()) {
                case KeyEvent.VK_A: rabbit.setDeltaX(-1); break;
                case KeyEvent.VK_D: rabbit.setDeltaX(1); break;
                case KeyEvent.VK_W: rabbit.setDeltaY(-1); break;
                case KeyEvent.VK_S: rabbit.setDeltaY(1); break;
            }
        }

        /**
         * Determines state of game (start, pause, rules, playing, finish) based on key being pressed
         * Main logic flow of game, handles pausing features, win/losing and starting new games, and Hero direction
         * @param e Key pressed on keyboard
         */
        @Override
        public void keyPressed(KeyEvent e) {
            Integer key = e.getKeyCode();

            if (!sawStart) {//on initial screen
                if (key == KeyEvent.VK_SPACE) {//transition to rules screen
                    sawStart = true;
                    gameTime = new GameTimer();//start in game timer
                }
            } else if (!sawRule) {
                if (key == KeyEvent.VK_SPACE) {//transition to begin game
                    sawRule = true;
                    gameTime.setStartTime();
                }
            } else if (finish) {
                if (key == KeyEvent.VK_SPACE) {//start a new game
                    pause = false;
                    sawStart = false;
                    finish = false;//restart game, reset vars, timers and counters
                    win = false;
                    timer.stop();
                    Egg.resetCount();
                    Door.close();
                    createLevel();
                    timer.restart();
                }
                else if (key == KeyEvent.VK_ESCAPE) {//quit game
                    System.exit(0);
                }
            } else if (pause) {//on pause screen
                if (key == KeyEvent.VK_SPACE) {//resume game
                    gameTime.setPauseDelay();//save duration paused for
                    pause = false;
                } else if (key == KeyEvent.VK_ESCAPE) {//quit game
                    System.exit(0);
                }
            } else {//playing game
                if (key == KeyEvent.VK_ESCAPE) {//pause
                    pause = true;
                } else {//find next move
                    boolean allowed = false;
                    for (var n : this.allowedKeys)//make sure key is valid for game
                        allowed = allowed || (n == key);
                    if (!allowed) return;

                    if (this.keyStack.contains(key))
                        this.keyStack.removeElement(key);
                    this.keyStack.push(key);
                    this.processDirection();//process new direction based on key
                }
            }
        }

        /**
         * Determine new direction for hero based on released key
         * @param e event of key that was released
         */
        @Override
        public void keyReleased(KeyEvent e) {
            var key = e.getKeyCode();
            if (this.keyStack.contains(key))
                this.keyStack.removeElement(key);
            this.processDirection();
        }
    }
    /**
     * Create game map of environment objects, add in characters at
     * spawn positions
     * Automatic garbage collection should clean up the old values.
     */
    private void createLevel() {
        map = new Map();
        rabbit = new Hero(50, 50);
        enemies = new ArrayList<Enemy>();
        enemies.add(new Bat(400, 100));
        enemies.add(new Bat(800, 480));
        enemies.add(new Hunter(300, 150));
        enemies.add(new Wolf(400, 200));
        insertBonus();
    }



    /**
     * chooses a random bonus of the 3 available (freeze, score, speed)
     * finds random empty cell and places bonus there in screenData array
     * will be drawn on the screen from levelData during following cycle
     * also sets time that bonus should disapear if not collected
     */
    private void insertBonus() {
        // Choose a random bonus.
        short bonus = BONUS[(int)(Math.round((Math.random() * 2)))];

        // Find a random cell for a bonus to appear in, insert in random bonus in screendata cell.
        // Find empty cell.
        int c = (int) (Math.random() * (N_COL - 2)) + 1;  // random column not including edge (guaranteed wall)
        int r = (int) (Math.random() * (N_ROW - 2)) + 1;  // random row not including edge (guaranteed wall)

        // Iterate until empty cell found.
        while (!(map.screenData[r][c].getClass().getName() == "Cell")) {
            c = (int) (Math.random() * (N_COL - 2)) + 1;
            r = (int) (Math.random() * (N_ROW - 2)) + 1;
        }
        switch (bonus) {//insert random bonus at location
            case EGGFREEZE:
                map.screenData[r][c] = new FreezeBonus();
                break;
            case EGGPOINTS:
                map.screenData[r][c] = new ScoreBonus();
                break;
            case EGGSPEED:
                map.screenData[r][c] = new SpeedBonus();
                break;
        }
        onScreen = true;
        gameTime.setBonusDeleteTime(BONUSWAIT);
        bonusCol = c;
        bonusRow = r;  //keep track of index of bonus in case it needs to be deleted
    }

    /**
     * Respaints the game panel with new images
     * @param g graphics object of screen
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);//draw background
        g.drawImage(bgImage, 0, 0, null);//draw background
        startDrawing(g);//draw rest of game
    }

    /**
     * Determine next moves, collisions, and draw objects and screen, called each clock cycle.
     * @param e action performed (triggered by Swing timer)
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        repaint();//calls paint component
    }

    /**
     * function checks if the bonus has been on the screen to long,
     * Replaces bonus with empty cell if needed
     * Also determines if a bonus should be spawned into game
     */
    private void checkBonus() {

        // Function called each clock cycle.
        // Determines if bonus should be taken of the screen or add a new bonus onto screen.
        if (onScreen) {
            if (gameTime.deleteTime()) {//check if it's been on the screen to long
                map.screenData[bonusRow][bonusCol] = new Cell();  // remove from screen
                onScreen = false;
                gameTime.setRespawnTime();
            }
        } else {
            // Check if bonus should be shown on the screen
            if (gameTime.respawnTime()) {
                // Add a bonus onto the screen.
                insertBonus();
            }
        }
    }

    /**
     * Draw the screen to be displayed depending on state of game
     * If playing the game draw all characters, objects and timers accordingly
     * @param g Graphics for maze panel
     */
    private void startDrawing(Graphics g) {
        if (!sawStart) {
            showStart(g);
        } else if (!sawRule) {
            showRule(g);
        } else if (finish) {
            showFinish(g);
        } else if (pause) {
            showPause(g);
        } else {
            checkBonus();  // check if bonus should be inserted or taken off screen
            drawMaze(g);
            drawTimer(g);
            drawScore(g);
            drawHero(g);
            drawEnemy(g);
            drawPauseInfo(g);
            // Draw bonus effect durations if applicable.
            if(enemyFrozen) {
                drawFreezeTimer(g);
            }
            if(rabbit.isFast) {
                drawSpeedTimer(g);
            }
            if(rabbit.isTrap){
                drawTrapTimer(g);
            }
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();//dispose previous graphics
    }

    /**
     * Draws all Environment (stationary) GameObjects on the screen
     * Translates array element to location on game panel
     * @param g Graphics for maze panel
     */
    private void drawMaze(Graphics g) {
        for (int r = 0; r < N_ROW; ++r) {
            for (int c = 0; c < N_COL; ++c) {
                g.drawImage(map.screenData[r][c].getImage(), c * CELL_SIZE, r * CELL_SIZE, this);
            }
        }
    }

    /**
     * Check if Hero is able to move to desired position
     * Draws Hero at new postion (if applicable)
     * @param g Graphics for maze panel
     */
    private void drawHero(Graphics g) {
        if(rabbit.isFast) {
            checkSpeedBonus();
        }
        if (rabbit.isTrap) {
            checkTrap();
        }
        checkCollision(rabbit);
        checkHero();
        rabbit.move();
        g.drawImage(rabbit.getImage(), rabbit.getX(), rabbit.getY(), this);
    }

    /**
     * draw all enemies at their new position
     * @param g Graphics for maze panel
     */
    private void drawEnemy(Graphics g) {
        // Check if enemies should resume moving.
        if(enemyFrozen) {
            checkFrozen();
        }

        for (var e : enemies) {//move and draw all enemies
            e.nextMove(rabbit);//find next move based on hero's position
            checkCollision(e);
            e.move();
            g.drawImage(e.getImage(), e.getX(), e.getY(), this);
        }
    }

    /**
     * draws game playing timer in bottom right of screen
     * time displayed in hour:min:sec
     */
    private void drawTimer(Graphics g){
        g.setFont(smallFont);
        g.drawString("Time: " + gameTime.getHour() + ":" + gameTime.getMin() + ":" + gameTime.getSec(), 24, SCREEN_HEIGHT - 26);
    }

    /**
     * draws player in bottom left of screen
     */
    private void drawScore(Graphics g) {
        //function draws game score which depends on bonuses, regular eggs, and traps
        g.setFont(smallFont);
        g.drawString("Score: " + rabbit.getScore(), 245, SCREEN_HEIGHT - 26);
    }

    /**
     * display pause screen when pause
     */
    private void drawPauseInfo(Graphics g) {
        //displays hint message on screen detailing how to pause
        g.setFont(new Font("MV Boli", Font.PLAIN, 12));
        g.drawString("Press <esc> to pause!", SCREEN_WIDTH - 140, SCREEN_HEIGHT - 13);
    }

    /**
     * display Freeze Timer when eneimes freeze
     */
    private void drawFreezeTimer(Graphics g) {
       //draws freeze bonus duration
        g.setFont(smallFont);
        g.drawString("Freeze boost time: " + gameTime.effectTimeLeft("freeze bonus"), 420, SCREEN_HEIGHT - 26);
    }

    /**
     * display Speed Timer when Hero speed up using speed bonus
     */
    private void drawSpeedTimer(Graphics g) {
        //draws speed bonus duration
        g.setFont(smallFont);
        g.drawString("Speed boost time: " + gameTime.effectTimeLeft("speed bonus"), 770, SCREEN_HEIGHT - 26);
    }
    /**
     * Display Traptime, when Hero is traped
     */
    private void drawTrapTimer(Graphics g) {
        g.setFont(smallFont);
        g.drawString("Trapped time: " + gameTime.effectTimeLeft("trap"), 770, SCREEN_HEIGHT - 26);
    }

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
     *   - Trap
     *   - ThornBush
     *
     * @param  c  The character for which to check collisions.
     */
    private void checkCollision(Character c) {

        int currRow = c.getY() / CELL_SIZE;
        int currCol = c.getX() / CELL_SIZE;
        int nextY = c.getY() + c.getSpeed() * c.getDeltaY();
        int nextX = c.getX() + c.getSpeed() * c.getDeltaX();

        // items: { row , col , edgeX , edgeY }
        var checkCells = new ArrayList<int[]>();

        if (c.getDeltaX() != 0) {
            if (c.getDeltaX() < 0) {  // check left collision
                if (c.getX() % CELL_SIZE >= CELL_SIZE - H_MARGIN && nextX % CELL_SIZE < CELL_SIZE - H_MARGIN) {
                    if (c.getY() % CELL_SIZE < CELL_SIZE - V_MARGIN)
                        checkCells.add(new int[]{currRow, nextX / CELL_SIZE, CELL_SIZE * (currCol + 1) - H_MARGIN, c.getY()});
                    if (c.getY() % CELL_SIZE >= V_MARGIN)
                        checkCells.add(new int[]{currRow + 1, nextX / CELL_SIZE, CELL_SIZE * (currCol + 1) - H_MARGIN, c.getY()});
                }
            } else {  // check right collision
                if (c.getX() % CELL_SIZE < H_MARGIN && nextX % CELL_SIZE >= H_MARGIN) {
                    if (c.getY() % CELL_SIZE < CELL_SIZE - V_MARGIN)
                        checkCells.add(new int[]{currRow, nextX / CELL_SIZE + 1, CELL_SIZE * currCol + H_MARGIN - 1, c.getY()});
                    if (c.getY() % CELL_SIZE >= V_MARGIN)
                        checkCells.add(new int[]{currRow + 1, nextX / CELL_SIZE + 1, CELL_SIZE * currCol + H_MARGIN - 1, c.getY()});
                }
            }
        } else if (c.getDeltaY() != 0) {
            if (c.getDeltaY() < 0) {  // check up collision
                if (c.getY() % CELL_SIZE >= CELL_SIZE - V_MARGIN && nextY % CELL_SIZE < CELL_SIZE - V_MARGIN) {
                    if (c.getX() % CELL_SIZE < CELL_SIZE - H_MARGIN)
                        checkCells.add(new int[]{nextY / CELL_SIZE, currCol, c.getX(), CELL_SIZE * (currRow + 1) - V_MARGIN});
                    if (c.getX() % CELL_SIZE >= H_MARGIN)
                        checkCells.add(new int[]{nextY / CELL_SIZE, currCol + 1, c.getX(), CELL_SIZE * (currRow + 1) - V_MARGIN});
                }
            } else {  // check down collision
                if (c.getY() % CELL_SIZE < V_MARGIN && nextY % CELL_SIZE >= V_MARGIN) {
                    if (c.getX() % CELL_SIZE < CELL_SIZE - H_MARGIN)
                        checkCells.add(new int[]{nextY / CELL_SIZE + 1, currCol, c.getX(), CELL_SIZE * currRow + V_MARGIN - 1});
                    if (c.getX() % CELL_SIZE >= H_MARGIN)
                        checkCells.add(new int[]{nextY / CELL_SIZE + 1, currCol + 1, c.getX(), CELL_SIZE * currRow + V_MARGIN - 1});
                }
            }
        }


        //void checkBonusCollision()
        for (var data : checkCells) {
            var nextEnv = map.screenData[data[0]][data[1]];

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
                map.screenData[data[0]][data[1]] = new Cell();
                Egg.decCount();
                rabbit.setScore(rabbit.getScore() + 1);  // increment score when received egg
                if (Egg.getCount() == 0)
                    Door.open();
            } else if (nextEnv instanceof Door) {
                finish = true;
                win = true;
            } else if (nextEnv instanceof ScoreBonus) {  // check for bonuses
                int bonus = (int) (Math.random() * 5) + 1;
                rabbit.setScore(rabbit.getScore() + bonus);//add score
                deleteBonus(data[0],data[1]);
            } else if (nextEnv instanceof FreezeBonus) {
                enemyFrozen = true;
                freezeEnemies();
                gameTime.setFreezeTime(((FreezeBonus) nextEnv).FREEZEDURATION);
                deleteBonus(data[0],data[1]);
            } else if (nextEnv instanceof SpeedBonus) {
                rabbit.isFast = true;
                rabbit.setSpeed(6);
                gameTime.setSpeedTime(((SpeedBonus) nextEnv).SPEEDDURATION);
                deleteBonus(data[0],data[1]);
            } else if (nextEnv instanceof TrapPunishment) {
                map.screenData[data[0]][data[1]] = new Cell();
                rabbit.setSpeed(0);
                rabbit.isTrap = true;
                rabbit.isFast = false;//also lose speed boost if in trap
                gameTime.setTrapTime(((TrapPunishment) nextEnv).TRAPDURATION);
            } else if (nextEnv instanceof ThornBushPunishment) {
                map.screenData[data[0]][data[1]] = new Cell();
                rabbit.setScore(rabbit.getScore()-1);//remove a point
            }
        }
    }
    /**
     * Checks the bonus eggs operation in the game
     * This replaces for the repeated code
     */
    private void deleteBonus(int Row, int Col){
        map.screenData[Row][Col] = new Cell();
        onScreen = false;
        gameTime.setRespawnTime();
    }
    /**
     * Checks the hero for collisions with any enemies.
     * This differs from ``checkCollision()'' in that this holds the
     * additional checks meant for the hero only whereas the other one is
     * more general and works for any ``Character'' object.
     */
    private void checkHero() {
        double dx, dy;
        for (var e : this.enemies) {//iterate through enemies, check if hero dead
            dx = (double) e.getX() - this.rabbit.getX();
            dy = (double) e.getY() - this.rabbit.getY();
            if (Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) <= CELL_SIZE) {
                this.rabbit.setDead();
            }
        }

        finish = finish || rabbit.isDead();
    }

    /**
     *function for frozen egg, checks if enemies should still be frozen.
     */
    private void checkFrozen() {
        if (gameTime.frozenTimeDone()) {
            enemyFrozen = false;  // unfreeze the enemies
            unFreezeEnemies();
        }
    }

    /**
     * Return enemies to movable entities
     */
    private void unFreezeEnemies() {
        for (var e : enemies) {
            e.setDefaultSpeed();
        }
    }

    /**
     * function stops enemys from moving, called when freeze bonus received
     */
     private void freezeEnemies() {
        for (var e : enemies) {
            e.setSpeed(0);
        }
    }

    /**
     *  Function checks if hero should still have speed boost.
     */
    private void checkSpeedBonus() {
        if (gameTime.speedTimeDone()) {
            // Return hero to normal speed.
            rabbit.isFast = false;
            rabbit.setDefaultSpeed();
        }
    }

    /**
     * Checks if the rabbit should still be trapped
     */
    private void checkTrap() {
        if (gameTime.trapTimeDone()) {
            rabbit.setDefaultSpeed();
            rabbit.isTrap = false;
        }
    }

    /**
     * Displayes rule background and draws info if in rules state
     * @param g Graphics for maze panel
     */
    private void showRule(Graphics g) {
        g.drawImage(ruleScreen, 0, 0, this);
        g.setFont(smallFont);

        String s[] = {
                "RULES",
                "",
                "Move the bunny with <w>, <a>, <s>, <d> keys",
                "Collect rainbow eggs to open the portal",
                "Collect bonus eggs to get extra points / power-up",
                "Avoid enemies, traps and thornbush",
                "Collect all egg and enter the portal to win",
                "press <esc> during game to pause",
                "",
                "Press <space> to continue"
        };

        var fm = g.getFontMetrics(smallFont);
        int y = (SCREEN_HEIGHT - fm.getHeight() * s.length) / 2;
        for (int i = 0; i < s.length; ++i) {
            int x = (SCREEN_WIDTH - fm.stringWidth(s[i])) / 2;
            g.drawString(s[i], x, y + i * fm.getHeight());
        }
    }

    /**
     * Displays pause screen if in paused state, stops Swing timer
     * @param g Graphics of maze panel
     */
    private void showPause(Graphics g) {
        g.drawImage(pauseScreen, 0, 0, this);
        g.setFont(smallFont);

        String s[] = {
                "PAUSED",
                "",
                "Press <space> to resume",
                "Press <esc> to quit"
        };

        var fm = g.getFontMetrics(smallFont);
        int y = (SCREEN_HEIGHT - fm.getHeight() * s.length) / 2;
        for (int i = 0; i < s.length; ++i) {
            int x = (SCREEN_WIDTH - fm.stringWidth(s[i])) / 2;
            g.drawString(s[i], x, y + i * fm.getHeight());
        }
        timer.stop();
    }

    /**
     * Show start Screen when the game starts
     * @param g Graphics for maze panel
     */
    private void showStart(Graphics g) {
        g.drawImage(introScreen, 0, 0, this);
    }

    /**
     * Show finish screen either victory or loss, offers instructions to play again or quit
     * @param g Graphics for maze panel
     */
    private void showFinish(Graphics g){
        if (win) {
            g.drawImage(winScreen, 0, 0, this);
        } else {
            g.drawImage(loseScreen, 0, 0, this);
        }

        g.setFont(smallFont);
        gameTime.stop();
        String s[] = {
                win ? "YOU WIN" : "YOU LOSE",
                "",
                "Time: " + gameTime.getEndHour() + ":" + gameTime.getEndMin() + ":" + gameTime.getEndSec(),
                "Score: " + rabbit.getScore(),
                "",
                "Press <space> to play again",
                "Press <esc> to quit"
        };

        var fm = g.getFontMetrics(smallFont);
        int y = (SCREEN_HEIGHT - fm.getHeight() * s.length) / 2;
        for (int i = 0; i < s.length; ++i) {
            int x = (SCREEN_WIDTH - fm.stringWidth(s[i])) / 2;
            g.drawString(s[i], x, y + i * fm.getHeight());
        }
    }
}
