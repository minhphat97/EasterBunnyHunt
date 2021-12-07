import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class BatTest {
    @Test
    void constructorTest() {
        var bat = new Bat(329, 143);
        assertEquals(bat.getX(), 329);
        assertEquals(bat.getY(), 143);
    }

	@Test
	void getImageTest() {
        var bat = new Bat(200, 100);

        var initial = bat.getImage();

		bat.setDeltaX(-1);
        var left = bat.getImage();

        bat.setDeltaX(1);
        var right = bat.getImage();

		assertNotNull(initial);
        assertNotNull(left);
        assertNotNull(right);
        assertNotEquals(left, right);
	}

    @Test
    void nextMoveTest_Random() {
        // Directions (2dy + dx + 2)
        // * 0 (dy = -1)
        // * 1 (dx = -1)
        // * 2 (no movement)
        // * 3 (dx = +1)
        // * 4 (dy = +1)

        var b = new Bat(40, 30);
        var h = new Hero(40, 30);

        int directions[] = {0, 0, 0, 0, 0};
        for (int i = 0; i < 1000; ++i) {
            b.setDeltaX(0);
            b.setDeltaY(0);
            b.nextMove(h);
            directions[b.getDeltaX() + 2 * b.getDeltaY() + 2]++;
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

        var b = new Bat(0, 0);
        var h = new Hero(0, 0);

        int directions[] = {0, 0, 0, 0, 0};
        for (int i = 0; i < 10000; ++i) {
            b.nextMove(h);
            directions[b.getDeltaX() + 2 * b.getDeltaY() + 2] = 1;
        }

        int s = 0;
        for (var n : directions)
            s += n;
        assertNotEquals(s, 1);
    }
}

