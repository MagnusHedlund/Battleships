package battleships.message;

import battleships.game.Coordinate;
import battleships.game.Ship;
 /**
  * Message for telling whether a shot was a hit or miss.
  * @author Magnus Hedlund
  *
  */
public class HitMessage extends Message{
	/**
	 * String telling type of message, Accessed through inherited method
	 */
	private static final String myType="HitMessage";
	
	/**
	 * Shows if it´s a hit
	 */
	private boolean isHit=false;
	
	/**
	 * The shot coordinate
	 */
	private Coordinate coordinate=null;
	
	/**
	 * Tells whether the shot sunk a ship
	 */
	private boolean isSunk=false;
	
	/**
	 * The sunken ship
	 */
	private Ship ship=null;
	
	
	/**
	 * Constructor
	 */
	public HitMessage(){
		super(myType);
	}
	
	/**
	 * Constructor for initializing the message
	 * @param isHit
	 * @param coordinate
	 * @param isSunk
	 * @param ship
	 */
	public HitMessage(boolean isHit, Coordinate coordinate, boolean isSunk, Ship ship){
		super(myType);
		this.isHit=isHit;
		this.coordinate=coordinate;
		this.isSunk=isSunk;
		this.ship=ship;
	}

	/**
	 * Returns isHit
	 * @return isHit
	 */
	public boolean getIsHit(){return isHit;}
	
	/**
	 * Returns coordinate
	 * @return coordinate
	 */
	public Coordinate getCoordinate(){return coordinate;}
	
	/**
	 * Returns isSunk
	 * @return isSunk
	 */
	public boolean getIsSunk(){return isSunk;}
	
	/**
	 * Returns the hit ship
	 * @return ship
	 */
	public Ship getShip(){return ship;}
	
	/**
	 * Sets isHit
	 * @param isHit
	 */
	public void setIsHit(boolean isHit){this.isHit=isHit;}
	
	/**
	 * Sets coordinate
	 * @param shotCoordinate
	 */
	public void setCoordinate(Coordinate shotCoordinate){coordinate=shotCoordinate;}
	
	/**
	 * Sets isSunk
	 * @param isSunk
	 */
	public void setIsSunk(boolean isSunk){this.isSunk=isSunk;}
	
	/**
	 * Sets ship
	 * @param sunkShip
	 */
	public void setShip(Ship sunkShip){ship=sunkShip;}
}

