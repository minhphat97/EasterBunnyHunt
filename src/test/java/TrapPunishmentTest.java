import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import java.awt.Image;

public class TrapPunishmentTest{

	@Test
	void getImageTest(){
		TrapPunishment trap = new TrapPunishment();
		Image image = trap.getImage();
		assertNotNull(image);
	}

}