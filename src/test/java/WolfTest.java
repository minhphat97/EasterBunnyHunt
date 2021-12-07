import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class WolfTest {
	private Wolf wolf;

	@BeforeEach
 	void setup() {
 		wolf = new Wolf(200, 100);
 	}

    @Test
    void constructorTest() {
        wolf = new Wolf(329, 143);
        assertEquals(wolf.getX(), 329);
        assertEquals(wolf.getY(), 143);
    }

	@Test
	void getImageTest() {
        var initial = wolf.getImage();

		wolf.setDeltaX(-1);
        var left = wolf.getImage();

        wolf.setDeltaX(1);
        var right = wolf.getImage();

		assertNotNull(initial);
        assertNotNull(left);
        assertNotNull(right);
        assertNotEquals(left, right);
	}

    @Test
    void nextMoveTest_Target() {
        // Directions (2dy + dx + 2)
        // * 0 (dy = -1)
        // * 1 (dx = -1)
        // * 2 (no movement)
        // * 3 (dx = +1)
        // * 4 (dy = +1)

        var tests = new int[][]{
            {  0,  0, 0, 1 },
            {  0, 10, 1, 1 },
            {  0, 20, 1, 4 },
            { 10, 20, 4, 4 },
            { 20, 20, 3, 4 },
            { 20, 10, 3, 3 },
            { 20,  0, 0, 3 },
            { 10,  0, 0, 0 }
        };

        var w = new Wolf(10, 10);
        for (var t : tests) {
            var h = new Hero(t[0], t[1]);

            int directions[] = {0, 0, 0, 0, 0};
            for (int i = 0; i < 1000; ++i) {
                w.setDeltaX(0);
                w.setDeltaY(0);
                w.nextMove(h);
                directions[w.getDeltaX() + 2 * w.getDeltaY() + 2]++;
            }

            for (int i = 0; i < directions.length; ++i) {
                if (i == t[2] || i == t[3])
                    assertNotEquals(directions[i], 0);
                else
                    assertEquals(directions[i], 0);
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

        var w = new Wolf(40, 30);
        var h = new Hero(40, 30);

        int directions[] = {0, 0, 0, 0, 0};
        for (int i = 0; i < 1000; ++i) {
            w.setDeltaX(0);
            w.setDeltaY(0);
            w.nextMove(h);
            directions[w.getDeltaX() + 2 * w.getDeltaY() + 2]++;
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

        var w = new Wolf(0, 0);
        var h = new Hero(0, 0);

        int directions[] = {0, 0, 0, 0, 0};
        for (int i = 0; i < 10000; ++i) {
            w.nextMove(h);
            directions[w.getDeltaX() + 2 * w.getDeltaY() + 2] = 1;
        }

        int s = 0;
        for (var n : directions)
            s += n;
        assertNotEquals(s, 1);
    }
}
