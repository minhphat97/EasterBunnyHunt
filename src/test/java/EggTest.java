import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import java.awt.Image;

public class EggTest{

	@Test
	void getImageTest(){
		Egg egg = new Egg();
		Image image = egg.getImage();
		assertNotNull(image);
		egg = new Egg();
		image = egg.getImage();
		assertNotNull(image);
		egg = new Egg();
		image = egg.getImage();
		assertNotNull(image);

	}

}