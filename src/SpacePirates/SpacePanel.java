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
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
	private SpaceShip 				mainShip			= null; 

	// used to pause painting and motion
	private boolean 				gamePaused			= false; // flag indicating game is paused

	
	
	

	
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
		this.mainShip = ship;
		
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
	public SpaceShip mainShip()
	{
		return this.mainShip;
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
			double rotation = Math.atan2 ((e.getY ( ) / zoomFactor + myColOffset - (mainShip.getY ( ) + mainShip.getImage ( ).getHeight ( ) / 2)),
										 (e.getX() / zoomFactor + myRowOffset - (mainShip.getX() + mainShip.getImage ( ).getWidth ( ) / 2)));
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
			double rotation = Math.atan2 ((e.getY ( ) / zoomFactor + myColOffset - (mainShip.getY ( ) + mainShip.getImage ( ).getHeight ( ) / 2)),
										 (e.getX() / zoomFactor + myRowOffset - (mainShip.getX() + mainShip.getImage ( ).getWidth ( ) / 2)));
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
		if (e.getButton ( ) == MouseEvent.BUTTON3 && e.getClickCount ( ) == 2)
		{
			// can't shoot weapon while in orbit
			if (mainShip.isInOrbit() == false)
			{
				// if weapon available then fire it (it automatically update ammo count)
				Weapon weapon = this.mainShip.fireCurrentWeapon ( );
				
				if (weapon != null) 
				{
					// weapon starts at center of the main ship
					weapon.setX(this.mainShip.getX ( ) + 
						        this.mainShip.getImage ( ).getWidth ( )/2 -
						        weapon.getImage ( ).getWidth ( )/2);
					weapon.setY (this.mainShip.getY ( ) + 
				        		this.mainShip.getImage ( ).getHeight( )/2 -
				        		weapon.getImage ( ).getHeight( )/2);
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
	 *
	 * <hr>
	 * Date created: Mar 4, 2020
	 *
	 * <hr>
	 */
	public void moveObjects()
	{
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
					this.mainShip.setSpeed ((speed <= 0 ? 0 : speed - 1));
			} // end if
		} // end iterate
	} // end moveObjects
	
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
			
			myRowOffset = mainShip.getX ( ) - ((int) width / 2 - mainShip.getImage ( ).getWidth ( ) / 2);
			myColOffset = mainShip.getY ( ) - ((int) height / 2 - mainShip.getImage ( ).getHeight ( ) / 2);
		
			g2.translate (-myRowOffset, -myColOffset);
		} 
					
		// build a temporary list of rectangles for each space object. 
		// their rectangle can change if they are moving so we do this each 
		// time paint is called (could do it in SpacePanel:moveObjects )
		ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
		for (SpaceObject obj : this.objects)
		{
			BufferedImage image = obj.getImage ( );
			Graphics2D g22 = (Graphics2D)g.create ( );
			
			// rotate each object separately based on its rotation
			g22.rotate(obj.getRotation ( ),(int)(obj.getX())+image.getWidth ( )/2,
								(int)(obj.getY())+image.getHeight ( )/2);
			// draw rotated image
			g22.drawImage (image, (int)(obj.getX()), (int)(obj.getY()), null);
			
			// set next rotation
			obj.setRotation (obj.getRotation ( )+obj.getRotationRate());
			
			// add a rectangle that outlines this image (used in collision calculations later)
			rects.add (new Rectangle(obj.getX ( ),obj.getY ( ), image.getWidth ( ),image.getHeight ( )));

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
			int shipIndex = objects.indexOf (this.mainShip);
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
