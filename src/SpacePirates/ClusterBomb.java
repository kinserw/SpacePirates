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
 * Specialized type of weapon
 *
 * <hr>
 * Date created: Mar 15, 2020
 * <hr>
 * @author William Kinser
 */
public class ClusterBomb extends Weapon
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7596162407236521197L;

	
	/**
	 * Constructor        
	 *
	 * <hr>
	 * Date created: Apr 21, 2020 
	 *
	 * 
	 * @param x
	 * @param y
	 */
	public ClusterBomb(int x, int y)
	{
		super (x, y);
		super.setSpeed(40);
		super.setType (SpaceObjectType.STEERABLE);
	}
	
}
