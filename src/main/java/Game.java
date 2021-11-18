import java.awt.EventQueue;
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
        var image = new ImageIcon("classes/images/72_egg_1.png");
        setIconImage(image.getImage());  // set application icon
        setResizable(false);
        setContentPane(new Maze());
        setTitle("Easter Bunny Hunt");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void run() {
        setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Game());
    }
}
