package battleships.message;

import com.thoughtworks.xstream.XStream;

public abstract class Message {
	protected String type;
	
	public Message(String type){
		this.type=type;
	}
	
	/**
	 * Converts the object to an XML-string.
	 * */
	public String toXML(){
		String xml;
		XStream xstream = new XStream();
		xml = xstream.toXML(this);
		return xml;
	}
	
	/**
	 * Returns the type of concrete message.
	 * */
	public String getType(){return type;}
}
