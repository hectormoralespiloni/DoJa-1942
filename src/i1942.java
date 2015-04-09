///============================================================================
///\file    i1942.java
///\brief   1942 videogame clone for i-mode
///\date    September 19, 2006
///\author  Héctor Morales Piloni
///============================================================================

import com.nttdocomo.ui.*;
import java.util.*;

public class i1942 extends IApplication
{
    private SSCanvas screen;
    
    public void start() 
    {
	screen  = new SSCanvas(this);
	Display.setCurrent(screen);
    }
    
    public void exitGame()
    {
	terminate();
    }
    
    public void startGame()
    {
	new Thread(screen).start();
    }
}

class SSCanvas extends Canvas implements Runnable
{ 
    private final int SOFT_LEFT = Frame.SOFT_KEY_1;
    private final int SOFT_RIGHT = Frame.SOFT_KEY_2;
    private final int SLEEP_TIME = 40;	    //how many millisecons the Thread will sleep
    private final int MAX_TILES = 16;
    private final int MAX_ENEMIES = 7;
    private final int MAX_BULLETS = 7;
    private final int MAX_DIGITS = 4;	    //from 0000 to 9999
    private final int MAX_EXPLOSIONS = 7;
    private final int MAX_LIVES = 3;
    private final int PLAYER = 1;
    private final int ENEMY = 2;
    private final int TILE_SIZE = 17;

    private int score;		    //game score		
    private int cicle;		    //number of game cycles (every sleepTime ms)
    private int lives;		    //player lives
    private int shield;		    //player shield
    private int deltaX,deltaY;	    //used to compute player position
    private int xTiles, yTiles;	    //pretty self explanatory =)
    private int index, index_in;    //used for scrolling background

    private boolean playing;	    //to know if we're playing
    private boolean fireOn;	    //to know if we're firing
    private boolean collision;	    //to know if we have a collision
    
    private sprite intro = new sprite(1);
    private sprite gameOver = new sprite(1);
    private sprite getReady = new sprite(2);
    private sprite scoreSprite = new sprite(1);
    private sprite livesSprite[] = new sprite[3];
    private sprite digits[] = new sprite[MAX_DIGITS];
    private sprite tiles[] = new sprite[MAX_TILES];
    private player player = new player(1);
    private enemy enemies[] = new enemy[MAX_ENEMIES];
    private bullet bullets[] = new bullet[MAX_BULLETS];
    private explosion explosions[] = new explosion[MAX_EXPLOSIONS];

    //the first and last 12 rows must be the same to avoid flickering
    int map [] = {0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  //-------------
		  0,1,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,3,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,4,5,0,0,0,2,0,
		  0,6,7,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  2,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,8,9,0,
		  0,0,0,0,0,10,11,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,2,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,3,0,0,0,0,2,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,12,13,0,0,0,0,
		  0,0,14,15,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,3,0,0,0,0,0,
		  0,0,0,0,0,0,1,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,2,0,0,0,1,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,2,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,1,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,3,0,2,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,4,5,0,0,0,2,0,
		  0,6,7,0,0,0,0,0,
		  0,0,0,0,0,1,0,0,
		  2,0,0,0,1,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,1,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,1,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,3,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,4,5,0,0,0,2,0,
		  0,6,7,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  2,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  //------------
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0};
    
    //finally the IApplication used to call the terminate method 
    i1942 listener;
    
    public SSCanvas(i1942 listener) 
    {
	this.listener = listener;
	setSoftLabel(SOFT_LEFT, "Play");
        setSoftLabel(SOFT_RIGHT, "Exit");
	
	intro.addFrame(1, "intro.gif");
	player.addFrame(1, "player.gif");
	
	intro.on();
	player.on();
    }
   
