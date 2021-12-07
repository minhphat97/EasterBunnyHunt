import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Image;

public class HeroTest {
    @Test
    void basicTest() {
        var hero = new Hero(329, 143);

        assertEquals(hero.getX(), 329);
        assertEquals(hero.getY(), 143);

        assertEquals(hero.getScore(), 0);
        assertFalse(hero.isDead());
        hero.addScore(-1);
        assertEquals(hero.getScore(), -1);
        assertTrue(hero.isDead());
        hero.addScore(3);
        assertEquals(hero.getScore(), 2);
        assertTrue(hero.isDead());
    }

	@Test
	void getImageTest() {
        var hero = new Hero(200, 100);

        var initial = hero.getImage();
		assertNotNull(initial);

		hero.setDeltaX(-1);
        var left = hero.getImage();

        hero.setDeltaX(1);
        var right = hero.getImage();

        hero.setDeltaX(0);

        hero.setDeltaY(-1);
        var up = hero.getImage();

        hero.setDeltaY(1);
        var down = hero.getImage();

        Image images[] = {left, right, up, down};
        for (int i = 0; i < images.length; ++i) {
            assertNotNull(images[i]);
            for (int j = i + 1; j < images.length; ++j) {
                assertNotEquals(images[i], images[j]);
            }
        }
	}
}

