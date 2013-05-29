package battleships.game;

import java.util.Iterator;
import java.util.LinkedList;

public class Navy {
	
	private final int SUBMARINES, DESTROYERS, AIRCRAFT_CARRIERS;
	
	private LinkedList<Ship> ships;
	private Map map;
	private Integer sunkShips;
	
	Navy(int s, int d, int ac){
		ships = new LinkedList<Ship>();
		
		SUBMARINES = s;
		DESTROYERS = d;
		AIRCRAFT_CARRIERS = ac;
		
		map = new Map();
		sunkShips=0;
	//	allGone = false;
	}
	
	public void addShip(Ship ship){
		ships.add(ship);
	}
	
	public Map getMap(){
		return map;
	}
	
	public LinkedList<Ship> getShips(){
		return ships;
	}
	
	public Ship shot(Coordinate c){
		// Look at map - exception if not EMPTY!!
		map.getValue(c);
		// Search for coordinate in ships - uses iterator, stop searching if found.
		Iterator<Ship> it = ships.iterator();
		Boolean found = false;
		//Boolean sunk = false;
		Integer result = 0;
		
		while(!found&&it.hasNext()){
			Ship tmp=it.next();
			if(tmp.hitByShot(c)){
				found = true;
				// Ask if target ship is sunk. If so put it in parameter ship, s.
				if(tmp.isSunk()){
					// sunk = true;
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
			this.markOnMap(c, map.HIT);
				
		return null;	
		
	}
	
	public Boolean allGone(){
		return sunkShips == SUBMARINES+DESTROYERS+AIRCRAFT_CARRIERS;
	}
	
	public int numberShips(){
		return ships.size();
	}
	
	public boolean allSet(){
		return ships.size() == SUBMARINES+DESTROYERS+AIRCRAFT_CARRIERS;
	}
	
	private void markOnMap(Coordinate c, int v){
		map.setValue(c, v);
	}
	
	
}