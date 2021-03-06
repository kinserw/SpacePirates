/**
 * ---------------------------------------------------------------------------
 * File name: WeighStation.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Mar 4, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;


/**
 * Enter type purpose here
 *
 * <hr>
 * Date created: Mar 4, 2020
 * <hr>
 * @author William Kinser
 */
public class WeighStation extends SpaceObject
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1862472330280340836L;

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
	public WeighStation (int x, int y)
	{
		super (x, y);
		this.setType (SpaceObjectType.STATIONARY);
		this.setRotationRate (0.1);
	}
	
	/**
	 * calculates damage as a result of a collision        
	 *
	 * <hr>
	 * Date created: Apr 13, 2020
	 *
	 * <hr>
	 * @param speed1
	 * @param speed2
	 */
	public void calculateDamage(int speed1, int speed2)
	{
		; // do nothing because I have a force field
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
		if (obj instanceof LargeAsteroid || obj instanceof SmallAsteroid)
		{
			obj.pointAt(this);
			obj.setSpeedAng(obj.getSpeedAng() + Math.PI);
			
			if(obj.getSpeed() < 5)
				obj.setSpeed(5);
		}
		else
		{
			super.simCollide (obj);
		}
	}
} // end WeighStation