    /**
     * gameInit()
     *
     * initializes game variables
     */
    public void gameInit()
    {
	int i;
	
	collision = false;
	//playing = true;
	deltaX = 0;
	deltaY = 0;
	xTiles = 8;
	yTiles = 11;
	cicle = 0;
	score = 0;
	shield = 0;
	lives = MAX_LIVES;
	index_in = 0;
	index = map.length - (xTiles*yTiles);
	
	scoreSprite.addFrame(1,"score.gif");
	scoreSprite.on();
	scoreSprite.setX(5);
	scoreSprite.setY(5);
	
	//init player position
	player.setX((getWidth()/2)-(player.getW()/2));
	player.setY(getHeight()-player.getH());
	
	//init banners
	gameOver.addFrame(1,"gameover.gif");
	gameOver.setX(0);
	gameOver.setY(0);
	gameOver.off();
	
	getReady.addFrame(1,"getready1.gif");
	getReady.addFrame(2,"getready2.gif");
	getReady.setX(getWidth()/2-(getReady.getW()/2));
	getReady.setY(getHeight()/2+(getReady.getH()/2));
	getReady.off();
	
	//init HUD
	for(i=0; i<MAX_LIVES; i++)
	{
	    livesSprite[i] = new sprite(1);
	    livesSprite[i].addFrame(1,"lives.gif");
	    livesSprite[i].on();
	    livesSprite[i].setX(getWidth()-(livesSprite[i].getW()*(i+1)));
	    livesSprite[i].setY(5);
	}
	
	for(i=0; i<MAX_DIGITS; i++)
	{
	    digits[i] = new sprite(10);
	    digits[i].addFrame(1,"zero.gif");
	    digits[i].addFrame(2,"one.gif");
	    digits[i].addFrame(3,"two.gif");
	    digits[i].addFrame(4,"three.gif");
	    digits[i].addFrame(5,"four.gif");
	    digits[i].addFrame(6,"five.gif");
	    digits[i].addFrame(7,"six.gif");
	    digits[i].addFrame(8,"seven.gif");
	    digits[i].addFrame(9,"eight.gif");
	    digits[i].addFrame(10,"nine.gif");
	    digits[i].off();
	}
	
	//init enemies
	for(i=0; i<MAX_ENEMIES; i++) 
	{
	    enemies[i] = new enemy(2);
	    enemies[i].addFrame(1,"enemy1.gif");
	    enemies[i].addFrame(2,"enemy2.gif");
	    enemies[i].off();
	}
	
	//init bullets
	for(i=0; i<MAX_BULLETS; i++) 
	{
	    bullets[i] = new bullet(2);
	    bullets[i].addFrame(1,"playerBullet.gif");
	    bullets[i].addFrame(2,"enemyBullet.gif");
	    bullets[i].off();
	}
	
	//init explosions
	for(i=0; i<MAX_EXPLOSIONS; i++) 
	{
	    explosions[i] = new explosion(7);
	    explosions[i].addFrame(1,"explosion1.gif");
	    explosions[i].addFrame(2,"explosion2.gif");
	    explosions[i].addFrame(3,"explosion3.gif");
	    explosions[i].addFrame(4,"explosion4.gif");
	    explosions[i].addFrame(5,"explosion5.gif");
	    explosions[i].addFrame(6,"explosion6.gif");
	    explosions[i].addFrame(7,"explosion7.gif");
	    explosions[i].off();
	}
	
	//init tiles
	for(i=0; i<MAX_TILES; i++) 
	{
	    tiles[i] = new sprite(1);
	    tiles[i].on();
	}
	
	for(i=1; i<=MAX_TILES; i++)
	    tiles[i-1].addFrame(1,"tile"+i+".gif");
    }
    
    /**
     * isPlaying()
     *
     * @return true is currently playing, false otherwise
     */
    boolean isPlaying() 
    {
	return playing;
    }
    
    /**
     * quitGame()
     *
     * sets playing flag to false to exit main game loop
     */
    void quitGame() 
    {
	playing = false;
    }
    
    /**
     * scroll()
     *
     * animates the background tiles
     */
    void scroll() 
    {
	index_in += 2;
	if(index_in >= TILE_SIZE)
	{
	    index_in = 0;
	    index -= xTiles;
	}
	
	if(index <= 0)
	{
	    index = map.length - (xTiles*yTiles);
	    index_in = 0;
	}
    }
    
