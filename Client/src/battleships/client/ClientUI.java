//----------------------------------------------------------------
// Namn: Fredrik Strömbergsson
// Datum: 2013-05-24
// 
// ClientUI.java
//----------------------------------------------------------------

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import battleships.message.*;
import battleships.game.*;

public class ClientUI implements ActionListener
{
	// Deklarationer
	private Vector<Square> playerSquares = new Vector<Square>(100);		// Innehåller spelarens rutor
	private Vector<Square> enemySquares = new Vector<Square>(100);		// Innehåller fiendens rutor
	private Vector<Square> placeSquares = new Vector<Square>(100);		// Innehåller rutorna när man skapar sina fartyg
	private JFrame window = new JFrame();								// Fönstret
	private enum states { connect, lobby, buildnavy, game };			// Enum räknare för "state"
	private int state = 0;												// Nuvarande state
	private JTextArea information = new JTextArea();					// Informations textfältet där händelser visas
	private JButton connectButton = new JButton("Connect");				// Connectknappen i startfönstret.
	private JTextField ipbox = new JTextField();						// IP nummer textbox i startfönstret.
	private JTextField portbox = new JTextField();						// Portnummer textbox i startfönstret.
	private JLabel connectionError = new JLabel("");					// label som visar fel i startfönstret.
	private JButton addSubmarineButton = new JButton("Submarine (0/5)");		// För att lägga till ubåtar
	private JButton addDestroyerButton = new JButton("Destroyer (0/3)");		// För att lägga till jägare
	private JButton addAircraftCarrierButton = new JButton("Aircraft Carrier (0/1)");	// För att lägga till hangarfartyg
	private JButton readyButton = new JButton("READY");						// READY (färdig med utplacering)
	private JButton clearButton = new JButton("CLEAR");				// rensa fältet där man lägger ut skepp
	private ShipPlacer placer = new ShipPlacer();					// Används när man sätter ut skepp
	private Navy myNavy = new Navy(5, 3, 1);						// NAVY
	private JList<String> lobbyList = new JList<String>();			// Listan i LOBBY
	private JButton challengeButton = new JButton("Challenge!");	// "challenge" knapp - lobby
	private JButton refreshButton = new JButton("Refresh");			// "refresh" knapp - lobby
	private Vector<String> playerList = new Vector<String>();		// objekten i listan - lobby
	private String lobbySelected = "";								// vilket objekt som är valt i listan - lobby
	private ClientNetwork cNetwork = new ClientNetwork();			// Wrapper för Socket
	private boolean waitingForChallenge = true;						// BOOL - för lobbymeddelanden
	private javax.swing.Timer t = null;								// Timer som uppdaterar nätverket varje sekund
	private Map<String, Integer> lobbyContenders = new HashMap<String,Integer>();
	private boolean ConnectedToServer = false;	
	
	//-----------------------------------------
	// Konstruktor
	//-----------------------------------------
	ClientUI()
	{	
		// Action listener för timer som varje sekund lyssnar efter nätverksmeddelanden
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
		
		t = new javax.swing.Timer(1000, networkListener);
		t.start();
		createConnectWindow();
	}
		
	// ----------------------------------
	// 	När man stränger ner fönstret...
	// ----------------------------------
	WindowAdapter exitListener = new WindowAdapter()
	{
		@Override
		public void windowClosing(WindowEvent e) {
			if(ConnectedToServer)
				cNetwork.disconnect();
		}
	};
	
