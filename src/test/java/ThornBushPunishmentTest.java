import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import java.awt.Image;

public class ThornBushPunishmentTest{

	@Test
	void getImageTest(){
		ThornBushPunishment tb = new ThornBushPunishment();
		Image image = tb.getImage();
		assertNotNull(image);
	}

}