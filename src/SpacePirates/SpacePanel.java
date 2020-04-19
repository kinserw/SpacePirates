/**
 * ---------------------------------------------------------------------------
 * File name: SpacePanel.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Mar 4, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Responsible for the game display and player interactions in the graphical panel.
 * 
 *
 * <hr>
 * Date created: Mar 4, 2020
 * <hr>
 * @author William Kinser
 */
public class SpacePanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener
{

	private static final long		serialVersionUID	= -3004935837396357680L;

	// these two offsets are used for zooming in and out, as well as in orientation of the main ship
	private int						myRowOffset			= 0; 
	private int						myColOffset			= 0;

	// used to track how far the panel is zooming (1 = normal view)
	private double					zoomFactor			= 1;

	// list of all SpaceObjects active in the game 
	private ArrayList <SpaceObject>	objects				= new ArrayList <SpaceObject> ( );

	// keep a handle to the mainShip for quick reference. The mainShip is in the 
	// objects array list of SpaceObjects too.
	private static SpaceShip 				mainShip			= null; 

	// used to pause painting and motion
	private boolean 				gamePaused			= false; // flag indicating game is paused

	// used to determine how far away asteroids need to be from the player
	// for new ones to generate
	private final double			GEN_DIST			= 500;
	

	
	/**
	 * Constructor        
	 *
	 * <hr>
	 * Date created: Mar 4, 2020 
	 *
	 * 
	 */
	public SpacePanel ( )
	{
		// register as listener for all mouse events
		addMouseListener (this);		
		addMouseMotionListener (this);
		addMouseWheelListener (this);
	}


	/**
	 * sets the ship passed in as the main ship in the game         
	 *
	 * <hr>
	 * Date created: Mar 4, 2020
	 *
	 * <hr>
	 * @param ship
	 */
	public void addMainShip (SpaceShip ship)
	{
		// forget about the old ship if any existed
		objects.remove (mainShip);
		
		// remove any listeners attached to that ship
		if (mainShip != null)
		{
			mainShip.addTreasureListener (null);
			mainShip.addOrbitListener (null);
		}
		
		// remember this one passed in
		objects.add (0,ship);		// make sure space ship is the first item in collection
		mainShip = ship;
		
		// register as listener to key ship events
		if (mainShip != null)
		{
			mainShip.addTreasureListener (PirateFrame.ourFrame);
			mainShip.addOrbitListener (PirateFrame.ourFrame);
		}
	} // end addMainShip
	
	/**
	 * @return mainShip
	 */	
	public static SpaceShip mainShip()
	{
		return mainShip;
	}
	
	/**
	 * Add a space object to this panel's collection
	 *
	 * <hr>
	 * Date created: Mar 4, 2020
	 *
	 * <hr>
	 * @param obj
	 */
	public void add (SpaceObject obj)
	{
		objects.add (obj);
	}

	/**
	 * remove an object from this panel's collection         
	 *
	 * <hr>
	 * Date created: Mar 4, 2020
	 *
	 * <hr>
	 * @param obj
	 */
	public void remove (SpaceObject obj)
	{
		objects.remove (obj);
	}

	/**
	 * @return myRowOffset
	 */	
	public int getRowOffset ( )
	{
		// TODO Auto-generated method stub
		return myRowOffset;
	}

	/**
	 * @return myColOffset
	 */	
	public int getColOffset ( )
	{
		return myColOffset;
	}

	
	/**
	 * @return zoomFactor
	 */
	public double getZoomFactor ( )
	{
		return zoomFactor;
	}

	
	/**
	 * @param zoomFactor the zoomFactor to set
	 */
	public void setZoomFactor (double zoomFactor)
	{
		this.zoomFactor = zoomFactor;
	}

