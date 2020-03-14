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

	private int						myRowOffset			= 32;
	private int						myColOffset			= 32;

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
			System.out.println("" + e.getX( ) + ", "+ e.getY() + "  set rotation angle of universe to follow direction being dragged");
			

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
		}
		// a click without dragging is a shot fired
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

	@Override
	public void paintComponent (Graphics g)
	{
		// super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		  //if (zoomer) {
		  AffineTransform at = new AffineTransform();
		  at.scale(zoomFactor, zoomFactor);
		  g2.transform(at);
		  if (zoomer) {
			  setPreferredSize(new Dimension((int)((600)*this.myRowOffset*zoomFactor),
			  (int)((600)*this.myColOffset*zoomFactor)));
		  }
		  zoomer = false;
		 
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
		int centerX = this.getWidth ( )/2+image.getWidth ( )/2;
		int centerY = this.getHeight ( )/2+image.getHeight ( )/2;
		g2.rotate(mainShip.getRotation ( ),centerX,centerY);
		g2.drawImage (image, this.getWidth ( )/2, this.getHeight ( )/2, null);
		mainShip.setRotation (mainShip.getRotation ( )+0.1);
			
		for (SpaceObject obj : this.objects)
		{
			image = obj.getImage ( );
			Graphics2D g22 = (Graphics2D)g.create ( );
			g22.rotate(obj.getRotation ( ),obj.getX()+image.getWidth ( )/2,obj.getY()+image.getHeight ( )/2);
			g22.drawImage (image, obj.getX(), obj.getY(), null);
			obj.setRotation (obj.getRotation ( )+15);
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
			repaint ( );
		}
		// Zoom out
		if (e.getWheelRotation ( ) > 0)
		{
			zoomFactor /= 1.1;
			repaint ( );
		}
	}


}
