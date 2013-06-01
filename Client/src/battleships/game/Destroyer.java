/*
 * Destroyer.java	
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
public class Destroyer extends Ship {

	public static final int LENGTH_D = 3;
	private static int instances_d=0;
	
	
	/**
	 * Constructor, creates a ship, subclass Destroyer.
	 * @param c1, c2, c3 Coordinate
	 */
	public Destroyer(Coordinate c1, Coordinate c2, Coordinate c3){
		
		super("Destroyer", LENGTH_D);
		++instances_d;
		
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
	}
	
	/**
	 * Returns how many instances that have been created of the subclass.
	 * @return instances, Integer
	 */
	public int getInstances(){
		return instances_d;
	}
	
}
