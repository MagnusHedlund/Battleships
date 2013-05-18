package battleships;

import java.util.Arrays;

public class Map {

	protected static final Integer EMPTY=0, HIT=1, BOM=3, SUNK=4;  // SHIP??? Only for the client to see
	private static final Integer SIZE = 10;
	private Integer[][] ocean;
	
	Map(){
		ocean = new Integer[SIZE][SIZE];
		Arrays.fill(ocean, EMPTY);		
	}
	
	public Integer[][] getOcean(){
		return ocean;
	}
	
	public void setValue(Coordinate c, Integer i){
		ocean[c.getX()][c.getY()]=i;
	}

	
	public Integer getValue(Coordinate c){
		return ocean[c.getX()][c.getY()];
	}
}
