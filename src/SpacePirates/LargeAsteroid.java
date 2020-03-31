/**
 * ---------------------------------------------------------------------------
 * File name: LargeAsteroid.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Mar 4, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;

import java.util.ArrayList;

/**
 * Enter type purpose here
 *
 * <hr>
 * Date created: Mar 4, 2020
 * <hr>
 * @author William Kinser
 */
public class LargeAsteroid extends SpaceObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3176116193088426885L;
	private int size = 0;
	
	public LargeAsteroid(int x, int y, int size)
	{
		super(x,y);
		type = SpaceObjectType.DYNAMIC;
		this.size = size;

		this.setRotationRate (5.0);
	}
	
	
	public LargeAsteroid(int x, int y, int size, double m, double v)
	{
		super(x,y,m,v);
		type = SpaceObjectType.DYNAMIC;
		this.size = size;
		this.setRotationRate (.5);
	}
	
	/**
	 *
	 * <hr>
	 * Date created: Mar 18, 2020
	 *
	 * <hr>
	 * @param speed1
	 * @param speed2
	 * @return true if destroyed by the collision
	 */
	public void calculateDamage(double speed1, double speed2)
	{
		super.calculateDamage (speed1, speed2);
		// based on the force of the impact, this large asteroid will break
		// up into multiple component parts. Each is a new LargeAsteroid with
		// its own collection of SmallAsteroids. The sum of all SmallAsteroids
		// will be equal to or less than the number originally contained herein.
		// these will be returned in the array provided by the caller. 
		// If the result is that this asteroid has no small asteroids left in it, 
		// then it returns true and the caller should delete it.
		
		// some of the original SmallAsteroids contained herein may be destroyed
		// by the collision 
		
	}
	
	public ArrayList<SpaceObject> getDebris()
	{
		ArrayList<SpaceObject> debrisField = super.getDebris ( );
		for (int i=0; i < size; i++)
		{
			SmallAsteroid debrisItem = new SmallAsteroid(getX ( ),getY(),getMass()/size,getSpeed());
			debrisItem.setSpeedAng (Math.random()*2*Math.PI);
			debrisItem.setSpeed (Math.random()*2*getSpeed());
			debrisField.add (debrisItem);
		}
		return debrisField;
	}
	

	
	public void setInOrbit (boolean inOrbit)
	{
		; // do nothing. asteroids can't be in orbit
	}

	public void orbit(SpaceObject obj)
	{
		; // do nothing. asteroids can't be in orbit
	}
	
	public void orbit()
	{
		; // do nothing. asteroids can't be in orbit
	}
	
}
