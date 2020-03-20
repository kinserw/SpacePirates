/**
 * ---------------------------------------------------------------------------
 * File name: SpaceTreasureType.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Mar 4, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;


/**
 * Defines the types of treasures the SpaceTreasure objects represent.
 * 
 *  * Note: enums are automatically serializable so this references to this class in 
 * the space objects will save to an io stream without issues.
 *
 * <hr>
 * Date created: Mar 4, 2020
 * <hr>
 * @author William Kinser
 */
public enum SpaceTreasureType
{
	STEEL,
	WATER,
	GOLD,
	URANIUM,
	TITANIUM,
	DARK_MATTER,
	ANTI_MATTER,
	SPACE_CREDITS
}
