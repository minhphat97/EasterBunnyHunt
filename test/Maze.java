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

    private void checkCollision(Character c)
    {
        int row = (c.getY() + c.getSpeed() * c.getDeltaY()) / CELL_SIZE;
        int col = (c.getX() + c.getSpeed() * c.getDeltaX()) / CELL_SIZE;

        switch (screenData[row][col].getClass().getName()) {
            case "Wall":
                c.setDeltaX(0);
                c.setDeltaY(0);
                break;
            case "Egg":
                if (c != rabbit) break;
                Egg.decCount();
                screenData[row][col] = new Cell();
                break;
            case "Door":
                if (Door.checkOpen()) {
                    System.out.println("Win!");
                } else {
                    c.setDeltaX(0);
                    c.setDeltaY(0);
                } break;
        }

        if (Egg.getCount() == 0) {
            Door.open();
        }

    }
}
