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
 * Specialized kind of space object, representing a large asteroid. When it is destroyed
 * it will break up into multiple small asteroids based on its size. 
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
	private int size = 0; // determines how many small asteroids are created when destroyed
	
	
	/**
	 * Constructor        
	 *
	 * <hr>
	 * Date created: Apr 12, 2020 
	 *
	 * 
	 * @param x
	 * @param y
	 * @param size
	 */
	public LargeAsteroid(int x, int y, int size)
	{
		super(x,y);
		type = SpaceObjectType.DYNAMIC;
		this.size = size;

		this.setRotationRate (5.0);
	}
	
	
	
	/**
	 * Constructor        
	 *
	 * <hr>
	 * Date created: Apr 12, 2020 
	 *
	 * 
	 * @param x
	 * @param y
	 * @param size  
	 * @param m		// mass
	 * @param v		// velocity
	 */
	public LargeAsteroid(int x, int y, int size, double m, double v)
	{
		super(x,y,m,v);
		type = SpaceObjectType.DYNAMIC;
		this.size = size;
		this.setRotationRate (.5);
	}
	

	
	/**
	 * Overrides base class method to create an array of small asteroids and space treasure
	 * when this large asteroid is destroyed         
	 *
	 * <hr>
	 * Date created: Apr 12, 2020 
	 *
	 * <hr>
	 * @return
	 * @see SpacePirates.SpaceObject#getDebris()
	 */
	@Override
	public ArrayList<SpaceObject> getDebris()
	{
		// get super's debris field and add to it
		ArrayList<SpaceObject> debrisField = super.getDebris ( );
		
		// loop to create size # of small asteroids
		for (int i=0; i < size; i++)
		{
			// create small asteroid with the same x,y as this and proportional mass
			SmallAsteroid debrisItem = new SmallAsteroid(getX ( ),getY(),getMass()/size,getSpeed()-10); //-10 decreases the speed slightly

			// set speed angle to be a random trajectory
			debrisItem.setSpeedAng (Math.random()*2*Math.PI);

			// set speed to be up to twice this one
			debrisItem.setSpeed (Math.random()*2*getSpeed());
			debrisField.add (debrisItem);
		}

		// add a space treasure as part of the debri field
		debrisField.add (addSpaceTreasure());
		return debrisField;
	} // end getDebris
	
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
		
		// use Math static random because it creates a single static instance of Random
		int prob = (int)(1000*Math.random ( ));
		
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
	} // end addSpaceTreasure
	
	
	
	/**
	 * Overrides base behavior to do nothing.        
	 *
	 * <hr>
	 * Date created: Apr 12, 2020 
	 *
	 * <hr>
	 * @param inOrbit
	 * @see SpacePirates.SpaceObject#setInOrbit(boolean)
	 */
	@Override
	public void setInOrbit (boolean inOrbit)
	{
		; // do nothing. asteroids can't be in orbit
	}

	/**
	 * Overrides base behavior to do nothing.        
	 *
	 * <hr>
	 * Date created: Apr 12, 2020 
	 *
	 * <hr>
	 * @param obj
	 * @see SpacePirates.SpaceObject#orbit(boolean)
	 */
	@Override
	public void orbit(SpaceObject obj)
	{
		; // do nothing. asteroids can't be in orbit
	}
	
	/**
	 * Overrides base behavior to do nothing.        
	 *
	 * <hr>
	 * Date created: Apr 12, 2020 
	 *
	 * <hr>
	 * @see SpacePirates.SpaceObject#orbit(boolean)
	 */
	public void orbit()
	{
		; // do nothing. asteroids can't be in orbit
	}
	
	/**
	 * Overrides simCollide to make asteroids get
	 * pushed away from WayStations and SpaceStations.      
	 *
	 * <hr>
	 * Date created: Apr 13, 2020 
	 *
	 * <hr>
	 * @param obj
	 * @see SpacePirates.SpaceObject#simCollide(SpacePirates.SpaceObject)
	 */
	public void simCollide(SpaceObject obj)
	{
		if (obj instanceof SpaceStation || obj instanceof WeighStation || (obj instanceof SpaceShip && ((SpaceShip) obj).getTakeDamage()==false))
		{
			pointAt(obj);
			setSpeedAng(getSpeedAng() + Math.PI);
			
			if(getSpeed() < 5)
				setSpeed(5);
		}
		else
		{
			super.simCollide (obj);
		}
	}
	
} // end LargeAsteroid
