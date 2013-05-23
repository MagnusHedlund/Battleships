package battleships;

import java.util.TreeSet;

public class Aircraft_carrier extends Ship {

	private final Integer DIRECTION;
	private static Integer instances_ac=0;
	
	Aircraft_carrier(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4, Coordinate c5){
		
		super("Aircraft carrier", 5);
		
		DIRECTION = shipsDirection(c1, c2);
		
		if(DIRECTION == HORIZONTAL){
			CompareHorizontal compH = new CompareHorizontal();
			hits = new TreeSet<Coordinate>(compH);
			coords = new TreeSet<Coordinate>(compH);
			}
			else if(DIRECTION == VERTICAL){
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
	
	public Integer getInstances(){
		return instances_ac;
	}
	
	public Integer getDirection(){
		return DIRECTION;
	}
}