    /**
     * enemyUpdate()
     *
     * updates enemies position
     */
    void enemyUpdate() 
    {
	int freeEnemy,i;
	Random random = new Random();
	
	//Create an enemy every 20 cicles
	if (cicle%20 == 0) 
	{
	    freeEnemy=-1;
	    //Look for a free enemy slot
	    for (i=0; i<MAX_ENEMIES; i++) 
	    {
		if (!enemies[i].isActive()) 
		{
		    freeEnemy = i;
		    break;
		}
	    }
	    
	    if (freeEnemy >= 0) 
	    {
		enemies[freeEnemy].on();
		enemies[freeEnemy].setX((Math.abs(random.nextInt()) % getWidth()));
		enemies[freeEnemy].setY(-1*enemies[freeEnemy].getH());
		enemies[freeEnemy].setState(1);
		enemies[freeEnemy].setType((Math.abs(random.nextInt()) % 2) + 1);
		enemies[freeEnemy].init(player.getX());
	    }
	}
	
	//Move enemies
	for (i=0; i<MAX_ENEMIES; i++) 
	{
	    if (enemies[i].isActive()) 
		enemies[i].move();

	    //check if enemy is out of the screen
	    if (enemies[i].getY() > (getHeight()+enemies[i].getH())) 
		enemies[i].off();
	}
    }
    
    /**
     * bulletUpdate()
     *
     * updates bullets position
     */
    void bulletUpdate() 
    {
	int freeBullet;
	int theEnemy, i, j;
	freeBullet=-1;
	
	if (fireOn && (cicle%5 == 0)) 
	{
	    //Look for an empty bullet slot
	    for (i=0; i<MAX_BULLETS; i++) 
	    {
		if (!bullets[i].isActive()) 
		{
		    freeBullet=i;
		    break;
		}
	    }
	    
	    if (freeBullet >= 0) 
	    {
		//shot already, this is the first bullet
		bullets[freeBullet].on();
		bullets[freeBullet].setX((player.getX()+(player.getW()/2))-(bullets[freeBullet].getW()/2));
		bullets[freeBullet].setY(player.getY()-bullets[freeBullet].getH());
		bullets[freeBullet].setOwner(PLAYER);
	    }//if freeBullet > 0
	}
	
	freeBullet=-1;
	theEnemy=0;
	for (i=0; i<MAX_ENEMIES; i++) 
	{
	    if(enemies[i].isActive() && enemies[i].getY() > getHeight()/3 && enemies[i].getY() < (getHeight()/3)+5) 
	    {
		//Look for an empty bullet slot
		for (j=1; j<MAX_BULLETS; j++) 
		{
		    if (!bullets[j].isActive()) 
		    {
			freeBullet=j;
			theEnemy=i;
			break;
		    }
		}
		
		if (freeBullet >=0) 
		{
		    bullets[freeBullet].on();
		    bullets[freeBullet].setX(enemies[theEnemy].getX()+(enemies[theEnemy].getW()/2)-(bullets[freeBullet].getW()/2));
		    bullets[freeBullet].setY(enemies[theEnemy].getY()+enemies[theEnemy].getH());
		    bullets[freeBullet].setOwner(ENEMY);
		}
	    }
	}
	
	//update bullets positions
	for (i=0; i<MAX_BULLETS; i++) 
	{
	    if (bullets[i].isActive()) 
		bullets[i].move();
	    
	    if ((bullets[i].getY() > getHeight()) || (bullets[i].getY() <= 0)) 
		bullets[i].off();
	}
    }
    
    /**
     * explosionUpdate()
     *
     * updates explosions animations
     */
    void explosionUpdate() 
    {
	int i;
	for (i=0; i<MAX_EXPLOSIONS; i++) 
	    explosions[i].move();
    }
    
    /**
     * HUDUpdate()
     *
     * updates score and lives counter
     */
    void HUDUpdate() 
    {
	int aux, factor;
	factor = 1;
	
	for(int i=0; i<=MAX_DIGITS; i++)
	{
	    try
	    {
		aux = score/factor;
		digits[i].selFrame((aux%10)+1);
		factor*=10;
	    } 
	    catch(Exception ex){}
	}
    }
    
