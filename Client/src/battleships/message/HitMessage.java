package message;

import game.Coordinate;
import game.Ship;

public class HitMessage extends Message{
	private static final String myType="HitMessage";
	private boolean isHit=false;
	private Coordinate coordinate=null;
	private boolean isSunk=false;
	private Ship ship=null;
	
	public HitMessage(){
		super(myType);
	}
	public HitMessage(boolean isHit, Coordinate coordinate, boolean isSunk, Ship ship){
		super(myType);
		this.isHit=isHit;
		this.coordinate=coordinate;
		this.isSunk=isSunk;
		this.ship=ship;
	}

	/* Getters */
	public boolean getIsHit(){return isHit;}
	public Coordinate getCoordinate(){return coordinate;}
	public boolean getIsSunk(){return isSunk;}
	public Ship getShip(){return ship;}
	
	/* Setters */
	public void setIsHit(boolean isHit){this.isHit=isHit;}
	public void setCoordinate(Coordinate shotCoordinate){coordinate=shotCoordinate;}
	public void setIsSunk(boolean isSunk){this.isSunk=isSunk;}
	public void setShip(Ship sunkShip){ship=sunkShip;}
}

