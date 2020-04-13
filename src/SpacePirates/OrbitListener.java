/**
 * ---------------------------------------------------------------------------
 * File name: OrbitListener.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Mar 31, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;


/**
 * Interface to notify objects when the main space ship enters or leaves orbit
 *
 * <hr>
 * Date created: Mar 31, 2020
 * <hr>
 * @author William Kinser
 */
public interface OrbitListener
{
	/**
	 * interface method invoked when the orbit changes status         
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 * @param orbiting
	 */
	public void orbitChanged(boolean orbiting);

}
