package battleships;

import java.util.TreeSet;

public class Submarine extends Ship {
	
	private static Integer instances;
	
	Submarine(Coordinate c){	
		
		super("Submarine", 1);
		
		coords = new TreeSet<Coordinate>();
		coords.add(c);
		
		++instances;
	}
	
	public Integer getInstances(){
		return instances;
	}
}
