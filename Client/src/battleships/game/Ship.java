/*
 * Ship.java	
 * Version 1.0 (2013-05-31)
 */

package battleships.game;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Ship is an abstract class for being used in the game Battleships. 
 * It contains methods and data that are important for ships in the game.
 * 
 * @author Åsa Waldhe
 *
 */
public abstract class Ship {
	
	protected static final int HORIZONTAL=0;
	protected static final int VERTICAL=1;
	
	protected final String NAME; // Initiated in each subclass's constructor..
	protected final int LENGTH; // Initiated in each subclass's constructor.	
	
	protected TreeSet<Coordinate> coords;
	protected TreeSet<Coordinate> hits;
	protected Boolean sunk;
	
	protected Integer direction=null;
	
	/**
	 * Initiate variables and constant. 
	 * 
	 * @param n, String	The name of the ship - from the subclass.
	 * @param l, int	Length of the ship.
	 */
	public Ship(String n, int l ){			
		sunk = false;
		NAME = n;
		LENGTH = l;
	}
	
	/**
	 * Overrides the method toString
	 * 
	 */
	public String toString(){
		return "Abstract class Ship";
	}
	
	/**
	 * Returns the name of the ship - different for each class.
	 * 
	 * @return name	The name of ship-type, as a String.
	 */
	public String getName(){
		return NAME;
	}
	
	/**
	 * Returns the first (upper left) coordinate in the ship. 
	 * 
	 * @return Coordinate					
	 */
	public Coordinate getFirstCoord(){
		return coords.first();
	}
	
	/**
	 * Answers whether the direction of the ship i vertical or horizontal.
	 * 
	 * @return Integer, direction - represented by constants HORIZONTAL=0, VERTICAL=1.
	 */
	public int getDirection(){
		return direction;
	}
	
	/**
	 * Returns the length of the ship.
	 * 
	 * @return LENGTH, Integer	
	 */
	public int getLength(){
		return LENGTH;
	}
	
	/**
	 * Returns whether the ship contains the specified coordinate.
	 * 
	 * @param coord, Coordinate
	 * @return Boolean
	 */
	public Boolean containsCoord(Coordinate coord){
		Iterator<Coordinate> it = coords.iterator();
		while(it.hasNext()){
			Coordinate test = it.next();
			if(coord.getX() == test.getX() && coord.getY() == test.getY())
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	protected int shipsDirection(Coordinate c1, Coordinate c2){
		if( c1.getY() == c2.getY() ){
			direction = HORIZONTAL;
			return direction;
		}
		else
		{
			direction = VERTICAL;
			return direction;
		}		
	}
	
	/**
	 * Returns whether the ship is hit by a shot by comparing the coordinate argument with the ships coordinates.
	 * Increments ads the coordinate to the array hits if true.
	 * 
	 * @param coord
	 * @return constant Integer 
	 */
	public Boolean hitByShot(Coordinate coord){
		if(containsCoord(coord)){
			if(!hits.add(coord)){ // Throw exception?			
			}
			return true;
		}
		else
			return false;				
	}
	
	/**
	 * Is the ship sunk?
	 * 
	 * @return	Boolean	Sunk or not
	 */
	public Boolean isSunk(){
		return hits.size()==LENGTH;		
	}
	
	/**
	 * Returns all coordinates in the ship.
	 * 
	 * @return TreeSet<Coordinate>	All coordinates covered by the ship.
	 */
	public TreeSet<Coordinate> getCoords(){
		return coords;
		
	}
	
	/**
	 * Provides a way to set all coordinates in a ship.
	 * 
	 * @param c, TreeSet<Coordinate>
	 */
	public void setCoords(TreeSet<Coordinate> c){
		coords.clear();
		coords.addAll(c);
	}
	
	

}