	/**
	 * use mousedragged to set direction of main ship         
	 *
	 * <hr>
	 * Date created: Apr 13, 2020 
	 *
	 * <hr>
	 * @param e
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged (MouseEvent e)
	{
			
		// include offsets in angle calculations since screen can be translated (shifted) but
		// the mouse event x & y are not translated
		// also need to adjust mouse event x & y based on zoomFactor to match adjustments already in the other values
		//double rotation = Math.atan2 ((e.getY ( )/zoomFactor- (mainShip.getY ( ) +myColOffset )),(e.getX()/zoomFactor - (mainShip.getX() + myRowOffset)));
		if (mainShip.isInOrbit() == false)
		{
			double rotation = Math.atan2 ((e.getY ( ) / zoomFactor + myColOffset - (mainShip.getY ( ) + mainShip.getImage ( ).getIconHeight () )),
										 (e.getX() / zoomFactor + myRowOffset - (mainShip.getX() + mainShip.getImage ( ).getIconWidth ()) ));
			mainShip.setSpeedAng (rotation);
			mainShip.setRotation (rotation);
		}
	}

	/**
	 * use mouseMoved to set direction of the main ship (same as with mouseDragged)       
	 *
	 * <hr>
	 * Date created: Mar 4, 2020 
	 *
	 * <hr>
	 * @param e
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved (MouseEvent e)
	{
		// include offsets in angle calculations since screen can be translated (shifted) but
		// the mouse event x & y are not translated
		// also need to adjust mouse event x & y based on zoomFactor to match adjustments already in the other values
		//double rotation = Math.atan2 ((e.getY ( )/zoomFactor- (mainShip.getY ( ) +myColOffset )),(e.getX()/zoomFactor - (mainShip.getX() + myRowOffset)));
		if (mainShip.isInOrbit() == false)
		{
			double rotation = Math.atan2 ((e.getY ( ) / zoomFactor + myColOffset - (mainShip.getY ( ) + mainShip.getImage ( ).getIconHeight () / 2)),
										 (e.getX() / zoomFactor + myRowOffset - (mainShip.getX() + mainShip.getImage ( ).getIconWidth () / 2)));
			mainShip.setSpeedAng (rotation);
			mainShip.setRotation (rotation);
		}
		
	}

	/**
	 *  fire weapon when mouse 2 is double clicked         
	 *
	 * <hr>
	 * Date created: Mar 4, 2020 
	 *
	 * <hr>
	 * @param e
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked (MouseEvent e)
	{
		// right mouse double click causes the player's ship to fire it's current
		// weapon. 
		if (e.getButton ( ) == MouseEvent.BUTTON3 && e.getClickCount ( ) == 1)
		{
			// can't shoot weapon while in orbit
			if (mainShip.isInOrbit() == false)
			{
				// if weapon available then fire it (it automatically update ammo count)
				Weapon weapon = mainShip.fireCurrentWeapon ( );
				
				if (weapon != null) 
				{
					// weapon starts at center of the main ship
					weapon.setX(mainShip.getX ( ) + 
						        mainShip.getImage ( ).getIconWidth ()/2 -
						        weapon.getImage ( ).getIconWidth ( )/2);
					weapon.setY (mainShip.getY ( ) + 
				        		mainShip.getImage ( ).getIconHeight( )/2 -
				        		weapon.getImage ( ).getIconHeight( )/2);
					// shoots in the direction the ship is facing
					weapon.setSpeedAng (mainShip.getSpeedAng ( ));
					weapon.setRotation (mainShip.getRotation ( ));
					
					weapon.setOrigin (mainShip); // missile knows where it came from
					
					// add weapon to list of objects in the game (right after the main ship)
					this.objects.add (1,weapon);
				} // need a weapon to fire
			} // can't fire if in orbit 
		}// fire weapon
	}

	/**
	 * Accelerate main ship when mouse 1 is clicked         
	 *
	 * <hr>
	 * Date created: Mar 4, 2020 
	 *
	 * <hr>
	 * @param e
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed (MouseEvent e)
	{
		if (e.getButton ( ) == MouseEvent.BUTTON1)
		{
			// accelerate ship if it isn't at max speed yet
			if (mainShip.getSpeed() < 50)
				mainShip.setSpeed (mainShip.getSpeed() + 10);
			
			// set coasting to false while accelerating
			mainShip.setCoasting (false);
			
			// if ship is in orbit, accelerating will break free of the orbit
			if (mainShip.isInOrbit ( ))
				mainShip.setBreakingOrbit (true);
			mainShip.setInOrbit (false);
		}

	} // end mousePressed

	/**
	 * releasing mouse means main ship is no longer accelerating.
	 * main ship starts coasting when no longer accelerating         
	 *
	 * <hr>
	 * Date created: Mar 4, 2020 
	 *
	 * <hr>
	 * @param e
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased (MouseEvent e)
	{
		if (e.getButton ( ) == MouseEvent.BUTTON1)
		{
			mainShip.setBreakingOrbit (false); // set to false so it can go back into orbit
			mainShip.setCoasting (true);			
		}
	}

	/**
	 * not used but has to be here for listener         
	 *
	 * <hr>
	 * Date created: Mar 4, 2020 
	 *
	 * <hr>
	 * @param e
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered (MouseEvent e)
	{
		// not used but has to be here for listener

	}

	/**
	 * 	not used but has to be here for listener
     *
	 * <hr>
	 * Date created: Mar 4, 2020 
	 *
	 * <hr>
	 * @param e
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited (MouseEvent e)
	{
		// not used but has to be here for listener

	}

	/**
	 * iterates through all the objects in space and tells them to move
	 * also generates more objects depending on relative player location          
	 *
	 * <hr>
	 * Date created: Mar 4, 2020
	 *
	 * <hr>
	 */
	public void moveObjects()
	{
		// boolean flag for if new asteroids should be generated
		boolean generateFlag = true;
		
		// the distance from the closest station
		Double minStationDist = null;
		
		// iterate through all active objects in space (and in the game)
		for (SpaceObject object : this.objects)
		{
			// if the object isn't stationary, calculate its movement
			if (object.getType ( ) != SpaceObjectType.STATIONARY)
			{
				// calculate new position and set
				double speed = object.getSpeed ( );
				double deltaX = speed*Math.cos(object.getSpeedAng ( ));
				double deltaY = speed*Math.sin(object.getSpeedAng ( ));
				object.setX (object.getX() + (int)((deltaX)));
				object.setY (object.getY() + (int)((deltaY)));
				
				// if this is the main ship, override movement when in orbit
				if ((object == mainShip) && mainShip.isInOrbit() == true)
					object.orbit();
				// if this is the main ship and is coasting, reduce speed a little
				else if ((object == mainShip) && mainShip.isCoasting())
					mainShip.setSpeed ((speed <= 0 ? 0 : speed - 1));
				
				// check to see if there are asteroids near the player
				// if there aren't any, generate new ones out of view
				if (object instanceof LargeAsteroid && generateFlag == true)
				{
					// Distance formula using the coordinates of the asteroid and player
					if (Math.sqrt (
						Math.pow((object.getX() - mainShip.getX ( )), 2) +
						Math.pow((object.getY() - mainShip.getY ( )), 2)
						) < GEN_DIST / zoomFactor)
					{
						generateFlag = false;
					}
				}
				
				
			} // end if
			
			// update distance from the nearest weighstation
			if (object instanceof WeighStation && minStationDist != null)
			{
				// Distance formula using the coordinates of the station and player
				if (Math.sqrt (
					Math.pow((object.getX() - mainShip.getX ( )), 2) +
					Math.pow((object.getY() - mainShip.getY ( )), 2)
					) < minStationDist)
				{
					minStationDist = (Math.sqrt (
						Math.pow((object.getX() - mainShip.getX ( )), 2) +
						Math.pow((object.getY() - mainShip.getY ( )), 2)));
				}
			}
			else if (object instanceof WeighStation)
			{
				minStationDist = (Math.sqrt (
					Math.pow((object.getX() - mainShip.getX ( )), 2) +
					Math.pow((object.getY() - mainShip.getY ( )), 2)));
			}
			
		} // end iterate
		
		if (generateFlag == true)
		{
			System.out.println("generated asteroids");
			generateAsteroids((int)(Math.random ( ) * 5),
							mainShip.getX ( ),
							mainShip.getY ( ),
							GEN_DIST / zoomFactor,
							(GEN_DIST / 2) / zoomFactor);
		}
		
		if (minStationDist != null && minStationDist > (GEN_DIST * 5) / zoomFactor)
		{
			System.out.println("generated station");
			generateStation(mainShip.getX ( ),
				mainShip.getY ( ),
				GEN_DIST / zoomFactor,
				(GEN_DIST / 2) / zoomFactor);
		}
	} // end moveObjects
	
	
	/**
	 * Generates n number of asteroids at random points about
	 * x, y based on the set of distances passed in.       
	 *
	 * <hr>
	 * Date created: Apr 13, 2020
	 *
	 * <hr>
	 * @param numObj	:the number of asteroids to be generated
	 * @param x			:the x position the generation will be based around
	 * @param y			:the y position the generation will be based around
	 * @param maxDist	:the maximum distance asteroids can be from point x, y
	 * @param minDist	:the minimum distance asteroids can be from point x, y
	 */
	public void generateAsteroids(int numObj, int x, int y, double maxDist, double minDist)
	{
		LargeAsteroid tempAst = null;
		int newX;
		int newY;
		int size;
		double mass;
		double speed;
		
		for(int i = 0; i < numObj; i++)
		{
			// sets new x and y to their distances from the original point x, y
			newX = (int) (minDist + ((maxDist - minDist) * Math.random()));
			newY = (int) (minDist + ((maxDist - minDist) * Math.random()));
			
			// puts the x and y in a random quadrant
			// also applies the original coordinates
			if (Math.random ( ) < .5)
				newX = newX * -1 + x;
			else
				newX += x;
			if (Math.random ( ) < .5)
				newY = newY * -1 + y;
			else
				newY += y;
			
			size = 5; //(int) Math.random() * 10;
			mass = Math.random() * 10;
			speed = Math.random() * 10;
			
			tempAst = new LargeAsteroid(newX, newY, size, mass, speed);
			tempAst.setRotationRate (Math.random());
			tempAst.setSpeedAng (Math.random ( ) * 2 * Math.PI);
			
			objects.add (tempAst);
		}
	}
	
	
	/**
	 * Generates a station at a random position based on given parameters      
	 *
	 * <hr>
	 * Date created: Apr 13, 2020
	 *
	 * <hr>
	 * @param x			:the x position the generation will be based around
	 * @param y			:the y position the generation will be based around
	 * @param maxDist	:the maximum distance the station can be from point x, y
	 * @param minDist	:the minimum distance the station can be from point x, y
	 */
	public void generateStation(int x, int y, double maxDist, double minDist)
	{
		WeighStation tempAst = null;
		int newX;
		int newY;
		
			// sets new x and y to their distances from the original point x, y
			newX = (int) (minDist + ((maxDist - minDist) * Math.random()));
			newY = (int) (minDist + ((maxDist - minDist) * Math.random()));
			
			// puts the x and y in a random quadrant
			// also applies the original coordinates
			if (Math.random ( ) < .5)
				newX = newX * -1 + x;
			else
				newX += x;
			if (Math.random ( ) < .5)
				newY = newY * -1 + y;
			else
				newY += y;
			
			tempAst = new WeighStation(newX, newY);
			
			objects.add (tempAst);
	}
	
