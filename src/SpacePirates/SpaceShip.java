/**
 * ---------------------------------------------------------------------------
 * File name: SpaceShip.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Mar 4, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;


/**
 * Represents a triangular ship in space ( a specialized kind of space object).
 *
 * <hr>
 * Date created: Mar 4, 2020
 * <hr>
 * @author William Kinser
 */
public class SpaceShip extends SpaceObject
{
	// space ships can have a weapon (default is missiles)
	private SpaceShipWeaponType currentWeapon = SpaceShipWeaponType.MISSILE;
	private int weaponCount = 99; // track how much ammo the weapon has
	private boolean coasting = true; // track if the ship is coasting in space or not

	// keep handle on to listeners 
	private transient TreasureListener treasureListener = null; // called when treasure captured 
	private transient OrbitListener orbitListener = null; // called when orbit status changes

	private transient boolean breakingOrbit = false; // track whether ship is breaking orbit
	private transient boolean takeDamage = true; // tracks whether the ship can take damage

	/**
	 * 
	 */
	private static final long serialVersionUID = -8921014322626492689L;

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
	public SpaceShip (int x, int y)
	{
		super (x, y);
		this.setRotation (0.0);
		this.setType (SpaceObjectType.STEERABLE);
	}

	/**
	 * is called when this ship is part of a collision, to calculate damage to ship based 
	 * on the speed of the two objects         
	 *
	 * <hr>
	 * Date created: Mar 4, 2020 
	 *
	 * <hr>
	 * @param speed1
	 * @param speed2
	 * @see SpacePirates.SpaceObject#calculateDamage(double, double)
	 */
	@Override
	public void calculateDamage(double speed1, double speed2)
	{
		// debug statement. TODO remove debug
		System.out.println("ship health = " + getHealth() + "  s1  s2 " + speed1 + "   " + speed2);
		if(takeDamage==true)
			super.calculateDamage (speed1, speed2);
			
	}
	
	/**
	 * is called when this ship is part of a collision with the object passed in     
	 *
	 * <hr>
	 * Date created: Mar 4, 2020 
	 *
	 * <hr>
	 * @param speed1
	 * @param speed2
	 * @see SpacePirates.SpaceObject#calculateDamage(double, double)
	 */
	@Override
	public void simCollide(SpaceObject obj)
	{
		// if colliding with a station, then go into orbit (i.e. dock)
		if (obj instanceof SpaceStation || obj instanceof WeighStation)
		{
			if (!breakingOrbit)
			{
				System.out.println("orbit");
				orbit(obj);
				setInOrbit(true);
				takeDamage = false;
			}
		}
		// if colliding with a spaceTreasure, capture it as cargo
		else if (obj instanceof SpaceTreasure)
		{
			SpaceTreasure treasure = (SpaceTreasure)obj;
			notifyTreasureListener(treasure);
			setInOrbit(false); // can't be in orbit if capturing treasures
			treasure.setHealth (0); // set treasure health to zero, causing it to be destroyed
		}
		else // revert to default behavior
		{
			setInOrbit(false);
			super.simCollide(obj);
		}
	} // end simCollide
	
	/**
	 * Allow a listener to register for treasure related events         
	 *
	 * <hr>
	 * Date created: Apr 13, 2020
	 *
	 * <hr>
	 * @param tl
	 */
	public void addTreasureListener(TreasureListener tl)
	{
		this.treasureListener = tl;
	}
	
	/**
	 * notify listener (if any) of treasure event         
	 *
	 * <hr>
	 * Date created: Apr 13, 2020
	 *
	 * <hr>
	 * @param treasure
	 */
	private void notifyTreasureListener(SpaceTreasure treasure)
	{
		if (this.treasureListener != null)
			this.treasureListener.treasureCaptured (treasure);
	}

	/**
	 * @param inOrbit the inOrbit to set
	 */
	public void setInOrbit (boolean inOrbit)
	{
		super.setInOrbit (inOrbit);
		notifyOrbitListener(inOrbit);
	}

	/**
	 * Allow a listener to register for notification of a change in orbit status         
	 *
	 * <hr>
	 * Date created: Apr 13, 2020
	 *
	 * <hr>
	 * @param tl
	 */
	public void addOrbitListener(OrbitListener listener)
	{
		this.orbitListener = listener;
	}

	/**
	 * notify listener (if any) that there was a change in orbit status         
	 *
	 * <hr>
	 * Date created: Apr 13, 2020
	 *
	 * <hr>
	 * @param orbiting
	 */
	private void notifyOrbitListener(boolean orbiting)
	{
		if (this.orbitListener != null)
			this.orbitListener.orbitChanged (orbiting);
	}

	/**
	 * called to set the ship in orbit around the object passed in         
	 *
	 * <hr>
	 * Date created: Apr 13, 2020 
	 *
	 * <hr>
	 * @param obj
	 * @see SpacePirates.SpaceObject#orbit(SpacePirates.SpaceObject)
	 */
	@Override
	public void orbit(SpaceObject obj)
	{
		coasting = false; // can't continue coasting while in orbit
		super.orbit (obj);
	}
	
	/**
	 * called while in orbit in order to maintain the orbit        
	 *
	 * <hr>
	 * Date created: Apr 13, 2020 
	 *
	 * <hr>
	 * @see SpacePirates.SpaceObject#orbit()
	 */
	@Override
	public void orbit()
	{
		coasting = false;
		super.orbit ();
	}
	

	/**
	 * Returns the appropriate weapon if there is sufficient ammo to fire it. this method
	 * manages the ammo inventory as well.         
	 *
	 * <hr>
	 * Date created: Apr 13, 2020
	 *
	 * <hr>
	 * @return
	 */
	public Weapon fireCurrentWeapon ( )
	{
		Weapon weapon = null;
		
		// if no ammo remaining, return null
		if (this.getWeaponCount ( ) <= 0)
			return weapon;
		
		// based on the type of weapon on board, create a new instance for the caller
		switch (this.currentWeapon)
		{
			case CLUSTERBOMB :
				weapon = new ClusterBomb(getX(),getY());
				break;
			case TORPEDO :
				weapon = new Torpedo(getX(),getY());
				break;
			default : // MISSILE
				weapon = new Missile(getX(),getY());
					
		}
		this.weaponCount--; // decrement ammo count
		
		return weapon;
	}

	
	/**
	 * @param currentWeapon the currentWeapon to set
	 */
	public void setCurrentWeapon (SpaceShipWeaponType currentWeaponType)
	{
		this.currentWeapon = currentWeaponType;
	}

	
	/**
	 * @return weaponCount
	 */
	public int getWeaponCount ( )
	{
		return weaponCount;
	}

	
	/**
	 * @param weaponCount the weaponCount to set
	 */
	public void setWeaponCount (int weaponCount)
	{
		this.weaponCount = weaponCount;
	}

	
	
	/**
	 * @return breakingOrbit
	 */
	public boolean isBreakingOrbit ( )
	{
		return breakingOrbit;
	}

	
	/**
	 * @param breakingOrbit the breakingOrbit to set
	 */
	public void setBreakingOrbit (boolean breakingOrbit)
	{
		this.breakingOrbit = breakingOrbit;
		takeDamage = true;
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
	 * Getter for takeDamage        
	 *
	 * <hr>
	 * Date created: Apr 15, 2020
	 *
	 * <hr>
	 * @return
	 */
	public boolean getTakeDamage ( )
	{
		return takeDamage;
	}
} // end SpaceShip
