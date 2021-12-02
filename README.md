Easter Bunny Hunt
=================

![:)](https://csil-git1.cs.surrey.sfu.ca/cmpt276f21_group9/project/-/blob/master/Design/game_images/rabbit_right.gif)

Easter Bunny Hunt is a game written in Java using the Swing GUI toolkit.
The style of the game harkens back to old school top down arcade games.
This project aims to deliver an easy to access nostalgia trip back to the
classics from the early 90s.

Compiling
---------

**Requirements: `Java 17`, `Maven 3.8.4+`**

The code base is managed with the [Maven][1] build automation system.
As such, using Maven will be provide the easiest means of compiling the game.
Follow the official instructions for installing Maven on your system (found [here][2]).

After Maven is installed, the project can now be built.
Download the project from this git repository and navigate to its base directory.
From there, building the project is as easy as running `mvn package`.

If any errors appear, double check to see if you have the proper requirements.
If the requirements are satisfied, please file a bug report [here][3].

Running
-------

If the build was successful, the JAR file should be located in `target/EasterBunnyHunt.jar`.
Assuming Java is in the PATH, the game can be started with `java -jar target/EasterBunnyHunt.jar`.

Awknowledgements
----------------

Thanks to the CMPT 276 (Fall 2021, D100) teaching staff for providing the
software engineering knowledge used to design and build this project.


[1]: https://maven.apache.org/
[2]: https://maven.apache.org/install.html
[3]: https://csil-git1.cs.surrey.sfu.ca/cmpt276f21_group9/project/-/issues