	/**
	 * This method draws all the objects in space, and handles their interactions with each other         
	 *
	 * <hr>
	 * Date created: Mar 4, 2020 
	 *
	 * <hr>
	 * @param g
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent (Graphics g)
	{
		// if game is paused, nothing to do
		if (gamePaused)
			return;
		
		Graphics2D g2 = (Graphics2D) g;
		
		// scales the window based on mouse scroll (i.e. does the zoom in and out)
		AffineTransform at = new AffineTransform();
		at.scale(zoomFactor, zoomFactor);
		g2.transform(at);
		
		// determine if ship is nearing the edges of the screen and
		// shift the coordinate system accordingly so that the ship 
		// does not leave the screen but can keep moving
		double width = getWidth()/zoomFactor;
		double height = getHeight()/zoomFactor;

		// if main ship exists then center screen on it (and set it's image based on rotation)
		if (mainShip != null)
		{
			// keep ship in center of screen 
			
			myRowOffset = mainShip.getX ( ) - ((int) width / 2 - mainShip.getImage ( ).getIconWidth () / 2);
			myColOffset = mainShip.getY ( ) - ((int) height / 2 - mainShip.getImage ( ).getIconHeight () / 2);
		
			g2.translate (-myRowOffset, -myColOffset);
		} 
					
		// build a temporary list of rectangles for each space object. 
		// their rectangle can change if they are moving so we do this each 
		// time paint is called (could do it in SpacePanel:moveObjects )
		ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
		for (SpaceObject obj : this.objects)
		{
			ImageIcon imageIcon = obj.getImage ( );
			Image image = imageIcon.getImage ( );
			Graphics2D g22 = (Graphics2D)g.create ( );
			
			// rotate each object separately based on its rotation
			g22.rotate(obj.getRotation ( ),(int)(obj.getX())+obj.getImage ( ).getIconWidth()/2,
								(int)(obj.getY())+obj.getImage ( ).getIconHeight ()/2);
			// draw rotated image
			g22.drawImage (image, (int)(obj.getX()), (int)(obj.getY()), null);
			
			// set next rotation
			obj.setRotation (obj.getRotation ( )+obj.getRotationRate());
			
			// add a rectangle that outlines this image (used in collision calculations later)
			rects.add (new Rectangle(obj.getX ( ),obj.getY ( ), obj.getImage ( ).getIconWidth(),obj.getImage ( ).getIconHeight ()));

			// get rid of custom graphics obj used for individual rotation
			g22.dispose ( );
		}

		// look for collisions (use loops since objects can be destroyed by collisions
		// and have to be removed from the collections of rects and objects
		
		// outer loop iterates through all rectangles
		
		for (int r1 =0; r1 < rects.size ( ); r1++)
		{
			Rectangle rect1 = rects.get (r1);
			
			// inner loop iterates through all rectangles after r1 in list
			for (int r2 = r1+1; r2 < rects.size ( ); r2++)
			{
				Rectangle rect2 = rects.get (r2);
				
				// if the rectangles intersect then the objects are potentially colliding
				if (rect1.intersects (rect2))
				{
					// get the objects for each rectangle so we can interrogate them further
					SpaceObject o1 = objects.get (r1);
					SpaceObject o2 = objects.get (r2);
										
					// make sure weapons aren't "colliding" with whatever fired them
					if ((o1.getOrigin ( ) == o2) || (o2.getOrigin ( ) == o1))
						;// as Trump says... "No Collision"
					
					else if((o1 instanceof SmallAsteroid || o1 instanceof SmallAsteroid || o1 instanceof SpaceTreasure) 
									&& (o2 instanceof SmallAsteroid || o2 instanceof SmallAsteroid || o2 instanceof SpaceTreasure) )
						;//keeps small asteroids from destroying each other
					else
					{
						// make the first object collide with the second
						o1.simCollide (o2);
						
						// see if either objects were destroyed by the collision
						boolean o1Destroyed = o1.collision (10, objects);
						boolean o2Destroyed = o2.collision (10, objects);
						
						// remove 2nd object first just in case first is also destroyed
						// since destroying the first affects the indexing of rects and objects
						if (o2Destroyed)
						{
							// if the object created debris when destroyed, add these to list of space objects
							objects.addAll (o2.getDebris()); 
							
							// remove 2nd object from both lists (objects and rectangles)
							objects.remove (o2);
							rects.remove (rect2);
							// increments asteroids hit in pirate frame
							if(o2 instanceof SmallAsteroid || o2 instanceof LargeAsteroid && o1 instanceof Weapon)
								PirateFrame.ourFrame.setAsteroidsHit (PirateFrame.ourFrame.getAsteroidsHit ( ) + 1);
							r2--; // decrement r2 since we removed it, index will then advance properly
							// don't need to modify r1 since its the outer loop and hasn't hit the r2
							// index yet
						} // end destroy second object
						
						// now remove the first object if it was destroyed
						if (o1Destroyed)
						{
							// if the object created debris when destroyed, add these to list of space objects
							objects.addAll (o1.getDebris());
							
							// remove object 1 from both lists (objects and rectangles)
							objects.remove (o1);
							rects.remove(rect1);
							//increments asteroids hit in pirateframe
							if(o1 instanceof SmallAsteroid || o1 instanceof LargeAsteroid && o2 instanceof Weapon)
								PirateFrame.ourFrame.setAsteroidsHit (PirateFrame.ourFrame.getAsteroidsHit ( ) + 1);
							r1--; // decrement r1 since we removed it, index will then advance properly
							r2--; // decrement r2 since r1 by definition is earlier in the list 
							break; // break out of inner loop since our r1 is gone and we need to find a new one
						}
					} // end else (weapon not colliding with origin)
					
				} // end if r1 and r2 intersect
			} // end inner loop for r2
		} // end outer loop for r1


	} // end paint component

	/**
	 * mouse wheel controls zooming of the game screen        
	 *
	 * <hr>
	 * Date created: Mar 4, 2020 
	 *
	 * <hr>
	 * @param e
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	@Override
	public void mouseWheelMoved (MouseWheelEvent e)
	{
		// Zoom in
		if (e.getWheelRotation ( ) < 0)
		{
			zoomFactor *= 1.1;
		}
		// Zoom out
		else if (e.getWheelRotation ( ) > 0)
		{
			zoomFactor /= 1.1;
		}
	}

	/**
	 * this method is called to set up game values for the start of a new game 
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 */
	public void startGame()
	{
		// no set up needed at this point, but this is here in case we need it later
	}
	
