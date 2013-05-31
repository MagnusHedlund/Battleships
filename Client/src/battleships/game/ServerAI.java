/*
 * ServerAI.java	
 * Version 1.0 (2013-05-31)
 */

package battleships.game;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;


/**
 * This class acts as a player on the server. In present it is a predictable opponent since
 * it does not make different placement of ships. It does try to shoot near by the last hit but without
 * consideration of space between ships or their sizes.
 * 
 * @author Åsa Waldhe
 *
 */
public class ServerAI {
	
	private Navy navy;
	private LinkedHashSet<Coordinate> hits;
	private HashSet<Coordinate> oldTargets;
	
	// Set of all coordinates not used, random an index from here to get target c????????????????????????????????
	
	private static final int MAX_X=9, MAX_Y=9, MIN_X=0, MIN_Y=0;
	
	/**
	 * This constructor creates a navy with ships according to the requirements.
	 * 
	 * Building a navy according to the parameters is not yet implemented. The set of ship and their placement is always the same:
	 * 	- 5 submarines
	 * 	- 3 destroyers
	 * 	- 1 aircraft carrier
	 * 
	 * @param int submarines, destroyers, aircraft _carriers	Number of different ships to put in navy.
	 * 
	 */
	public ServerAI(int submarines, int destroyers, int aircraft_carriers){
		
		// Navy with given number of each kind of ship.
		navy = new Navy(submarines, destroyers, aircraft_carriers);
		
		// Ships
		Submarine sub1, sub2, sub3, sub4, sub5;
		Destroyer d1, d2, d3;
		Aircraft_carrier ac;
		
		// Coordinates for placement
		Coordinate c1, c2, c3, c4, c5;
		Coordinate c6, c7, c8, c9, c10, c11, c12, c13, c14;
		Coordinate c15, c16, c17, c18, c19;
		
		// Initiate coordinates and ships
		c1 = new Coordinate(0,0);
		c2 = new Coordinate(0,3);
		c3 = new Coordinate(0,9);
		c4 = new Coordinate(3,6);
		c5 = new Coordinate(4,9);
		c6 = new Coordinate(5,1);
		c7 = new Coordinate(5,2);
		c8 = new Coordinate(5,3);
		c9 = new Coordinate(7,0);
		c10 = new Coordinate(7,1);
		c11 = new Coordinate(7,2);
		c12 = new Coordinate(9,0);
		c13 = new Coordinate(9,1);
		c14 = new Coordinate(9,2);
		c15 = new Coordinate(9,5);
		c16 = new Coordinate(9,6);
		c17 = new Coordinate(9,7);
		c18 = new Coordinate(9,8);
		c19 = new Coordinate(9,9);
		
		sub1 = new Submarine(c1);
		sub2 = new Submarine(c2);
		sub3 = new Submarine(c3);
		sub4 = new Submarine(c4);
		sub5 = new Submarine(c5);
		
		d1 = new Destroyer(c6, c7, c8);
		d2 = new Destroyer(c9, c10, c11);
		d3 = new Destroyer(c12, c13, c14);
		
		ac = new Aircraft_carrier(c15, c16, c17, c18, c19);
		
		navy.addShip(sub1);
		navy.addShip(sub2);
		navy.addShip(sub3);
		navy.addShip(sub4);
		navy.addShip(sub5);
		
		navy.addShip(d1);
		navy.addShip(d2);
		navy.addShip(d3);
		
		navy.addShip(ac);
		
		hits = new LinkedHashSet<Coordinate>();
		oldTargets = new HashSet<Coordinate>();
	}
	
	/**
	 * Invokes the ServerAI to choose a target-Coordinate.
	 * 
	 * @return c	Coordinate from server AI to player.
	 */
	public Coordinate shoot(){
		
		Coordinate c=null;
		
		// If a ship was hit but not sunk before.
		if(!hits.isEmpty()){
			// Choose a coordinate next to the last hit.
			Iterator<Coordinate> it = hits.iterator();
			Coordinate hit = it.next();
			
			while(it.hasNext())
			{
				// Try the coordinate right above, given that it lies in the interval for the coordinates.
				if(hit.getY()> MIN_Y ){
					c = new Coordinate(hit.getX(), hit.getY()-1);
					// Is it used before?
					if(!oldTargets.contains(c))
						return c;
				}
				// Now try with the coordinate beneath the hit, given that it lies in the interval for the coordinates.
				if(hit.getY() < MAX_Y ){
					c = new Coordinate(hit.getX(), hit.getY()+1);
					// Is it used before?
					if(!oldTargets.contains(c))
						return c;
				}
				// What about the coordinate to the left, 
				if(hit.getX()> MIN_X ){
					c = new Coordinate(hit.getX()-1, hit.getY());
					// Is it used before?
					if(!oldTargets.contains(c))
						return c;
				}
				// Finally the coordinate on the right must be a success, if none of the ones above (and given that it lies in the interval for the coordinates).
				if(hit.getX()< MAX_X ){
					c = new Coordinate(hit.getX()+1, hit.getY());
					// Is it used before?
					if(!oldTargets.contains(c))
						return c;
				}
			} // End while
		}// End if
		else { // When the collection of hits is empty.
			// Get random values for coordinates and test whether they are old targets or not. 
			// Could be risky in the way that almost all possible coordinates are used.
			do{
				int x = (int)(10*Math.random());
				int y = (int)(10*Math.random());
				c = new Coordinate(x, y);
			}while(oldTargets.contains(c));				
		}	
		return c;
	}
	
	/**
	 * Lets the server, or other calling agent, get the navy.
	 * 
	 * @return Navy navy
	 */
	public Navy getNavy(){
		return navy;
	}
	
	/**
	 * Takes the result from last shot and registers it in the proper Set of coordinates - hits or oldTargets.
	 *  
	 * @param c	Coordinate or last shot.
	 * @param ship	Ship that was hit, if any.
	 */
	public void setResult(Coordinate c, Ship ship){		
		
		// If ship exists.
		if(ship!=null){
			// If ship is sunk.
			if(ship.isSunk()){				
				// Move ships coordinates from hits to oldTargets.
				hits.removeAll(ship.getCoords());
				oldTargets.addAll(ship.getCoords());
			}else
				hits.add(c); // Register a hit.
		}else
			oldTargets.add(c);		
	}
}
