package SpacePirates;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

public class SpacePanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener
{

	private static final long		serialVersionUID	= -3004935837396357680L;

	private int						myRowOffset			= 0;
	private int						myColOffset			= 0;

	private double					zoomFactor			= 1;
	private boolean					zoomer				= false;
	private boolean 				accelerating		= false;

	private ArrayList <SpaceObject>	objects				= new ArrayList <SpaceObject> ( );
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

	@Override
	public void mouseDragged (MouseEvent e)
	{
		// TODO Auto-generated method stub
		
		// track previous x,y 
		// use a directional cone whose apex is the ship at the center of the screen.
		// if dragging to the right of and away from center of screen then accelerating 
		// 				(must be pass 20% of distance from center to side so draggin near ship will turn w/o accelerating)
		// if dragging above the horizontal line and to the right of the ship, then turn left (rotate universe clockwise)
		// if dragging below the horiztonal line and to the right o fthe ship, then turn right (rotate universe counter clockwise)
		// if dragging behind the ship then decelerating (no turning involved)
		// 
			//System.out.println("" + e.getX( ) + ", "+ e.getY() + "  set rotation angle of universe to follow direction being dragged");
			

	}

	@Override
	public void mouseMoved (MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked (MouseEvent e)
	{
		// TODO Auto-generated method stub
		if (e.getClickCount ( ) == 2)
		{
			System.out.println("FIRE");
		}
		else
			if (e.getClickCount ( ) == 1)
			{
				System.out.print("Move  ");
				int x = (int)(e.getX());
				int y = (int)(e.getY());
				if (x > (getWidth()/2))
				{
					mainShip.setSpeed (10*(x-getWidth()/2)/(getWidth()/2));
					mainShip.setRotationRate (-1*((y-(getHeight()/2.0))/(getHeight()/2.0))/10);
					if (y < (getHeight()/2))
					{
						System.out.println("Up");
					}
					else
					{
						System.out.println("Down");
					}
				}
				
			}
	}

	@Override
	public void mousePressed (MouseEvent e)
	{

	}

	@Override
	public void mouseReleased (MouseEvent e)
	{

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
		System.out.println("speed = " + mainShip.getSpeed() +   " rotation rate = " +
							mainShip.getRotationRate ( ) + "x = " + mainShip.getX ( ) +
							"y = " + mainShip.getY ( ));
		double deg = Math.toDegrees (mainShip.getRotationRate ( ));
		double deltaX = mainShip.getSpeed()*Math.cos(deg);
		double deltaY = mainShip.getSpeed()*Math.sin(deg);
		this.mainShip.setX (mainShip.getX() + (int)deltaX);
		this.mainShip.setY (mainShip.getY() - (int)deltaY);
		this.mainShip.setRotation (this.mainShip.getRotation()+this.mainShip.getRotationRate ( ));
	}
	
	@Override
	public void paintComponent (Graphics g)
	{
		// super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		  //if (zoomer) {
		  AffineTransform at = new AffineTransform();
		  at.scale(zoomFactor, zoomFactor);
		  g2.transform(at);
		  //if (zoomer) {
			  setPreferredSize(new Dimension((int)((getWidth())*zoomFactor),
			  (int)((getHeight())*zoomFactor)));
		  //}
		  zoomer = false;

		  // calculate offsets so main ship stays in center of screen always
		  this.myRowOffset = (int)(getWidth()/zoomFactor)/2 - mainShip.getX ( );
		  this.myColOffset = (int)(getHeight()/zoomFactor)/2 - mainShip.getY ( );

/*
 * probably should make the player's ship a separate member/attribute outside of the collection.
 * the collection holds all the space things that have a position relative to the universe but the ship
 * really represents a cursor that navigate through the universe.
 * For testing purposes, can we create "stars" in a uniform pattern through out the universe so that we
 * can "see" the ships maneuvering? 
 * 
 * need to shift the ship to always be at the center of the screen even if the screen size is changed OR
 * if the wheel zooms in or out.
 * 
 */
	  
		BufferedImage image = mainShip.getImage ( );
		int centerX = (int)(getWidth()/zoomFactor)/2+image.getWidth ( )/2;
		int centerY = (int)(getHeight()/zoomFactor)/2+image.getHeight ( )/2;
		g.drawImage (image, mainShip.getX ( ) + myRowOffset, 
							mainShip.getY() + myColOffset, null);
		g2.rotate(mainShip.getRotation ( ),centerX,centerY);
		//mainShip.setRotation (mainShip.getRotation ( ));
			
		for (SpaceObject obj : this.objects)
		{
			image = obj.getImage ( );
			Graphics2D g22 = (Graphics2D)g.create ( );
			g22.rotate(obj.getRotation ( ),(int)(obj.getX()+myRowOffset)+image.getWidth ( )/2,
								(int)(obj.getY()+myColOffset)+image.getHeight ( )/2);
			g22.drawImage (image, (int)(obj.getX()+myRowOffset), (int)(obj.getY()+myColOffset), null);
			obj.setRotation (obj.getRotation ( )+obj.getRotationRate());
		}
		if (accelerating) 
			System.out.println("accelerates");

		// }

	} // end paint component

	@Override
	public void mouseWheelMoved (MouseWheelEvent e)
	{
		// TODO Auto-generated method stub
		zoomer = true;
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


}
