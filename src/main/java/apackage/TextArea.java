package apackage;

import Menu.Menu;
import battlePackage.Battle;
import battlePackage.EnemyList;
import battlePackage.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TextArea extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextArea log;
	private JScrollPane logScrollPane;
	private JPanel buttonPanel;
	private Player player;

	private String inputString;
	private JTextField input;
	private JButton enterButton, menuButton;
	private Logger logger = Logger.getLogger("Error Logger");

	private int stepCount = 0;
	private Building building = new Building("CASH");

	private EnemyList e = new EnemyList();

	public TextArea(Player p, EnemyList e) {
		// Text Area Set Up
		super(new BorderLayout());
		this.player = p;
		this.e = e;
		this.log = new JTextArea(700, 700);
		this.log.setOpaque(true);
		this.log.setMargin(new Insets(5, 5, 5, 5));
		this.log.setEditable(false);
		this.log.setBackground(Color.BLACK);
		this.log.setForeground(Color.GREEN);
		this.logScrollPane = new JScrollPane(this.log);
		this.logScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.logScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		// Add scroll pane and tell border layout
		this.add(this.logScrollPane, BorderLayout.CENTER);

		// Make a buttonListener
		ButtonListener buttonListener = new ButtonListener();

		// Create enterButton
		this.enterButton = new JButton("ENTER");
		this.enterButton.setName("ENTER");
		this.enterButton.addActionListener(this);
		this.enterButton.setBackground(Color.GREEN);
		this.enterButton.setForeground(Color.black);
		this.enterButton.setOpaque(true);
		this.enterButton.setBorderPainted(false);
		this.enterButton.setEnabled(true);

		// Create menuButton
		this.menuButton = new JButton("MENU");
		this.menuButton.setName("MENU");
		this.menuButton.addActionListener(this);
		this.menuButton.setBackground(Color.GREEN);
		this.menuButton.setForeground(Color.black);
		this.menuButton.setOpaque(true);
		this.menuButton.setBorderPainted(false);
		this.menuButton.setEnabled(true);
		//this.menuButton.setActionCommand("Menu");

		// Make buttons have a buttonListener
		enterButton.addActionListener(buttonListener);
		menuButton.addActionListener(buttonListener);

		// Make input text field
		input = new JTextField(20);
		input.setActionCommand("ENTER");
		input.addActionListener(buttonListener);

		// Make new buttonPanel
		this.buttonPanel = new JPanel();

		// Add input field and enter button to buttonPanel
		this.buttonPanel.add(this.input);
		this.buttonPanel.add(this.enterButton);
		this.buttonPanel.add(this.menuButton);
		this.add(this.buttonPanel, BorderLayout.PAGE_END);

		// Print the menu of options to travel to
		log.append("\nWhere would you like to go?\n");
		log.append("\n+------------------------------+\n");
		building.printBuildingMenu(log);
		building.buildingList.clear();
		log.append("Please capitalize the first letter\n" + "+------------------------------+\n");

		this.setVisible(true);
		// Create and Show Map
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}

	
	@Override
	public void actionPerformed(ActionEvent event) {
	
	}
	

	public void createAndShowGUI() {
		// Frame Set Up
		this.frame = new JFrame("Map");
		this.frame.setLocationRelativeTo(null);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setPreferredSize(new Dimension(800, 700));
		this.frame.setResizable(false);
		this.setOpaque(true);
		this.frame.add(this);

		// Show Frame
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
	}

	public class ButtonListener implements ActionListener {
		public void actionPerformed(final ActionEvent ev) {
			if (ev.getSource() == menuButton) {

				// Create Menu screen
				Menu menu = new Menu(e, player);
				menu.createMenuScreen();
			}
			
			if(ev.getSource() == enterButton) {
				// Get input from input box
				inputString = input.getText();
				input.setText("");
				input.requestFocus();
	
				if (!inputString.equals("Y") && !inputString.equals("y")) {
	
					// Change buildings
					if (building.isValid(inputString)) {
						// Echo print
						log.append("You selected " + inputString);
	
						building.setID(inputString);
	
						// Print current building
						log.append("\nYou are now in " + building.getID() + "\n\n");
	
						stepCount++;
	
						// Show building description
						log.append(building.buildingDesc() + "\n");
						//building.printBuildingMenu(log);
					} else {
						log.append("Sorry, " + inputString + " is not an option.\n");
					}
	
					log.append("\nWhere would you like to go?\n");
					log.append("\n+------------------------------+\n");
					building.printBuildingMenu(log);
					building.buildingList.clear();
					log.append("Please capitalize the first letter\n" + "+------------------------------+\n");
	
					// Show boss battles if available
					if (building.isValid(inputString) && building.getID().equals("CASH")
							&& !e.getBossList().get(1).getDefeated()) {
						log.append("You have entered the domain of Dr. Cerny...\n"
								+ "Would you like to battle him? Enter Y to battle. \n");
						log.append("If no, enter where you would like to go.\n");
					} else if (building.isValid(inputString) && building.getID().equals("BSB")
							&& !e.getBossList().get(2).getDefeated()) {
						log.append("You have entered the domain of Prof. Fry...\n"
								+ "Would you like to battle her? Enter Y to battle. \n");
						log.append("If no, enter where you would like to go.\n");
					} else if (building.isValid(inputString) && building.getID().equals("TEAL")
							&& !e.getBossList().get(0).getDefeated()) {
						log.append("You have entered the domain of Dr. Booth...\n"
								+ "Would you like to battle him? Enter Y to battle. \n");
						log.append("If no, enter where you would like to go.\n");
					}
	
				} else {
					// Have a boss battle!
					log.append("\nBOSS BATTLE!!!\n");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Battle.bossBattle(player, building.getID(), e);
					log.append("\n+------------------------------+\n");
					building.printBuildingMenu(log);
					building.buildingList.clear();
					log.append("Please capitalize the first letter\n" + "+------------------------------+\n");
					/*building.printBuildingMenu(log);
					log.append("\nWhere would you like to go?\n");*/
				}
	
				// Show an encounter if there is one
				String encounter = null;
				if (building.isValid(inputString)) {
					try {
						encounter = Encounter.randomEncounter(building.getID());
						if (encounter.length() > 0) {
							log.append("\nENCOUNTER!\n");
							log.append(encounter + "\n\n");
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
	
				// Have a battle every three steps
				if (stepCount == 3) {
					log.append("RANDOM BATTLE!!!\n");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						logger.log(Level.SEVERE, "Sleep Interrupted!");
					}
					Battle.battle(player);
					stepCount = 0;
				}
				
				if (e.bossesDefeated()) {
					System.out.println("\n\n" + 
								"*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*\n" + 
								"*                    YOU                         *\n" + 
								"*                    WIN                         *\n" + 
								"*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*\n");
					System.out.println("\nThank you for playing BearQuest! We hope that you have enjoyed \n" + 
								"our game, please email feedback to Cameron_Cole@baylor.edu\n" + 
								"Thanks again, and play again soon!\n");
					
					System.exit(0);
				}
			}
		}
	}

}