	/**
	 * this method is called to reset game values to end the game but not the program
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 */
	public void endGame()
	{
		objects.clear ( );
	}
	
	/**
	 * @param paused
	 */
	public void setGamePaused(boolean paused)
	{
		gamePaused = paused;
	}
	
	/**
	 * Save all game specific values in the panel to the provided output stream         
	 *
	 * <hr>
	 * Date created: Mar 4, 2020
	 *
	 * <hr>
	 * @param out
	 * @return
	 */
	public String saveGame(ObjectOutputStream out) 
    {
    	// returns a string containing any error messages generated while
    	// saving the game
		String errors = null;

		try
		{
			out.writeDouble (zoomFactor);
			out.writeInt (this.myColOffset);
			out.writeInt (this.myRowOffset);
			
			// save index in objects that corresponds to mainShip
			// so that we can identify which one it is when we load the game
			int shipIndex = objects.indexOf (mainShip);
			out.writeInt (shipIndex);

			// save count of how many space objects we're saving
			// so we know how many to expect when we load the game
			out.writeInt (this.objects.size ( ));

			// save all space objects active at this time
			// NOTE: the origin of a space object can't be saved with 
			// the object so we'll process this after the list is done
			// but get the count now.
			int originCount = 0;
			for (SpaceObject obj : this.objects)
			{
				out.writeObject (obj);
				if (obj.getOrigin ( ) != null)
					originCount++;
			}
			
			// loop through a second time to handle any space objects
			// with an origin that isn't null
			out.writeInt (originCount);
			for (SpaceObject obj : this.objects)
			{
				if (obj.getOrigin ( ) != null)
				{
					out.writeInt(objects.indexOf (obj));
					out.writeInt (objects.indexOf (obj.getOrigin ( )));
				}
			}
		}
		catch (IOException e)
		{
			errors = new String("Errors while saving panel data.");
							
		}

    	return errors;
    } // end saveGame
	
