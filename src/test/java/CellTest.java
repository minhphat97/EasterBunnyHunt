import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest{

	@Test
	void getImageTest(){
		Cell cell = new Cell();
		assertNull(cell.getImage());
	}

}