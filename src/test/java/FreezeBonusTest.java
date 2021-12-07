import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Image;

public class FreezeBonusTest{

	@Test
	void getImageTest(){
		FreezeBonus fb = new FreezeBonus();
		Image image = fb.getImage();
		assertNotNull(image);
	}

}