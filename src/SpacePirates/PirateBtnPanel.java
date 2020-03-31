package SpacePirates;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PirateBtnPanel extends JPanel {
	JTextField myTextBox = null;
	JButton statsButton = null;
	
	public PirateBtnPanel()
	{
		
	}

	
	public boolean addStatsListener(ActionListener al)
	{
		boolean success = statsButton != null;
		if (success)
			statsButton.addActionListener (al);
		return success;
	}
	
	
	public void updateScore (int score)
	{
		if (myTextBox != null)
		{
			myTextBox.setText ("Score: " + score);
		}
	}
	
	public void createContent()
	{
		    statsButton =new JButton("Stats & Inventory");  
		    
		    GridBagLayout grid = new GridBagLayout();
		    this.setLayout(grid);
		    GridBagConstraints constraints = new GridBagConstraints();
		    constraints.fill = GridBagConstraints.BOTH;
		    constraints.weightx = 1.0;
		    statsButton.setPreferredSize(new Dimension(100,32));
		    statsButton.setSize(new Dimension(100,32));
		    grid.setConstraints(statsButton, constraints);
		    this.add(statsButton);

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
	}

}
