import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import java.awt.Image;

public class SpeedBonusTest{

	@Test
	void getImageTest(){
		SpeedBonus sb = new SpeedBonus();
		Image image = sb.getImage();
		assertNotNull(image);
	}

}