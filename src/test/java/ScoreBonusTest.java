import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Image;

public class ScoreBonusTest{

	@Test
	void getImageTest(){
		ScoreBonus sb = new ScoreBonus();
		Image image = sb.getImage();
		assertNotNull(image);
	}

}