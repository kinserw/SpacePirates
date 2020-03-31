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
public class SmallAsteroid extends SpaceObject
{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7082185730627517184L;

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
	public SmallAsteroid (int x, int y)
	{
		super (x, y);
		// TODO Auto-generated constructor stub
	}
	
	public SmallAsteroid(int x, int y, double m, double v)
	{
		super(x,y,m,v);
		type = SpaceObjectType.DYNAMIC;
		this.setRotationRate (.5);
	}
}
