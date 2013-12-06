SpriteWorld
===========
Version
-------
ß 1.2.0

Screenshots
-----------
Launcher
![Screenshot of Launcher](http://i.imgur.com/RdlTZL8.png)

View of the spawn area
![Screenshot 1](http://i.imgur.com/neAVbjF.png)

View of character in water
![Screenshot 2](http://i.imgur.com/hZELzSx.png)

View of multiplayer, foreground-background, directional moving and health (the numbers above the name are place holders)
![Screenshot 3](http://i.imgur.com/QcpoxcA.png)

View of images used to create the map, first being the background, and the second being the foreground
![Screenshot 4](http://i.imgur.com/oAOigZs.png)
![Screenshot 5](http://i.imgur.com/juW3XEz.png)

Foreground and background blended together
![Screenshot 6](http://i.imgur.com/eL3GQea.png)

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

Copyright 2013 Beau Taylor-Ladd
