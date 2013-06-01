/*
 * Aircraft_carrier.java	
 * Version 1.0 (2013-05-30)
 */

package battleships.game;

import java.util.TreeSet;

/**
 * This class extends the abstract class Ship for the game Battleships.
 * 
 * @author Åsa Waldhe
 *
 */
public class Aircraft_carrier extends Ship {
	
	public static final int LENGTH_A = 5;
	private static int instances_ac=0;
	
	/**
	 * Constructor, creates a ship, subclass Destroyer.
	 * @param c1, c2, c3, c4, c5 Coordinate
	 */
	public Aircraft_carrier(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4, Coordinate c5){
		
		super("Aircraft carrier", LENGTH_A);
		
		super.shipsDirection(c1, c2);
		
		if(direction == HORIZONTAL){
			CompareHorizontal compH = new CompareHorizontal();
			hits = new TreeSet<Coordinate>(compH);
			coords = new TreeSet<Coordinate>(compH);
			}
			else if(direction == VERTICAL){
				CompareVertical compV = new CompareVertical();
				hits = new TreeSet<Coordinate>(compV);
				coords = new TreeSet<Coordinate>(compV);
			}                                                           // No more else? exception?
		
		coords.add(c1);
		coords.add(c2);
		coords.add(c3);
		coords.add(c4);
		coords.add(c5);
		
		
		++instances_ac;
	}
	
	/**
	 * Returns how many instances that have been created of the subclass.
	 * @return instances, Integer
	 */
	public int getInstances(){
		return instances_ac;
	}
	
}