	/**
	 * Save all game specific values into the panel from the provided input stream         
	 *
	 * <hr>
	 * Date created: Mar 4, 2020
	 *
	 * <hr>
	 * @param out
	 * @return
	 */
    public String loadGame(ObjectInputStream in) 
    {
    	// returns a string containing any error messages generated while
    	String errors = null;
    	
		try
		{
	    	// load screen layout info
	    	this.zoomFactor = in.readDouble ();
	    	this.myColOffset = in.readInt ();
	    	this.myRowOffset = in.readInt ();
			
			// load index in objects that corresponds to mainShip
			int shipIndex = in.readInt (); // use this after all objects loaded
	
			// get count of how many space objects we're loading
			int objCount = in.readInt ();
	
			// load all space objects saved
			// NOTE: the origin of a space object can't be saved with 
			// the object so we'll process this after the list is done
			this.objects.clear ( ); // remove any current objects
			for (int i = 0; i < objCount; i++)
			{
				SpaceObject obj = (SpaceObject)in.readObject ();
				this.objects.add (obj);
				
				// make sure the main indicated is really the main ship
				if (i == shipIndex)
				{
					if (obj.getClass ( ).equals (SpaceShip.class))
						addMainShip((SpaceShip)obj);
					else
						errors = new String("Can't find main space ship");
				}
			} // load all the space objects
			
			// loop through a second time to handle any space objects
			// with an origin that isn't null
			int originCount;
				originCount = in.readInt ();
			for (int i = 0; i < originCount; i++)
			{
				int indexObj = in.readInt ( );
				int indexOrigin = in.readInt ( );
				this.objects.get (indexObj).setOrigin (this.objects.get (indexOrigin));
			}
		}
		catch (ClassNotFoundException cnfe )
		{
			if (errors == null)
				errors = new String("");
			errors += " Error when reading in panel objects. Class not found. ";
			
		}
		catch (IOException e)
		{
			if (errors == null)
				errors = new String("");
			errors += " Error when reading in panel objects.";
		}
    	return errors;
    } // end loadGame

} // end SpacePanel
