import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.IOException;
import javax.swing.ImageIcon;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.AWTException;
import java.lang.Thread;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class MazeTest {
    private Maze maze;
    private Robot bot;
    private Graphics g;
    BufferedImage bi = new BufferedImage(1152,648, BufferedImage.TYPE_INT_ARGB);

    @Test
    void constructorTest(){
        try{
        bot = new Robot();
        g = bi.createGraphics();        
        bot.delay(2000);
        maze = new Maze();
        assertNotNull(maze);
        maze.repaint();
        maze.paintComponent(g);
        g = bi.createGraphics(); 
        bot.delay(2000);
        bot.keyPress(KeyEvent.VK_SPACE);
        bot.delay(100);
        bot.keyRelease(KeyEvent.VK_SPACE);
        maze.paintComponent(g);
        g = bi.createGraphics(); 
        bot.keyPress(KeyEvent.VK_SPACE);
        bot.delay(100);
        bot.keyRelease(KeyEvent.VK_SPACE);
        maze.paintComponent(g);
        g = bi.createGraphics(); 
        bot.keyPress(KeyEvent.VK_ESCAPE);
        bot.delay(100);
        bot.keyRelease(KeyEvent.VK_ESCAPE);
        maze.paintComponent(g);
        g = bi.createGraphics(); 
        bot.keyPress(KeyEvent.VK_ESCAPE);
        bot.delay(100);
        bot.keyRelease(KeyEvent.VK_ESCAPE);
        maze.paintComponent(g);
        g = bi.createGraphics(); 
        }
        catch(AWTException e){
            System.out.println("");
        }

    }
}
