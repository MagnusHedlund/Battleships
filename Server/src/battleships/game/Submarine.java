/*
 * Submarine.java	
 * Version 1.0 (2013-05-30)
 */

package battleships.game;

import java.util.TreeSet;

/**
 * 
 * @author Åsa Waldhe
 *
 */
public class Submarine extends Ship {
	
	private static Integer instances_s =0;
	public static final int LENGTH_S = 1;
	
	/**
	 * 
	 * @param c
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
	 * 
	 * @return
	 */
	public Integer getInstances(){
		return instances_s;
	}
	
}
