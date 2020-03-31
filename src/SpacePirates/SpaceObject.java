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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * Abstract base class for all objects in space. Defines common behaviors and
 * attributes. Defined as Serializable, thus forcing all subclasses to be as 
 * well so they can be stored and retrieved from an iostream.
 *
 * <hr>
 * Date created: Mar 4, 2020
 * <hr>
 * @author William Kinser
 */
abstract public class SpaceObject implements Serializable
{
	/**
	 * needed for serialization 
	 */
	private static final long serialVersionUID = -1723319757299677962L;
	
	/*
	 * transient & static data below are attributes not saved when a game is saved.
	 * All member data is initialized at declaration to avoid undefined values
	 * when object is created from an iostream (i.e. when loaded from a file).
	 */
	private transient BufferedImage icon = null;
	private static HashMap<String,BufferedImage> ourImages = new HashMap<String,BufferedImage>();
	private transient SpaceObject origin = null;  // reference to object this one came from (if any)

	// x,y represent coordinates on the 2D plane
	private int x = 0;
	private int y = 0; 
	
	// represents the type of space objects. Subclasses should set this in their
	// constructors.
	protected SpaceObjectType type = SpaceObjectType.ELLIPTICAL;

	private double rotation = 0;		// current angle in reference to rotational velocity
	private double rotationRate = 0.0;	// rotational velocity
	private double speed = 0;			// space object's velocity
	private double speedAng = 0;		// angle used for determining velocity vector
	private double mass = 1;			// mass for simulating force and collisions
	private int health = 100;			// percentage of health object has
	private boolean inOrbit = false;	// is the object in orbit
	private SpaceObject lastOrb = null;	// the last object orbited
	


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
		return health<= 0;
	}

	public void calculateDamage(double speed1, double speed2)
	{
		// speed max's out at 50 so two object's speed combined is max 100
		// the faster they are going the more damage they do

		// CHARLES TODO: consider using angular speed since current calculation doesn't
		// take into consideration whether the two objects are doing a head on 
		// collision or if they are glancing off each other.
		health -= (int)(speed1+speed2);
		
	}
	
	public ArrayList<SpaceObject> getDebris()
	{
		return new ArrayList<SpaceObject>(); // default is no debris
	}

	public void orbit(SpaceObject obj)
	{
		pointAt(obj);
		speedAng -= Math.PI/2;
		rotation -= Math.PI/2;
		speed = 5;
		lastOrb = obj;
	}
	
	public void orbit()
	{
		if (lastOrb != null)
		{
			pointAt(lastOrb);
			speedAng -= Math.PI/2.2; //TODO 2.2 is a temporary fix, it should work as 2
			rotation -= Math.PI/2.2;
			speed = 5;
		}
	}
	
	public void pointAt(double x, double y)
	{
		double rotation = Math.atan2 (y - this.y, x - this.x);
		setSpeedAng (rotation);
		setRotation (rotation);
	}
	
	public void pointAt(SpaceObject obj)
	{
		double rotation = Math.atan2 (((obj.getY ( ) + obj.getImage ( ).getHeight ( ) / 2) - (this.y + getImage ( ).getHeight ( ) / 2)),
			 ((obj.getX() + obj.getImage ( ).getWidth( ) / 2) - (this.x + getImage ( ).getWidth ( ) / 2)));
		setSpeedAng (rotation);
		setRotation (rotation);
		
	}
	
	/**
	 * This method simulates the physics of two space objects colliding       
	 *
	 * <hr>
	 * Date created: Mar 22, 2020
	 *
	 * <hr>
	 * @param obj the object being collided with
	 */
	public void simCollide(SpaceObject obj)
	{
		double speed2 = obj.getSpeed ( );			// store the speed of the second object as a temporary variable
		double speedAng2 = obj.getSpeedAng ( );		// store the angle of the second object as a temporary variable
		double mass2 = obj.getMass ( );				// store the mass of the second object as a temporary variable
		
		// make this a virtual function so space objects that have force fields
		// can adjust the calculations but the rest use the same default calc.
		calculateDamage(speed,speed2);
		
		
		// separate the vector into individual components
		double deltaX = speed*Math.cos(speedAng);
		double deltaY = speed*Math.sin(speedAng);
		double deltaX2 = speed2*Math.cos(speedAng2);
		double deltaY2 = speed2*Math.sin(speedAng2);
		
		// store new values for x and y vectors of each object here
		double newDX = 0;
		double newDY = 0;
		double newDX2 = 0;
		double newDY2 = 0;
		
		// ref angles are based on quadrant of the vector 90, 180, 270
		double refAngle = 0;
		double refAngle2 = 0;
		
		// add angles are in addition to ref angles
		double addAng = 0;
		double addAng2 = 0;
		
		// use physics formulas to calculate new vectors
		// formulas differ based on whether or not objects are at rest
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
		
		// determine quadrant
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
		
		// apply new values to the objects
		refAngle = Math.atan(Math.abs (newDY/newDX));
		refAngle2 = Math.atan(Math.abs (newDY2/newDX2));
		
		if (Double.isNaN(refAngle))
			refAngle = 0;
		if (Double.isNaN(refAngle2))
			refAngle2 = 0;
		
		// only change speed and angle if not a stationary object
		
		if (type != SpaceObjectType.STATIONARY)
		{
			speed = Math.sqrt (newDX * newDX + newDY * newDY);
			speedAng = refAngle + addAng;
		}
		if (obj.getType ( )!= SpaceObjectType.STATIONARY)
		{
			obj.setSpeed (Math.sqrt (newDX2 * newDX2 + newDY2 * newDY2));
			obj.setSpeedAng(refAngle2 + addAng2);
		}
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

		// if class name prefixed with package name, remove the package name
		if (name.contains ("."))
		{
			name = name.substring (name.lastIndexOf (".")+1);
		}
		
		// see if we've already loaded this image. Since static, no need to 
		// load it more than once 
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
		// need this logic for when space objects are loaded from an io stream
		// and don't go through the defined constructors.
		if (icon == null)
		{
			icon = fetchImage();
		}
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

	
	/**
	 * @return health
	 */
	public int getHealth ( )
	{
		return health;
	}

	
	/**
	 * @param health the health to set
	 */
	public void setHealth (int health)
	{
		this.health = health;
	}

	
	/**
	 * @return inOrbit
	 */
	public boolean isInOrbit ( )
	{
		return inOrbit;
	}

	
	/**
	 * @param inOrbit the inOrbit to set
	 */
	public void setInOrbit (boolean inOrbit)
	{
		this.inOrbit = inOrbit;
	}

} // end SpaceObject
