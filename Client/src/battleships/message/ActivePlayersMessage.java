package message;

import java.util.HashMap;
import java.util.Map;

public class ActivePlayersMessage extends Message {
	
	private static final String myType="ActivePlayersMessage";
	private Map<String, Integer> contenders = new HashMap<String,Integer>();
	
	public ActivePlayersMessage() {
		super(myType);
	}
	
	public ActivePlayersMessage(HashMap<String,Integer> contenders) {
		super(myType);
		this.contenders=contenders;
	}
	
	public Map<String, Integer> getContenders(){
		return contenders;
	}

	public void setContenders(HashMap<String,Integer> contenders){
		this.contenders=contenders;
	}
}
