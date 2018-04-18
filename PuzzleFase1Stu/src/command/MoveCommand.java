package command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.mongodb.BasicDBObject;

import control.Controller;
import view.PuzzleGUI;

@XmlRootElement
public class MoveCommand implements Command{

	
	private Controller controller;

	private int pos0;
	private int pos1;
	
	public MoveCommand() {
		
	}
	public MoveCommand(Controller c, int posX, int posY) {
		this.controller=c;	
		this.pos0=posX;
		this.pos1=posY;
	}
	
	@Override
	public void undoCommand() {
		// TODO Auto-generated method stub

			controller.notifyObservers(pos1,pos0);
				
			
		}
		
		
	
		
	@Override
	public void redoCommand() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void execute() {
	controller.notifyObservers(pos0,pos1);
	}


	@XmlElement
	public void setPos0(int p) {
		pos0=p;
	}
	public int getPos0() {
		return pos0;
	}
	
	
	@XmlElement
	public void setPos1(int p) {
		pos1=p;
	}
	public int getPos1() {
		return pos1;
	}
	/*
	public Controller getController() {
		return this.controller;
	}
	*/
	public void setController(Controller c) {
		this.controller=c;
	}
	
	/*
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
	*/
}
