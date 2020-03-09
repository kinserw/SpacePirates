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

	private ArrayList <SpaceObject>	objects				= new ArrayList <SpaceObject> ( );

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
		 

		for (SpaceObject obj : this.objects)
		{
			BufferedImage image = obj.getImage ( );
			double rads = Math.toRadians(obj.getRotation ( ));
			double sin = Math.abs(Math.sin(rads));
			double cos = Math.abs(Math.cos(rads));
			int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
			int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
			BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
			AffineTransform transform = new AffineTransform();
			transform.translate(w / 2, h / 2);
			transform.rotate(rads,0, 0);
			transform.translate(-image.getWidth() / 2, -image.getHeight() / 2);
			AffineTransformOp rotateOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
			rotateOp.filter(image,rotatedImage);
			g2.drawImage (rotatedImage, obj.getX ( ), obj.getY ( ), Color.BLACK, null);
			obj.setRotation (obj.getRotation ( )+15);
		}
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
