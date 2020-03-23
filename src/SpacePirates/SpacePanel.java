package SpacePirates;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
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

public class SpacePanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener
{

	private static final long		serialVersionUID	= -3004935837396357680L;

	private int						myRowOffset			= 0;
	private int						myColOffset			= 0;

	private double					zoomFactor			= 1;

	private ArrayList <SpaceObject>	objects				= new ArrayList <SpaceObject> ( );

	// keep a handle to the mainShip for quick reference. The mainShip is in the 
	// objects array list of SpaceObjects too.
	private SpaceShip 				mainShip			= null; 

	
	
	

	public SpacePanel ( )
	{
		addMouseListener (this);
		addMouseMotionListener (this);
		addMouseWheelListener (this);
	}

	public SpacePanel (LayoutManager layout)
	{
		super (layout);
		addMouseListener (this);
		addMouseMotionListener (this);
		addMouseWheelListener (this);
	}

	public SpacePanel (boolean isDoubleBuffered)
	{
		super (isDoubleBuffered);
		addMouseListener (this);
		addMouseMotionListener (this);
		addMouseWheelListener (this);
	}

	public SpacePanel (LayoutManager layout, boolean isDoubleBuffered)
	{
		super (layout, isDoubleBuffered);
		addMouseListener (this);
		addMouseMotionListener (this);
		addMouseWheelListener (this);
	}

	public void addMainShip (SpaceShip ship)
	{
		objects.remove (mainShip);
		objects.add (ship);
		this.mainShip = ship;
	}
	
	public SpaceShip mainShip()
	{
		return this.mainShip;
	}
	
	public void add (SpaceObject obj)
	{
		objects.add (obj);
	}

	public void remove (SpaceObject obj)
	{
		objects.remove (obj);
	}

	public int getRowOffset ( )
	{
		// TODO Auto-generated method stub
		return myRowOffset;
	}

