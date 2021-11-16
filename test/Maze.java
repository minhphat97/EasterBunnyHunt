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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;


import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Maze extends JPanel implements ActionListener {
    private final Font smallFont = new Font("MV Boli", Font.BOLD, 25);
    private final Color background = new Color(18, 125, 9);
    private Hero rabbit;
    private ArrayList<Enemy> enemies;

    boolean pause = false;
    boolean sawStart = false;
    boolean sawRule = false;
    boolean finish = false;
    boolean win = false;

    // Height of the bottom info panel.
    private final int INFO_HEIGHT = 72;

    private final int CELL_SIZE = 48;
    private final int N_ROW = 12;
    private final int N_COL = 24;
    private final int SCREEN_WIDTH = N_COL * CELL_SIZE;
    private final int SCREEN_HEIGHT = N_ROW * CELL_SIZE + INFO_HEIGHT;
    private int score;
    private int key_x, key_y;

    private short[][] levelData;
    private Environment[][] screenData;

    private Timer timer;
    private final int DELAY = 40;  // added final for delay, used for in game timer
    private double gameTimer;  // keeps track of playing time
    private double startTime;
    private double pauseDelay;

    // Bonus vars.
    private boolean enemyFrozen = false;
    private final int BONUSDURATION = 7; // duration in seconds for the bonus effects to last
    private final int TRAPDURATION = 2;
    private double freezeTimer;  // timers required for bunus durations
    private double speedTimer;
    private final int BONUSWAIT = 10;  // time in seconds that the bonus will remain on screen before hiding
    private double bonusTimer;  // timer to count down while bonus is on screen
    private double respawnTimer;  // timer to count down till next bonus
    private double trapTimer; // timer to count down to keep bunny stop
    private boolean onScreen = false;  // indicate if there is bonus currently on the screen
    private int bonusCol;  // index of the bonus
    private int bonusRow;

    private Image introScreen, ruleScreen, pauseScreen, winScreen, loseScreen, bgImage;

    public final short EMPTY = 0;
    public final short WALL = 1;
    public final short EGG = 2;
    public final short DOOR = 3;
    public final short EGGFREEZE = 4;
    public final short EGGPOINTS = 5;
    public final short EGGSPEED = 6;
    public final short TRAP = 7;
    public final short THORNBUSH = 8;
    public final short[] BONUS = { EGGFREEZE, EGGSPEED, EGGPOINTS };

    public Maze() {
        introScreen = new ImageIcon("images/intro.png").getImage();
        pauseScreen = new ImageIcon("images/rule.png").getImage();
        winScreen = new ImageIcon("images/win.png").getImage();
        loseScreen = new ImageIcon("images/lose.png").getImage();
        ruleScreen = new ImageIcon("images/rule.png").getImage();
        bgImage = new ImageIcon("images/Background.png").getImage();

        screenData = new Environment[N_ROW][N_COL];
        levelData = new short[N_ROW][N_COL];

        timer = new Timer(DELAY, this);
        addKeyListener(new Key());

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(background);
        setForeground(Color.WHITE);
        setFocusable(true);

        createLevel();
        initLevel();

        timer.start();
    }


    class Key extends KeyAdapter {
        // This cannot be static because inner classes are lame.
        private int[] allowedKeys = {
                KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D
        };

        // Stack of Integers to keep track of which direction we're moving.
        private Stack<Integer> keyStack = new Stack<Integer>();

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

        @Override
        public void keyPressed(KeyEvent e) {
            Integer key = e.getKeyCode();

            if (!sawStart) {
                if (key == KeyEvent.VK_SPACE) {
                    sawStart = true;
                    startTime = System.currentTimeMillis();//for recurring games, reset timer
                    pauseDelay = 0;
                }
            } else if (!sawRule) {
                if (key == KeyEvent.VK_SPACE) {
                    sawRule = true;
                    startTime = System.currentTimeMillis();//for 1st time playing
                }
            } else if (finish) {
                if (key == KeyEvent.VK_SPACE) {
                    pause = false;
                    sawStart = false;
                    finish = false;
                    win = false;
                    timer.stop();
                    gameTimer = 0;
                    Egg.resetCount();
                    Door.close();
                    createLevel();
                    initLevel();

                    timer.restart();
                }
            } else if (pause) {
                if (key == KeyEvent.VK_SPACE) {
                    pauseDelay = (System.currentTimeMillis()-startTime-gameTimer*1000);//find time passed while paused (ms)
                    pause = false;
                } else if (key == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            } else {
                if (key == KeyEvent.VK_ESCAPE) {
                    pause = true;
                } else {
                    boolean allowed = false;
                    for (var n : this.allowedKeys)
                        allowed = allowed || (n == key);
                    if (!allowed) return;

                    if (this.keyStack.contains(key))
                        this.keyStack.removeElement(key);
                    this.keyStack.push(key);
                    this.processDirection();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            var key = e.getKeyCode();
            if (this.keyStack.contains(key))
                this.keyStack.removeElement(key);
            this.processDirection();
        }
    }

    private void createLevel() {
        /**
         * Generate the data for a level.
         * Automatic garbage collection should clean up the old values.
         */

        rabbit = new Hero(50, 50);
        enemies = new ArrayList<Enemy>();
        enemies.add(new Bat(400, 100));
        enemies.add(new Hunter(300, 150));
        enemies.add(new Wolf(400, 200));
        readLevel();//reads data from a map
    }


    private void readLevel()
    {
        /**
         * reads map matrix from text file into levelData
         * Sets the map for the game
         */
        String Maps[] = {"maps/map1.txt","maps/map2.txt"};
        int max = 2;
        int min = 1;
        Random random = new Random();
        int option =  random.nextInt((max - min) + 1) + min;
        try {
            File myObj = new File("maps/map2.txt");
            Scanner myReader = new Scanner(myObj);//to read through file
            int r = 0;
            while (r < N_ROW) {
                String wholeRow = myReader.nextLine();//read and save next line in file
                String[] rowData = wholeRow.split(",", N_COL);//split up row into elements
                int c=0;
                for(String obj:rowData)
                {
                    levelData[r][c] = Short.parseShort(obj);//assign to proper location
                    c++;
                }
                r++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred loading map from file.");
        }
    }

    private void initLevel() {
        /**
         * Translates the level map into objects at proper locations
         */
        for (int r = 0; r < N_ROW; ++r) {
            for (int c = 0; c < N_COL; ++c) {
                switch (levelData[r][c]) {
                    case EMPTY: screenData[r][c] = new Cell(); break;
                    case WALL: screenData[r][c] = new Wall(); break;
                    case EGG: screenData[r][c] = new Egg(); break;
                    case DOOR: screenData[r][c] = new Door(); break;
                    case TRAP: screenData[r][c] = new TrapPunishment(); break;
                    case THORNBUSH: screenData[r][c] = new ThornBushPunishment(); break;
                }
            }
        }
        insertBonus();
    }

    // Find a random cell for a bonus to appear in, insert in random bonus in screendata cell.
    private void insertBonus() {
        /**
         * chooses a random bonus of the 3 available (freeze, score, speed)
         * finds random empty cell and places bonus there in screenData array
         * will be drawn on the screen from levelData during following cycle
         * also sets time that bonus should disapear if not collected
         */
        // Choose a random bonus.
        short bonus = BONUS[(int)(Math.round((Math.random() * 2)))];

        // Find empty cell.
        int c = (int) (Math.random() * (N_COL - 2)) + 1;  // random column not including edge (guaranteed wall)
        int r = (int) (Math.random() * (N_ROW - 2)) + 1;  // random row not including edge (guaranteed wall)

        // Iterate until empty cell found.
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
        bonusTimer = BONUSWAIT+gameTimer;  //start the waiting timer
        bonusCol = c;
        bonusRow = r;  //keep track of index of bonus in case it needs to be deleted
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, null);
        startDrawing(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Determine next moves, collisions, and draw screen, called each clock cycle.
        repaint();
    }

    // Function called each clock cycle.
    // Determines if bonus should be taken of the screen or add a new bonus onto screen.
    private void checkBonus() {
        /**
         * function checks if the bonus has been on the screen to long,
         * Replaces bonus with empty cell if needed
         * Also determines if a bonus should be spawned into game
         */
        if (onScreen) {
            if ((int) bonusTimer == (int)gameTimer) {
                screenData[bonusRow][bonusCol] = new Cell();  // remove from screen
                onScreen = false;
                resetRespawnTime();
            }
        } else {
            // Check if bonus should be shown on the screen
            if ((int) respawnTimer == (int)gameTimer) {
                // Add a bonus onto the screen.
                insertBonus();
            }
        }
    }

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
        g.dispose();
    }

    private void drawMaze(Graphics g) {
        for (int r = 0; r < N_ROW; ++r) {
            for (int c = 0; c < N_COL; ++c) {
                g.drawImage(screenData[r][c].getImage(), c * CELL_SIZE, r * CELL_SIZE, this);
            }
        }
    }

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

    private void drawEnemy(Graphics g) {
        // Check if enemies should resume moving.
        if(enemyFrozen) {
            checkFrozen();
        }

        for (var e : enemies) {
            e.nextMove(rabbit);
            checkCollision(e);
            e.move();
            g.drawImage(e.getImage(), e.getX(), e.getY(), this);
        }
    }


    private void drawTimer(Graphics g){
        /**
         * draws game playing timer in bottom right of screen
         * time displayed in hour:min:sec
         */
        int hour, min, sec, gameTimerS;
        //calculate playing time based on current time and paused time
        gameTimer = (System.currentTimeMillis() - (startTime+pauseDelay))/ 1000;
        gameTimerS = (int)gameTimer;
        sec = gameTimerS % 60;
        min = (gameTimerS / 60) % 60;
        hour = gameTimerS / 3600;
        g.setFont(smallFont);
        g.drawString("Time: " + hour + ":" + min + ":" + sec, 24, SCREEN_HEIGHT - 26);
    }


    private void drawScore(Graphics g) {
        /**
         * function draws game score which depends on bonuses, regular eggs, and traps
         */
        g.setFont(smallFont);
        g.drawString("Score: " + rabbit.getScore(), 245, SCREEN_HEIGHT - 26);
    }


    private void drawPauseInfo(Graphics g) {
        /**
         * displays hint message on screen detailing how to pause
         */
        g.setFont(new Font("MV Boli", Font.PLAIN, 12));
        g.drawString("Press <esc> to pause!", SCREEN_WIDTH - 140, SCREEN_HEIGHT - 13);
    }


    private void drawFreezeTimer(Graphics g) {
        g.setFont(smallFont);
        double timeLeft = freezeTimer - gameTimer;
        g.drawString("Freeze boost time: " + (int) timeLeft, 420, SCREEN_HEIGHT - 26);
    }

    private void drawSpeedTimer(Graphics g) {
        g.setFont(smallFont);
        double timeLeft = speedTimer - gameTimer;
        g.drawString("Speed boost time: " + (int)timeLeft, 770, SCREEN_HEIGHT - 26);
    }
    private void drawTrapTimer(Graphics g) {
        g.setFont(smallFont);
        double timeLeft = trapTimer - gameTimer;
        g.drawString("Trapped time: " + (int)timeLeft, 770, SCREEN_HEIGHT - 26);
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
                finish = true;
                win = true;
            } else if (nextEnv instanceof ScoreBonus) {  // check for bonuses
                screenData[data[0]][data[1]] = new Cell();
                int bonus = (int) (Math.random() * 5) + 1;
                rabbit.setScore(rabbit.getScore() + bonus);
                onScreen = false;
                resetRespawnTime();
            } else if (nextEnv instanceof FreezeBonus) {
                screenData[data[0]][data[1]] = new Cell();
                enemyFrozen = true;
                freezeEnemies();
                freezeTimer = BONUSDURATION + gameTimer;  // set the freeze timer to begin
                onScreen = false;  // bonus no longer on screen
                resetRespawnTime();
            } else if (nextEnv instanceof SpeedBonus) {
                screenData[data[0]][data[1]] = new Cell();
                rabbit.isFast = true;
                rabbit.setSpeed(6);
                speedTimer = BONUSDURATION + gameTimer;
                onScreen = false;
                resetRespawnTime();
            } else if (nextEnv instanceof TrapPunishment) {
                screenData[data[0]][data[1]] = new Cell();
                rabbit.setSpeed(0);
                rabbit.isTrap = true;
                rabbit.isFast = false;//also lose speed boost if in trap
                trapTimer = TRAPDURATION + gameTimer;
            } else if (nextEnv instanceof ThornBushPunishment) {
                screenData[data[0]][data[1]] = new Cell();
                rabbit.setScore(rabbit.getScore()-1);

            }

        }
    }
    private void resetRespawnTime(){
        /**
         * resets the time to indicate when the next bonus should respawn
         */
        respawnTimer = gameTimer+5 + (int) (Math.random() * 10);  // wait up to 15 secs for next bonus
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
                this.rabbit.setDead();
            }
        }

        finish = finish || rabbit.isDead();
    }

    // function for frozen egg, checks if enemies should still be frozen.
    private void checkFrozen() {
        if ((int) freezeTimer == (int)gameTimer) {
            enemyFrozen = false;  // unfreeze the enemies
            unFreezeEnemies();
        }
    }

    //enemies become movable again
    private void unFreezeEnemies() {
        for (var e : enemies) {
            e.setDefaultSpeed();
        }
    }
    //function stops enemys from moving, called when freeze bonus received
    private void freezeEnemies() {
        for (var e : enemies) {
            e.setSpeed(0);
        }
    }

    // Function checks if hero should still have speed boost.
    private void checkSpeedBonus() {
        if ((int) speedTimer == (int)gameTimer) {
            // Return hero to normal speed.
            rabbit.isFast = false;
            rabbit.setDefaultSpeed();
        }

    }

    private void checkTrap() {
        if ((int)trapTimer == (int)gameTimer) {
            rabbit.setDefaultSpeed();
            rabbit.isTrap = false;
        }
    }

    private void showRule(Graphics g) {
        g.drawImage(ruleScreen, 0, 0, this);
        g.setFont(smallFont);

        String s[] = {
                "RULES",
                "",
                "Collect rainbow eggs to open the portal",
                "Collect golden eggs to get bonuses",
                "Avoid all other objects",
                "Enter the portal to win",
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

    private void showStart(Graphics g) {
        g.drawImage(introScreen, 0, 0, this);
    }

    private void showFinish(Graphics g){
        if (win) {
            g.drawImage(winScreen, 0, 0, this);
        } else {
            g.drawImage(loseScreen, 0, 0, this);
        }

        g.setFont(smallFont);

        String s[] = {
                win ? "YOU WIN" : "YOU LOSE",
                "",
                "Time: " + (int) (gameTimer / 3600) + ":" + (int) ((gameTimer / 60) % 60) + ":" + (int) (gameTimer % 60),
                "Score: " + rabbit.getScore(),
                "",
                "Press <space> to play again"
        };

        var fm = g.getFontMetrics(smallFont);
        int y = (SCREEN_HEIGHT - fm.getHeight() * s.length) / 2;
        for (int i = 0; i < s.length; ++i) {
            int x = (SCREEN_WIDTH - fm.stringWidth(s[i])) / 2;
            g.drawString(s[i], x, y + i * fm.getHeight());
        }
    }
}
