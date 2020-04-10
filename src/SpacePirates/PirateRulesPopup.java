/**
 * ---------------------------------------------------------------------------
 * File name: PirateRulesPopup.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Apr 10, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;

import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * Displays the rules on how to play in a dialog that the user
 *  can place next to the game screen for reference.
 *
 * <hr>
 * Date created: Apr 10, 2020
 * <hr>
 * @author William Kinser
 */
public class PirateRulesPopup extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor        
	 *
	 * <hr>
	 * Date created: Apr 10, 2020 
	 *
	 * 
	 * @param owner
	 */
	public PirateRulesPopup (Frame owner)
	{
		super (owner,"How to Play",false);
		JLabel label = new JLabel (
			"<html><h1>Space Pirates</h1><p style='width:400'>This is a strategy game in which the player searches the galaxy for treasure. The more treasure found, the more upgrades you can buy. The more upgrades you get, the more treasure you can find. The fun is endless!!<br>"
							+ "<br>You pilot your ship around space. Destroying asteroids, other ships and space debris will potentially release valuable treasure. Flying over the treasure (at a reasonable speed) allows you to add it to your payload.<br>" +
											"<br>A status bar at the top of the screen shows your current payload contents and equipment status. A health bar at the bottom of the screen helps you know when you need to seek repairs.<br>" + 
											"<br>Docking with a space station or weigh station will allow you to bank your payload, buy repairs and upgrades. While docked you can't fire at anything. On the plus side, nothing can harm you <br>" +
											"either since the stations are guarded by force fields.<br><br>Moving: <br><\t>left click to accelerate" +
											"<br><\t>right double click to fire your weapon(s)" + 
											"<br><\t>move the mouse in the direction you want to travel");	
		label.setBorder (BorderFactory.createEmptyBorder (10,10,10,10));
		this.add (label);
		this.pack();
		this.setVisible(true);
	}


}
