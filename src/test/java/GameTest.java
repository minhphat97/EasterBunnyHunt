import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.assertj.swing.finder.WindowFinder.findFrame;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import java.awt.event.KeyEvent;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;


public class GameTest{
    private FrameFixture window;
    private Robot robot = BasicRobot.robotWithNewAwtHierarchy();


    @BeforeEach
    public void setUp() {
    Game game = GuiActionRunner.execute(() -> new Game());
    AssertNotNull(game)
    window = new FrameFixture(robot,game);
    AssertNotNull(window)
    window.show(); // shows the frame to test
    }

    @Test
    public void ruleScreensTest() {
        window.panel().pressAndReleaseKeys(KeyEvent.VK_SPACE);
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
    public void restartTest() {
        window.panel().pressAndReleaseKeys(KeyEvent.VK_SPACE,KeyEvent.VK_SPACE,
                                           KeyEvent.VK_ESCAPE,KeyEvent.VK_SPACE,
                                           KeyEvent.VK_ESCAPE,KeyEvent.VK_SPACE);
    }

    @Test
    public void validMoveTest() {
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
    public void inValidMoveTest() {
        window.panel().pressAndReleaseKeys(KeyEvent.VK_SPACE,KeyEvent.VK_SPACE);
        robot.settings().delayBetweenEvents(200);
        window.panel().pressKey(KeyEvent.VK_G);
        window.panel().releaseKey(KeyEvent.VK_G);
        window.panel().pressKey(KeyEvent.VK_T);
        window.panel().releaseKey(KeyEvent.VK_T);       
        window.panel().pressAndReleaseKeys(KeyEvent.VK_ESCAPE);
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }
}
