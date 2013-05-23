package battleships;

// import java.util.Arrays;

public class Map {

	public final int EMPTY=0, HIT=1, BOM=2, SUNK=3;  // SHIP??? Only for the client to see
	private static final int SIZE = 10;
	private int[][] ocean;
	
	Map(){
		ocean = new int[SIZE][SIZE];
	//	Arrays.fill(ocean, 1);		
	}
	
	public int[][] getOcean(){
		return ocean;
	}
	
	public void setValue(Coordinate c, Integer i){
		ocean[c.getX()][c.getY()]=i;
	}

	
	public Integer getValue(Coordinate c){
		return ocean[c.getX()][c.getY()];
	}
}
