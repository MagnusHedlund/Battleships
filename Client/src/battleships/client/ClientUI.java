//----------------------------------------------------------------
// Namn: Fredrik Str�mbergsson
// Datum: 2013-05-24
// 
// ClientUI.java
//----------------------------------------------------------------

package battleships.client;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import battleships.game.Coordinate;
import battleships.game.Navy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

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
	private Vector<String> playerList = new Vector<String>();		// objekten i listan - lobby
	private String lobbySelected = "";								// vilket objekt som �r valt i listan - lobby
	
	//-----------------------------------------
	// Konstruktor
	//-----------------------------------------
	ClientUI()
	{
		createConnectWindow();
	}
	
	//-----------------------------------------
	// Skapar f�nstret f�r anslutning mot server
	//-----------------------------------------
	private void createConnectWindow()
	{
		// S�tt state till connect
		state = states.connect.ordinal();
		
		// F�nsteregenskaper
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(720, 520);
		window.setResizable(false);
		window.setTitle("Project Battleship");
		
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
		else
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
		portbox.setText("5000");
		
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
	
	//-----------------------------------------
	// Skapar lobbyf�nstret
	//-----------------------------------------
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
		
		// S�tt storlek p� button...
		challengeButton.setMaximumSize(new Dimension(150,30));
		challengeButton.setMinimumSize(new Dimension(150,30));
		challengeButton.setPreferredSize(new Dimension(150,30));
		challengeButton.addActionListener(this);
		
		// L�gg button i en panel
		JPanel panel2 = new JPanel();
		panel2.setMaximumSize(new Dimension(700,50));
		panel2.setMinimumSize(new Dimension(700,50));
		panel2.setPreferredSize(new Dimension(700,50));
		panel2.add(challengeButton);

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
		playerList.add("Player");
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
	}
	
	//-----------------------------------------
	// Skapar f�nstret f�r utplacering av skepp
	//-----------------------------------------
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
	
	//-----------------------------------------
	// Skapar spelf�nstret
	//-----------------------------------------
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
		
		// Uppdatera navy
		updateMyNavy();
	}
	
	// Anslut till servern										---- Det h�r ska egentligen inte vara h�r ....
	public boolean connectToServer(String ip, String port)
	{
		return true;
	}
	
	// Kontrollerar input [kollar bara om det st�r n�got i rutorna �verhuvudtaget]
	private boolean checkInput(String ip, String port)
	{
		if(ip.length() == 0 || port.length() == 0)
			return false;
		else
			return true;
	}
	
	//-----------------------------------------
	// Inaktiverar knappar som anv�nds vid utplacering av skepp
	//-----------------------------------------
	private void disablePlacementbuttons(){
		addAircraftCarrierButton.setEnabled(false);
		addDestroyerButton.setEnabled(false);
		addSubmarineButton.setEnabled(false);
	}
	
	//-----------------------------------------
	// Logik f�r utplacering av skepp
	//-----------------------------------------	
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
				if(placer.whatShipWasPlaced() == "submarine")
					placer.addNumSubmarines();
				else if(placer.whatShipWasPlaced() == "destroyer")
					placer.addNumDestroyers();
				else if(placer.whatShipWasPlaced() == "aircraft carrier")
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
	
	//-----------------------------------------
	// Nollst�ller rutorna f�r utplacering av skepp
	//-----------------------------------------
	private void resetSquares() {
		for(int i = 0; i < placeSquares.size(); i++){
			placeSquares.elementAt(i).resetMe();
		}
	}
	
	//-----------------------------------------
	// Nollst�ller knappar f�r utplacering av skepp
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
	// Uppdaterar spelarens NAVY p� spelplanen
	//-----------------------------------------	
	public void updateMyNavy() {
		// Vector med alla koordinater
		Vector<Coordinate> cords = new Vector<Coordinate>();
		
		// L�gger �ver till vectorn
		for(int i = 0; i < myNavy.getShips().size(); i++)
			cords.addAll(myNavy.getShips().get(i).getCoords());
		
		// S�tt ut koordinaterna p� playerSquares
		for(int i = 0; i < playerSquares.size(); i++){
			for(int j = 0; j < cords.size(); j++){
				if(cords.elementAt(j).getX().equals(playerSquares.elementAt(i).getXcoordinate()) && cords.elementAt(j).getY().equals(playerSquares.elementAt(i).getYcoordinate())) {
					playerSquares.elementAt(i).setShipHere();		
				}
			}
		}
		
		// Det m�ste vara exakt 19 koordinater, annars �r n�got fel.
		if(cords.size() != 19)
			System.err.println("ERROR: INVALID NUMBER OF COORDINATES PLACED (" + Integer.toString(cords.size()) + "). MUST BE 19.");
		
	}
	
	
	//-----------------------------------------
	// ActionEvents - CONNECT WINDOW
	//-----------------------------------------	
	private void connectEvents(ActionEvent e) 
	{
		if(e.getSource() == connectButton){
			if(checkInput(ipbox.getText().toString(), portbox.getText().toString())){			// Kontrollerar input i textrutorna
				if(connectToServer(ipbox.getText().toString(), portbox.getText().toString()))	// Ansluter till servern
					createLobbyWindow();														// Om ansluten - G� till lobby
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
			// SKICKA CHALLENGE TILL SERVER MED NAMNET P� DEN MAN VALT, tex "SERVER" �r ju en challenge mot den.
			// V�nta p� "accept" eller "deny"
			createNavyWindow();
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
			myNavy = placer.getNavy();			// H�mta Navy fr�n ShipPlacer
			// ____ H�R SKA NAVY SKICKAS TILL SERVER OCH V�NTA SVAR P� OM KORREKT ELLER EJ ___
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
		// Loopa genom alla rutor vid klick.				---- BOOL h�r som best�mmer om det �r ens egen tur eller ej.
		for(int i = 0; i < enemySquares.size(); i++){
			if(e.getSource() == enemySquares.elementAt(i)){
				if(enemySquares.elementAt(i).isAlive()){
					enemySquares.elementAt(i).setMiss();		// Skicka meddelande till server h�r...
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





