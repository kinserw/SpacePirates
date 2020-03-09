/**
 * ---------------------------------------------------------------------------
 * File name: SpaceObject.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Mar 4, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * Enter type purpose here
 *
 * <hr>
 * Date created: Mar 4, 2020
 * <hr>
 * @author William Kinser
 */
abstract public class SpaceObject
{
	private int x, y;
	private SpaceObjectType type = SpaceObjectType.STATIONARY;
	private BufferedImage icon = null;
	private static HashMap<String,BufferedImage> ourImages = new HashMap<String,BufferedImage>();
	private int rotation = 0;
	


	public SpaceObject(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		// TODO: load image based on the image name set by most derived class for this instance
		icon = fetchImage();
	}

	/**
	 * All image files are named based on the java class name they are associated with. So,
	 * this method will extract the most derived class' name and pass that back as the 
	 * image file name so that this base class will load the image for every object created.         
	 *
	 * <hr>
	 * Date created: Mar 4, 2020
	 *
	 * <hr>
	 * @return
	 */
	private BufferedImage fetchImage()
	{
		String name = this.getClass ( ).getName ( );
		if (name.contains ("."))
		{
			name = name.substring (name.lastIndexOf (".")+1);
		}
		BufferedImage i = ourImages.get (name);
		if (i == null)
		{
			// load the image and add it to the hashmap
			try
	        {
					i = ImageIO.read(new File(name+".gif"));
					ourImages.put(name,i);
			}
			catch (Exception e)
			{
			}
		}
		return i;
	}
	
	public BufferedImage getImage()
	{
		return this.icon;
	}
	/**
	 * @return x
	 */
	public int getX ( )
	{
		return x;
	}

	
	/**
	 * @param x the x to set
	 */
	public void setX (int x)
	{
		this.x = x;
	}

	
	/**
	 * @return y
	 */
	public int getY ( )
	{
		return y;
	}

	
	/**
	 * @param y the y to set
	 */
	public void setY (int y)
	{
		this.y = y;
	}

	
	/**
	 * @return rotation
	 */
	public int getRotation ( )
	{
		return rotation;
	}

	
	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation (int rotation)
	{
		this.rotation = rotation;
	}
	
	/**
	 * @return type
	 */
	public SpaceObjectType getType ( )
	{
		return type;
	}

	
	/**
	 * @param type the type to set
	 */
	public void setType (SpaceObjectType type)
	{
		this.type = type;
	}

} // end SpaceObject
