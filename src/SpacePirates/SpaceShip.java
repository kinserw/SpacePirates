/**
 * ---------------------------------------------------------------------------
 * File name: SpaceShip.java
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
public class SpaceShip extends SpaceObject
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8921014322626492689L;

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
	public SpaceShip (int x, int y)
	{
		super (x, y);
		this.setRotation (0.0);
		this.setType (SpaceObjectType.STEERABLE);
	}

	public void calculateDamage(double speed1, double speed2)
	{
		System.out.println("ship health = " + getHealth() + "  s1  s2 " + speed1 + "   " + speed2);

		super.calculateDamage (speed1, speed2);
	}
}
