SpriteWorld
-----------
Version
-------
ß 1.2.0

Installation
--------------
You must have Java installed to run the program.
###Windows
Browse over to [here](https://www.dropbox.com/s/a6qg5h8haib651d/SpriteWorld%20Launcher%201.0.4.zip) to download the latest launcher.

###Mac/Linux
Jar located [here](https://www.dropbox.com/s/og0ummabbhliso4/SpriteWorld.jar)

Server
```sh
java -jar SpriteWorld.jar <username>,true
```
Client connecting to server
```sh
java -jar SpriteWorld.jar <username>,<ip>
```

Running a Server
----------------
Running a server is a very simple process, whether you are on Mac/Linux or on Windows.  There are two different ways to setup a server,

###LAN
If you just want to play with your friends over the same internet connection, then this option is for you.  Just have you (or one of your friends) choose to run the server with the launcher for Windows or the following command for Linux/Mac
```sh
java -jar SpriteWorld.jar <username>,true
```

Then for the people connecting, if your on Windows, enter in the hoster's ip into the Connect to server box, and hit play.  For those on Linux/Mac, you must run the following command;
```sh
java -jar SpriteWorld.jar <username>,<hoster's ip>
```
###Wi-Fi
For WiFi, you must follow all the steps above, but before the hoster runs the server, they must port-forward the port '4133' for their computer.


License
-------

MIT

*Free Software, Hell Yeah!*
