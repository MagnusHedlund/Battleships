/*
 * Destroyer.java	
 * Version 1.0 (2013-05-30)
 */

package battleships.game;

import java.util.TreeSet;

public class Destroyer extends Ship {

	public static final int LENGTH_D = 3;
	private static Integer instances_d=0;
	
	
	
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
	
	public Integer getInstances(){
		return instances_d;
	}
	
}
