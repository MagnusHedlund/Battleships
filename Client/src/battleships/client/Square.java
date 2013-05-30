//----------------------------------------------------------------
// Namn: Fredrik Str�mbergsson
// Datum: 2013-05-17
// 
// Square.java:
// �rver fr�n JButton och ritar ut som default en vit knapp med svart kant.
// N�r man klickar p� knappen ska den kunna ha 4 olika utseenden:
// 1. Default (vit)
// 2. Tr�ff   (gr�n)
// 3. Miss    (r�d)
// 4. Skepp   (m�rkgr�)
//----------------------------------------------------------------

package battleships.client;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Square extends JButton
{
	// Deklarationer
	private boolean Alive = true;					// Avg�r om knappen g�r klicka p�.
	private int x = 0;								// X Koordinat
	private int y = 0;								// Y Koordinat
	private Color myColor = Color.WHITE;			// F�rg
	
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
	
   // Ritar ut kanten p� knappen (svart - default)
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
   
   // Indikerar tr�ff
   public void setHit()
   {
	   Alive = false;
	   myColor = Color.GREEN;
	   repaint();	   
   }
   
   // S�tter att detta �r ett skepp
   public void setShipHere()
   {
	   Alive = false;
	   if(myColor == Color.DARK_GRAY || myColor == Color.WHITE)
		   myColor = Color.DARK_GRAY;
	   repaint();	   
   }
   
   // Nollst�ller rutan till default
   public void resetMe()
   {
	   Alive = true;
	   myColor = Color.WHITE;
	   repaint();	   
   }
   
   // GET metoder
   public boolean isAlive() {return Alive;};
   public int getXcoordinate() {return x;};
   public int getYcoordinate() {return y;};

}
