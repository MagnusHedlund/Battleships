package battleships;

import java.util.Iterator;
import java.util.LinkedList;

public class Navy {
	
	private LinkedList<Ship> ships;
	private Map map;
	private Integer sunkShips;
	private Boolean allGone;
	
	Navy(){
		ships = new LinkedList<Ship>();
		map = new Map();
		sunkShips=0;
		allGone = false;
	}
	
	public void addShip(Ship ship){
		ships.add(ship);
	}
	
	public Map getMap(){
		return map;
	}
	
	public Integer shot(Coordinate c, Ship s){
		// Look at map - exception if not EMPTY!!
		map.getValue(c);
		// Search for coordinate in ships - uses iterator, stop searching if found.
		Iterator<Ship> it = ships.iterator();
		Boolean found = false;
		Boolean sunk = false;
		Integer result = 0;
		
		while(!found||!it.hasNext()){
			Ship tmp=it.next();
			if(tmp.hitByShot(c)){
				found = true;
				// Ask if target ship is sunk. If so put it in parameter ship, s.
				if(tmp.isSunk()){
					sunk = true;
					// Increase sunkShips.
					++sunkShips;
					s = tmp;
					result = map.SUNK;
				}
				result = map.HIT;
			}			
		}		
		if(!found)
			result = map.BOM;
		
		this.markOnMap(c, result);		
		return result;	
		
	}
	
	public Boolean isAllGone(){
		return sunkShips == ships.size();
	}
	
	private void markOnMap(Coordinate c, Integer v){
		map.setValue(c, v);
	}
}
