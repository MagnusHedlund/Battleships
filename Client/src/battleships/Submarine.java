package battleships;

import java.util.TreeSet;

public class Submarine extends Ship {
	
	private static Integer instances_s =0;
	public static final int LENGTH_S = 1;
	
	public Submarine(Coordinate c){	
		
		super("Submarine", LENGTH_S);
		
		CompareHorizontal compH = new CompareHorizontal();
		coords = new TreeSet<Coordinate>(compH);
		hits = new TreeSet<Coordinate>(compH);
		coords.add(c);
		
		++instances_s;
	}
	
	public Integer getInstances(){
		return instances_s;
	}
	
}
