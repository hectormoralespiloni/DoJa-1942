///============================================================================
///\file    bullet.java
///\brief   bullet class for 1942 demo
///\date    September 20, 2006
///\author  Héctor Morales Piloni
///============================================================================

import com.nttdocomo.ui.*;

class bullet extends sprite 
{
    private int owner;
    
    public bullet(int nFrames) {
	super(nFrames);
    }
    
    public void setOwner(int owner) {
	this.owner=owner;
    }
    
    public int getOwner() {
	return owner;
    }
    
    public void move() 
    {
	//player's bullet
	if (owner == 1)
	    setY(getY()-6);

	//enemy's bullet
	else
	    setY(getY()+6);
    }
    
    public void draw(Graphics g) 
    {
	selFrame(owner);
	super.draw(g);
    }
}
