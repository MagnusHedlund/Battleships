/*
 * Map.java	
 * Version 1.0 (2013-05-30)
 */

package battleships.game;

/**
 * Map contains a two dimensional array symbols the ocean in the game Battleships.
 * The class uses constants for empty (not used space), hit, bom or sunk ship.
 * 
 * @author Åsa Waldhe
 *
 */
public class Map {

	public final int EMPTY=0, HIT=1, BOM=2, SUNK=3;  // SHIP??? Only for the client to see
	private static final int SIZE = 10;
	private int[][] ocean;
	
	/**
	 * Initiates an empty array.
	 */
	Map(){
		ocean = new int[SIZE][SIZE];
	}
	
	/**
	 * Returns the array ocean.
	 * 
	 * @return int[][]
	 */
	public int[][] getOcean(){
		return ocean;
	}
	
	/**
	 * Marks hit, bom or sunk in the element corrseponding to a coordinate.
	 * 
	 * @param c, Coordinate 
	 * @param i, Integer	Integer representing bom, hit or sunk.
	 */
	public void setValue(Coordinate c, int i){
		ocean[c.getY()][c.getX()]=i;
	}

	/**
	 * Returns the value from the element in tha array corresponding to chosen coordinate.
	 * 
	 * @param c, Coordinate
	 * @return Integer, representing empty (not used), hit, bom or sunk ship.
	 */
	public int getValue(Coordinate c){
		return ocean[c.getY()][c.getX()];
	}
}
