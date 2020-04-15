/**
 * ---------------------------------------------------------------------------
 * File name: PirateFrame.java
 * Project name: SpacePirates
 * ---------------------------------------------------------------------------
 * Creator's name and email: William Kinser, kinserw@etsu.edu
 * Course:  CSCI 1260 277
 * Creation Date: Mar 31, 2020
 * ---------------------------------------------------------------------------
 */

package SpacePirates;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileNameExtensionFilter;



/**
 * This class is the main class. It has its own thread and is the listener for key events.
 *
 * <hr>
 * Date created: Apr 12, 2020
 * <hr>
 * @author William Kinser
 */
public class PirateFrame extends JFrame implements Runnable, ActionListener, TreasureListener, OrbitListener
{
	public static PirateFrame ourFrame = null;	// static handle to myself
	SpacePanel spacePanel = null;				// handle to main game screen
	private PirateBtnPanel myBtnPanel = null;	// handle to the btnPanel at top of screen
	JProgressBar progressBar = null;			// handle to health bar at bottom of screen
	private boolean gameInProgress = false; 	// flag to indicate if a game is underway
	private boolean gameOver = false ; 			// forces game to end 
	private boolean firstTimeThru = true; 		// flag used to display welcome screen
	private boolean gamePaused = false;			// flag indicating if game is paused or running

	// TODO: Move all the following to SpaceGame
	private Difficulty difficulty = Difficulty.EASY; // difficulty setting for the game
	private int health = 100; 					// tracks health of main ship
	private int asteroidsHit = 0;				// count of how man weapons hit asteroids
	private int[] treasuresCaptured = new int[SpaceTreasureType.SPACE_CREDITS.ordinal()+1];
												// count of how man treasures were captured
	private int currency = 0;					// total currency available. Can trade in treasure for currency
	private boolean lastOrbitStatus = false;    // orbit status used to determine if a change in status happened

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -943895067531390799L;

	
	/**
	 * Constructor that creates main display components and starts its thread
	 *
	 * <hr>
	 * Date created: Apr 12, 2020 
	 *
	 * 
	 * @throws HeadlessException
	 */
	public PirateFrame() throws HeadlessException 
	{
		super("Space Pirates"); // set caption
		
		// set up default operations and starting size/position of screen 
		setTitle("Space Pirates");
		PirateFrame.ourFrame = this; 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(00,0);
		setPreferredSize(new Dimension(600, 600));

		// add menu bar
		createMenuBar();
		
		// add main game panel
		spacePanel = new SpacePanel();	

		// have to have a main ship even if a game hasn't started
		spacePanel.addMainShip (new SpaceShip(300,300));

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
		progressBar.setBackground (Color.RED);
		add(BorderLayout.SOUTH,this.progressBar);
		
		pack();

		setVisible(true);
		setResizable(true);

		Thread myThread = new Thread(this);
		
		// display welcom screen
		if (this.firstTimeThru)
		{
			this.firstTimeThru = false;
			String[] options = {"New Game", "Load Game","Pick Options"};
			int answer = JOptionPane.showOptionDialog (null, 
							"Welcome to SpacePirates!\n" +
						    "\nWould you like to start a new game or load an existing game?", 
				"Welcome", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, 
				options, options[0]);
			// change the way we start depending on player's choice
			if (answer == 0)
				startGame();
			else if (answer == 1)
				loadGame();
			// default option is to not start right away
				
		}

		//run()
		myThread.start();

	} // end default constructor


	/**
	 * the static public main method that starts the game
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 * @param args
	 */
	public static void main(String[] args)
	{
		// load the game icon
		String name = "SpaceShip.gif";
		BufferedImage i = null;
		try
        {
				i = ImageIO.read(new File(name));
		}
		catch (Exception e)
		{
		}

		PirateFrame gameFrame = new PirateFrame();
		gameFrame.setIconImage (i);
	}

	
	
