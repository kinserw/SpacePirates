/**
 * ---------------------------------------------------------------------------
 * File name: SpaceStation.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Mar 4, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;


/**
 * Specialized type of WeighStation that allows ships to dock, exchange cargo, do repairs, etc.
 *
 * <hr>
 * Date created: Mar 4, 2020
 * <hr>
 * @author William Kinser
 */
public class SpaceStation extends WeighStation
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5505197545698742171L;

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
	public SpaceStation (int x, int y)
	{
		super (x, y);
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
			//obj.pointAt(this);
			//obj.setSpeedAng(obj.getSpeedAng() + Math.PI);
			
			//if(obj.getSpeed() < 5)
				//obj.setSpeed(5);
			
			obj.anchorTo (this);
		}
		else
		{
			super.simCollide (obj);
		}
	}
} // end SpaceStation
