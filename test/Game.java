import java.awt.EventQueue;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable
{
    public Game()
    {

        ImageIcon image = new ImageIcon("images/tree.png");
        setIconImage(image.getImage());//sets application icon
        setContentPane(new Maze());
        setTitle("Easter Bunny Hunt");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void run()
    {
        setVisible(true);
    }

    public static void main(String[] args)
    {

        EventQueue.invokeLater(new Game());
    }
}
