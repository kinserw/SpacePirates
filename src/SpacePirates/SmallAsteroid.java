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
import java.util.Random;

/**
 * Enter type purpose here
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
		// TODO Auto-generated constructor stub
	}
	
	public SmallAsteroid(int x, int y, double m, double v)
	{
		super(x,y,m,v);
		type = SpaceObjectType.DYNAMIC;
		this.setRotationRate (.5);
	}
	
	public ArrayList <SpaceObject> getDebris()
	{
		ArrayList<SpaceObject> debris = super.getDebris ( );
		debris.add (addSpaceTreasure());
		return debris;
		
	}
	
	public SpaceTreasure addSpaceTreasure()
	{
		
		SpaceTreasure treasure = new SpaceTreasure(x,y);
		
		Random r = new Random();
		int prob = r.nextInt(1000);
		
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
			
			if(getSpeed() < 10)
				setSpeed(10);
		}
		else
		{
			super.simCollide (obj);
		}
	}
}
