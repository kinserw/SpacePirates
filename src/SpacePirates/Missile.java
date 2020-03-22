/**
 * ---------------------------------------------------------------------------
 * File name: Missile.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Mar 15, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;

/**
 * Enter type purpose here
 *
 * <hr>
 * Date created: Mar 15, 2020
 * <hr>
 * @author William Kinser
 */
public class Missile extends Weapon
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 466225001077917721L;

	public Missile(int x, int y)
	{
		super (x, y);
		super.setSpeed(20);
		super.setType (SpaceObjectType.STEERABLE);
	}
	
}
