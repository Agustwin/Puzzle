package control;

import java.util.Stack;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.*;

import command.MoveCommand;

//Clase que nos permite realizar el marshall y unmarshall de la pila de comandos
@XmlRootElement(name = "saveGame")
public class SaveGame {
	Stack<MoveCommand>command;
	
	public SaveGame() {}
	
	public Stack<MoveCommand> getStack() {
		return command;
	}
	//Método que nos va a permitir escribir la lista de comandos en el XML
	@XmlElement(name="Command")
	public void setStack(Stack<MoveCommand> s) {
		this.command=s;
	}
}
