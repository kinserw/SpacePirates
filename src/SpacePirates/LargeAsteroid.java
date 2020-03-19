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
	private ArrayList<SmallAsteroid> pieces = new ArrayList<SmallAsteroid>();
	
	public LargeAsteroid(int x, int y, int size)
	{
		super(x,y);
		for (int i = 0; i < size; i++)
			pieces.add (new SmallAsteroid(x,y));
		this.setRotationRate (5.0);
	}
	
	/**
	 * if this object collides with something, it will process the effect the
	 * collision has on it based on the forceOfImpact provided. The ArrayList
	 * is a list that can be populated in this method with new SpaceObjects 
	 * created as a result of the collision (i.e. debris).
	 * Returns true if this object was effectively destroyed by the collision. Caller
	 * is responsible for deleting this object in that scenario.         
	 *
	 * <hr>
	 * Date created: Mar 18, 2020
	 *
	 * <hr>
	 * @param forceOfImpact
	 * @param componentParts
	 * @return true if destroyed by the collision
	 */
	public boolean collision(int forceOfImpact, ArrayList<SpaceObject> componentParts)
	{
		int numberOfDestroyedSmallAsteroids = 0;
		
		// based on the force of the impact, this large asteroid will break
		// up into multiple component parts. Each is a new LargeAsteroid with
		// its own collection of SmallAsteroids. The sum of all SmallAsteroids
		// will be equal to or less than the number originally contained herein.
		// these will be returned in the array provided by the caller. 
		// If the result is that this asteroid has no small asteroids left in it, 
		// then it returns true and the caller should delete it.
		
		// some of the original SmallAsteroids contained herein may be destroyed
		// by the collision 
		
		return false;
	}
}
