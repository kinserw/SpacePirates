/**
 * ---------------------------------------------------------------------------
 * File name: SpacePirateGame.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Mar 18, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Controls various aspects of the game such as difficulty of play, 
 * physics, etc.
 * 
 * NOTE: Currently this class is not used. It is here to refactor SpacePanel to remove 
 * game specific logic from the UI classes. This will make it helpful to port the game 
 * to different platforms in the future. For now, it doesn't get used.
 *
 * <hr>
 * Date created: Mar 18, 2020
 * <hr>
 * @author William Kinser
 */
public class SpacePirateGame implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7901517065602915834L;

	public static final SpacePirateGame theGame = new SpacePirateGame();

	private Difficulty difficultyLevel = Difficulty.EASY;
	private double zoomLevel = 1; 
	private int progress = 0;
	private ArrayList <SpaceObject>	objects	= new ArrayList <SpaceObject> ( );

	// keep a handle to the mainShip for quick reference. The mainShip is in the 
	// objects array list of SpaceObjects too.
	private SpaceShip 	mainShip	= null; 

	private boolean coasting = true;

	
					
	/**
	 * Constructor        
	 *
	 * <hr>
	 * Date created: Mar 18, 2020 
	 *
	 * 
	 */
	public SpacePirateGame ( )
	{
	}
	
    
	/**
	 * @return difficultyLevel
	 */
	public Difficulty getDifficultyLevel ( )
	{
		return difficultyLevel;
	}

	
	/**
	 * @param difficultyLevel the difficultyLevel to set
	 */
	public void setDifficultyLevel (Difficulty difficultyLevel)
	{
		this.difficultyLevel = difficultyLevel;
	}

	
	/**
	 * @return zoomLevel
	 */
	public double getZoomLevel ( )
	{
		return zoomLevel;
	}

	
	/**
	 * @param zoomLevel the zoomLevel to set
	 */
	public void setZoomLevel (double zoomLevel)
	{
		this.zoomLevel = zoomLevel;
	}

	
	/**
	 * @return progress
	 */
	public int getProgress ( )
	{
		return progress;
	}

	
	/**
	 * @param progress the progress to set
	 */
	public void setProgress (int progress)
	{
		this.progress = progress;
	}

	
	/**
	 * @return mainShip
	 */
	public SpaceShip getMainShip ( )
	{
		return mainShip;
	}

	
	/**
	 * @param mainShip the mainShip to set
	 */
	public void setMainShip (SpaceShip mainShip)
	{
		this.mainShip = mainShip;
	}

	
	/**
	 * @return coasting
	 */
	public boolean isCoasting ( )
	{
		return coasting;
	}

	
	/**
	 * @param coasting the coasting to set
	 */
	public void setCoasting (boolean coasting)
	{
		this.coasting = coasting;
	}

	
	/**
	 * @return objects
	 */
	public ArrayList <SpaceObject> getObjects ( )
	{
		return objects;
	}

	public String saveGame(ObjectOutputStream out) throws IOException
    {
    	// returns a string containing any error messages generated while
    	// saving the game
    	return null;
    }
    public String loadGame(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
    	// returns a string containing any error messages generated while
    	// saving the game
    	return null;
    }

} // end SpacePirateGame
