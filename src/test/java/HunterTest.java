import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.IOException;
import javax.swing.ImageIcon;


public class HunterTest {
    @Test
    void constructorTest() {
        var hunter = new Hunter(329, 143);
        assertEquals(hunter.getX(), 329);
        assertEquals(hunter.getY(), 143);
    }

	@Test
	void getImageTest() {
        var hunter = new Hunter(200, 100);

        var initial = hunter.getImage();
		assertNotNull(initial);

		hunter.setDeltaX(-1);
        var left = hunter.getImage();

        hunter.setDeltaX(1);
        var right = hunter.getImage();

        hunter.setDeltaX(0);

        hunter.setDeltaY(-1);
        var up = hunter.getImage();

        hunter.setDeltaY(1);
        var down = hunter.getImage();

        Image images[] = {left, right, up, down};
        for (int i = 0; i < images.length; ++i) {
            assertNotNull(images[i]);
            for (int j = i + 1; j < images.length; ++j) {
                assertNotEquals(images[i], images[j]);
            }
        }
	}

    @Test
    void nextMoveTest_Random() {
        // Directions (2dy + dx + 2)
        // * 0 (dy = -1)
        // * 1 (dx = -1)
        // * 2 (no movement)
        // * 3 (dx = +1)
        // * 4 (dy = +1)

        var hunter = new Hunter(40, 30);
        var h = new Hero(40, 30);

        int directions[] = {0, 0, 0, 0, 0};
        for (int i = 0; i < 1000; ++i) {
            hunter.setDeltaX(0);
            hunter.setDeltaY(0);
            hunter.nextMove(h);
            directions[hunter.getDeltaX() + 2 * hunter.getDeltaY() + 2]++;
        }

        for (int i = 0; i < directions.length; ++i) {
            if (i == 2)
                assertEquals(directions[i], 0);
            else
                assertNotEquals(directions[i], 0);
        }
    }

    @Test
    void nextMoveTest_Turn() {
        // Directions (2dy + dx + 2)
        // * 0 (dy = -1)
        // * 1 (dx = -1)
        // * 2 (no movement)
        // * 3 (dx = +1)
        // * 4 (dy = +1)

        var hunter = new Hunter(0, 0);
        var h = new Hero(0, 0);

        int directions[] = {0, 0, 0, 0, 0};
        for (int i = 0; i < 10000; ++i) {
            hunter.nextMove(h);
            directions[hunter.getDeltaX() + 2 * hunter.getDeltaY() + 2] = 1;
        }

        int s = 0;
        for (var n : directions)
            s += n;
        assertNotEquals(s, 1);
    }
}

