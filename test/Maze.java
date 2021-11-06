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
import javax.swing.Timer;


public class Maze extends JPanel implements ActionListener 
{
    private final Font smallFont = new Font("Helvetica", Font.BOLD, 25);
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

    private Image introScreen,pauseScreen,winScreen,loseScreen;

    public final short EMPTY = 0;
    public final short WALL = 1;
    public final short EGG = 2;
    public final short DOOR = 3;


    public Maze() 
    {
        screenData = new Environment[N_ROW][N_COL];
        levelData = new short[N_ROW][N_COL];
        timer = new Timer(40, this);
        timer.start();
        addKeyListener(new Key());
        setFocusable(true);
        setBackground(Color.green);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        rabbit = new Hero(50, 50);
        enemies = new ArrayList<Enemy>();
        enemies.add(new Hunter(200, 100));
        enemies.add(new Hunter(100, 100));

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
         System.out.println("Maze");

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

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        startDrawing(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        repaint();
    }

    private void startDrawing(Graphics g) 
    {
        drawMaze(g);
        drawHero(g);
        drawEnemy(g);
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
        checkCollision(rabbit);
        rabbit.move();
        g.drawImage(rabbit.getImage(), rabbit.getX(), rabbit.getY(), this);
    }

    private void drawEnemy(Graphics g)
    {
        for (var e : enemies) {
            e.nextMove();
            checkCollision(e);
            e.move();
            g.drawImage(e.getImage(), e.getX(), e.getY(), this);
        }
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
                Egg.decCount();
                screenData[data[0]][data[1]] = new Cell();
                if (Egg.getCount() == 0)
                    Door.open();
            } else if (nextEnv instanceof Door) {
                System.out.println("Win!");
            }
        }
    }
}
