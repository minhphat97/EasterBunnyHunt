Easter Bunny Hunt
=================

![:)](https://csil-git1.cs.surrey.sfu.ca/cmpt276f21_group9/project/-/raw/master/Design/game_images/readme.gif)

Easter Bunny Hunt is a game written in Java using the Swing GUI toolkit.
The style of the game harkens back to old-school top-down arcade games.
This project aims to deliver an easy-to-access nostalgia trip back to the
classics from the early 90s.

Compiling
---------

**Requirements: `Java 17`, `Maven 3.8.4+`**

The codebase is managed with the [Maven][1] build automation system.
As such, using Maven will provide the easiest means of compiling the game.
Follow the official instructions for installing Maven on your system (found [here][2]).

After Maven is installed, the project can now be built.
Download the project from this git repository and navigate to its base directory.
From there, building the project is as easy as running `mvn package`.

If any errors appear, double-check to see if you have the proper requirements.
If the requirements are satisfied, please file a bug report [here][3].

Testing
-------
As mentioned in the previous section, `mvn package` will compile the java file and test all the test cases. We use [Junit 5][4] and [AssertJ Swing][5] library for testing Swing classes. 

During testing, our integration test case will create a [Robot Class][6] from the AssertJ Swing Library to simulate user input, you will temporarily see our game pop up during testing, this is normal as the Robot is trying to play the game. 

PLEASE DO NOT PRESS ANY BUTTON DURING THAT TESTING PHASE AS IT WILL INTERRUPT THE TEST!

Moreover, certain users may get a warning like `WARNING: Exception thrown by a TimerTask`, please ignore the warning as we have confirmed that it does not affect compiling and running the game, the JAR file will still be built with that warning.

We ulilize [JaCoCo Java Code Coverage Library][7] to calulate our test coverages, the detailed report on the coverage can accessed via an HTML file generated in `target/site/jacoco/index.html`.

Running
-------

If the build was successful, the JAR file should be located in `target/EasterBunnyHunt.jar`.
Assuming Java is in the PATH, the game can be started with `java -jar target/EasterBunnyHunt.jar`.

Gameplay
--------

Press Space to enter the Rule Screen to read through the rules. Press Space again to enter the game, you will use the W-A-S-D button to move your character, The Easter Bunny, as you collect all the eggs to open the portal back to Easter Island. 

Avoid all moving Enemies like the Wolf that can sense your small and track you or the Bat Brothers that will gang up on you, it is Game Over if they got you. Be mindful of traps and thornbush laying around the ground as the trap can freeze you and the thornbush can kill you by decreasing your score below 0.

To tip the balance of the scale, Bonus Eggs with special power are randomly popping up to help the Easter Bunny, the Fire Egg can speed up the Easter Bunny, the Ice Egg can freeze all the enemies temporarily, and the Golden Egg can increase your score so you will harder to killed by thornbushes.

When you collected all your eggs, the portal to Easter Island is open and you need to run to the portal to win the game, remember your enemies are still out to get you so don't lower your guard before you enter the portal.

If you want to pause or exit the game, just press ESC to enter the pause screen, then press Space to continue or ESC again to quit the game. 

Documentation
-------------

To generate a javaDoc, please use the command `mvn javadoc::javadoc` , it will genrate a directory in `target/site` named `apidoc`, within you can find all the documentation for our classes' public methods and member. 

Acknowledgements
----------------

Thanks to the CMPT 276 (Fall 2021, D100) teaching staff for providing the
software engineering knowledge that was used to design and build this project.

Special thanks to all the friends and family that acted as our beta testers and provide us with valuable feedback on our game. 

[1]: https://maven.apache.org/
[2]: https://maven.apache.org/install.html
[3]: https://csil-git1.cs.surrey.sfu.ca/cmpt276f21_group9/project/-/issues
[4]: https://junit.org/junit5/
[5]: https://joel-costigliola.github.io/assertj/assertj-swing.html
[6]: https://joel-costigliola.github.io/assertj/swing/api/org/assertj/swing/core/Robot.html
[7]: https://www.eclemma.org/jacoco/

