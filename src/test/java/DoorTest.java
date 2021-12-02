import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import java.awt.Image;

public class DoorTest{

	@Test
	void getImageTest(){
		Door door = new Door();
		Image image = door.getImage();
		assertNotNull(image);
		door.open();
		assertEquals(door.checkOpen(), true);
		image = door.getImage();
		assertNotNull(image);
		door.close();
		image = door.getImage();
		assertNotNull(image);
	}

}