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
 * Specialized kind of space object. This is an abstract class meant to be extended but provides
 * some default behavior common across all weapons.
 *
 * <hr>
 * Date created: Mar 4, 2020
 * <hr>
 * @author William Kinser
 */
public abstract class Weapon extends SpaceObject
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

	
	/**
	 * called when this object collides with another one (passed in as a parameter)         
	 *
	 * <hr>
	 * Date created: Apr 13, 2020 
	 *
	 * <hr>
	 * @param obj
	 * @see SpacePirates.SpaceObject#simCollide(SpacePirates.SpaceObject)
	 */
	@Override
	public void simCollide(SpaceObject obj)
	{
		// if colliding with a station, the weapon is destroyed by the station's force field
		if (obj instanceof SpaceStation || obj instanceof WeighStation)
		{
			this.setHealth (0); // weapon hitting a station just disappear
			PirateScore.score -= 25; // negative score to hit a station, bad thing, bad bad bad
		}
		// if weapon hits a space treasure, destroy it and update score accordingly
		else if (obj instanceof SpaceTreasure)
		{
			// weapons hitting a space treasure destroys it and itself (without collecting any booty)
			SpaceTreasure treasure = (SpaceTreasure)obj;
			treasure.setHealth (0); // set health on treasure to cause it to be destroyed
			this.setHealth (0);     // set health on this object to cause it to be destroyed
			PirateScore.score += 25; 
		}
		// if weapon hits a large asteroid, destroy it and continue with default behavior
		else if (obj instanceof LargeAsteroid)
		{
			LargeAsteroid asteroid = (LargeAsteroid)obj;
			asteroid.setHealth(0); // force the asteroid to break up
			super.simCollide(obj);
			PirateScore.score += 100;
		}
		// if weapon hits a small asteroid, destroy it and continue with default behavior
		else if (obj instanceof SmallAsteroid)
		{
			SmallAsteroid asteroid = (SmallAsteroid)obj;
			asteroid.setHealth(0); // force the asteroid to break up
			super.simCollide(obj);
			PirateScore.score += 200;
		}
		// else do the default thing
		else
		{
			super.simCollide(obj);
		}
	}
} // end Weapon
