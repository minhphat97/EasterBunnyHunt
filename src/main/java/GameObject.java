import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Super Class for all Game Object
 */
public abstract class GameObject 
{
    /**
     * Get Image for the object to display in game 
     * @return image of the game Object
     * @see Image
     */
    public abstract Image getImage();

    /**
     * Find an file with a given name and return the image it contains
     * @param   fileName  the name of the image that is being search and return 
     * @return  Image     GameObject image that will be display in the game
     * @see               Image
     * @see               Class               
     */
    public Image loadImage(String fileName){
        InputStream is = null;
        Image image = null;
        try{
            is = this.getClass().getClassLoader().getResourceAsStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] byBuf = new byte[10000];
            int byteRead = bis.read(byBuf,0,10000);
            image = Toolkit.getDefaultToolkit().createImage(byBuf);
        }
        catch(IOException e) {
            System.out.println("An error occurred when loading image from file.");
        }
        return image;
    }



}
