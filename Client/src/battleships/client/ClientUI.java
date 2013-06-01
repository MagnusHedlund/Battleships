/*
 * ClientUI.java
 * Version 1.0 (2013-05-30)
 */

package battleships.client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import battleships.message.*;
import battleships.game.*;

/**
 * Contains the graphical UI and all game related logic.
 * 
 * @author Fredrik Str�mbergsson
 */
public class ClientUI implements ActionListener
{
	// Deklarationer
	private Vector<Square> playerSquares = new Vector<Square>(100);		// Inneh�ller spelarens rutor
	private Vector<Square> enemySquares = new Vector<Square>(100);		// Inneh�ller fiendens rutor
	private Vector<Square> placeSquares = new Vector<Square>(100);		// Inneh�ller rutorna n�r man skapar sina fartyg
	private JFrame window = new JFrame();								// F�nstret
	private enum states { connect, lobby, buildnavy, game };			// Enum r�knare f�r "state"
	private int state = 0;												// Nuvarande state
	private JTextArea information = new JTextArea();					// Informations textf�ltet d�r h�ndelser visas
	private JButton connectButton = new JButton("Connect");				// Connectknappen i startf�nstret.
	private JTextField ipbox = new JTextField();						// IP nummer textbox i startf�nstret.
	private JTextField portbox = new JTextField();						// Portnummer textbox i startf�nstret.
	private JLabel connectionError = new JLabel("");					// label som visar fel i startf�nstret.
	private JButton addSubmarineButton = new JButton("Submarine (0/5)");		// F�r att l�gga till ub�tar
	private JButton addDestroyerButton = new JButton("Destroyer (0/3)");		// F�r att l�gga till j�gare
	private JButton addAircraftCarrierButton = new JButton("Aircraft Carrier (0/1)");	// F�r att l�gga till hangarfartyg
	private JButton readyButton = new JButton("READY");						// READY (f�rdig med utplacering)
	private JButton clearButton = new JButton("CLEAR");				// rensa f�ltet d�r man l�gger ut skepp
	private ShipPlacer placer = new ShipPlacer();					// Anv�nds n�r man s�tter ut skepp
	private Navy myNavy = new Navy(5, 3, 1);						// NAVY
	private JList<String> lobbyList = new JList<String>();			// Listan i LOBBY
	private JButton challengeButton = new JButton("Challenge!");	// "challenge" knapp - lobby
	private JButton refreshButton = new JButton("Refresh");			// "refresh" knapp - lobby
	private Vector<String> playerList = new Vector<String>();		// objekten i listan - lobby
	private String lobbySelected = "";								// vilket objekt som �r valt i listan - lobby
	private ClientNetwork cNetwork = new ClientNetwork();			// Wrapper f�r Socket
	private boolean waitingForChallenge = true;						// BOOL - f�r lobbymeddelanden
	private javax.swing.Timer t = null;								// Timer som uppdaterar n�tverket varje sekund
	private Map<String, Integer> lobbyContenders = new HashMap<String,Integer>();	// Lobbylistan - Fr�n server (i lobby)
	private boolean ConnectedToServer = false;	// Ansluten?
	private boolean myTurn = false;				// Min tur att anfalla?	
	private JLabel direction = new JLabel("?");	// Visar vems tur det �r, visuellt.
	private int myAttack = 0; 	// Den square man attackerade
	private boolean debug = false;	// P�/AV debug/consol meddelanden.
	
