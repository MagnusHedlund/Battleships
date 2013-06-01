/*
 * Navy.java	
 * Version 1.0 (2013-05-31)
 */

package battleships.game;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author Åsa Waldhe
 *
 */
public class Navy {
	
	private final int SUBMARINES, DESTROYERS, AIRCRAFT_CARRIERS;
	
	private LinkedList<Ship> ships;
	private Map map;
	private int sunkShips;
	
	/**
	 * Initiates member variables and constants.
	 * 
	 * @param s	Number submarines
	 * @param d	Number destroyers
	 * @param ac Number aircraft carriers
	 */
	public Navy(int s, int d, int ac){
		ships = new LinkedList<Ship>();
		
		SUBMARINES = s;
		DESTROYERS = d;
		AIRCRAFT_CARRIERS = ac;
		
		map = new Map();
		sunkShips=0;
		
	}
	
	/**
	 * Adds a ship to the navy.
	 * 
	 * @param ship
	 */
	public void addShip(Ship ship){
		ships.add(ship);
	}
	
	/**
	 * Returns the map.
	 * 
	 * @return map, Map
	 */
	public Map getMap(){
		return map;
	}
	
	/**
	 * Returns the ships in the navy.
	 * 
	 * @return	LinkedList<Ships> ships	All ships in the navy.
	 */
	public LinkedList<Ship> getShips(){
		return ships;
	}
	
	/**
	 * Administers a shot to the navy. Marks it on the map an returns a ship if hit.
	 * To know if the ship is sunk the caller have to ask the returned ship.
	 * 
	 * @param c			The target-Coordinate.
	 * @return			The Ship that was hit or null, if no hit.
	 */
	public Ship shot(Coordinate c){
		// Look at map - exception if not EMPTY!!
		map.getValue(c);
		// Search for coordinate in ships - uses iterator, stop searching if found.
		Iterator<Ship> it = ships.iterator();
		Boolean found = false;
		
		while(!found&&it.hasNext()){
			Ship tmp=it.next();
			if(tmp.hitByShot(c)){
				found = true;
				// Ask if target ship is sunk. If so put it in parameter ship, s.
				if(tmp.isSunk()){					
					// Increase sunkShips.
					++sunkShips;
					this.markOnMap(c, map.SUNK); 
				}
				else
					this.markOnMap(c, map.HIT);				
				return tmp;
			}			
		}		
		if(!found)
			this.markOnMap(c, map.BOM);
				
		return null;	
		
	}
	
	/**
	 * Returns whether all ships are sunk or not.
	 * 
	 * @return Boolean
	 */
	public Boolean allGone(){
		return sunkShips == SUBMARINES+DESTROYERS+AIRCRAFT_CARRIERS;
	}
	
	/**
	 * 
	 * @return amount of ships
	 */
	public int numberShips(){
		return ships.size();
	}
	
	/**
	 * Returns if all ships in the navy are placed.
	 * 
	 * @return
	 */
	public boolean allSet(){
		return ships.size() == SUBMARINES+DESTROYERS+AIRCRAFT_CARRIERS;
	}
	
	/**
	 * Sets value from a shot on the map.
	 * 
	 * @param c, Coordinate	Coordinate to mark, value for hit, sunk or miss.
	 * @param v
	 */
	private void markOnMap(Coordinate c, int v){
		map.setValue(c, v);
	}
	
	
}
