package SpacePirates;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;


public class PirateFrame extends JFrame implements Runnable
{
	int difficulty = 3;
	public static PirateFrame ourFrame = null;
	SpacePanel spacePanel = null;
	private PirateBtnPanel myBtnPanel = null;
	JProgressBar progressBar = null;
	


	/**
	 * 
	 */
	private static final long serialVersionUID = -943895067531390799L;

	public PirateFrame() throws HeadlessException {
		// set up default operations and starting size/position of screen 
		setTitle("Space Pirates");
		PirateFrame.ourFrame = this; 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(00,0);
		setPreferredSize(new Dimension(600, 600));

		createMenuBar();
		
		spacePanel = new SpacePanel();
		spacePanel.add (new SpaceShip(200,150));

		setLayout(new BorderLayout());
		JPanel buttonPanel = this.createButtons();

		//Create a split pane with the two scroll panes in it.
		buttonPanel.setMinimumSize(new Dimension(16,32));
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
		                           buttonPanel, spacePanel);

		//Provide minimum sizes for the two components in the split pane
		splitPane.setOneTouchExpandable(false);
		splitPane.setAutoscrolls(false);
		splitPane.setDividerSize(0);
		splitPane.setResizeWeight(.05);
		splitPane.setBackground(Color.BLACK);
		//splitPane.setForeground(Color.BLACK);		
		add(BorderLayout.CENTER, splitPane);
		progressBar = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		progressBar.setStringPainted(true);
		add(BorderLayout.SOUTH,this.progressBar);
		
		shapeMyFrame();

		Thread myThread = new Thread(this);
		
		//run();
		myThread.start();


	}

	public PirateFrame(GraphicsConfiguration gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	public PirateFrame(String title) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public PirateFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
	}
	
	public void shapeMyFrame()
	{
		/*
		 * setPreferredSize(new Dimension((myGrid.getMaxX()+1)*spacePanel.getRowOffset(),
		 * (myGrid.getMaxY()+4)*spacePanel.getColOffset()));
		 * spacePanel.setPreferredSize(
		 * new Dimension((myGrid.getMaxX()+1)*spacePanel.getRowOffset(),
		 * (myGrid.getMaxY()+4)*spacePanel.getColOffset()));
		 */		
		pack();

		setVisible(true);
		setResizable(true);

	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings("unused")
		PirateFrame gameFrame = new PirateFrame();
	}
	
	private JPanel createButtons()
	{
		this.myBtnPanel = new PirateBtnPanel();
		myBtnPanel.createContent();
		return myBtnPanel;
		
	}

	@Override
	public void run() {
		while (true)
		{
			progressBar.setValue( (int)(0.44*100));

					

			repaint();
			//for (int i = 0; i < 1000000; i++);'
			synchronized(this)
			{
				try {
					this.wait(250);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		}
		
	}

	private void saveGame()
	{
		
	}

	private void loadGame()
	{
		
	}
		

	private void endGame()
	{
		
	}
	

	private void startGame()
	{
		
	}
	
	private void createMenuBar() 
	{

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem loadMenuItem = new JMenuItem("Load");
        loadMenuItem.setMnemonic(KeyEvent.VK_L);
        loadMenuItem.setToolTipText("Load Game");
        // need to place action listener near end of this method
        // so that it can see all the menu variables and 
        // update them based on the loaded file.

        fileMenu.add(loadMenuItem);
        
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setToolTipText("Save Game");
        saveMenuItem.setEnabled(false);
        saveMenuItem.addActionListener((event) -> this.saveGame());

        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        
        JMenu optionMenu = new JMenu("Options");
        
        JMenuItem newMenuItem = new JMenuItem("New Game");
        JMenuItem endMenuItem = new JMenuItem("End Game");
        newMenuItem.setMnemonic(KeyEvent.VK_N);
        newMenuItem.setToolTipText("Start a new game");
        newMenuItem.addActionListener((event) -> {
        				this.startGame ( );
        			});

        fileMenu.add(newMenuItem);
 
        endMenuItem.setMnemonic(KeyEvent.VK_G);
        endMenuItem.setToolTipText("End current game");
        endMenuItem.setEnabled(false);
        endMenuItem.addActionListener((event) -> 
        			{
        				this.endGame ( );
        			});

        fileMenu.add(endMenuItem);
        fileMenu.addSeparator();
         
        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((event) -> System.exit(0));

        fileMenu.add(eMenuItem);
        menuBar.add(fileMenu);

        optionMenu.setMnemonic(KeyEvent.VK_O);

        ButtonGroup difGroup = new ButtonGroup();

        JRadioButtonMenuItem easyRMenuItem = new JRadioButtonMenuItem("Easy");
        optionMenu.add(easyRMenuItem);
        easyRMenuItem.setToolTipText("Set difficulty of game play to Easy");

        easyRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.difficulty = 3;
            }
        });

        JRadioButtonMenuItem mediumRMenuItem = new JRadioButtonMenuItem("Normal");
        mediumRMenuItem.setSelected(true);
        optionMenu.add(mediumRMenuItem);
        mediumRMenuItem.setToolTipText("Set difficulty of game play to Normal");

        mediumRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.difficulty = 5;
            }
        });

        JRadioButtonMenuItem hardRMenuItem = new JRadioButtonMenuItem("Hard");
        optionMenu.add(hardRMenuItem);
        hardRMenuItem.setToolTipText("Set difficulty of game play to Hard");

        hardRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.difficulty = 8;
            }
        });
        
        JRadioButtonMenuItem reallyHardRMenuItem = new JRadioButtonMenuItem("Really Hard");
        optionMenu.add(reallyHardRMenuItem);
        reallyHardRMenuItem.setToolTipText("Set difficulty of game play to Really Hard");

        reallyHardRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.difficulty = 10;
            }
        });

        difGroup.add(easyRMenuItem);
        difGroup.add(mediumRMenuItem);
        difGroup.add(hardRMenuItem);
        difGroup.add(reallyHardRMenuItem);
        
        optionMenu.addSeparator();

        ButtonGroup sizeGroup = new ButtonGroup();

        JRadioButtonMenuItem smallRMenuItem = new JRadioButtonMenuItem("Small Map");
        optionMenu.add(smallRMenuItem);

        smallRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
