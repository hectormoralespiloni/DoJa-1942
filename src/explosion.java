///============================================================================
///\file    explosion.java
///\brief   explosion class for 1942 demo
///\date    September 20, 2006
///\author  Héctor Morales Piloni
///============================================================================

import com.nttdocomo.ui.*;

class explosion extends sprite 
{
    private int state;
    
    public explosion(int nFrames) 
    {
	super(nFrames);
	state=1;
    }
    
    public void setState(int state) {
	this.state=state;
    }
    
    public int getState() {
	return state;
    }
    
    public void move() 
    {
	state++;
	if (state > super.frames())
	    super.off();
    }
    
    public void draw(Graphics g) 
    {
	selFrame(state);
	super.draw(g);
    }
}