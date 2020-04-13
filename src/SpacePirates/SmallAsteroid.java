/**
 * ---------------------------------------------------------------------------
 * File name: SmallAsteroid.java
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
 * This is a specialized type of space object
 *
 * <hr>
 * Date created: Mar 4, 2020
 * <hr>
 * @author William Kinser
 */
public class SmallAsteroid extends SpaceObject
{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7082185730627517184L;

	/**
	 * Constructor        
	 *
	 * <hr>
	 * Date created: Mar 4, 2020 
	 *
	 * 
	 * @param x
	 * @param y
	 */
	public SmallAsteroid (int x, int y)
	{
		super (x, y);
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
	 * @param m
	 * @param v
	 */
	public SmallAsteroid(int x, int y, double m, double v)
	{
		super(x,y,m,v);
		type = SpaceObjectType.DYNAMIC;
		this.setRotationRate (.5);
	}
	
	/**
	 * overrid base behavior to return a space treasure         
	 *
	 * <hr>
	 * Date created: Apr 12, 2020 
	 *
	 * <hr>
	 * @return
	 * @see SpacePirates.SpaceObject#getDebris()
	 */
	@Override
	public ArrayList <SpaceObject> getDebris()
	{
		ArrayList<SpaceObject> debris = super.getDebris ( );
		debris.add (addSpaceTreasure());
		return debris;
		
	}
	
	/**
	 * add a space treasure when this space object is destroyed         
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 * @return
	 */
	public SpaceTreasure addSpaceTreasure()
	{
		
		SpaceTreasure treasure = new SpaceTreasure(x,y);
		
		// use Math static random because it creates a single static instance of Random
		int prob = (int)(1000*Math.random ( ));
				
		if(prob<=100)					//anti-matter has a 10% chance of being the treasure type
		{
			treasure.setTreasureType (SpaceTreasureType.ANTI_MATTER);
		}
		else if(prob>100 && prob<=200)	//dark-matter has a 10% chance of being the treasure type
		{
			treasure.setTreasureType (SpaceTreasureType.DARK_MATTER);
		}
		else if(prob>200 && prob<=425) 	//titanium has a 22.5% chance of being the treasure type
		{
			treasure.setTreasureType (SpaceTreasureType.TITANIUM);
		}
		else if(prob>425 && prob<=700)	//Uranium has a 27.5% chance of being the treasure type
		{
			treasure.setTreasureType (SpaceTreasureType.URANIUM);
		}
		else							//Gold has a 30% chance of being the treasure type
		{
			treasure.setTreasureType (SpaceTreasureType.GOLD);
		}
		
		
		return treasure;
		
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
		if (obj instanceof SpaceStation || obj instanceof WeighStation)
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
} // end SmallAsteroid