	public int getColOffset ( )
	{
		// TODO Auto-generated method stub
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

	@Override
	public void mouseDragged (MouseEvent e)
	{
			

	}

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

	@Override
	public void mouseClicked (MouseEvent e)
	{
		// right mouse double click causes the player's ship to fire it's current
		// weapon. 
		
		// TODO: get the weapon being fired from the mainship object.
		
		if (e.getButton ( ) == MouseEvent.BUTTON3 && e.getClickCount ( ) == 2)
		{
			System.out.println("FIRE");
			Weapon weapon = this.mainShip.getCurrentWeapon ( );
			if (weapon != null)
			{
				weapon.setX(this.mainShip.getX ( ) + 
					        this.mainShip.getImage ( ).getWidth ( )/2 -
					        weapon.getImage ( ).getWidth ( )/2);
				weapon.setY (this.mainShip.getY ( ) + 
			        		this.mainShip.getImage ( ).getHeight( )/2 -
			        		weapon.getImage ( ).getHeight( )/2);
				weapon.setSpeedAng (mainShip.getSpeedAng ( ));
				weapon.setRotation (mainShip.getRotation ( ));
				weapon.setOrigin (mainShip); // missile knows where it came from
				this.objects.add (weapon);
			} // fire weapon
		}
	}

	@Override
	public void mousePressed (MouseEvent e)
	{
		if (e.getButton ( ) == MouseEvent.BUTTON1)
		{
			System.out.print("Move  ");
			if (mainShip.getSpeed() < 50)
				mainShip.setSpeed (mainShip.getSpeed() + 10);
			mainShip.setCoasting (false);
			mainShip.setInOrbit (false);
		}

	}

	@Override
	public void mouseReleased (MouseEvent e)
	{
		if (e.getButton ( ) == MouseEvent.BUTTON1)
		{
			//System.out.print("Stop Move  ");
			
			//mainShip.setSpeed (0);
			mainShip.setCoasting (true);
			
		}

	}

	@Override
	public void mouseEntered (MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited (MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void moveObjects()
	{
		
		for (SpaceObject object : this.objects)
		{
			double speed = object.getSpeed ( );
			double deltaX = speed*Math.cos(object.getSpeedAng ( ));
			double deltaY = speed*Math.sin(object.getSpeedAng ( ));
			object.setX (object.getX() + (int)((deltaX)));
			object.setY (object.getY() + (int)((deltaY)));
			if ((object == mainShip) && mainShip.isInOrbit() == true)
				object.orbit();
			else if ((object == mainShip) && mainShip.isCoasting())
				this.mainShip.setSpeed ((speed <= 0 ? 0 : speed - 1));
			
		}
	}
	
	@Override
	public void paintComponent (Graphics g)
	{
		
		Graphics2D g2 = (Graphics2D) g;
		
		// scales the window based on mouse scroll
		AffineTransform at = new AffineTransform();
		at.scale(zoomFactor, zoomFactor);
		g2.transform(at);
		//setPreferredSize(new Dimension((int)((getWidth())*zoomFactor),
		//	(int)((getHeight())*zoomFactor)));

		
		// determine if ship is nearing the edges of the screen and
		// shift the coordinate system accordingly so that the ship 
		// does not leave the screen but can keep moving
		double width = getWidth()/zoomFactor;
		double height = getHeight()/zoomFactor;

		if (mainShip != null)
		{
			if (mainShip.getX() > (width*.8))
				myRowOffset = (int)(width*.8- mainShip.getX ( ) );
			else if (mainShip.getX ( ) < width*.2)
				myRowOffset = (int)(width*.2 - mainShip.getX ( ));
		
		
		
			if (mainShip.getY() > (height*.8))
				myColOffset = (int)(height*.8 - mainShip.getY ( ) );
			else if (mainShip.getY ( ) < height*.2)
				myColOffset = (int)(height*.2 - mainShip.getY ( ) );
			
			myRowOffset = mainShip.getX ( ) - ((int) width / 2 - mainShip.getImage ( ).getWidth ( ) / 2);
			myColOffset = mainShip.getY ( ) - ((int) height / 2 - mainShip.getImage ( ).getHeight ( ) / 2);
		
		
			g2.translate (-myRowOffset, -myColOffset);
		} // end if no ship
		
// the commented out code keeps the screen centered as we zoom in and out but
		// it messes up the directional calculations and the edge of screen 
		// calculations above.
		//		g2.translate (myRowOffset + (width/2-300), myColOffset  + (height/2-300));
		  
			
		// build a temporary list of rectangles for each space object. 
		// their rectangle can change if they are moving so we do this each 
		// time paint is called (could do it in SpacePanel:moveObjects )
		ArrayList<Rectangle> rects = new ArrayList<Rectangle>();

		for (SpaceObject obj : this.objects)
		{
			BufferedImage image = obj.getImage ( );
			Graphics2D g22 = (Graphics2D)g.create ( );
			g22.rotate(obj.getRotation ( ),(int)(obj.getX())+image.getWidth ( )/2,
								(int)(obj.getY())+image.getHeight ( )/2);
			g22.drawImage (image, (int)(obj.getX()), (int)(obj.getY()), null);
			obj.setRotation (obj.getRotation ( )+obj.getRotationRate());
			rects.add (new Rectangle(obj.getX ( ),obj.getY ( ), image.getWidth ( ),image.getHeight ( )));
			g22.dispose ( );
		}

		// look for collisions
		// outer loop iterates through all rectangles
		for (int r1 =0; r1 < rects.size ( ); r1++)
		{
			Rectangle rect1 = rects.get (r1);
			// inner loop iterates through all rectangles after r1 in list
			for (int r2 = r1+1; r2 < rects.size ( ); r2++)
			{
				Rectangle rect2 = rects.get (r2);
				if (rect1.intersects (rect2))
				{
					// make sure weapons aren't "colliding" with whatever fired them
					SpaceObject o1 = objects.get (r1);
					SpaceObject o2 = objects.get (r2);
					if ((o1.getOrigin ( ) == o2) || (o2.getOrigin ( ) == o1))
						;// as Trump says... "No Collision"
					else
					{
						o1.simCollide (o2);
						System.out.println("collision between " + r1 + " and " +r2);						
						boolean o1Destroyed = o1.collision (10, objects);
						boolean o2Destroyed = o2.collision (10, objects);
						if (o1Destroyed)
						{
							objects.remove (o1);
						}
						if (o2Destroyed)
						{
							objects.remove (o2);
						} // end destroy second object
					} // end else (weapon not colliding with origin)
					
				} // end if r1 and r2 intersect
			} // end inner loop for r2
		} // end outer loop for r1


	} // end paint component

	@Override
	public void mouseWheelMoved (MouseWheelEvent e)
	{
		// Zoom in
		if (e.getWheelRotation ( ) < 0)
		{
			zoomFactor *= 1.1;
			//repaint ( );
		}
		// Zoom out
		if (e.getWheelRotation ( ) > 0)
		{
			zoomFactor /= 1.1;
			//repaint ( );
		}
	}

	public void startGame()
	{
		
	}
	
	public void endGame()
	{
		objects.clear ( );
	}
	
	
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
			int shipIndex = objects.indexOf (this.mainShip);
			out.writeInt (shipIndex);

			// save count of how many space objects we're saving
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
    }
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
				if (i == shipIndex)
				{
					if (obj.getClass ( ).equals (SpaceShip.class))
						this.mainShip = (SpaceShip)obj;
					else
						errors = new String("Can't find main space ship");
				}
			}
			
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
    }

}
