package battleships.message;

import battleships.Coordinate;
import battleships.Ship;

public class HitMessage {
	private boolean isHit=false;
	private Coordinate coordinate=new Coordinate(1,1);
	private boolean isSunk=false;
	private Ship ship=null;
	
	public HitMessage(){
		this.isHit=isHit;
	}
	public HitMessage(boolean isHit, Coordinate coordinate, boolean isSunk, Ship ship){
		this.isHit=isHit;
		this.coordinate=coordinate;
		this.isSunk=isSunk;
		this.ship=ship;
	}
	public HitMessage(){
	
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

