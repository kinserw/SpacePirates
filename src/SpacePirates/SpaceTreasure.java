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
	private static final long serialVersionUID = 6638950121102790576L;
	/**
	 * 
	 */
	private SpaceTreasureType type = SpaceTreasureType.STEEL;

	public static final int[] value = {
					1, // STEEL,
					3, // WATER,
					5, // GOLD,
					8, // URANIUM,
					9, // TITANIUM,
					15, // DARK_MATTER,
					15, // ANTI_MATTER,
					5 // SPACE_CREDITS0
					};

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
