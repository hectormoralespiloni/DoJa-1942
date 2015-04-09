///============================================================================
///\file    player.java
///\brief   player class for 1942 demo
///\date    September 20, 2006
///\author  Héctor Morales Piloni
///============================================================================

class player extends sprite 
{
    private int state;
    
    public player(int nFrames) {
	super(nFrames);
    }
    
    public void setState(int state) {
	this.state=state;
    }
    
    public int getState(int state) {
	return state;
    }
}