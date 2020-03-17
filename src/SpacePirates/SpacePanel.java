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
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.sound.sampled.Line;
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
			

	}

	@Override
	public void mouseMoved (MouseEvent e)
	{
		double rotation = Math.atan2 ((e.getY ( ) - mainShip.getY ( )),(e.getX() - mainShip.getY()));
		mainShip.setRotation (rotation);

	}

	@Override
	public void mouseClicked (MouseEvent e)
	{
		// TODO Auto-generated method stub
		if (e.getButton ( ) == MouseEvent.BUTTON3 && e.getClickCount ( ) == 2)
		{
			System.out.println("FIRE");
		}
	}

	@Override
	public void mousePressed (MouseEvent e)
	{
		if (e.getButton ( ) == MouseEvent.BUTTON1)
		{
			System.out.print("Move  ");
			
			mainShip.setSpeed (10);
			
		}

	}

	@Override
	public void mouseReleased (MouseEvent e)
	{
		if (e.getButton ( ) == MouseEvent.BUTTON1)
		{
			System.out.print("Stop Move  ");
			
			mainShip.setSpeed (0);
			
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
		double deltaX = mainShip.getSpeed()*Math.cos(mainShip.getRotation ( ));
		double deltaY = mainShip.getSpeed()*Math.sin(mainShip.getRotation ( ));
		this.mainShip.setX (mainShip.getX() + (int)(deltaX));
		this.mainShip.setY (mainShip.getY() + (int)(deltaY));
	}
	
	@Override
	public void paintComponent (Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
			
		AffineTransform at = new AffineTransform();
		at.scale(zoomFactor, zoomFactor);
		g2.transform(at);
		int xOffset = 0; 
		if (mainShip.getX() > (getWidth()*.8))
			xOffset = (int)(getWidth()*.8 - mainShip.getX ( ));
		else if (mainShip.getX ( ) < getWidth()*.2)
			xOffset = (int)(getWidth()*.2 - mainShip.getX ( ));
		int yOffset = 0; 
		if (mainShip.getY() > (getHeight()*.8))
			yOffset = (int)(getHeight()*.8 - mainShip.getY ( ));
		else if (mainShip.getY ( ) < getHeight()*.2)
			yOffset = (int)(getHeight()*.2 - mainShip.getY ( ));
		g2.translate (xOffset, yOffset);
		  
		setPreferredSize(new Dimension((int)((getWidth())*zoomFactor),
										(int)((getHeight())*zoomFactor)));

		zoomer = false;
		
		// calculate offsets so main ship stays in center of screen always
		this.myRowOffset = (int)(getWidth()/zoomFactor)/5 - mainShip.getX ( );
		this.myColOffset = (int)(getHeight()/zoomFactor)/2 - mainShip.getY ( );

  
		Graphics2D g22 = (Graphics2D)g.create ( );
		BufferedImage image = mainShip.getImage ( );
		g22.rotate(mainShip.getRotation ( ),mainShip.getX ( )+image.getWidth()/2 , 
			mainShip.getY()+image.getHeight ( )/2);
		g22.drawImage (image, mainShip.getX ( ) , 
							mainShip.getY() , null);

		for (SpaceObject obj : this.objects)
		{
			image = obj.getImage ( );
			g22 = (Graphics2D)g.create ( );
			g22.rotate(obj.getRotation ( ),(int)(obj.getX())+image.getWidth ( )/2,
								(int)(obj.getY())+image.getHeight ( )/2);
			g22.drawImage (image, (int)(obj.getX()), (int)(obj.getY()), null);
			obj.setRotation (obj.getRotation ( )+obj.getRotationRate());
		}
		if (accelerating) 
			System.out.println("accelerates");


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
