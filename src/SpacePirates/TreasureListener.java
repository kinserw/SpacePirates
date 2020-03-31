/**
 * ---------------------------------------------------------------------------
 * File name: TreasureListener.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Mar 31, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;


/**
 * Interface class used to notify other object of when a treasure was 
 * captured.
 *
 * <hr>
 * Date created: Mar 31, 2020
 * <hr>
 * @author William Kinser
 */
public interface TreasureListener
{

	public void treasureCaptured(SpaceTreasure treasure);
}
