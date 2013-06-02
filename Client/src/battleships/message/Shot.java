package battleships.message;

import battleships.game.Coordinate;
/**
 * Message containing information about a shot
 * @author Magnus Hedlund
 *
 */
public class Shot extends Message {
	/**
	 * The shot coordinate
	 */
	private Coordinate coordinate=null;
	
	/**
	 * String telling type of message, Accessed through inherited method
	 */
	private static final String myType="Shot";
	
	/**
	 * Initializing Constructor
	 * @param target
	 */
	public Shot(Coordinate target){
		super(myType);
		coordinate = target;
	}
	
	/**
	 * Initializing constructor
	 * @param x
	 * @param y
	 */
	public Shot(int x, int y){
		super(myType);
		coordinate = new Coordinate(x,y);
	}
	
	/**
	 * Returns coordinate
	 * @return coordinate
	 */
	public Coordinate getCoordinate(){return coordinate;}
	
	/**
	 * Sets coordinate
	 * @param c
	 */
	public void setCoordinate(Coordinate c){
		coordinate=c;
	}
	
	/**
	 * Sets coordinate
	 * @param x
	 * @param y
	 */
	public void setCoordinate(int x, int y){
		coordinate = new Coordinate(x,y);
	}
	
}
