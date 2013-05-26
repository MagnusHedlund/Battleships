package battleships.message;

import com.thoughtworks.xstream.XStream;

public abstract class Message {
	protected String type;
	
	public Message(String type){
		this.type=type;
	}
	
	public String toXML(){
		String xml;
		XStream xstream = new XStream();
		xml = xstream.toXML(this);
		return xml;
	}
	public String getType(){return type;}
}
