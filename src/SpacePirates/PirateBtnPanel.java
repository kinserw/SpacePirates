/**
 * ---------------------------------------------------------------------------
 * File name: PirateBtnPanel.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Mar 31, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * This class creates and initializes all the display components on the button panel
 *
 * <hr>
 * Date created: Apr 12, 2020
 * <hr>
 * @author William Kinser
 */
public class PirateBtnPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField myTextBox = null;	// non-editable text field to show score
	private JButton statsButton = null; 	// button to display stats
	
	
	/**
	 * Constructor        
	 *
	 * <hr>
	 * Date created: Apr 12, 2020 
	 *
	 * 
	 */
	public PirateBtnPanel()
	{
		
	}

	
	/**
	 * this method registers a listener for when the stats button is pressed         
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 * @param al
	 * @return
	 */
	public boolean addStatsListener(ActionListener al)
	{
		// make sure the button exists
		boolean success = statsButton != null;
		if (success)
			statsButton.addActionListener (al);

		return success;
	}
	
	
	/**
	 * this method will use the score passed in to update the display         
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 * @param score
	 */
	public void updateScore (int score)
	{
		// make sure the text box exists
		if (myTextBox != null)
		{
			myTextBox.setText ("Score: " + score);
		}
	}
	
	/**
	 * this method will create the layout and display components         
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 */
	public void createContent()
	{
		// create the button
	    statsButton =new JButton("Stats & Inventory");  
	    statsButton.setMnemonic (KeyEvent.VK_S);
	    
	    // make layout
	    GridBagLayout grid = new GridBagLayout();
	    this.setLayout(grid);

	    // make constraints
	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.BOTH;
	    constraints.weightx = 1.0;

	    statsButton.setPreferredSize(new Dimension(100,32));
	    statsButton.setSize(new Dimension(100,32));
	    grid.setConstraints(statsButton, constraints);
	    this.add(statsButton);

	    // make text box (uneditable)
	    myTextBox = new JTextField();
	    myTextBox.setText("Score:");
	    myTextBox.setBounds(0,0,50,16);
	    myTextBox.setPreferredSize(new Dimension(50,20));
	    myTextBox.setSize(new Dimension(50,20));
	    myTextBox.setFocusable (false);
	    myTextBox.setEditable (false);
        constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        grid.setConstraints(myTextBox,constraints);
	    this.add(myTextBox);
	    
	    this.revalidate();
	} // end createContent

} // end pirateBtnPanel
