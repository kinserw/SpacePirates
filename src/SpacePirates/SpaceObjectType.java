/**
 * ---------------------------------------------------------------------------
 * File name: SpaceObjectType.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Mar 4, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;


/**
 * Describes the nature of space objects mobility or the type of motion it will follow.
 * 
 * Note: enums are automatically serializable so this references to this class in 
 * the space objects will save to an io stream without issues.
 *
 * <hr>
 * Date created: Mar 4, 2020
 * <hr>
 * @author William Kinser
 */
public enum SpaceObjectType 
{
	STATIONARY,
	ELLIPTICAL,
	STEERABLE,
	DYNAMIC
}