	/**
	 * adds button panel to main display
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 * @return
	 */
	private JPanel createButtons()
	{
		// create button panel and add content
		this.myBtnPanel = new PirateBtnPanel();
		myBtnPanel.createContent();
		myBtnPanel.addStatsListener (this);
		return myBtnPanel;
		
	}

	/**
	 * captures when the stats button is pressed and displays the current stats        
	 *
	 * <hr>
	 * Date created: Mar 31, 2020 
	 *
	 * <hr>
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed (ActionEvent e)
	{
		// pause game while stats are displayed
		setGamePaused(true);
		
		// build string for treasures captured
		String treasures = "";
		for (SpaceTreasureType treasureType : SpaceTreasureType.values ( ))
			treasures += "\t\t                     " + treasureType + " = " + treasuresCaptured[treasureType.ordinal ( )] + "\n";

		// show dialog
		JOptionPane.showMessageDialog (this, 
			"Game is currently" + (gameInProgress ? " " : " not " ) + "in progress.\n" +
			"Difficulty set at: " + difficulty.toString ( ) + ". \n" +
			"Health remaining: " + health + "%\n"+
			"Current Weapon: " + SpacePanel.mainShip().getWeapon ( ) + "\n" +
			"Remaining Ammunition: " + SpacePanel.mainShip ( ).getWeaponCount ( ) + "\n" +
			"Space Credits: " + currency + "\n" +
			"Score " + PirateScore.score + "\n" +
					"\t\tAsteroids hit: " + asteroidsHit + "\n" +
					"\t\tTreasures captured: "+ "\n" + treasures
			, "Space Pirates Stats", JOptionPane.INFORMATION_MESSAGE, null);

		// unpause the game once they click ok
		setGamePaused(false);
	}
	
	/**
	 * call to set whether or not the game is paused         
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 * @param paused
	 */
	public void setGamePaused(boolean paused)
	{
		gamePaused = paused;
		this.spacePanel.setGamePaused(paused);
	}

