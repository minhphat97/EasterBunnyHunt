import java.awt.Image;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.awt.Toolkit;
public abstract class GameObject 
{
    /**
     *Parent of all the game entities used such as enemies, hero, and environment stationary entities
     * @return image of this
     */
    public abstract Image getImage();

    public Image loadImage(String input){
        InputStream is = null;
        Image image = null;
        try{
            is = this.getClass().getClassLoader().getResourceAsStream(input);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] byBuf = new byte[10000];
            int byteRead = bis.read(byBuf,0,10000);
            image = Toolkit.getDefaultToolkit().createImage(byBuf);
        }
        catch(IOException e) {
            System.out.println("An error occurred loading image from file.");
        }
        return image;
    }



}