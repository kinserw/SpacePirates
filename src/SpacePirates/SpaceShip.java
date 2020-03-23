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
 * Represents a triangular ship in space.
 *
 * <hr>
 * Date created: Mar 4, 2020
 * <hr>
 * @author William Kinser
 */
public class SpaceShip extends SpaceObject
{
	private SpaceShipWeaponType currentWeapon = SpaceShipWeaponType.MISSILE;
	private int weaponCount = 99;
	private boolean coasting = true;

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

	public void calculateDamage(double speed1, double speed2)
	{
		System.out.println("ship health = " + getHealth() + "  s1  s2 " + speed1 + "   " + speed2);

		super.calculateDamage (speed1, speed2);
	}
	
	public void simCollide(SpaceObject obj)
	{
		if (obj instanceof SpaceStation)
		{
			System.out.println("orbit");
			orbit(obj);
			setInOrbit(true);
		}
		else
		{
			setInOrbit(false);
			super.simCollide(obj);
		}
	}

	public void orbit(SpaceObject obj)
	{
		coasting = false;
		super.orbit (obj);
	}
	
	public void orbit()
	{
		coasting = false;
		super.orbit ();
	}
	
	/**
	 * @return currentWeapon
	 */
	public Weapon getCurrentWeapon ( )
	{
		Weapon weapon = null;
		if (weaponCount <= 0)
			return weapon;
		
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
		this.weaponCount--;
		
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
}
