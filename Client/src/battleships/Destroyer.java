package battleships;

import java.util.TreeSet;

public class Destroyer extends Ship {

	private final Integer DIRECTION;
	private static Integer instances_d=0;
	
	public Destroyer(Coordinate c1, Coordinate c2, Coordinate c3){
		
		super("Destroyer", 3);
		++instances_d;
		
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
	}
	
	public Integer getInstances(){
		return instances_d;
	}
	
	public Integer getDirection(){
		return DIRECTION;
	}
}
