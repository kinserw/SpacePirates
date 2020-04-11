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
import java.util.Random;

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
		debrisField.add (addSpaceTreasure());
		return debrisField;
	}
	
	/**
	 * Adds space treasure to debris based on probability    
	 *
	 * <hr>
	 * Date created: Apr 11, 2020
	 *
	 * <hr>
	 * @return Space Treasure
	 */
	public SpaceTreasure addSpaceTreasure()
	{
		
		SpaceTreasure treasure = new SpaceTreasure(x,y);
		
		Random r = new Random();
		int prob = r.nextInt(1000);
		
		if(prob<=25)					//anti-matter has a 2.5% chance of being the treasure type
		{
			treasure.setTreasureType (SpaceTreasureType.ANTI_MATTER);
		}
		else if(prob>25 && prob<=50)	//dark-matter has a 2.5% chance of being the treasure type
		{
			treasure.setTreasureType (SpaceTreasureType.DARK_MATTER);
		}
		else if(prob>50 && prob<=100) 	//titanium has a 5% chance of being the treasure type
		{
			treasure.setTreasureType (SpaceTreasureType.TITANIUM);
		}
		else if(prob>100 && prob<=200)	//Uranium has a 10% chance of being the treasure type
		{
			treasure.setTreasureType (SpaceTreasureType.URANIUM);
		}
		else if(prob>200 && prob<=350)	//Gold has a 15% chance of being the treasure type
		{
			treasure.setTreasureType (SpaceTreasureType.GOLD);
		}
		else if(prob>350 && prob<=500) //SpaceCredits has a 15% chance of being the treasure type
		{
			treasure.setTreasureType (SpaceTreasureType.SPACE_CREDITS);
		}
		else if(prob>500 && prob<=700)	//Water has a 20% chance of being the treasure type
		{
			treasure.setTreasureType (SpaceTreasureType.WATER);
		}
		else							//Steel has a 30% chance of being the treasure type
			treasure.setTreasureType (SpaceTreasureType.STEEL);
		
		return treasure;	
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
