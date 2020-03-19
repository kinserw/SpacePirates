package SpacePirates;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PirateBtnPanel extends JPanel {
	JTextField myTextBox = null;
	
	public PirateBtnPanel()
	{
		
	}
	
	public void updateBtns()
	{
		myTextBox.setText("Score: " );
		
	}
	
	public void createContent()
	{
		    JButton newGameBtn =new JButton("Stats & Inventory");  
		    newGameBtn.addActionListener( (event) ->  
		    			{String str = "add stuff here";}
		    	);
		    
		    GridBagLayout grid = new GridBagLayout();
		    this.setLayout(grid);
		    GridBagConstraints constraints = new GridBagConstraints();
		    constraints.fill = GridBagConstraints.BOTH;
		    constraints.weightx = 1.0;
		    newGameBtn.setPreferredSize(new Dimension(100,32));
		    newGameBtn.setSize(new Dimension(100,32));
		    grid.setConstraints(newGameBtn, constraints);
		    this.add(newGameBtn);

		    myTextBox = new JTextField();
		    myTextBox.setText("Score:");
		    myTextBox.setBounds(0,0,50,16);
		    myTextBox.setPreferredSize(new Dimension(50,20));
		    myTextBox.setSize(new Dimension(50,20));
		    myTextBox.addActionListener( (event) -> {String str = "action event here" ; });
	        constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
	        grid.setConstraints(myTextBox,constraints);
		    this.add(myTextBox);
		    
		    this.revalidate();
	}

}
