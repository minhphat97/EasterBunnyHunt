import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import java.awt.event.KeyEvent;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;


public class MazeTest{
    private FrameFixture window;
    private Robot robot = BasicRobot.robotWithNewAwtHierarchy();


    @BeforeEach
    public void setUp() {
    Game game = GuiActionRunner.execute(() -> new Game());
    assertThat(game).isNotNull();
    window = new FrameFixture(robot,game);
    assertThat(window).isNotNull();
    window.show(); // shows the frame to test
    }

    @Test
    public void gameScreensTest() {
        window.panel().pressAndReleaseKeys(KeyEvent.VK_SPACE,KeyEvent.VK_SPACE);
    }

    @Test
    public void pauseScreensTest() {
        window.panel().pressAndReleaseKeys(KeyEvent.VK_SPACE,KeyEvent.VK_SPACE,
                                           KeyEvent.VK_ESCAPE);
    }
    @Test
    public void unpauseTest() {
        window.panel().pressAndReleaseKeys(KeyEvent.VK_SPACE,KeyEvent.VK_SPACE,
                                           KeyEvent.VK_ESCAPE,KeyEvent.VK_SPACE,
                                           KeyEvent.VK_ESCAPE);
    }

    @Test
    public void characterMoveTest() {
        window.panel().pressAndReleaseKeys(KeyEvent.VK_SPACE,KeyEvent.VK_SPACE);
        robot.settings().delayBetweenEvents(200);
        window.panel().pressKey(KeyEvent.VK_D);
        window.panel().releaseKey(KeyEvent.VK_D);
        window.panel().pressKey(KeyEvent.VK_S);
        window.panel().releaseKey(KeyEvent.VK_S);
        window.panel().pressKey(KeyEvent.VK_W);
        window.panel().releaseKey(KeyEvent.VK_W);
        window.panel().pressKey(KeyEvent.VK_A);
        window.panel().releaseKey(KeyEvent.VK_A);        
        window.panel().pressAndReleaseKeys(KeyEvent.VK_ESCAPE);
    }


    @Test
    public void thornTest() {
        window.panel().pressAndReleaseKeys(KeyEvent.VK_SPACE,KeyEvent.VK_SPACE);;
        robot.settings().delayBetweenEvents(500);
        window.panel().pressKey(KeyEvent.VK_S);
        window.panel().releaseKey(KeyEvent.VK_S); 
    }

    @Test
    public void deathTest() {
        window.panel().pressAndReleaseKeys(KeyEvent.VK_SPACE,KeyEvent.VK_SPACE);
        robot.settings().delayBetweenEvents(600);
        window.panel().pressKey(KeyEvent.VK_S);
        window.panel().releaseKey(KeyEvent.VK_S);
        window.panel().pressKey(KeyEvent.VK_S);
        window.panel().releaseKey(KeyEvent.VK_S); 
    }

    @Test
    public void trapTest() {
        window.panel().pressAndReleaseKeys(KeyEvent.VK_SPACE,KeyEvent.VK_SPACE);
        robot.settings().delayBetweenEvents(2500);
        window.panel().pressKey(KeyEvent.VK_D);
        window.panel().releaseKey(KeyEvent.VK_D);  
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }
}
