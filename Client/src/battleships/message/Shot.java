package message;

import game.Coordinate;

public class Shot extends Message {
	private Coordinate coordinate=null;
	private static final String myType="Shot";
	
	public Shot(Coordinate target){
		super(myType);
		coordinate = target;
	}
	
	public Shot(int x, int y){
		super(myType);
		coordinate = new Coordinate(x,y);
	}
	
	public Coordinate getCoordinate(){return coordinate;}
	
	public void setCoordinate(Coordinate c){
		coordinate=c;
	}
	
	public void setCoordinate(int x, int y){
		coordinate = new Coordinate(x,y);
	}
	
}
