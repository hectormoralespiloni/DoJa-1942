Doja 1942 (May 2005)
--------------------

![](https://github.com/hectormoralespiloni/DoJa-1942/blob/master/1942_full.jpg)

1. SUMMARY 
	I ported my J2ME 1942 clone to the i-mode (DoJa) platform

2. REQUIREMENTS TO RUN THE JAR
	* Java 2 Runtime Environment
	* DoJa 1.5 Emulator Ver. 1.07 which can be downloaded from the DoJa Developer
	Network: www.doja-developer.net
	
3. HOW TO PLAY THE DEMO
	* Distributable files are located in the "dist" folder
	* Arrows (or 8/2, 4/6) to move the airplane
	* Action button (or 5) to fire 
	
4. HOW TO COMPILE
	* Once you have downloaded the iAppli (DoJa) emulator, just copy the  i1942 
	directory to the iDK\apps\ folder and open the project from the iApplitool;
	press "build" and then "run". That should work...

5. CODE STURCTURE
	* The images folder contains all the png with transparency used in the game.
	* There are 6 classes:
	    * Sprite.java: 	this class manages the basic sprite stuff such as get and set
			its position, collision detection and drawing the sprite.
	    * Player.java: 	this class inherits from sprite and it just adds the set/get
			state functionality (playing, dying, exploding, or whatever)
	    * Bullet.java:	this class inherits from sprite and adds the owner functionality
			(to know to whom it belongs, a player or an enemy)
	    * Enemy.java:	this class inherits from sprite and adds a move method (which is
			basic AI to move the enemies in a randomly way)
	    * Explosion.java: just another sprite child to handle explosions.
	    * i1942.java:	this is where the magic happens, it creates an IApplication, starts
			and initializes the app. It has a threaded canvas (runnable)
			which acts as the main game loop entry.