	/**
	 * ovverride the base class behavior to run this thread         
	 *
	 * <hr>
	 * Date created: Apr 12, 2020 
	 *
	 * <hr>
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		// loop forever 
		while (true)
		{
			// set health based on main ship if it exists
			if (spacePanel.mainShip ( ) != null)
				health = spacePanel.mainShip().getHealth();
			
			progressBar.setValue( (int)(this.health));

			// if game is not in progresss we don't do anything, otherwise display objects
			if (gameInProgress)
			{
				
				repaint();		
				this.myBtnPanel.updateScore (PirateScore.score);
				
				// now that collisions have been processed, determine if the main ship is
				// so damaged that the game is over.
				if (spacePanel.mainShip().getHealth ( ) <= 0)
				{
					JOptionPane.showMessageDialog (this, "Game Over!\nYour ship has been destroyed!");
					gameOver = true;
					endGame();
					gameInProgress = false;
					repaint();
				}
				else if (!gamePaused)
					this.spacePanel.moveObjects();
			}
			
			// set delay 
			synchronized(this)
			{
				try {
					this.wait(50);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} // end synchronized 
		
		} // end loop 
		
	} // end run method

	/**
	 * this method to save game specific values         
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 */
	private void saveGame()
	{
		// use JFileChooser to get file to save to
		JFileChooser fileChooser = new JFileChooser("src");
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "SpacePirate files", "spf");
	    fileChooser.setFileFilter(filter);	
	    fileChooser.addChoosableFileFilter(filter);
	    fileChooser.setAcceptAllFileFilterUsed(false);
	    fileChooser.setSelectedFile(new File("*.spf"));

	    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
	      // get file 
		  File file = fileChooser.getSelectedFile();
		  try 
		  {
			  // if it exists then delete it and create a new one
			  if (file.exists())
				  file.delete();
			 
			  // create a new one
			  file.createNewFile();
			  
			  // make sure I have permissions to write to this location
			  if (file.canWrite())
			  {
				  // set game heading (this helps uniquely identify files as spf files)
				  String heading = "SpacePirate Game";
				  
				  // create an output stream for the file
				  FileOutputStream fileOut = new FileOutputStream(file); 
		          ObjectOutputStream out = new ObjectOutputStream(fileOut); 

		          // put the heading file there
		          out.writeObject(heading); 
		          
		          // call method to save game specific values
		          String errors = saveGame(out);
		          if (errors != null)
		        	  JOptionPane.showMessageDialog (this, errors);
		          
		          out.close(); 
		          fileOut.close(); 
				  
			  	}// can write to file
			  	else
					  JOptionPane.showMessageDialog(null, "Unable to save game");
		  }
		  catch (Exception e)
		  {
			  JOptionPane.showMessageDialog(null, "Unable to save game");
		  }
		  
		} // end get file
	} // end save game

	/**
	 * this method is the exact opposite of saveGame         
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 * @return
	 */
	private boolean loadGame()
	{
		// use JFileChooser to specify the file to load
		JFileChooser fileChooser = new JFileChooser("src");
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("SpacePirate files", "spf");
	    fileChooser.setFileFilter(filter);
	    fileChooser.setDialogTitle ("Load Game");
	    fileChooser.setApproveButtonToolTipText ("Select the SpacePirate saved game to load.");
	    boolean status = true;

	    // define the file 
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			// load from file
		  	try 
			{
		  		// make sure the file exists and I can read it (permissions)
				if (file.exists() && file.canRead())
				{
					// find the special heading
					String heading = "SpacePirate Game";
					FileInputStream fileIn = new FileInputStream(file); 
					ObjectInputStream in = new ObjectInputStream(fileIn); 
					      
					// verify the special heading is in the file
					String str = (String)in.readObject(); 
					if (!str.equals(heading))
						System.out.println("this is not a SpacePirate game file");
					else 
					{
						// call the method that loads game specific values from file
						String errors = loadGame(in);
						if (errors != null) 
						{
							JOptionPane.showMessageDialog (this, errors);
							status = false;
						}
					}
			        in.close(); 
			        fileIn.close(); 
			          
				} // can read from file
				else
				{
					JOptionPane.showMessageDialog(null, "Unable to load game");
					return false;
				}
			}
		  	catch (Exception e)
			{
				  JOptionPane.showMessageDialog(null, "Unable to load game");
				  return false;
			}
		} // end get file

		gameInProgress = status;

		return status;
	} // end load game
		

	/**
	 * this method is called to end the game but not the program
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 */
	private void endGame()
	{

		// if game isn't over by definition it must be at the request of the player
		if (!gameOver) 
		{
			int answer = JOptionPane.showConfirmDialog (this, "Are you sure you want to end this game?");
			if (answer != JOptionPane.YES_OPTION)
				return; // exit this method if they don't want to quit the game
		}
		
		// set game values to indicate end
		gameInProgress = false;
		progressBar.setBackground (Color.RED);
		health = 100;
		gameOver = false;

		// tell the panel to do the same thing
		this.spacePanel.endGame();
		
		// keep at least the main ship to avoid errors in mouse events
		spacePanel.addMainShip (new SpaceShip(300,300));
		
	} // end endGame
	

	/**
	 * this method is called to set up game values for the start of a new game 
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 */
	private void startGame()
	{
		// if game is already in progress then end it 
		if (gameInProgress)
		{
			// confirm they want to end the game in progress
			endGame();
			if (gameInProgress)
				return; // they don't want to end the current game
		}

		// set the game specific parameters to start
		gameInProgress = true;
		gameOver = false;
		health = 100;
		PirateScore.score = 0;
		this.asteroidsHit = 0;
		this.currency = 0;
		
		// reset all the treasures that have been captured
		for (SpaceTreasureType type : SpaceTreasureType.values())
			treasuresCaptured[type.ordinal()] = 0;

		// for a new game, start with a main ship and stations
		spacePanel.addMainShip (new SpaceShip(300,300));
		spacePanel.add (new SpaceStation(100,100));
		spacePanel.add (new WeighStation(-100,-100));

		// create a large asteroid based on the difficulty level
		for (int i = 0; i <= (difficulty.ordinal ( )*3); i++)
		{
			spacePanel.add (new LargeAsteroid((int)(Math.random ( )*1200)-600,
												(int)(Math.random ( )*1200)-600,
												5 + (int)(Math.random()*5),
												1,
												1));
		}
		spacePanel.add (new SpaceTreasure(300,150,SpaceTreasureType.URANIUM));
		progressBar.setBackground (Color.WHITE);

		this.spacePanel.startGame();
	}
	
	/**
	 * this method will create the menu bar for the main display    
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 */
	private void createMenuBar() 
	{
		// make menu bar
		JMenuBar menuBar = new JMenuBar();

		// create file menu to load, save, new, end
		JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem loadMenuItem = new JMenuItem("Load");
        loadMenuItem.setMnemonic(KeyEvent.VK_L);
        loadMenuItem.setToolTipText("Load Game");

        fileMenu.add(loadMenuItem);
        
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setToolTipText("Save Game");
        saveMenuItem.addActionListener((event) -> this.saveGame());

        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        
        JMenuItem newMenuItem = new JMenuItem("New Game");
        JMenuItem endMenuItem = new JMenuItem("End Game");
        newMenuItem.setMnemonic(KeyEvent.VK_N);
        newMenuItem.setToolTipText("Start a new game");

        fileMenu.add(newMenuItem);
 
        endMenuItem.setMnemonic(KeyEvent.VK_G);
        endMenuItem.setToolTipText("End current game");

        fileMenu.add(endMenuItem);
        fileMenu.addSeparator();
         
        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((event) -> System.exit(0));

        fileMenu.add(eMenuItem);
        menuBar.add(fileMenu);

        // create options menu with size of map and difficulty level
        JMenu optionMenu = new JMenu("Options");
        optionMenu.setMnemonic(KeyEvent.VK_O);

        ButtonGroup difGroup = new ButtonGroup();

        JRadioButtonMenuItem easyRMenuItem = new JRadioButtonMenuItem("Easy");
        easyRMenuItem.setMnemonic (KeyEvent.VK_E);
        optionMenu.add(easyRMenuItem);
        easyRMenuItem.setToolTipText("Set difficulty of game play to Easy");

        easyRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.difficulty = Difficulty.EASY;

            }
        });

        JRadioButtonMenuItem mediumRMenuItem = new JRadioButtonMenuItem("Normal");
        mediumRMenuItem.setMnemonic (KeyEvent.VK_N);
        mediumRMenuItem.setSelected(true);
        difficulty = Difficulty.MEDIUM;
        optionMenu.add(mediumRMenuItem);
        mediumRMenuItem.setToolTipText("Set difficulty of game play to Normal");

        mediumRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.difficulty = Difficulty.MEDIUM;
            }
        });

        JRadioButtonMenuItem hardRMenuItem = new JRadioButtonMenuItem("Hard");
        hardRMenuItem.setMnemonic (KeyEvent.VK_H);
        optionMenu.add(hardRMenuItem);
        hardRMenuItem.setToolTipText("Set difficulty of game play to Hard");

        hardRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.difficulty = Difficulty.HARD;
            }
        });

        JRadioButtonMenuItem reallyHardRMenuItem = new JRadioButtonMenuItem("Really Hard");
        reallyHardRMenuItem.setMnemonic (KeyEvent.VK_R);
        optionMenu.add(reallyHardRMenuItem);
        reallyHardRMenuItem.setToolTipText("Set difficulty of game play to Really Hard");

        reallyHardRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.difficulty = Difficulty.REALLY_HARD;
            }
        });

        // add all the difficulty buttons to the same group
        difGroup.add(easyRMenuItem);
        difGroup.add(mediumRMenuItem);
        difGroup.add(hardRMenuItem);
        difGroup.add(reallyHardRMenuItem);
        
        optionMenu.addSeparator();

        // now make the map size options
        ButtonGroup sizeGroup = new ButtonGroup();

        JRadioButtonMenuItem smallRMenuItem = new JRadioButtonMenuItem("Small Map");
        smallRMenuItem.setMnemonic (KeyEvent.VK_S);
        optionMenu.add(smallRMenuItem);

        smallRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.spacePanel.setZoomFactor(1);
            }
        });

        JRadioButtonMenuItem medRMenuItem = new JRadioButtonMenuItem("Medium Map");
        mediumRMenuItem.setMnemonic (KeyEvent.VK_M);
        optionMenu.add(medRMenuItem);

        medRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.spacePanel.setZoomFactor(0.5);
            	}
        });

        JRadioButtonMenuItem largeRMenuItem = new JRadioButtonMenuItem("Large Map");
        largeRMenuItem.setMnemonic (KeyEvent.VK_L);
        largeRMenuItem.setSelected(true);
        optionMenu.add(largeRMenuItem);

        largeRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.spacePanel.setZoomFactor(0.25);
    			}
        });

        // put all the map size options into a group
        sizeGroup.add(smallRMenuItem);
        sizeGroup.add(medRMenuItem);
        sizeGroup.add(largeRMenuItem);

        optionMenu.addSeparator();
        
        loadMenuItem.addActionListener((event) -> 
        	{
        		// loadGame returns true or false but we don't care at this point
        		// because we have to set the panel items based on the current 
        		// state. LoadGame should leave the game in a valid state so
        		// just reflect that in the panel items.
        		this.loadGame();
        		
    			easyRMenuItem.setEnabled(false);
    			mediumRMenuItem.setEnabled(false);
    			hardRMenuItem.setEnabled(false);
    			reallyHardRMenuItem.setEnabled(false);
    			smallRMenuItem.setEnabled(false);
    			medRMenuItem.setEnabled(false);
    			largeRMenuItem.setEnabled(false);
    			if (this.difficulty == Difficulty.REALLY_HARD) 
    	    		reallyHardRMenuItem.setSelected (true);
    			else if (this.difficulty == Difficulty.HARD) 
    				hardRMenuItem.setSelected (true);
    			else if (this.difficulty == Difficulty.MEDIUM) 
    				mediumRMenuItem.setSelected (true);
    			else  
    				easyRMenuItem.setSelected (true);

    			if (this.spacePanel.getZoomFactor ( ) < 0.5) 
    			{
    				largeRMenuItem.setEnabled(true);			
    			}
    			else if (this.spacePanel.getZoomFactor ( ) < 1.0) 
    			{
    				medRMenuItem.setEnabled(true);			
    			}
    			else  
    			{
    				smallRMenuItem.setEnabled(true);			
    			}
    			
    			this.progressBar.setBackground (Color.WHITE);
        		
        	});

 
        menuBar.add(optionMenu);
        
        newMenuItem.addActionListener((event) -> {
			this.startGame ( );
			easyRMenuItem.setEnabled(false);
			mediumRMenuItem.setEnabled(false);
			hardRMenuItem.setEnabled(false);
			reallyHardRMenuItem.setEnabled(false);
			smallRMenuItem.setEnabled(false);
			medRMenuItem.setEnabled(false);
			largeRMenuItem.setEnabled(false);
			
		});
        endMenuItem.addActionListener((event) -> 
		{
			this.endGame ( );
			easyRMenuItem.setEnabled(true);
			mediumRMenuItem.setEnabled(true);
			hardRMenuItem.setEnabled(true);
			reallyHardRMenuItem.setEnabled(true);
			smallRMenuItem.setEnabled(true);
			medRMenuItem.setEnabled(true);
			largeRMenuItem.setEnabled(true);
		});

        
        JMenuItem helpMenu = new JMenuItem("How to Play");
        helpMenu.setMnemonic (KeyEvent.VK_H);
        helpMenu.addActionListener( (event) -> new PirateRulesPopup(this) );
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
	
	/**
	 * save the game specific values          
	 *
	 * <hr>
	 * Date created: Apr 12, 2020
	 *
	 * <hr>
	 * @param out
	 * @return
	 */
	public String saveGame(ObjectOutputStream out) 
    {
    	// returns a string containing any error messages generated while
    	// saving the game
		
		String errors = null;
		try 
		{
	        out.writeInt(health);
	        out.writeBoolean (gameInProgress);
	        out.writeInt (difficulty.ordinal());
	        out.writeInt (PirateScore.score);
	        out.writeInt (asteroidsHit);
	        
	        int numTreasures = treasuresCaptured.length;
	        out.writeInt (numTreasures); // save size of array so it can be initialized on load	        
	        
	        for(int tc: treasuresCaptured)
	        	out.writeInt (tc);
	        
	        out.writeInt (currency);
	        out.writeBoolean(lastOrbitStatus);

		}
		catch (Exception e)
		{
			errors = new String("Errors while saving frame data.");
		}
		
		String panelErrors = this.spacePanel.saveGame (out);

		if (panelErrors != null)
		{
			if (errors == null)
				errors = panelErrors;
			else 
				errors += panelErrors;
		}
		
		return errors;
    }
	
	
    /**
     * this method loads game specific values from a stream         
     *
     * <hr>
     * Date created: Apr 12, 2020
     *
     * <hr>
     * @param in
     * @return
     */
    public String loadGame(ObjectInputStream in) 
    {
    	// returns a string containing any error messages generated while
    	// saving the game
		String errors = null;

		try
		{
			// start pulling in game specific values
			// load health, gameInProgress, difficulty, etc
			health = in.readInt();
			gameInProgress = in.readBoolean ();
	        difficulty = Difficulty.values ( )[in.readInt ()];
	        PirateScore.score = in.readInt ( );
	        asteroidsHit = in.readInt ( );
	        
	        int numTreasures = in.readInt ( );
	        treasuresCaptured = new int[numTreasures];
	        for(int i = 0; i<numTreasures; i++) 
	        	treasuresCaptured[i] = in.readInt ( );
	        
	       
	        currency = in.readInt();
	        lastOrbitStatus = in.readBoolean ( );
		}
		catch (IOException e)
		{
			errors = new String ("Error while loading frame data.");
		}
		
		String panelErrors = this.spacePanel.loadGame (in);
		
		progressBar.setBackground (Color.WHITE);

		if (panelErrors != null)
		{
			if (errors == null)
				errors = panelErrors;
			else 
				errors += panelErrors;
		}
		
		// failure to read new game in may leave program in a disjointed 
		// state so best to just reset in this scenario.
		if (errors != null)
		{			
			health = 100;
			gameOver = true;
			endGame();
		}
		
		gameOver = false;
		
		return errors;    
	}

	/**
	 * this method is called when a new treasure is captured         
	 *
	 * <hr>
	 * Date created: Mar 31, 2020 
	 *
	 * <hr>
	 * @param treasure
	 * @see SpacePirates.TreasureListener#treasureCaptured(SpacePirates.SpaceTreasure)
	 */
	@Override
	public void treasureCaptured (SpaceTreasure treasure)
	{
		this.treasuresCaptured[treasure.getTreasureType ( ).ordinal()] += 1;
		currency += SpaceTreasure.value[treasure.getTreasureType ( ).ordinal()];
		PirateScore.score += 100;
		
	}

	/**
	 * ObitListener method that gets called if the orbit 
	 * status of the main space ship changes.          
	 *
	 * <hr>
	 * Date created: Mar 31, 2020 
	 *
	 * <hr>
	 * @param orbiting
	 * @see SpacePirates.OrbitListener#orbitChanged(boolean)
	 */
	@Override
	public void orbitChanged (boolean orbiting)
	{
		if (lastOrbitStatus == orbiting)
		{
			return;
		}

		lastOrbitStatus = orbiting;
		
		if (orbiting)
		{
			String userResponse ="";
			try
			{
				setGamePaused(true);
				PirateScore.score += 25; 
				String[] options = {"Buy Health", "Buy Ammo", "Trade Treasure", "Buy Weapon", "All Done"};
	        
				userResponse = (String) JOptionPane.showInputDialog (this, "You've docked with a station! \n" +
								"You have " + this.currency + " space credits, " +
								health + "% health.\nWhat would you like to do" 
								, "Docking in Progess", JOptionPane.PLAIN_MESSAGE, null, 
								options, options[0]);
				
				if(userResponse.equals("Buy Health"))
				{
					buyHealth();
					setGamePaused(false);
				}
				else if(userResponse.equals("Buy Ammo"))
				{
					buyAmmo();
					setGamePaused(false);
				}
				else if(userResponse.equals("Trade Treasure"))
				{
					tradeTreasure();
					setGamePaused(false);
				}
				else if(userResponse.equals("Buy Weapon"))
				{
					buyWeapon();
					setGamePaused(false);
				}
				else
				
				setGamePaused(false);
			}
			catch(NullPointerException NFE)
			{
				setGamePaused(false);
			}
			
		}
	}
	
	
	/**
	 * Gives the user the option to buy health with Space Credit        
	 *
	 * <hr>
	 * Date created: Apr 15, 2020
	 *
	 * <hr>
	 */
	public void buyHealth()
	{
		int userResponse = 5;
		String[] options = {"5 HP for 10 Credits","Done"};
		try
		{
			userResponse = JOptionPane.showOptionDialog (this, "Current Health: "+ this.health + 
				"\nSpace Credits: " + currency, "Buy Health", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE, null, options,Integer.valueOf(2));
		}
		catch(NullPointerException NFE)
		{
			
		}
		switch(userResponse)
		{
			case 0:
				if(currency<10)
					JOptionPane.showMessageDialog (this, "You do not have enough Space Credits");
				else if(this.health==100)
				{
					JOptionPane.showMessageDialog (this, "Health is already maxed");
				}
				else
				{
					currency-=10;
					if(this.health>95)
					{
						SpacePanel.mainShip ( ).setHealth (health+= (100-this.health));;
						this.health = SpacePanel.mainShip ( ).getHealth ( );
					}
					else
					{
						SpacePanel.mainShip ( ).setHealth (health+= 5);;
						this.health = SpacePanel.mainShip ( ).getHealth ( );
					}		
				}
				buyHealth();
				break;
			default:
				break;	
		}
		
	}
	
	/**
	 * User can trade treasure accumulated for Space Credits        
	 *
	 * <hr>
	 * Date created: Apr 15, 2020
	 *
	 * <hr>
	 */
	public void tradeTreasure()
	{
		JOptionPane.showMessageDialog (this, "bye");
	}
	
	
	/**
	 * User can buy more ammo for their current weapon         
	 *
	 * <hr>
	 * Date created: Apr 15, 2020
	 *
	 * <hr>
	 */
	public void buyAmmo()
	{
		int userResponse = 5;
		String[] options = {"10 rounds for 5 Credits","Done"};
		try
		{
			userResponse = JOptionPane.showOptionDialog (this, "Current Weapon: "+ SpacePanel.mainShip ( ).getWeapon ( ) + "\nAmmo Rounds Left: " + 
							SpacePanel.mainShip ( ).getWeaponCount ( ) + "\nSpace Credits: " + currency, "Buy Ammunition", JOptionPane.PLAIN_MESSAGE,
			JOptionPane.PLAIN_MESSAGE, null, options,Integer.valueOf(2));
		}
		catch(NullPointerException NFE)
		{
			
		}
		switch(userResponse)
		{
			case 0:
				if(currency<5)
					JOptionPane.showMessageDialog (this, "You do not have enough Space Credits");
				else
				{
					currency-=5;
					SpacePanel.mainShip ( ).setWeaponCount (SpacePanel.mainShip ( ).getWeaponCount()+10);
				}
				buyAmmo();
				break;
				
			default:
				break;	
		}
	}
	
	/**
	 * Users can buy new weapons with Space Credits         
	 *
	 * <hr>
	 * Date created: Apr 15, 2020
	 *
	 * <hr>
	 */
	public void buyWeapon()
	{
		
	}
} // end pirateFrame
