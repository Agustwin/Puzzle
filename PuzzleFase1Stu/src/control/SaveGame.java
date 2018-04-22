package control;

import java.util.Stack;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import command.MoveCommand;

@XmlRootElement(name = "saveGame")
public class SaveGame {
	Stack<MoveCommand>command;
	
	public SaveGame() {}
	
	public Stack<MoveCommand> getStack() {
		return command;
	}
	
	@XmlElement(name="Command")
	public void setStack(Stack<MoveCommand> s) {
		this.command=s;
	}
}
