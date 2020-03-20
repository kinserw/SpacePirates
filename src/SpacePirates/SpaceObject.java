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

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
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
	protected SpaceObjectType type = SpaceObjectType.STATIONARY;
	private BufferedImage icon = null;
	private static HashMap<String,BufferedImage> ourImages = new HashMap<String,BufferedImage>();
	private double rotation = 0;		// current angle in reference to rotational velocity
	private double rotationRate = 0.0;	// rotational velocity
	private double speed = 0;			// space object's velocity
	private double speedAng = 0;		// angle used for determining velocity vector
	private double mass = 1;			// mass for simulating force and collisions
	private SpaceObject origin = null;  // reference to object this one came from (if any)
	


	public SpaceObject(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		// TODO: load image based on the image name set by most derived class for this instance
		icon = fetchImage();
	}
	
	public SpaceObject(int x, int y, double m, double v)
	{
		this.x = x;
		this.y = y;
		this.mass = m;
		this.speed = v;
		
		// TODO: load image based on the image name set by most derived class for this instance
		icon = fetchImage();
	}
	
	/**
	 * if this object collides with something, it will process the effect the
	 * collision has on it based on the forceOfImpact provided. The ArrayList
	 * is a list that can be populated in this method with new SpaceObjects 
	 * created as a result of the collision (i.e. debris).
	 * Returns true if this object was effectively destroyed by the collision. Caller
	 * is responsible for deleting this object in that scenario.         
	 *
	 * <hr>
	 * Date created: Mar 18, 2020
	 *
	 * <hr>
	 * @param forceOfImpact
	 * @param componentParts
	 * @return true if destroyed by the collision
	 */
	public boolean collision(int forceOfImpact, ArrayList<SpaceObject> componentParts)
	{
		return false;
	}
	
	public void simCollide(SpaceObject obj)
	{
		double speed2 = obj.getSpeed ( );
		double speedAng2 = obj.getSpeedAng ( );
		double mass2 = obj.getMass ( );
		
		double deltaX = speed*Math.cos(speedAng);
		double deltaY = speed*Math.sin(speedAng);
		double deltaX2 = speed2*Math.cos(speedAng2);
		double deltaY2 = speed2*Math.sin(speedAng2);
		
		double newDX = 0;
		double newDY = 0;
		double newDX2 = 0;
		double newDY2 = 0;
		double refAngle = 0;
		double refAngle2 = 0;
		double addAng = 0;
		double addAng2 = 0;
		
		if ((deltaX == 0 && deltaY == 0))
		{
			newDX = (mass2 * deltaX2) / mass;
			newDY = (mass2 * deltaY2) / mass;
		}
		else if ((deltaX2 == 0 && deltaY2 == 0))
		{
			newDX2 = (mass * deltaX) / mass2;
			newDY2 = (mass * deltaY) / mass2;
		}
		else
		{
			newDX = (mass - mass2)/(mass + mass2)*deltaX + (mass2 * 2)/(mass + mass2)*deltaX2;
			newDY = (mass - mass2)/(mass + mass2)*deltaY + (mass2 * 2)/(mass + mass2)*deltaY2;
			newDX2 = (2 * mass)/(mass + mass2)*deltaX + (mass - mass2)/(mass + mass2)*deltaX2;
			newDY2 = (2 * mass)/(mass + mass2)*deltaY + (mass - mass2)/(mass + mass2)*deltaY2;
		
		}
		
		if (newDX >= 0 && newDY >= 0)
			;
		else if (newDX <= 0 && newDY >= 0)
			addAng = 90;
		else if (newDX <= 0 && newDY <= 0)
			addAng = 180;
		else
			addAng = 270;
		
		if (newDX2 >= 0 && newDY2 >= 0)
			;
		else if (newDX2 <= 0 && newDY2 >= 0)
			addAng2 = 90;
		else if (newDX2 <= 0 && newDY2 <= 0)
			addAng2 = 180;
		else
			addAng2 = 270;
		
		refAngle = Math.atan(Math.abs (newDY/newDX));
		refAngle2 = Math.atan(Math.abs (newDY2/newDX2));
		
		if (Double.isNaN(refAngle))
			refAngle = 0;
		if (Double.isNaN(refAngle2))
			refAngle2 = 0;
		
		speed = Math.sqrt (newDX * newDX + newDY * newDY);
		speedAng = refAngle + addAng;
		obj.setSpeed (Math.sqrt (newDX2 * newDX2 + newDY2 * newDY2));
		obj.setSpeedAng(refAngle2 + addAng2);

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
	public double getRotation ( )
	{
		return rotation;
	}

	
	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation (double rotation)
	{
		this.rotation = rotation;
	}
	
	
	/**
	 * @return rotationRate
	 */
	public double getRotationRate ( )
	{
		return rotationRate;
	}

	
	/**
	 * @param rotationRate the rotationRate to set
	 */
	public void setRotationRate (double rotationRate)
	{
		this.rotationRate = rotationRate;
	}

	
	/**
	 * @return speed
	 */
	public double getSpeed ( )
	{
		return speed;
	}

	
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed (double speed)
	{
		this.speed = speed;
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
	
	/**
	 * @return speedAng
	 */
	public double getSpeedAng ( )
	{
		return speedAng;
	}

	
	/**
	 * @param speedAng the speedAng to set
	 */
	public void setSpeedAng (double speedAng)
	{
		this.speedAng = speedAng;
	}

	
	/**
	 * @return mass
	 */
	public double getMass ( )
	{
		return mass;
	}

	
	/**
	 * @param mass the mass to set
	 */
	public void setMass (double mass)
	{
		this.mass = mass;
	}

	
	/**
	 * @return origin
	 */
	public SpaceObject getOrigin ( )
	{
		return origin;
	}

	
	/**
	 * @param origin the origin to set
	 */
	public void setOrigin (SpaceObject origin)
	{
		this.origin = origin;
	}

} // end SpaceObject
