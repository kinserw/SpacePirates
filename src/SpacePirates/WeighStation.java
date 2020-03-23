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
		this.setRotationRate (0.2);
	}
	
	public void calculateDamage(double speed1, double speed2)
	{
		// override base class method to not take damage since stations have a force field
	}

}
