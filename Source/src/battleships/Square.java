package battleships;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;


@SuppressWarnings("serial")
public class Square extends JButton
{
	// Deklarationer
	private boolean Alive = true;					// Avgör om knappen går klicka på.
	private int x = 0;								// X Koordinat
	private int y = 0;								// Y Koordinat
	private Color myColor = Color.WHITE;			// Färg
	
	// Konstruktor
	Square(int px, int py, boolean palive)
	{
		Alive = palive;
		x = px;
		y = py;
		setBorderPainted(true);
	}
	
	// Ritar ut knappen (vit - default)
    @Override
    protected void paintComponent(Graphics g) 
    {
       Graphics2D g2 = (Graphics2D) g.create(); 
       g2.setPaint(myColor);
       g2.fillRect(0, 0, getWidth(), getHeight());
       g2.dispose();
   }
	
   // Ritar ut kanten på knappen (svart - default)
   @Override
   public void paintBorder(Graphics g) 
   {
       g.setColor(Color.BLACK);
       g.drawRect(0, 0, getWidth(), getHeight());
   }
   
   // Indikerar miss
   public void setMiss()
   {
	   Alive = false;
	   myColor = Color.RED;
	   repaint();
   }
   
   // Indikerar träff
   public void setHit()
   {
	   Alive = false;
	   myColor = Color.GREEN;
	   repaint();	   
   }
   
   // Sätter att detta är ett skepp
   public void setShipHere()
   {
	   Alive = false;
	   myColor = Color.DARK_GRAY;
	   repaint();	   
   }
   
   // GET metoder
   public boolean isAlive() {return Alive;};
   public int getXcoordinate() {return x;};
   public int getYcoordinate() {return y;};

}
