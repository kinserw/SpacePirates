package SpacePirates;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	private int difficulty = 3;					// difficulty setting for the game
	private int health = 100; 					// tracks health of main ship
	private int score = 0;						// overall score in the game
	private int asteroidsHit = 0;				// count of how man weapons hit asteroids
	private int[] treasuresCaptured = new int[SpaceTreasureType.SPACE_CREDITS.ordinal()+1];
												// count of how man treasures were captured
	private int currency = 0;					// total currency available. Can trade in treasure for currency
	private boolean lastOrbitStatus = false;    // orbit status used to determine if a change in status happened

	
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
		// have to have a main ship even if a game isn't in health.
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
		
		shapeMyFrame();

		Thread myThread = new Thread(this);
		
		if (this.firstTimeThru)
		{
			this.firstTimeThru = false;
			String[] options = {"New Game", "Load Game","Pick Options"};
			int answer = JOptionPane.showOptionDialog (null, 
							"Welcome to SpacePirates!\n" +
						    "\nWould you like to start a new game or load an existing game?", 
				"Welcome", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, 
				options, 0);
			if (answer == 0)
				startGame();
			else if (answer == 1)
				loadGame();
				
		}

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
		setGamePaused(true);
		
		
		String[] difficultyString = {"?","?", "?", "Easy", "?", "Medium", "?", "?", "Hard", "?", "Really Hard"};
		String treasures = "";
		for (SpaceTreasureType treasureType : SpaceTreasureType.values ( ))
			treasures += "\t\t                     " + treasureType + " = " + treasuresCaptured[treasureType.ordinal ( )] + "\n";

		JOptionPane.showMessageDialog (this, 
			"Game is currently" + (gameInProgress ? " " : " not " ) + "in progress.\n" +
			"Difficulty set at: " + difficultyString[difficulty] + ". \n" +
			"Health remaining: " + health + "%\n"+
			"Wealth accumulated: " + currency + "\n" +
			"Score " + score + "\n" +
					"\t\t Asteroids hit: " + asteroidsHit + "\n" +
					"\t\t Treasures captured: "+ "\n" + treasures
			, "Space Pirates Stats", JOptionPane.INFORMATION_MESSAGE, null);

		setGamePaused(false);
	}
	
	public void setGamePaused(boolean paused)
	{
		gamePaused = paused;
		this.spacePanel.setGamePaused(paused);
	}

	@Override
	public void run() {
		while (true)
		{
			if (spacePanel.mainShip ( ) != null)
				health = spacePanel.mainShip().getHealth();
			
			progressBar.setValue( (int)(this.health));

			if (gameInProgress)
			{
				repaint();		
				this.myBtnPanel.updateScore (score);
				
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
			
			//for (int i = 0; i < 1000000; i++);'
			synchronized(this)
			{
				try {
					this.wait(50);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		}
		
	}

	private void saveGame()
	{
		JFileChooser fileChooser = new JFileChooser("src");
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "SpacePirate files", "spf");
	    fileChooser.setFileFilter(filter);	
	    fileChooser.addChoosableFileFilter(filter);
	    fileChooser.setAcceptAllFileFilterUsed(false);
	    fileChooser.setSelectedFile(new File("*.spf"));
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
		  File file = fileChooser.getSelectedFile();
		  try 
		  {
			  if (file.exists())
				  file.delete();
			  file.createNewFile();
			  
			  if (file.canWrite())
			  {
				  String heading = "SpacePirate Game";
				  FileOutputStream fileOut = new FileOutputStream(file); 
		          ObjectOutputStream out = new ObjectOutputStream(fileOut); 
		          
		          out.writeObject(heading); 
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

	private boolean loadGame()
	{
		JFileChooser fileChooser = new JFileChooser("src");
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("SpacePirate files", "spf");
	    fileChooser.setFileFilter(filter);
	    fileChooser.setDialogTitle ("Load Game");
	    fileChooser.setApproveButtonToolTipText ("Select the SpacePirate saved game to load.");
	    boolean status = true;
	    
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			// load from file
		  	try 
			{
				if (file.exists() && file.canRead())
				{
					  String heading = "SpacePirate Game";
					  FileInputStream fileIn = new FileInputStream(file); 
			          ObjectInputStream in = new ObjectInputStream(fileIn); 
			              
					
			          // Method for serialization of object 
			          String str = (String)in.readObject(); 
			          if (!str.equals(heading))
			        	  System.out.println("this is not a SpacePirate game file");
			          else {
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
		

	private void endGame()
	{

		if (!gameOver) 
		{
			int answer = JOptionPane.showConfirmDialog (this, "Are you sure you want to end this game?");
			if (answer != JOptionPane.YES_OPTION)
				return;
		}
		
		gameInProgress = false;
		progressBar.setBackground (Color.RED);
		health = 100;
		gameOver = false;

		this.spacePanel.endGame();
		
		// keep at least the main ship to avoid errors in mouse events
		spacePanel.addMainShip (new SpaceShip(300,300));
		
	}
	

	private void startGame()
	{
		if (gameInProgress)
		{
			// confirm they want to end the game in progress
			endGame();
			if (gameInProgress)
				return; // they don't want to end the current game
		}

		// TODO: do start game stuff here
		gameInProgress = true;
		gameOver = false;
		health = 100;
		this.score = 0;
		this.asteroidsHit = 0;
		this.currency = 0;
		for (SpaceTreasureType type : SpaceTreasureType.values())
			treasuresCaptured[type.ordinal()] = 0;

		spacePanel.addMainShip (new SpaceShip(300,300));
		spacePanel.add (new SpaceStation(100,100));
		spacePanel.add (new WeighStation(-100,-100));
		for (int i = 0; i <= difficulty; i++)
		{
			spacePanel.add (new LargeAsteroid((int)(Math.random ( )*1200)-600,
												(int)(Math.random ( )*1200)-600,
												6,
												1,
												1));
		}
		spacePanel.add (new SpaceTreasure(300,150,SpaceTreasureType.URANIUM));
		progressBar.setBackground (Color.WHITE);

		this.spacePanel.startGame();
	}
	
	private void createMenuBar() 
	{

		JMenuBar menuBar = new JMenuBar();

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
        
        JMenu optionMenu = new JMenu("Options");
        
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
        difficulty = 5;
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
            	this.spacePanel.setZoomFactor(1);
            }
        });

        JRadioButtonMenuItem medRMenuItem = new JRadioButtonMenuItem("Medium Map");
        optionMenu.add(medRMenuItem);

        medRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.spacePanel.setZoomFactor(0.5);
            	}
        });

        JRadioButtonMenuItem largeRMenuItem = new JRadioButtonMenuItem("Large Map");
        largeRMenuItem.setSelected(true);
        optionMenu.add(largeRMenuItem);

        largeRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.spacePanel.setZoomFactor(0.25);
    			}
        });

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
    			switch (this.difficulty) 
    			{
    				case 10 : // really hard
    	    			reallyHardRMenuItem.setSelected (true);
    					break;
    				case 8 : // hard
    	    			hardRMenuItem.setSelected (true);
    					break;
    				case 5 : // medium
    	    			mediumRMenuItem.setSelected (true);
    					break;
    				case 3 : // easy, fall through to default
    				default :
    	    			easyRMenuItem.setSelected (true);
    					break;
    						
    			}
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
        helpMenu.addActionListener( (event) -> JOptionPane.showMessageDialog(this, 
    		"<html><h1>Space Pirates</h1><p style='width:400'>This is a strategy game in which the player searches the galaxy for treasure. The more treasure found, the more upgrades you can buy. The more upgrades you get, the more treasure you can find. The fun is endless!!<br>"
    		+ "<br>You pilot your ship around space. Destroying asteroids, other ships and space debris will potentially release valuable treasure. Flying over the treasure (at a reasonable speed) allows you to add it to your payload.<br>" +
    						"<br>A status bar at the top of the screen shows your current payload contents and equipment status. A health bar at the bottom of the screen helps you know when you need to seek repairs.<br>" + 
    						"<br>Docking with a space station or weigh station will allow you to bank your payload, buy repairs and upgrades. While docked you can't fire at anything. On the plus side, nothing can harm you <br>" +
    						"either since the stations are guarded by force fields.<br><br>Moving: <br><\t>left click to accelerate" +
							"<br><\t>right double click to fire your weapon(s)" + 
							"<br><\t>move the mouse in the direction you want to travel"        		));
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
	
	public String saveGame(ObjectOutputStream out) 
    {
    	// returns a string containing any error messages generated while
    	// saving the game
		
		String errors = null;
		try 
		{
	        out.writeInt(health);
	        out.writeBoolean (gameInProgress);
	        out.writeInt (difficulty);
	        out.writeInt (score);
	        out.writeInt (asteroidsHit);
	        
	        int numTreasures = 0;
	        for(@SuppressWarnings ("unused") int tc:treasuresCaptured)
	        	numTreasures ++;
	        
	        out.writeInt (numTreasures);
	        
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
    public String loadGame(ObjectInputStream in) 
    {
    	// returns a string containing any error messages generated while
    	// saving the game
		String errors = null;

		try
		{
			health = in.readInt();
			gameInProgress = in.readBoolean ();
	        difficulty = in.readInt ();
	        score = in.readInt ( );
	        asteroidsHit = in.readInt ( );
	        
	        int numTreasures = in.readInt ( );
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
		// load health, gameInProgress, difficulty, etc

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
	 * Enter method description here         
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
		score += 50;
		
	}

	/**
	 * Enter method description here         
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
			return;
		lastOrbitStatus = orbiting;
		
		if (orbiting)
		{
			setGamePaused(true);
			score += 100; 
	        Object[] options = {"Buy Health", "Turn Treasure", "All Done"};
	
			Object answer = JOptionPane.showInputDialog (this, "You've docked with a station! \n" +
				"You have " + this.currency + " space credits, " +
				health + "% health.\nWhat would you like to do" 
				, "Docking in Progess", JOptionPane.PLAIN_MESSAGE, null, 
				options, options[0]);
			setGamePaused(false);
		}
	}

}
