/*
 * Square.java
 * Version 1.0 (2013-05-30)
 */

package battleships.client;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;

/**
 * Square: a modified JButton. Represents a square in the game.
 * 
 * @author Fredrik Strömbergsson
 */
@SuppressWarnings("serial")
public class Square extends JButton
{
	// Deklarationer
	private boolean Alive = true;					// Avgör om knappen går klicka på.
	private int x = 0;								// X Koordinat
	private int y = 0;								// Y Koordinat
	private Color myColor = Color.WHITE;			// Färg
	private boolean iShip = false;					// Skepp?
	
	// Konstruktor
	Square(int px, int py, boolean palive)
	{
		Alive = palive;
		x = px;
		y = py;
		setBorderPainted(true);
	}
	
	/**
	 * Paints the button
	 */
    @Override
    protected void paintComponent(Graphics g) 
    {
       Graphics2D g2 = (Graphics2D) g.create(); 
       g2.setPaint(myColor);
       g2.fillRect(0, 0, getWidth(), getHeight());
       g2.dispose();
   }
	
	/**
	 * Paints the button border
	 */
   @Override
   public void paintBorder(Graphics g) 
   {
       g.setColor(Color.BLACK);
       g.drawRect(0, 0, getWidth(), getHeight());
   }
   
	/**
	 * Sets the button to be a "miss" button with color: RED.
	 */
   public void setMiss()
   {
	   Alive = false;
	   myColor = Color.RED;
	   repaint();
   }
   
	/**
	 * Sets the button to be a "bom" button with color: ORANGE.
	 * "bom" only happens from the Clients own perspective.
	 */
  public void setBom()
  {
	   Alive = false;
	   myColor = Color.orange;
	   repaint();
  }
   
	/**
	 * Sets the button to be a "hit" button with color: GREEN.
	 */
   public void setHit()
   {
	   Alive = false;
	   myColor = Color.GREEN;
	   repaint();	   
   }
   
	/**
	 * Sets the button to be a "ship" button.
	 */
   public void setShipHere()
   {
	   Alive = false;
	   iShip = true;
	   if(myColor == Color.DARK_GRAY || myColor == Color.WHITE)
		   myColor = Color.DARK_GRAY;
	   repaint();	   
   }
   
	/**
	 * Resets this button to default.
	 */
   public void resetMe()
   {
	   Alive = true;
	   myColor = Color.WHITE;
	   repaint();	   
   }
   
	/**
	 * GET - Is this button alive? (clickable)
	 */
   public boolean isAlive() {return Alive;};
   
	/**
	 * GET - The X coordinate.
	 */
   public int getXcoordinate() {return x;};
   
	/**
	 * GET - The Y coordinate
	 */
   public int getYcoordinate() {return y;};
   
	/**
	 * GET - Is there a Ship on this square?
	 */  
   public boolean isAship() {return iShip;}

}
