package battleships.message;

import battleships.Coordinate;

public class Shot {
	private Coordinate coordinate=null;
	
	public Shot(Coordinate target){
		coordinate = target;
	}
	
	public Shot(int x, int y){
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