	/**
	 * Constructor
	 */
	ClientUI()
	{	
		// Action listener f�r timer som varje sekund lyssnar efter n�tverksmeddelanden
		ActionListener networkListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(state == states.lobby.ordinal())
					lobbyNetwork();
				else if(state == states.buildnavy.ordinal())
					navyNetwork();
				else if(state == states.game.ordinal())
					gameNetwork();
			}
		};
		
		// Starta timer och g� till connect-window
		t = new javax.swing.Timer(1000, networkListener);
		t.start();
		createConnectWindow();
	}
		
	/**
	 * WindowAdapter. Disconnects the client if connected.
	 */
	WindowAdapter exitListener = new WindowAdapter()
	{
		@Override
		public void windowClosing(WindowEvent e) {
			if(ConnectedToServer)
				cNetwork.disconnect();
		}
	};
	
	/**
	 * Creates the "Connect window"
	 */
	private void createConnectWindow()
	{
		// S�tt state till connect
		state = states.connect.ordinal();
		
		// F�nsteregenskaper
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(720, 520);
		window.setResizable(false);
		window.setTitle("Project Battleship");
		window.addWindowListener(exitListener);
		
		// Meny -- anv�nds ej.
		JMenuBar theMenu = new JMenuBar();
		JMenu menuTitle = new JMenu("Menu");
		JMenuItem menuExit = new JMenuItem("Exit");
		menuExit.setEnabled(false);
		window.setJMenuBar(theMenu);
		theMenu.add(menuTitle);
		menuTitle.add(menuExit);
	
		// Layout
		GridLayout layout = new GridLayout(4, 1);
		window.setLayout(layout);
				
		// Top panelen, existerar enbart som ett mellanrum.
		JPanel top = new JPanel();
		
		// Laddar in bilden (�verskriften) och l�gger den i headpanelen
		BufferedImage myPicture = null;
		JPanel head = new JPanel();
		
		try {
			myPicture = ImageIO.read(new File("images/bs.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Anv�nder text om bilden inte hittas
		if(myPicture == null)
		{
			JLabel picLabel = new JLabel("BATTLESHIP");
			picLabel.setFont(new Font("SansSerif", Font.BOLD, 50));
			head.add(picLabel);			
		}
		else	// Anv�nder bilden
		{
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			head.add(picLabel);			
		}
		
		// Text f�lt och deras label
		ipbox.setMaximumSize(new Dimension(100,20));
		ipbox.setMinimumSize(new Dimension(100,20));
		ipbox.setPreferredSize(new Dimension(100,20));
		portbox.setMaximumSize(new Dimension(40,20));
		portbox.setMinimumSize(new Dimension(40,20));
		portbox.setPreferredSize(new Dimension(40,20));
		connectButton.setMaximumSize(new Dimension(100,20));
		connectButton.setMinimumSize(new Dimension(100,20));
		connectButton.setPreferredSize(new Dimension(100,20));		
		JLabel ipboxlabel = new JLabel("Server ip:");
		JLabel portboxlabel = new JLabel("Port:");
		connectButton.addActionListener(this);

		// S�tt default v�rden f�r textrutorna
		ipbox.setText("127.0.0.1");
		portbox.setText("5168");
		
		// text panelen, inneh�ller textf�lten osv
		JPanel textpanel = new JPanel();
		textpanel.add(ipboxlabel);
		textpanel.add(ipbox);
		textpanel.add(portboxlabel);
		textpanel.add(portbox);	
		textpanel.add(connectButton);	
		
		// botten panel
		JPanel bottom = new JPanel();
		connectionError.setFont(new Font("SansSerif", Font.BOLD, 24));
		bottom.add(connectionError);
	
		// L�gg till i layouten
		window.add(top);
		window.add(head);
		window.add(textpanel);
		window.add(bottom);
				
		// Visa f�nster
		window.validate();
		window.setVisible(true);
	}
	
	/**
	 * Creates the "Lobby window"
	 */
	private void createLobbyWindow()
	{
		// Rensa f�nstret p� f�reg�ende komponenter
		window.getContentPane().removeAll();
		
		// S�tt state till lobby
		state = states.lobby.ordinal();
				
		// GridBagLayout
		GridBagLayout theLayout = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		window.setLayout(theLayout);
		
		// S�tt storlek p� lobbylistan
		lobbyList.setMaximumSize(new Dimension(700,300));
		lobbyList.setMinimumSize(new Dimension(700,300));
		lobbyList.setPreferredSize(new Dimension(700,300));
		lobbyList.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		lobbyList.setFont(new Font("SansSerif", Font.BOLD, 18));
		
		// �verskift
		JLabel lobbyText = new JLabel("Select and challenge your enemy!");
		lobbyText.setFont(new Font("SansSerif", Font.BOLD, 24));
		
		// L�gg i panel
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension(700,50));
		panel.setMinimumSize(new Dimension(700,50));
		panel.setPreferredSize(new Dimension(700,50));
		panel.add(lobbyText);
		
		// S�tt storlek p� knappar...
		challengeButton.setMaximumSize(new Dimension(150,30));
		challengeButton.setMinimumSize(new Dimension(150,30));
		challengeButton.setPreferredSize(new Dimension(150,30));
		challengeButton.addActionListener(this);
		
		refreshButton.setMaximumSize(new Dimension(150,30));
		refreshButton.setMinimumSize(new Dimension(150,30));
		refreshButton.setPreferredSize(new Dimension(150,30));
		refreshButton.addActionListener(this);
		
		// L�gg button i en panel
		JPanel panel2 = new JPanel();
		panel2.setMaximumSize(new Dimension(700,50));
		panel2.setMinimumSize(new Dimension(700,50));
		panel2.setPreferredSize(new Dimension(700,50));
		panel2.add(challengeButton);
		panel2.add(refreshButton);

		// H�r l�ggs komponenterna in i GridBagLayouten	
		con.gridx = 1;
		con.gridy = 0;
		theLayout.setConstraints(panel, con);
		window.add(panel);
		
		con.gridx = 1;
		con.gridy = 1;
		theLayout.setConstraints(lobbyList, con);
		window.add(lobbyList);
		
		con.gridx = 1;
		con.gridy = 2;
		theLayout.setConstraints(panel2, con);
		window.add(panel2);		

		// L�gg till servern i spelarlistan per default
		playerList.add("Server");
		lobbyList.setListData(playerList);
		
		// Lyssnare som h�mtar v�rdet p� den sak i listan man klickat p�.
		lobbyList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
            	lobbySelected = (String) lobbyList.getSelectedValue();
            	challengeButton.setText("Challenge: " + lobbySelected);
            }
        });
		
		// Visa f�nster
		window.validate();
		window.repaint();
		window.setVisible(true);
		
		if(debug)
			System.err.println("Created Lobby Window");
		
		// Skicka namn
		NameMessage msg = new NameMessage();
		msg.setName("IAmAPlayerName");
		cNetwork.sendMessage(msg);
		
		// Uppdatera lobbyn
		refreshLobby();
	}
	
	/**
	 * Listens for messages while in Lobby
	 */
	private void lobbyNetwork()
	{
		Message lobbyUpdate = cNetwork.getMessage();
		
		if(lobbyUpdate != null)
			if(lobbyUpdate.getType().equals("ActivePlayersMessage")) 	// F� en lista av spelare
				handleActivePlayerMessage(lobbyUpdate);
			else if(lobbyUpdate.getType().equals("ChallengeMessage")) 	// Challenge
				handleChallengeMessage(lobbyUpdate);
	}
	
	/**
	 * Listens for messages while in createNavy
	 */
	private void navyNetwork()
	{
		Message navyUpdate = cNetwork.getMessage();
			
		if(navyUpdate != null)
			if(navyUpdate.getType().equals("ValidationMessage"))	// Korrekt navy eller inte?
				handleValidationMessage(navyUpdate);
	}
	
	/**
	 * Listens for messages while in Game
	 */
	private void gameNetwork()
	{
		Message gameUpdate = cNetwork.getMessage();
			
		if(gameUpdate != null) {
			if(gameUpdate.getType().equals("NavyMessage"))			// Uppdatering av Navy och grantTurn
				handleNavyMessage(gameUpdate);
			else if(gameUpdate.getType().equals("HitMessage"))		// Tr�ff eller Miss?
				handleHitMessage(gameUpdate);
			else if(gameUpdate.getType().equals("FinishedMessage"))	// Game Over
				handleFinishedMessage(gameUpdate);
		}
	}
	
	/**
	 * Handle this type of message
	 * @param	msg		The Message
	 */	
	private void handleActivePlayerMessage(Message msg) 
	{
		ActivePlayersMessage playerL = (ActivePlayersMessage) msg;
		lobbyContenders = playerL.getContenders();
		if(debug)
			System.err.println("Received: ActivePlayersMessage");
		
		// Uppdatera lobbylistan
		playerList.clear();
		playerList.add("Server");
		for (int value : lobbyContenders.values()) {
			playerList.add("Player " + Integer.toString(value));
		}
		lobbyList.setListData(playerList);		
	}
	
	/**
	 * Handle this type of message
	 * @param	msg		The Message
	 */	
	private void handleChallengeMessage(Message msg) 
	{
		ChallengeMessage challenge = (ChallengeMessage) msg;
		if(debug)
			System.err.println("Received: ChallengeMessage");
						
		// Avg�r om man v�ntar p� ett svar p� skickad challenge, eller v�ntar p� en challenge.
		if(waitingForChallenge) {
			triggerChallenge(Integer.toString(challenge.getOpponentID()));
		}
		else {
			// Accept meddelande?
			if(challenge.getAccept())
				createNavyWindow();
			else
				waitingForChallenge = true;		// Deny message
		}		
	}
	
	/**
	 * Handle this type of message
	 * @param	msg		The Message
	 */	
	private void handleValidationMessage(Message msg) 
	{
		ValidationMessage valid = (ValidationMessage) msg;
		if(debug)
			System.err.println("Received: ValidationMessage");
		
		// Korrekt utplacerad Navy?
		if(valid.getMessage()) {
			createGameWindow();		// Ja - Spela
			setPlayerNavy();		// Ja - S�tt Navy till spelplanen grafiskt
		}
		else {						// Nej - G�r om
			placer.Reset();
			resetSquares();
			resetPlacementbuttons();
			JOptionPane.showMessageDialog(window,
				    "The placement of your navy was invalid. \n" +
				    "Replace and try again.",
				    "Invalid Navy",
				    JOptionPane.ERROR_MESSAGE);
		}		
	}
	
	/**
	 * Handle this type of message
	 * @param	msg		The Message
	 */	
	private void handleNavyMessage(Message msg) 
	{
		// Uppdatera Navy
		NavyMessage navy = (NavyMessage) msg;
		if(debug)
			System.err.println("Received: NavyMessage");
		myNavy = navy.getNavy();
		updateMyNavy();

		// Vems tur?
		myTurn = navy.getGrantTurn();
		changeDirection();
		
		// FOR ERROR TESTING ONLY
		if(debug)
			if(myTurn)
				System.err.println("myTurn value: TRUE");
			else
				System.err.println("myTurn value: FALSE");
	}
	
	/**
	 * Handle this type of message
	 * @param	msg		The Message
	 */	
	private void handleHitMessage(Message msg) 
	{
		HitMessage Hit = (HitMessage) msg;
		if(debug)
			System.err.println("Received: HitMessage");
		
		// Tr�ff eller bom?
		if(Hit.getIsHit()) {
			enemySquares.elementAt(myAttack).setHit();
			myTurn = true;
			
			// S�nkte vi ett skepp?
			if(Hit.getIsSunk())
				information.append("You sunk a " + Hit.getShip().getName() + "! \n");
			else
				information.append("You managed to hit a ship! \n");
				
		}
		else {
			enemySquares.elementAt(myAttack).setMiss();
			information.append("You missed. \n");
			changeDirection();
		}
	}
	
	/**
	 * Handle this type of message
	 * @param	msg		The Message
	 */	
	private void handleFinishedMessage(Message msg) 
	{
		FinishedMessage finished = (FinishedMessage) msg;
		if(debug)
			System.err.println("Received: FinishedMessage");
		
		// Vem vann?
		if(finished.getWinner()) {
			information.append("******* YOU WIN! *******" + "\n");
			direction.setForeground(Color.blue);
			direction.setText("=)");
		}
		else {
			information.append("******* YOU LOSE! *******" + "\n");
			direction.setForeground(Color.blue);
			direction.setText("=(");
		}
		
		// Sluta l�sa efter meddelanden	
		myTurn = false;
		t.stop();	
	}
	
	/**
	 * Triggers a challenge dialogbox.
	 * You are asked to Accept or Deny.
	 * 
	 * @param opponent	String representation of the opponent ID.
	 */	
	private void triggerChallenge(final String opponent) 
	{
		waitingForChallenge = false;
		if(debug)
			System.err.println("Triggered Challenge Dialogbox");
		
		// Skapar en JDialog f�r "challenge" meddelandet
		final JDialog challengeDialog = new JDialog();
		final JButton accept = new JButton("Accept");
		final JButton deny = new JButton("Deny");
		JLabel title = new JLabel("Player " + opponent + " has challenged you.");
		challengeDialog.add(title, BorderLayout.NORTH);
		challengeDialog.add(accept, BorderLayout.WEST);
		challengeDialog.add(deny, BorderLayout.EAST);
		challengeDialog.setLocation(window.getX() + 200, window.getY() + 200);
		
		// action listener f�r denna.
		ActionListener dialogListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == accept) {
					ChallengeMessage msg = new ChallengeMessage("NotImportant", Integer.parseInt(opponent), true, true);
					msg.accept();
					cNetwork.sendMessage(msg);
					if(debug)
						System.err.println("Sent: Accept ChallengeMessage");
					challengeDialog.setVisible(false);
					createNavyWindow();	// Accepted - hoppa vidare.
				}
				else if(arg0.getSource() == deny) {
					ChallengeMessage msg = new ChallengeMessage("NotImportant", Integer.parseInt(opponent), true, false);
					msg.decline();
					cNetwork.sendMessage(msg);
					if(debug)
						System.err.println("Sent: Deny ChallengeMessage");
					challengeDialog.setVisible(false);
				}
			}
		};
		accept.addActionListener(dialogListener);
		deny.addActionListener(dialogListener);
		challengeDialog.pack();
		challengeDialog.setResizable(false);
		challengeDialog.setVisible(true);
	}
	
	/**
	 * Sends a RefreshMessage to the Server
	 */	
	private void refreshLobby() 
	{
		RefreshMessage refresh = new RefreshMessage();
		cNetwork.sendMessage(refresh);
		if(debug)
			System.err.println("Refreshed Lobby");
	}
	
	/**
	 * Creates the "create Navy Window"
	 */	
	private void createNavyWindow()
	{
		// Rensa f�nstret p� f�reg�ende komponenter
		window.getContentPane().removeAll();
		
		// S�tt state till buildnavy
		state = states.buildnavy.ordinal();
		
		// Skapa 100 rutor med GridLayout d�r man kan placera ut sina fartyg.
		GridLayout layout = new GridLayout(10, 10);
		JPanel panel = new JPanel(layout);
		panel.setMaximumSize(new Dimension(300,300));
		panel.setMinimumSize(new Dimension(300,300));
		panel.setPreferredSize(new Dimension(300,300));
		panel.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++)
			{
				// L�gg till ny ruta i vector och panel
				Square aSquare = new Square(j, i, true);
				aSquare.addActionListener(this);
				placeSquares.add(aSquare);
				panel.add(aSquare);
			}
		}
		
		// linePanel
		JPanel linePanel = new JPanel();
		linePanel.setMaximumSize(new Dimension(400,2));
		linePanel.setMinimumSize(new Dimension(400,2));
		linePanel.setPreferredSize(new Dimension(400,2));
		linePanel.setBackground(Color.black);
		
		// buttonPanel - Inneh�ller knappar och linePanel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setMaximumSize(new Dimension(450,100));
		buttonPanel.setMinimumSize(new Dimension(450,100));
		buttonPanel.setPreferredSize(new Dimension(450,100));		
		buttonPanel.add(addSubmarineButton);
		buttonPanel.add(addDestroyerButton);
		buttonPanel.add(addAircraftCarrierButton);
		buttonPanel.add(linePanel);
		buttonPanel.add(readyButton);
		buttonPanel.add(clearButton);
		addSubmarineButton.addActionListener(this);
		addDestroyerButton.addActionListener(this);
		addAircraftCarrierButton.addActionListener(this);
		readyButton.addActionListener(this);
		clearButton.addActionListener(this);
		readyButton.setEnabled(false);
		
		// GridBagLayout
		GridBagLayout theLayout = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		window.setLayout(theLayout);
		
		// �verskift i placeShips
		JLabel placeShipsLabel = new JLabel("Place your ships!");
		placeShipsLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
		
		// H�r l�ggs komponenterna in i GridBagLayouten	
		con.gridx = 3;
		con.gridy = 0;
		theLayout.setConstraints(placeShipsLabel, con);
		window.add(placeShipsLabel);
		
		con.gridx = 3;
		con.gridy = 1;
		theLayout.setConstraints(panel, con);
		window.add(panel);
		
		con.gridx = 3;
		con.gridy = 2;
		theLayout.setConstraints(buttonPanel, con);
		window.add(buttonPanel);
				
		// Visa f�nster
		window.validate();
		window.repaint();
		window.setVisible(true);
	}	
	
	/**
	 * Creates the "Game window"
	 */	
	private void createGameWindow()
	{
		// Rensa f�nstret p� f�reg�ende komponenter
		window.getContentPane().removeAll();
		
		// S�tt state till game
		state = states.game.ordinal();
		
		// Skapa komponenter
		JLabel you = new JLabel();
		JLabel him = new JLabel();
		JScrollPane scroller = new JScrollPane(information, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		information.setEditable(false);
		
		// Text�verskrifter
		you.setText("You");
		him.setText("Enemy");
		you.setFont(new Font("SansSerif", Font.BOLD, 24));
		him.setFont(new Font("SansSerif", Font.BOLD, 24));
		
		// GridBagLayout
		GridBagLayout theLayout = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		window.setLayout(theLayout);
		
		// Skapa 100 rutor med GridLayout f�r FIENDENS SPELPLAN
		GridLayout layout = new GridLayout(10, 10);
		JPanel panel = new JPanel(layout);
		panel.setMaximumSize(new Dimension(300,300));
		panel.setMinimumSize(new Dimension(300,300));
		panel.setPreferredSize(new Dimension(300,300));
		panel.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++)
			{
				// L�gg till ny ruta i vector och panel
				Square aSquare = new Square(j, i, true);
				aSquare.addActionListener(this);
				enemySquares.add(aSquare);
				panel.add(aSquare);
			}
		}
		
		// Skapa 100 rutor med GridLayout f�r SPELARENS SPELPLAN
		GridLayout layout2 = new GridLayout(10, 10);
		JPanel panel2 = new JPanel(layout2);
		panel2.setMaximumSize(new Dimension(300,300));
		panel2.setMinimumSize(new Dimension(300,300));
		panel2.setPreferredSize(new Dimension(300,300));
		panel2.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++)
			{
				// L�gg till ny ruta i panel och vector
				Square aSquare = new Square(j, i, true);
				playerSquares.add(aSquare);
				panel2.add(aSquare);
			}
		}
		
		// Mittenpanelen, fungerar som ett mellanrum
		JPanel middle = new JPanel();
		middle.setMaximumSize(new Dimension(90,90));
		middle.setMinimumSize(new Dimension(90,90));
		middle.setPreferredSize(new Dimension(90,90));
		direction.setFont(new Font("SansSerif", Font.BOLD, 50));
		middle.add(direction);
		
		// Bottenpanelen inneh�ller textf�ltet med information om h�ndelser
		JPanel bottom = new JPanel();
		bottom.setMaximumSize(new Dimension(690,130));
		bottom.setMinimumSize(new Dimension(690,130));
		bottom.setPreferredSize(new Dimension(690,130));
		bottom.setLayout(new GridLayout(1,1));
		bottom.add(scroller);
		
		// H�r l�ggs komponenterna in i GridBagLayouten	
		con.gridx = 0;
		con.gridy = 0;
		theLayout.setConstraints(you, con);
		window.add(you);
				
		con.gridx = 2;
		con.gridy = 0;
		theLayout.setConstraints(him, con);
		window.add(him);	
	
		con.gridx = 0;
		con.gridy = 1;
		con.anchor = GridBagConstraints.WEST;
		theLayout.setConstraints(panel2, con);
		window.add(panel2);	
		
		con.gridx = 1;
		con.gridy = 1;
		con.gridheight = 2;
		con.anchor = GridBagConstraints.CENTER;
		theLayout.setConstraints(middle, con);
		window.add(middle);
		
		con.gridx = 2;
		con.gridy = 1;
		con.anchor = GridBagConstraints.EAST;
		theLayout.setConstraints(panel, con);
		window.add(panel);
		
		con.gridx = 0;
		con.gridy = 3;
		con.gridwidth = 3;
		con.weighty = 0.5;
		con.anchor = GridBagConstraints.CENTER;
		theLayout.setConstraints(bottom, con);
		window.add(bottom);
		
		// Visa f�nster
		window.validate();
		window.repaint();
		window.setVisible(true);
	}
		
	/**
	 * Checks wether you typed anything at all in the boxes.
	 * 
	 * @param ip	IP Address
	 * @param port	Port Number
	 */	
	private boolean checkInput(String ip, String port)
	{
		if(ip.length() == 0 || port.length() == 0)
			return false;
		else
			return true;
	}
	
	/**
	 * Disables the buttons for which you place ships/coordinates.
	 */	
	private void disablePlacementbuttons(){
		addAircraftCarrierButton.setEnabled(false);
		addDestroyerButton.setEnabled(false);
		addSubmarineButton.setEnabled(false);
	}
	
	/**
	 * Logic for placing the coordinates and ships.
	 * 
	 * @param e		Action Event
	 */	
	private void placeShip(ActionEvent e)
	{
		// Om utplacering av skepp �r f�rdigt, aktiveras knapparna igen och klick p� rutorna inaktiveras.
		// Nu ska vi ocks� ha skapat skeppet i fr�ga, n�gonstans.
		if(placer.placementIsDone())
		{
			// Vilken knapp blev nedtryckt?
			if(e.getSource() == addAircraftCarrierButton)
			{
				placer.setCounter(5);
				placer.addingShip("aircraft carrier");
				disablePlacementbuttons();
			}
			else if(e.getSource() == addDestroyerButton)
			{
				placer.setCounter(3);
				placer.addingShip("destroyer");
				disablePlacementbuttons();
			}
			else if(e.getSource() == addSubmarineButton)
			{
				placer.setCounter(1);
				placer.addingShip("submarine");
				disablePlacementbuttons();
			}
			
		}
		else	// Utplacering p�g�r
		{
			// Loopa genom alla rutor vid klick.
			for(int i = 0; i < placeSquares.size(); i++){
				if(e.getSource() == placeSquares.elementAt(i)){
					if(placeSquares.elementAt(i).isAlive()){		// S�tt visuellt samt koordinaterna i ShipPlacer
						placeSquares.elementAt(i).setShipHere();
						placer.addCurrentCoordinates(placeSquares.elementAt(i).getXcoordinate(), placeSquares.elementAt(i).getYcoordinate());
						placer.Count();
					}
					break;
				}
			}	
			
			// Om detta klick resulterade i att vi blev f�rdiga med en utplacering av ett skepp
			if(placer.placementIsDone()){
				
				// L�gg till skepp
				if(placer.whatShipWasPlaced().equals("submarine"))
					placer.addNumSubmarines();
				else if(placer.whatShipWasPlaced().equals("destroyer"))
					placer.addNumDestroyers();
				else if(placer.whatShipWasPlaced().equals("aircraft carrier"))
					placer.addNumAircraftcarriers();
				
				// Kolla om vi ska inaktivera knappen eller ej
				if(placer.getNumAircraftcarriers() == 1)
					addAircraftCarrierButton.setEnabled(false);
				else
					addAircraftCarrierButton.setEnabled(true);
				
				if(placer.getNumDestroyers() == 3)
					addDestroyerButton.setEnabled(false);
				else
					addDestroyerButton.setEnabled(true);
				
				if(placer.getNumSubmarines() == 5)
					addSubmarineButton.setEnabled(false);
				else
					addSubmarineButton.setEnabled(true);
				
				// Kolla om vi ska l�sa upp READY knappen
				if(placer.getNumDestroyers() == 3 && placer.getNumAircraftcarriers() == 1 && placer.getNumSubmarines() == 5)
					readyButton.setEnabled(true);
				
				// Uppdatera visuellt knapparna
				addAircraftCarrierButton.setText("Aircraft Carrier (" + Integer.toString(placer.getNumAircraftcarriers()) + "/1)");
				addDestroyerButton.setText("Destroyer (" + Integer.toString(placer.getNumDestroyers()) + "/3)");
				addSubmarineButton.setText("Submarine (" + Integer.toString(placer.getNumSubmarines()) + "/5)");
			}
		}
	}
	
	/**
	 * Changes the visual "arrow" that indicates who is attacking.
	 */	
	private void changeDirection() {
		if(myTurn){
			direction.setForeground(Color.green);
			direction.setText(">");
		}
		else {
			direction.setForeground(Color.red);
			direction.setText("<");
		}
	}
	
	/**
	 * Resets all Squares while creating navy
	 */	
	private void resetSquares() {
		for(int i = 0; i < placeSquares.size(); i++){
			placeSquares.elementAt(i).resetMe();
		}
	}
	
	/**
	 * Resets all buttons to default in navy creation
	 */	
	private void resetPlacementbuttons() {
		addAircraftCarrierButton.setText("Aircraft Carrier (0/1)");
		addDestroyerButton.setText("Destroyer (0/3)");
		addSubmarineButton.setText("Submarine (0/5)");	
		addAircraftCarrierButton.setEnabled(true);
		addDestroyerButton.setEnabled(true);
		addSubmarineButton.setEnabled(true);
		readyButton.setEnabled(false);
		clearButton.setEnabled(true);
	}
	
	/**
	 * Converts Navy coordinates to Squares, visually in the game.
	 */		
	private void setPlayerNavy()
	{
		// Vector med alla koordinater
		Vector<Coordinate> cords = new Vector<Coordinate>();
		
		// L�gger �ver till vectorn
		for(int i = 0; i < myNavy.getShips().size(); i++)
			cords.addAll(myNavy.getShips().get(i).getCoords());	
		
		// S�tt ut koordinaterna p� playerSquares
		for(int i = 0; i < playerSquares.size(); i++){
			for(int j = 0; j < cords.size(); j++){
				if(cords.elementAt(j).getX() == playerSquares.elementAt(i).getXcoordinate() && cords.elementAt(j).getY() == playerSquares.elementAt(i).getYcoordinate()) {
					playerSquares.elementAt(i).setShipHere();	
				}
			}
		}
	}
	
	/**
	 * Updates the visual representation of the clients navy
	 */	
	private void updateMyNavy() 
	{
		// L�gg �ver 2D array till Int Vector
		Vector<Integer> Ocean = new Vector<Integer>(100);
		for (int x[] : myNavy.getMap().getOcean()) {
			for (int y : x) 
				Ocean.add(y);
		}

		// FOR ERROR TESTING ONLY...
		if(debug){
			System.out.println("OCEAN[][] VALUES: ");
			for(int i = 0; i < Ocean.size(); i++) {
				System.out.print(Ocean.elementAt(i));
				if((i+1) % 10 == 0)
					System.out.println();
			}
			System.err.println("");
		}
		
		
		// Loopa genom b�da vectorer, om v�rdet i Ocean == 3 eller 1, s� �r detta en tr�ff av fienden.
		for(int i = 0; i < playerSquares.size(); i++) {
			if((Ocean.elementAt(i) == 3 || Ocean.elementAt(i) == 1))
				playerSquares.elementAt(i).setMiss();	// Egentligen en tr�ff, men f�r fienden. (blir r�tt f�rg)
			else if(Ocean.elementAt(i) == 2)
				playerSquares.elementAt(i).setBom();	// "BOM" �r allts� en miss (f�r fienden)
		}
	}
	
	
	/**
	 * Action Events in CONNECT WINDOW.
	 * 
	 * @param e		ActionEvent
	 */	
	private void connectEvents(ActionEvent e) 
	{
		if(e.getSource() == connectButton){
			if(checkInput(ipbox.getText().toString(), portbox.getText().toString())){			// Kontrollerar input i textrutorna
				if(cNetwork.connect(ipbox.getText().toString(), portbox.getText().toString())){	// Ansluter till servern
					createLobbyWindow();														// Om ansluten - G� till lobby
					ConnectedToServer = true;
				}
				else
					connectionError.setText("Unable to connect to server.");					// Felmeddelande
			}
			else
				connectionError.setText("Invalid input.");										// Felmeddelande	
		}
	}
	
	/**
	 * Action Events in LOBBY WINDOW.
	 * 
	 * @param e		ActionEvent
	 */		
	private void lobbyEvents(ActionEvent e) 
	{
		if(e.getSource() == challengeButton) {
			
			// Skicka ett challenge till den man valt
			if(lobbySelected != null && lobbySelected.length() > 0) {
				waitingForChallenge = false;
				
				// S�tter ID och Namn
				int playerID = 0;
				if(!lobbySelected.equals("Server"))
					playerID = Character.getNumericValue(lobbySelected.charAt(lobbySelected.length()-1));

				// Skicka
				ChallengeMessage challenge = new ChallengeMessage(lobbySelected, playerID);
				cNetwork.sendMessage(challenge);
				if(debug)
					System.err.println("Sent challenge message against: " + lobbySelected + " with ID: " + Integer.toString(playerID));
			}
		}
		else if(e.getSource() == refreshButton) {
			refreshLobby();
		}
	}
	
	/**
	 * Action Events in CREATE NAVY WINDOW.
	 * 
	 * @param e		ActionEvent
	 */		
	private void createNavyEvents(ActionEvent e) 
	{
		// Utplacering av skepp
		placeShip(e);	
		
		// READY och CLEAR knapparna
		if(e.getSource() == readyButton) {
			myNavy = placer.getNavy();							// H�mta Navy fr�n ShipPlacer
			
			if(debug) {
				Validator valid = new Validator(5, 3, 1);
				if(valid.validateNavy(myNavy))
					System.err.println("Validator: Navy OK!");
				else
					System.err.println("Validator: Navy INVALID!!!");
			}
			
			NavyMessage sendNavy = new NavyMessage(myNavy);		// Skicka till server
			cNetwork.sendMessage(sendNavy);
			if(debug)
				System.err.println("Sent NavyMessage to Server.");
			clearButton.setEnabled(false);
			readyButton.setEnabled(false);
		}
		else if(e.getSource() == clearButton) {					// Nollst�ll Navy
			placer.Reset();
			resetSquares();
			resetPlacementbuttons();
		}		
	}
	
	/**
	 * Action Events in GAME WINDOW.
	 * 
	 * @param e		ActionEvent
	 */		
	private void gameEvents(ActionEvent e) 
	{
		if(myTurn)	// G� igenom alla rutor.
			for(int i = 0; i < enemySquares.size(); i++){
				if(e.getSource() == enemySquares.elementAt(i)){
					if(enemySquares.elementAt(i).isAlive()){
						Shot shoot = new Shot(enemySquares.elementAt(i).getXcoordinate(), enemySquares.elementAt(i).getYcoordinate()); 
						cNetwork.sendMessage(shoot);
						enemySquares.elementAt(i).setBom();			// "BOM" tills man vet om det �r tr�ff eller miss.
						if(debug)
							System.err.println("Sent: ShootMessage");
						myAttack = i;
						myTurn = false;
					}
					break;
				}
			}		
	}
	
	/**
	 * Action Events
	 * 
	 * @param e		ActionEvent
	 */	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(state == states.connect.ordinal())
			connectEvents(e);
		else if(state == states.lobby.ordinal())
			lobbyEvents(e);
		else if(state == states.buildnavy.ordinal())
			createNavyEvents(e);
		else
			gameEvents(e);
	}

}	// END OF CLIENT

