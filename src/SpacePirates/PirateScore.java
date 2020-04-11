/**
 * ---------------------------------------------------------------------------
 * File name: PirateScore.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Apr 11, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;


/**
 * This class serves as a single, static location to model the current
 * score. By having a single location, independent of all other classes,
 * any area of the game logic that needs to adjust the score, can do so
 * without having to have scores passed through various classes or registering
 * listeners and events.
 *
 * <hr>
 * Date created: Apr 11, 2020
 * <hr>
 * @author William Kinser
 */
public class PirateScore
{
	public static int score = 0;
}
