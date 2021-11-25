import java.awt.EventQueue;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.net.URL;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Main method that runs, opens game window which game is on
 */
public class Game extends JFrame implements Runnable {
    /**
     * Initializes window and sets image/title
     * Initializes the new maze to be displayed on this
     */
    public Game() {
        Image image = loadImage("images/72_egg_1.png");
        setIconImage(image);  // set application icon
        setResizable(false);
        setContentPane(new Maze());
        setTitle("Easter Bunny Hunt");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }
    public Image loadImage(String input){
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

    @Override
    public void run() {
        setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Game());
    }
}
