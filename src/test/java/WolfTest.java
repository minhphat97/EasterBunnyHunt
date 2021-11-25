import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.IOException;
import javax.swing.ImageIcon;

public class WolfTest{
	private Wolf wolf;

	@BeforeEach
 	void setup() {
 		wolf = new Wolf(100,100);
 	}
	@Test
	void getImageTest(){
		wolf.setDeltaX(1);
		assert wolf.getImage()!=null;
		wolf.setDeltaX(-1);
		assert wolf.getImage()!=null;
	}
}