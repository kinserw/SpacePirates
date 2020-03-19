package SpacePirates;

import java.awt.Dimension;
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

	private boolean coasting = true;

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
		double rotation = Math.atan2 ((e.getY ( )/zoomFactor- (mainShip.getY ( ) +myColOffset )),(e.getX()/zoomFactor - (mainShip.getX() + myRowOffset)));
		mainShip.setSpeedAng (rotation);
		mainShip.setRotation (rotation);
		
		
	}

	@Override
	public void mouseClicked (MouseEvent e)
	{
		// TODO Auto-generated method stub
		if (e.getButton ( ) == MouseEvent.BUTTON3 && e.getClickCount ( ) == 2)
		{
			System.out.println("FIRE");
			Missile missile = new Missile(this.mainShip.getX ( ), 
										  this.mainShip.getY ( ));
			missile.setSpeedAng (mainShip.getSpeedAng ( ));
			missile.setRotation (mainShip.getRotation ( ));
			missile.setSpeed (20);
			missile.setOrigin (mainShip); // missile knows where it came from
			this.objects.add (missile);
		}
	}

	@Override
	public void mousePressed (MouseEvent e)
	{
		if (e.getButton ( ) == MouseEvent.BUTTON1)
		{
			System.out.print("Move  ");
			
			mainShip.setSpeed (10);
			coasting = false;
			
		}

	}

	@Override
	public void mouseReleased (MouseEvent e)
	{
		if (e.getButton ( ) == MouseEvent.BUTTON1)
		{
			//System.out.print("Stop Move  ");
			
			//mainShip.setSpeed (0);
			coasting = true;
			
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
			object.setX (object.getX() + (int)(deltaX));
			object.setY (object.getY() + (int)(deltaY));
			if ((object == mainShip) && coasting)
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

		if (mainShip.getX() > (width*.8))
			myRowOffset = (int)(width*.8- mainShip.getX ( ) );
		else if (mainShip.getX ( ) < width*.2)
			myRowOffset = (int)(width*.2 - mainShip.getX ( ));



		if (mainShip.getY() > (height*.8))
			myColOffset = (int)(height*.8 - mainShip.getY ( ) );
		else if (mainShip.getY ( ) < height*.2)
			myColOffset = (int)(height*.2 - mainShip.getY ( ) );


		g2.translate (myRowOffset, myColOffset);
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
	
	
	public String saveGame(ObjectOutputStream out) throws IOException
    {
    	// returns a string containing any error messages generated while
    	// saving the game
		// save zoomFactor
		// save objects
		// save index in objects that corresponds to mainShip
		// save offsets
		// 
    	return null;
    }
    public String loadGame(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
    	// returns a string containing any error messages generated while
    	// loading the game
		// load zoomFactor
		// load objects
		// load index in objects that corresponds to mainShip, set mainShip
		// load offsets
    	return null;
    }

}