    /**
     * createExplosion()
     *
     * create a new explosion animation
     *
     * @param posX - integer representing the X coordinate
     * @param posY - integer representing the Y coordinate
     */
    void createExplosion(int posx, int posy) 
    {
	int freeExplode,i;
	freeExplode=-1;
	
	//Look for a free explosion slo
	for (i=0; i<MAX_EXPLOSIONS; i++) 
	{
	    if (!explosions[i].isActive()) 
	    {
		freeExplode=i;
		break;
	    }
	}
	
	if (freeExplode >= 0) 
	{
	    explosions[freeExplode].setState(1);
	    explosions[freeExplode].on();
	    explosions[freeExplode].setX(posx);
	    explosions[freeExplode].setY(posy);
	}
    }
    
    /**
     * checkCollisions()
     * 
     * performs bullet - player - enemy collision detection
     * if a collision is found an exposion is created
     */
    void checkCollisions() 
    {
	int i,j;
	
	if(player.isActive()) 
	{
	    //collision Player - Enemy
	    for (i=0; i<MAX_ENEMIES; i++) 
	    {
		if (player.collide(enemies[i]) && enemies[i].isActive() && shield == 0) 
		{
		    createExplosion(player.getX(),player.getY());
		    createExplosion(enemies[i].getX(),enemies[i].getY());
		    enemies[i].off();
		    player.off();
		    cicle=0;
		    collision=true;
		}
	    }
	    
	    //Collision Player - Bullet
	    for (i=0; i<MAX_BULLETS; i++) 
	    {
		if (bullets[i].isActive() && player.collide(bullets[i]) && bullets[i].getOwner() != PLAYER && shield == 0) 
		{
		    createExplosion(player.getX(),player.getY());
		    bullets[i].off();
		    player.off();
		    cicle=0;
		    collision=true;
		}
	    }
	}//END-IF player isActive
	
	//Collision Enemy - Bullet
	for (i=0; i<MAX_BULLETS; i++) 
	{
	    if (bullets[i].getOwner() == PLAYER && bullets[i].isActive()) 
	    {
		for (j=0; j<MAX_ENEMIES; j++) 
		{
		    if (enemies[j].isActive()) 
		    {
			if (bullets[i].collide(enemies[j])) 
			{
			    createExplosion(enemies[j].getX(),enemies[j].getY());
			    enemies[j].off();
			    bullets[i].off();
			    score+=10;
			}
		    }
		}
	    }
	}
	
	//we must wait 10 cicles before reseting player and showing getReady banner
	if(collision && (cicle > 10)) 
	{
	    collision = false;
	    player.on();
	    getReady.on();
	    lives--;
	    player.setX((getWidth()/2)-(player.getW()/2));
	    player.setY(getHeight()-player.getH());
	    
	    //During 30 cicles our ship will be inmune
	    shield=30;
	    if (lives <= 0) 
	    {
		gameOver.on();
		playing=false;
	    }
	}
	
	if (shield > 0)
	{
	    shield--;
	    if(cicle%3 == 0)
		getReady.selFrame(getReady.getFrame()==1?2:1);
	} 
	else
	    getReady.off();
    }
    
    /**
     * playerUpdate()
     *
     * updates player position
     */
    void playerUpdate() 
    {
	if(!player.isActive())
	    return;
	
	int x,y;
	
	x = player.getX()+deltaX;
	y = player.getY()+deltaY;
	
	if((x >= 0) && (x <= (getWidth()-player.getW())))
	    player.setX(x);
	
	if((y >= 0) && (y <= (getHeight()-player.getH())))
	    player.setY(y);
    }

    /**
     * run()
     *
     * main game loop occurs here
     */
    public void run() 
    {
	//initialize game data
	gameInit();

	while(playing)
	{
	    //scroll background
	    scroll();

	    //update player´s potision
	    playerUpdate();

	    //update enemies' position
	    enemyUpdate();

	    //update bullets' poition
	    bulletUpdate();

	    //update explosions' position
	    explosionUpdate();

	    //check for collisions
	    checkCollisions();

	    //update HUD
	    HUDUpdate();

	    //increment cicle
	    cicle++;

	    //update screen
	    repaint();
	    
	    try
	    {
		Thread.sleep(SLEEP_TIME);
	    } 
	    catch(InterruptedException ex)
	    {
		System.out.println(ex.toString());
	    }
	}
	
    }
    
