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

public class Client implements ActionListener
{
	// Deklarationer
	private Vector<Square> playerSquares = new Vector<Square>(100);		// Inneh�ller spelarens rutor
	private Vector<Square> enemySquares = new Vector<Square>(100);		// Inneh�ller fiendens rutor
	private JFrame window = new JFrame();								// F�nstret
	private enum states { connect, lobby, buildnavy, game };			// Enum r�knare f�r "state"
	private int state = 0;												// Nuvarande state
	private JTextArea information = new JTextArea();					// Informations textf�ltet d�r h�ndelser visas
	private JButton connectButton = new JButton("Connect");				// Connectknappen i startf�nstret.
	private JTextField ipbox = new JTextField();						// IP nummer textbox
	private JTextField portbox = new JTextField();						// Portnummer textbox
	private JLabel connectionError = new JLabel("");					// label som visar fel

	// ------- MAIN -------
	public static void main(String[] args)
	{	
		Client c = new Client();
	}
	
	// Konstruktor
	Client()
	{
		createConnectWindow();
		//createLobbyWindow();
		//createCreateNavyWindow();
		//createGameWindow();
	}
	
	// Skapar CONNECT window
	public void createConnectWindow()
	{
		// S�tt state till connect
		state = states.connect.ordinal();
		
		// F�nsteregenskaper
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(720, 520);
		window.setResizable(false);
		window.setTitle("Project Battleship");
		
		// Meny										--- Beh�ver l�gga till LISTENERS f�r dessa.
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
	
	// Skapar LOBBY window
	public void createLobbyWindow()
	{
		// Rensa f�nstret p� f�reg�ende komponenter
		window.getContentPane().removeAll();	
		
		// S�tt state till lobby
		state = states.lobby.ordinal();
		
		// Visa f�nster
		window.validate();
		window.repaint();
		window.setVisible(true);
	}
	
	// Skapar CREATE_NAVY window
	public void createCreateNavyWindow()
	{
		// Rensa f�nstret p� f�reg�ende komponenter
		window.getContentPane().removeAll();
		
		// S�tt state till buildnavy
		state = states.buildnavy.ordinal();
		
		// Visa f�nster
		window.validate();
		window.repaint();
		window.setVisible(true);
	}	
	
	// Skapar GAME window
	public void createGameWindow()
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

	}
	
	// Anslut till servern
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
	
	// Action events..
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(state == states.connect.ordinal()){														// STATE: CONNECT (f�nster 1)
			if(e.getSource() == connectButton){
				if(checkInput(ipbox.getText().toString(), portbox.getText().toString())){			// Kontrollerar input i textrutorna
					if(connectToServer(ipbox.getText().toString(), portbox.getText().toString()))	// Ansluter till servern
						createGameWindow();														// Om ansluten - G� till lobby
					else
						connectionError.setText("Unable to connect to server.");					// Felmeddelande
				}
				else
					connectionError.setText("Invalid input.");										// Felmeddelande
			}
				
			
		}
		else if(state == states.lobby.ordinal())
		{
			// lobby klicks
		}
		else if(state == states.buildnavy.ordinal())
		{
			// buildnavy klicks
		}
		else	// Game
		{
			// Loopa genom alla rutor vid klick.
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
	}	
	
}	// END OF CLIENT



