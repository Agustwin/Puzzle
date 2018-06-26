package command;

import java.io.File;
import java.util.Stack;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;

import com.mongodb.DBCursor;

import control.Controller;
import control.SaveGame;
import model.MongoModel;
import model.BaseXModel;

public class SolveCommand implements Command {
	private Controller controller;
	private String db=null;
	//Controller tiene la lista de comandos cargada que es lo que hay que deshacer
	public SolveCommand(Controller c) {
		this.controller=c;
		
			}
	
	@Override
	public void undoCommand() {
		// TODO Auto-generated method stub
		
	}
	
	//Deshace todos los command del controller
	@Override
	public void redoCommand() {
		// TODO Auto-generated method stub
			
				Stack<MoveCommand> aux=controller.getMoves();
				while(!aux.isEmpty()) {
					MoveCommand m=aux.pop();	
					m.undoCommand();
				}
				
				///////////////////////////
				//Borramos todos los comandos de las bases de datos al solucionar el puzzle
				this.controller.getMyModel().remove();
				///////////////////////////
				
				
	}

	
}