    public void paint(Graphics g) 
    {
	//use lock/unlock methods to perform double buffering
	g.lock();
	
	int x,y,t,i,j;
	
	x = y = t = 0;
	
	g.setColor(Graphics.WHITE);
	g.fillRect(0,0,this.getWidth(),this.getHeight());
	g.setFont(Font.getFont(Font.FACE_SYSTEM|Font.STYLE_BOLD|Font.SIZE_MEDIUM));
	
	if(!playing)
	{
	    if(gameOver.isActive())
	    {
		gameOver.draw(g);
		g.drawString("Your score: " + score, getWidth()/2, 110);
	    } 
	    else
		intro.draw(g);
	} 
	else 
	{
	    //draw background
	    for(i=0; i<yTiles; i++) 
	    {
		for(j=0; j<xTiles; j++) 
		{
		    t = map[index+(i*xTiles+j)];
		    
		    //get position of tile
		    x = j*TILE_SIZE;
		    y = (i-1)*TILE_SIZE+index_in;
		    
		    tiles[t].setX(x);
		    tiles[t].setY(y);
		    tiles[t].draw(g);
		}
	    }
	    
	    //draw enemies
	    for(i=0; i<MAX_ENEMIES; i++) 
	    {
		if(enemies[i].isActive()) 
		{
		    enemies[i].setX(enemies[i].getX());
		    enemies[i].setY(enemies[i].getY());
		    enemies[i].draw(g);
		}
	    }
	    
	    
	    //draw player
	    if(player.isActive()) 
	    {
		player.setX(player.getX());
		player.setY(player.getY());
		player.draw(g);
	    }
	    
	    //draw bullets
	    for(i=0; i<MAX_BULLETS; i++) 
	    {
		if(bullets[i].isActive()) 
		{
		    bullets[i].setX(bullets[i].getX());
		    bullets[i].setY(bullets[i].getY());
		    bullets[i].draw(g);
		}
	    }
	    
	    //draw explosions
	    for(i=0; i<MAX_EXPLOSIONS; i++) 
	    {
		if(explosions[i].isActive())
		    explosions[i].draw(g);
	    }
	    
	    //draw HUD
	    scoreSprite.draw(g);
	    for(i=MAX_DIGITS-1, j=0; i>=0; i--, j++)
	    {
		digits[i].setX(scoreSprite.getW()+10+(digits[i].getW()*j));
		digits[i].setY(5);
		digits[i].draw(g);
	    }
	    
	    for(i=0; i<lives; i++)
		livesSprite[i].draw(g);
	    
	    //draw banner
	    if(getReady.isActive())
		getReady.draw(g);
	}//else
	
	g.unlock(true);
    }
  
    public void processEvent(int type, int param) 
    {
	if(!player.isActive())
	    return;
	
        if (type == Display.KEY_PRESSED_EVENT) 
	{
	    switch(param)
	    {
		//Right softkey
		case Display.KEY_SOFT2:
		    playing = false;
		    listener.exitGame();
		    break;
		    
		//Left softkey
		case Display.KEY_SOFT1:
		    playing = true;
		    listener.startGame();
		    break;
		    
		case Display.KEY_LEFT:
		    deltaX -= 5;
		    break;
		    
		case Display.KEY_RIGHT:
		    deltaX += 5;
		    break;
		    
		case Display.KEY_DOWN:
		    deltaY += 5;
		    break;
		    
		case Display.KEY_UP:
		    deltaY -= 5;
		    break;
		    
		case Display.KEY_SELECT:
		    fireOn = true;
		    break;
		    
		default:
		    break;
	    }//switch
        }//if
	else if (type == Display.KEY_RELEASED_EVENT) 
	{
	    switch(param)
	    {	    
		case Display.KEY_LEFT:
		    deltaX = 0;
		    break;
		    
		case Display.KEY_RIGHT:
		    deltaX = 0;
		    break;
		    
		case Display.KEY_DOWN:
		    deltaY = 0;
		    break;
		    
		case Display.KEY_UP:
		    deltaY = 0;
		    break;
		    
		case Display.KEY_SELECT:
		    fireOn = false;
		    break;
		    
		default:
		    break;
	    }//switch
        }//else
    }
}