// small map logic goes here
            }
        });

        JRadioButtonMenuItem medRMenuItem = new JRadioButtonMenuItem("Medium Map");
        optionMenu.add(medRMenuItem);

        medRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
//medium map logic goes here
            	}
        });

        JRadioButtonMenuItem largeRMenuItem = new JRadioButtonMenuItem("Large Map");
        largeRMenuItem.setSelected(true);
        optionMenu.add(largeRMenuItem);

        largeRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
// large map logic goes here
    			}
        });

        sizeGroup.add(smallRMenuItem);
        sizeGroup.add(medRMenuItem);
        sizeGroup.add(largeRMenuItem);

        optionMenu.addSeparator();
        
        loadMenuItem.addActionListener((event) -> 
        		{
        		this.loadGame();
        		});

 
        menuBar.add(optionMenu);
        
        JMenuItem helpMenu = new JMenuItem("How to Play");
        helpMenu.addActionListener( (event) -> JOptionPane.showMessageDialog(this, 
        		"<html><h1>Space Pirates</h1><p style='width:400'>This is a strategy game in which the player searches the galaxy for treasure. The more treasure found, the more upgrades you can buy. The more upgrades you get, the more treasure you can find. The fun is endless!!<br>"
        		+ "<br>You pilot your ship around space. Destroying asteroids, other ships and space debris will potentially release valuable treasure. Flying over the treasure (at a reasonable speed) allows you to add it to your payload.<br>" +
        						"<br>A status bar at the top of the screen shows your current payload contents and equipment status. A health bar at the bottom of the screen helps you know when you need to seek repairs.<br>" + 
        						"<br>Docking with a space station or weigh station will allow you to bank your payload, buy repairs and upgrades. While docked you can't fire at anything. On the plus side, nothing can harm you <br>" +
        						"either since the stations are guarded by force fields."
        		));
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
	
	
}
