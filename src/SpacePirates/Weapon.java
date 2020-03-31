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


/**
 * Enter type purpose here
 *
 * <hr>
 * Date created: Mar 4, 2020
 * <hr>
 * @author William Kinser
 */
public class Weapon extends SpaceObject
{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6953553329561591850L;

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
	public Weapon (int x, int y)
	{
		super (x, y);
		super.setHealth (0); // causes weapons to be destroyed on collision
	}

	
	public void simCollide(SpaceObject obj)
	{
		if (obj instanceof SpaceStation || obj instanceof WeighStation)
		{
			this.setHealth (0); // missiles hitting a station just disappear
		}
		else if (obj instanceof SpaceTreasure)
		{
			// weapons hitting a space treasure destroys it and itself (without collecting any booty)
			SpaceTreasure treasure = (SpaceTreasure)obj;
			treasure.setHealth (0);
			this.setHealth (0);
		}
		else if (obj instanceof LargeAsteroid)
		{
			LargeAsteroid asteroid = (LargeAsteroid)obj;
			asteroid.setHealth(0); // force the asteroid to break up
			super.simCollide(obj);
		}
		else
		{
			super.simCollide(obj);
		}
	}
}
