package battleships;
/**
 * 
 * @author asa
 *
 */
public class ServerAI {
	
	private Map map;
	private Coordinate[] hits;
	private Coordinate[] oldTargets;
	
	publicServerAI(){
		
	}
	
	public Coordinate shoot(){
		int x = 0;
		int y= 0;
		Coordinate c = new Coordinate(x, y);
		return;
	}
	
	public int shot(Coordinate c){
		int result=0;
		return result;
	}
	
	public void retrieveResult(Ship s, int result){
		if(s){
			
			if(s.isSunk()){
				// Move coordinates from hits to oldTargets.
			}
			else{
				// Put ship's coordinate in his[].
			}
		}
			
		
	}
	

}
