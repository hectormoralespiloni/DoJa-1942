///============================================================================
///\file    enemy.java
///\brief   enemy class for 1942 demo
///\date    September 20, 2006
///\author  Héctor Morales Piloni
///============================================================================

import com.nttdocomo.ui.*;
import java.util.*;

class enemy extends sprite 
{
    private int type;
    private int state;
    private int deltaX,deltaY;
    
    public enemy(int nFrames) {
	super(nFrames);
    }
    
    public void setState(int state) {
	this.state = state;
    }
    
    public void setType(int type) {
	this.type = type;
    }
    
    public int getState(int state) {
	return state;
    }
    
    public int getType() {
	return type;
    }
    
    public void move() 
    {
	Random random = new Random();
	
	//type II enemies will change their trayectory
	//whenever they reach position y=50!
	if ((type == 2) && (getY() > 100) && (state != 2)) 
	{
	    //diagonal movement
	    state = 2;
	    if (((Math.abs(random.nextInt()) % 2) + 1) == 1)
		deltaX = 2;
	    else
		deltaX =- 2;
	    
	    //Kamikaze!
	    deltaY *= 2;
	}
	
	// move the ship
	setX(getX()+deltaX);
	setY(getY()+deltaY);
    }
    
    public void init(int playerX) 
    {
	deltaY = 3;
	deltaX = 0;
	
	if (type == 1) 
	{
	    if (playerX > getX())
		deltaX = 2;
	    else
		deltaX =- 2;
	}
    }
    
    public void draw(Graphics g) 
    {
	selFrame(type);
	super.draw(g);
    }
}