	//-----------------------------------------
	// Skapar fönstret för anslutning mot server
	//-----------------------------------------
	private void createConnectWindow()
	{
		// Sätt state till connect
		state = states.connect.ordinal();
		
		// Fönsteregenskaper
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(720, 520);
		window.setResizable(false);
		window.setTitle("Project Battleship");
		window.addWindowListener(exitListener);
		
		// Meny -- används ej.
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
		
		// Laddar in bilden (överskriften) och lägger den i headpanelen
		BufferedImage myPicture = null;
		JPanel head = new JPanel();
		
		try {
			myPicture = ImageIO.read(new File("images/bs.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Använder text om bilden inte hittas
		if(myPicture == null)
		{
			JLabel picLabel = new JLabel("BATTLESHIP");
			picLabel.setFont(new Font("SansSerif", Font.BOLD, 50));
			head.add(picLabel);			
		}
		else	// Använder bilden
		{
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			head.add(picLabel);			
		}
		
		// Text fält och deras label
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

		// Sätt default värden för textrutorna
		ipbox.setText("127.0.0.1");
		portbox.setText("5168");
		
		// text panelen, innehåller textfälten osv
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
	
		// Lägg till i layouten
		window.add(top);
		window.add(head);
		window.add(textpanel);
		window.add(bottom);
				
		// Visa fönster
		window.validate();
		window.setVisible(true);
	}
	
	//-----------------------------------------
	// Skapar lobbyfönstret
	//-----------------------------------------
	private void createLobbyWindow()
	{
		// Rensa fönstret på föregående komponenter
		window.getContentPane().removeAll();
		
		// Sätt state till lobby
		state = states.lobby.ordinal();
				
		// GridBagLayout
		GridBagLayout theLayout = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		window.setLayout(theLayout);
		
		// Sätt storlek på lobbylistan
		lobbyList.setMaximumSize(new Dimension(700,300));
		lobbyList.setMinimumSize(new Dimension(700,300));
		lobbyList.setPreferredSize(new Dimension(700,300));
		lobbyList.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		lobbyList.setFont(new Font("SansSerif", Font.BOLD, 18));
		
		// överskift
		JLabel lobbyText = new JLabel("Select and challenge your enemy!");
		lobbyText.setFont(new Font("SansSerif", Font.BOLD, 24));
		
		// Lägg i panel
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension(700,50));
		panel.setMinimumSize(new Dimension(700,50));
		panel.setPreferredSize(new Dimension(700,50));
		panel.add(lobbyText);
		
		// Sätt storlek på knappar...
		challengeButton.setMaximumSize(new Dimension(150,30));
		challengeButton.setMinimumSize(new Dimension(150,30));
		challengeButton.setPreferredSize(new Dimension(150,30));
		challengeButton.addActionListener(this);
		
		refreshButton.setMaximumSize(new Dimension(150,30));
		refreshButton.setMinimumSize(new Dimension(150,30));
		refreshButton.setPreferredSize(new Dimension(150,30));
		refreshButton.addActionListener(this);
		
		// Lägg button i en panel
		JPanel panel2 = new JPanel();
		panel2.setMaximumSize(new Dimension(700,50));
		panel2.setMinimumSize(new Dimension(700,50));
		panel2.setPreferredSize(new Dimension(700,50));
		panel2.add(challengeButton);
		panel2.add(refreshButton);

		// Här läggs komponenterna in i GridBagLayouten	
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

		// Lägg till servern i spelarlistan per default
		playerList.add("Server");
		lobbyList.setListData(playerList);
		
		// Lyssnare som hämtar värdet på den sak i listan man klickat på.
		lobbyList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
            	lobbySelected = (String) lobbyList.getSelectedValue();
            	challengeButton.setText("Challenge: " + lobbySelected);
            }
        });
		
		// Visa fönster
		window.validate();
		window.repaint();
		window.setVisible(true);
		
		System.err.println("Created Lobby Window");
		
		// Skicka namn
		NameMessage msg = new NameMessage();
		msg.setName("IAmAPlayerName");
		cNetwork.sendMessage(msg);
		
