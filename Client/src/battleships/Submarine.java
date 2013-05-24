package battleships;

import java.util.TreeSet;

public class Submarine extends Ship {
	
	private static Integer instances_s =0;
	
	public Submarine(Coordinate c){	
		
		super("Submarine", 1);
		
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
