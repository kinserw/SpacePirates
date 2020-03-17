/**
 * ---------------------------------------------------------------------------
 * File name: SpaceTreasure.java
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
public class SpaceTreasure extends SpaceObject
{
	/**
	 * 
	 */
	private SpaceTreasureType type = SpaceTreasureType.STEEL;

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
	public SpaceTreasure (int x, int y)
	{
		super (x, y);
		this.setRotationRate (5.0);
	}


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
	public SpaceTreasure (int x, int y, SpaceTreasureType type)
	{
		super (x, y);
		this.setTreasureType (type);
	}
	
	/**
	 * @return type
	 */
	public SpaceTreasureType getTreasureType ( )
	{
		return type;
	}

	
	/**
	 * @param type the type to set
	 */
	public void setTreasureType (SpaceTreasureType type)
	{
		this.type = type;
	}

}