		// Uppdatera lobbyn
		refreshLobby();
	}
	
	//-----------------------------------------
	// Uppdaterar lobbyn (NETWORK - Reciever)
	//-----------------------------------------	
	private void lobbyNetwork()
	{
		Message lobbyUpdate = cNetwork.getMessage();
		
		if(lobbyUpdate != null)
			if(lobbyUpdate.getType().equals("ActivePlayersMessage")) 
			{
				ActivePlayersMessage playerL = (ActivePlayersMessage) lobbyUpdate;
				lobbyContenders = playerL.getContenders();
				System.err.println("Recieved - ActivePlayersMessage");
				
				// Uppdatera lobbylistan
				playerList.clear();
				playerList.add("Server");
				for(int i = 0; i < lobbyContenders.size(); i++)
					playerList.add("Player " + Integer.toString(i+1));
				lobbyList.setListData(playerList);
				
			}
			else if(lobbyUpdate.getType().equals("ChallengeMessage")) 
			{
				ChallengeMessage challenge = (ChallengeMessage) lobbyUpdate;
				System.err.println("Recieved - ChallengeMessage");
				
				playerList.add("ChallengeMessage recieved, this is a test.");
				
				// Avgör om man väntar på ett svar på skickad challenge, eller väntar på en challenge.
				if(waitingForChallenge) {
					triggerChallenge(challenge.getOpponentName());
				}
				else {
					// Accept meddelande?
					if(challenge.getAccept())
						createNavyWindow();
					else
						waitingForChallenge = true;		// Deny message
				}
			}
	}
	
	//-----------------------------------------
	// Uppdaterar create navy (NETWORK - Reciever)
	//-----------------------------------------	
	private void navyNetwork()
	{
		Message navyUpdate = cNetwork.getMessage();
			
		if(navyUpdate != null)
			;
	}
	
	//-----------------------------------------
	// Uppdaterar game (NETWORK - Reciever)
	//-----------------------------------------	
	private void gameNetwork()
	{

		Message gameUpdate = cNetwork.getMessage();
			
		if(gameUpdate != null)
			;			

	}
	
	//-----------------------------------------
	// Challenge Meddelandet, accept eller deny (NETWORK - Sender)
	//-----------------------------------------		
	private void triggerChallenge(String opponent) {
		waitingForChallenge = false;
		
		// Skapar en JDialog för "challenge" meddelandet
		final JDialog challengeDialog = new JDialog();
		final JButton accept = new JButton("Accept");
		final JButton deny = new JButton("Deny");
		JLabel title = new JLabel(opponent + " has challenged you.");
		challengeDialog.add(title, BorderLayout.NORTH);
		challengeDialog.add(accept, BorderLayout.WEST);
		challengeDialog.add(deny, BorderLayout.EAST);
		
		// action listener för denna.
		ActionListener dialogListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == accept) {
					ChallengeMessage msg = new ChallengeMessage();
					msg.accept();
					cNetwork.sendMessage(msg);
					challengeDialog.setVisible(false);
				}
				else if(arg0.getSource() == deny) {
					ChallengeMessage msg = new ChallengeMessage();
					msg.decline();
					cNetwork.sendMessage(msg);
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
	
	//-----------------------------------------
	// Skickar ett refresh meddelande (NETWORK - Sender)
	//-----------------------------------------		
	private void refreshLobby() 
	{
		RefreshMessage refresh = new RefreshMessage();
		cNetwork.sendMessage(refresh);	
		System.err.println("Refreshed Lobby");
	}
	
	//-----------------------------------------
	// Skapar fönstret för utplacering av skepp
	//-----------------------------------------
	private void createNavyWindow()
	{
		// Rensa fönstret på föregående komponenter
		window.getContentPane().removeAll();
		
		// Sätt state till buildnavy
		state = states.buildnavy.ordinal();
		
		// Skapa 100 rutor med GridLayout där man kan placera ut sina fartyg.
		GridLayout layout = new GridLayout(10, 10);
		JPanel panel = new JPanel(layout);
		panel.setMaximumSize(new Dimension(300,300));
		panel.setMinimumSize(new Dimension(300,300));
		panel.setPreferredSize(new Dimension(300,300));
		panel.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++)
			{
				// Lägg till ny ruta i vector och panel
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
		
		// buttonPanel - Innehåller knappar och linePanel
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
		
		// överskift i placeShips
		JLabel placeShipsLabel = new JLabel("Place your ships!");
		placeShipsLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
		
		// Här läggs komponenterna in i GridBagLayouten	
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
				
		// Visa fönster
		window.validate();
		window.repaint();
		window.setVisible(true);
	}	
	
	//-----------------------------------------
	// Skapar spelfönstret
	//-----------------------------------------
	private void createGameWindow()
	{
		// Rensa fönstret på föregående komponenter
		window.getContentPane().removeAll();
		
		// Sätt state till game
		state = states.game.ordinal();
		
		// Skapa komponenter
		JLabel you = new JLabel();
		JLabel him = new JLabel();
		JScrollPane scroller = new JScrollPane(information, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		information.setEditable(false);
		
		// Textöverskrifter
		you.setText("You");
		him.setText("Enemy");
		you.setFont(new Font("SansSerif", Font.BOLD, 24));
		him.setFont(new Font("SansSerif", Font.BOLD, 24));
		
		// GridBagLayout
		GridBagLayout theLayout = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		window.setLayout(theLayout);
		
		// Skapa 100 rutor med GridLayout för FIENDENS SPELPLAN
		GridLayout layout = new GridLayout(10, 10);
		JPanel panel = new JPanel(layout);
		panel.setMaximumSize(new Dimension(300,300));
		panel.setMinimumSize(new Dimension(300,300));
		panel.setPreferredSize(new Dimension(300,300));
		panel.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++)
			{
				// Lägg till ny ruta i vector och panel
				Square aSquare = new Square(j, i, true);
				aSquare.addActionListener(this);
				enemySquares.add(aSquare);
				panel.add(aSquare);
			}
		}
		
		// Skapa 100 rutor med GridLayout för SPELARENS SPELPLAN
		GridLayout layout2 = new GridLayout(10, 10);
		JPanel panel2 = new JPanel(layout2);
		panel2.setMaximumSize(new Dimension(300,300));
		panel2.setMinimumSize(new Dimension(300,300));
		panel2.setPreferredSize(new Dimension(300,300));
		panel2.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++)
			{
				// Lägg till ny ruta i panel och vector
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
		
		// Bottenpanelen innehåller textfältet med information om händelser
		JPanel bottom = new JPanel();
		bottom.setMaximumSize(new Dimension(690,130));
		bottom.setMinimumSize(new Dimension(690,130));
		bottom.setPreferredSize(new Dimension(690,130));
		bottom.setLayout(new GridLayout(1,1));
		bottom.add(scroller);
		
		// Här läggs komponenterna in i GridBagLayouten	
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
		
		// Visa fönster
		window.validate();
		window.repaint();
		window.setVisible(true);
		
		// Uppdatera navy
		updateMyNavy();
	}
		
	// Kontrollerar input [kollar bara om det står något i rutorna överhuvudtaget]
	private boolean checkInput(String ip, String port)
	{
		if(ip.length() == 0 || port.length() == 0)
			return false;
		else
			return true;
	}
	
	//-----------------------------------------
	// Inaktiverar knappar som används vid utplacering av skepp
	//-----------------------------------------
	private void disablePlacementbuttons(){
		addAircraftCarrierButton.setEnabled(false);
		addDestroyerButton.setEnabled(false);
		addSubmarineButton.setEnabled(false);
	}
	
	//-----------------------------------------
	// Logik för utplacering av skepp
	//-----------------------------------------	
	private void placeShip(ActionEvent e)
	{
		// Om utplacering av skepp är färdigt, aktiveras knapparna igen och klick på rutorna inaktiveras.
		// Nu ska vi också ha skapat skeppet i fråga, någonstans.
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
		else	// Utplacering pågår
		{
			// Loopa genom alla rutor vid klick.
			for(int i = 0; i < placeSquares.size(); i++){
				if(e.getSource() == placeSquares.elementAt(i)){
					if(placeSquares.elementAt(i).isAlive()){		// Sätt visuellt samt koordinaterna i ShipPlacer
						placeSquares.elementAt(i).setShipHere();
						placer.addCurrentCoordinates(placeSquares.elementAt(i).getXcoordinate(), placeSquares.elementAt(i).getYcoordinate());
						placer.Count();
					}
					break;
				}
			}	
			
			// Om detta klick resulterade i att vi blev färdiga med en utplacering av ett skepp
			if(placer.placementIsDone()){
				
				// Lägg till skepp
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
				
				// Kolla om vi ska låsa upp READY knappen
				if(placer.getNumDestroyers() == 3 && placer.getNumAircraftcarriers() == 1 && placer.getNumSubmarines() == 5)
					readyButton.setEnabled(true);
				
				// Uppdatera visuellt knapparna
				addAircraftCarrierButton.setText("Aircraft Carrier (" + Integer.toString(placer.getNumAircraftcarriers()) + "/1)");
				addDestroyerButton.setText("Destroyer (" + Integer.toString(placer.getNumDestroyers()) + "/3)");
				addSubmarineButton.setText("Submarine (" + Integer.toString(placer.getNumSubmarines()) + "/5)");
			}
		}
	}
	
	//-----------------------------------------
	// Nollställer rutorna för utplacering av skepp
	//-----------------------------------------
	private void resetSquares() {
		for(int i = 0; i < placeSquares.size(); i++){
			placeSquares.elementAt(i).resetMe();
		}
	}
	
	//-----------------------------------------
	// Nollställer knappar för utplacering av skepp
	//-----------------------------------------
	private void resetPlacementbuttons() {
		addAircraftCarrierButton.setText("Aircraft Carrier (0/1)");
		addDestroyerButton.setText("Destroyer (0/3)");
		addSubmarineButton.setText("Submarine (0/5)");	
		addAircraftCarrierButton.setEnabled(true);
		addDestroyerButton.setEnabled(true);
		addSubmarineButton.setEnabled(true);
		readyButton.setEnabled(false);
	}
	
	//-----------------------------------------
	// Uppdaterar spelarens NAVY på spelplanen
	//-----------------------------------------	
	public void updateMyNavy() {
		// Vector med alla koordinater
		Vector<Coordinate> cords = new Vector<Coordinate>();
		
		// Lägger över till vectorn
		for(int i = 0; i < myNavy.getShips().size(); i++)
			cords.addAll(myNavy.getShips().get(i).getCoords());
		
		// Sätt ut koordinaterna på playerSquares
		for(int i = 0; i < playerSquares.size(); i++){
			for(int j = 0; j < cords.size(); j++){
				if(cords.elementAt(j).getX().equals(playerSquares.elementAt(i).getXcoordinate()) && cords.elementAt(j).getY().equals(playerSquares.elementAt(i).getYcoordinate())) {
					playerSquares.elementAt(i).setShipHere();		
				}
			}
		}
	}
	
	
	//-----------------------------------------
	// ActionEvents - CONNECT WINDOW
	//-----------------------------------------	
	private void connectEvents(ActionEvent e) 
	{
		if(e.getSource() == connectButton){
			if(checkInput(ipbox.getText().toString(), portbox.getText().toString())){			// Kontrollerar input i textrutorna
				if(cNetwork.connect(ipbox.getText().toString(), portbox.getText().toString())){	// Ansluter till servern
					createLobbyWindow();														// Om ansluten - Gå till lobby
					ConnectedToServer = true;
				}
				else
					connectionError.setText("Unable to connect to server.");					// Felmeddelande
			}
			else
				connectionError.setText("Invalid input.");										// Felmeddelande	
		}
	}
	
	//-----------------------------------------
	// ActionEvents - LOBBY WINDOW
	//-----------------------------------------		
	private void lobbyEvents(ActionEvent e) 
	{
		if(e.getSource() == challengeButton) {
			
			// Skicka ett challenge till den man valt
			if(lobbySelected.length() > 0) {
				waitingForChallenge = false;
				ChallengeMessage challenge = new ChallengeMessage();
				challenge.accept();
				challenge.setOpponentName(lobbySelected);
				cNetwork.sendMessage(challenge);
				System.err.println("Sent challenge message");
			}
		}
		else if(e.getSource() == refreshButton) {
			refreshLobby();
		}
	}
	
	//-----------------------------------------
	// ActionEvents - CREATE NAVY WINDOW
	//-----------------------------------------		
	private void createNavyEvents(ActionEvent e) 
	{
		// Utplacering av skepp
		placeShip(e);	
		
		// READY eller CLEAR knapparna
		if(e.getSource() == readyButton) {
			myNavy = placer.getNavy();			// Hämta Navy från ShipPlacer
			// SKICKA NAVY TILL SERVER FÖR VALIDERING, VÄNTA PÅ SVAR...
			createGameWindow();
		}
		else if(e.getSource() == clearButton) {
			placer.Reset();
			resetSquares();
			resetPlacementbuttons();
		}		
	}
	
	//-----------------------------------------
	// ActionEvents - GAME WINDOW
	//-----------------------------------------		
	private void gameEvents(ActionEvent e) 
	{
		// Loopa genom alla rutor vid klick.				---- BOOL här som bestämmer om det är ens egen tur eller ej.
		for(int i = 0; i < enemySquares.size(); i++){
			if(e.getSource() == enemySquares.elementAt(i)){
				if(enemySquares.elementAt(i).isAlive()){
					enemySquares.elementAt(i).setMiss();		// Skicka meddelande till server här...
					information.append("X: " + Integer.toString(enemySquares.elementAt(i).getXcoordinate()) + " Y: " + Integer.toString(enemySquares.elementAt(i).getYcoordinate()) + "\n");
				}
				break;
			}
		}		
	}
	
	//-----------------------------------------
	// Action Events
	//-----------------------------------------
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

