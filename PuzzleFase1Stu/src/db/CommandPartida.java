package db;
import javax.xml.bind.annotation.XmlRootElement;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;


@XmlRootElement(name="Command")
public class CommandPartida {
		
	private String pos0;
	private String pos1;	
	
	public CommandPartida(String pos0, String pos1) {
		super();

		this.pos0 = pos0;
		this.pos1 = pos1;
	}

	public String getPos0() {
		return pos0;
	}

	public void setPos0(String pos0) {
		this.pos0 = pos0;
	}

	public String getPos1() {
		return pos1;
	}

	public void setPos1(String pos1) {
		this.pos1 = pos1;
	}
	
	// Transformo un objecto que me da MongoDB a un Objecto Java
	public CommandPartida(BasicDBObject dBObjectCommand) {
		this.pos0 = dBObjectCommand.getString("pos0");
		this.pos1 = dBObjectCommand.getString("pos1");
	}
	
	public BasicDBObject toDBObjectCommand() {

	    // Creamos una instancia BasicDBObject
	    BasicDBObject dBObjectCommand = new BasicDBObject();

	    dBObjectCommand.append("pos0", this.getPos0());
	    dBObjectCommand.append("pos1", this.getPos1());
	    
	    return dBObjectCommand;
	}

	@Override
	public String toString() {
		return "CommandPartida [pos0=" + pos0 + ", pos1=" + pos1 + "]";
	}

}
