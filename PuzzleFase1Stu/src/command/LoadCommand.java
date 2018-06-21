package command;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import control.Controller;
import view.PuzzleGUI;

public class LoadCommand implements Command {

	private Controller controller;
	public LoadCommand(Controller c) {
		controller=c;
	}	
	
	@Override
	public void undoCommand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redoCommand() {
		// TODO Auto-generated method stub
		controller.readXML();
	}

}
