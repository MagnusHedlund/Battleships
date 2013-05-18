//----------------------------------------------------------------
// Namn: Fredrik Strömbergsson
// Datum: 2013-05-17
// 
// ClientUI.java
//----------------------------------------------------------------

import javax.imageio.ImageIO;
import javax.swing.*;
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
	
	//-----------------------------------------
	// Konstruktor
	//-----------------------------------------
	ClientUI()
	{
		createConnectWindow();
	}
	
	//-----------------------------------------
	// Skapar fönstret för anslutning mot server
	//-----------------------------------------
	public void createConnectWindow()
	{
		// Sätt state till connect
		state = states.connect.ordinal();
		
		// Fönsteregenskaper
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(720, 520);
		window.setResizable(false);
		window.setTitle("Project Battleship");
		
		// Meny										--- Behöver lägga till LISTENERS för dessa.
		JMenuBar theMenu = new JMenuBar();
		JMenu menuTitle = new JMenu("Menu");
		JMenuItem menuConnect = new JMenuItem("Connect");
		JMenuItem menuDisconnect = new JMenuItem("Disconnect");
		JMenuItem menuExit = new JMenuItem("Exit");
		window.setJMenuBar(theMenu);
		theMenu.add(menuTitle);
		menuTitle.add(menuConnect);
		menuTitle.add(menuDisconnect);
		menuTitle.addSeparator();
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
		else
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
	public void createLobbyWindow()
	{
		// Rensa fönstret på föregående komponenter
		window.getContentPane().removeAll();	
		
		// Sätt state till lobby
		state = states.lobby.ordinal();
		
		// Visa fönster
		window.validate();
		window.repaint();
		window.setVisible(true);
	}
	
	//-----------------------------------------
	// Skapar fönstret för utplacering av skepp
	//-----------------------------------------
	public void createNavyWindow()
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
	public void createGameWindow()
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

	}
	
	// Anslut till servern										---- Det här ska egentligen inte vara här ....
	public boolean connectToServer(String ip, String port)
	{
		return true;
	}
	
	// Kontrollerar input
	public boolean checkInput(String ip, String port)
	{
		if(ip.length() == 0 || port.length() == 0)
			return false;
		else
			return true;
	}
	
	//-----------------------------------------
	// Inaktiverar knappar som används vid utplacering av skepp
	//-----------------------------------------
	public void disablePlacementbuttons(){
		addAircraftCarrierButton.setEnabled(false);
		addDestroyerButton.setEnabled(false);
		addSubmarineButton.setEnabled(false);
	}
	
	//-----------------------------------------
	// Logik för utplacering av skepp
	//-----------------------------------------	
	public void placeShip(ActionEvent e)
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
					if(placeSquares.elementAt(i).isAlive()){
						placeSquares.elementAt(i).setShipHere();	// Del av skepp/skepp får koordinaterna satt här typ?
						placer.Count();
					}
					break;
				}
			}	
			
			// Om detta klick resulterade i att vi blev färdiga med en utplacering av ett skepp
			if(placer.placementIsDone()){
				
				// Lägg till skepp
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
	public void resetSquares() {
		for(int i = 0; i < placeSquares.size(); i++){
			placeSquares.elementAt(i).resetMe();
		}
	}
	
	//-----------------------------------------
	// Nollställer knappar för utplacering av skepp
	//-----------------------------------------
	public void resetPlacementbuttons(){
		addAircraftCarrierButton.setText("Aircraft Carrier (0/1)");
		addDestroyerButton.setText("Destroyer (0/3)");
		addSubmarineButton.setText("Submarine (0/5)");	
		addAircraftCarrierButton.setEnabled(true);
		addDestroyerButton.setEnabled(true);
		addSubmarineButton.setEnabled(true);
	}
	
	
	//-----------------------------------------
	// Action Events
	//-----------------------------------------
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(state == states.connect.ordinal()){														// STATE: CONNECT (fönster 1)
			if(e.getSource() == connectButton){
				if(checkInput(ipbox.getText().toString(), portbox.getText().toString())){			// Kontrollerar input i textrutorna
					if(connectToServer(ipbox.getText().toString(), portbox.getText().toString()))	// Ansluter till servern
						createNavyWindow();														// Om ansluten - Gå till lobby
					else
						connectionError.setText("Unable to connect to server.");					// Felmeddelande
				}
				else
					connectionError.setText("Invalid input.");										// Felmeddelande
			}
		}
		else if(state == states.lobby.ordinal())													// STATE: LOBBY (fönster 2)
		{
			// events i lobby
		}
		else if(state == states.buildnavy.ordinal())												// STATE: BUILDNAVY (fönster 3)
		{	
			// Utplacering av skepp
			placeShip(e);
			
			// READY eller CLEAR knapparna
			if(e.getSource() == readyButton) {
				createGameWindow();
				// skicka READY (navy objekt) till Server.
			}
			else if(e.getSource() == clearButton) {
				placer.Reset();
				resetSquares();
				resetPlacementbuttons();
			}
		}
		else	// Game
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
	}	
	
}	// END OF CLIENT





