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
 * Enter type purpose here
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

	public void calculateDamage(int speed1, int speed2)
	{
		; // do nothing because I have a force field
	}
}
