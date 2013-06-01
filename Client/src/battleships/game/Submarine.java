/*
 * Submarine.java	
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
public class Submarine extends Ship {
	
	private static int instances_s =0;
	public static final int LENGTH_S = 1;
	
	/**
	 * Constructor, creates a ship, subclass submarine.
	 * @param c, Coordinate
	 */
	public Submarine(Coordinate c){	
		
		super("Submarine", LENGTH_S);
		
		CompareHorizontal compH = new CompareHorizontal();
		coords = new TreeSet<Coordinate>(compH);
		hits = new TreeSet<Coordinate>(compH);
		coords.add(c);
		
		++instances_s;
	}
	
	/**
	 * Returns how many instances that have been created of the subclass.
	 * @return instances, Integer
	 */
	public int getInstances(){
		return instances_s;
	}
	
}
