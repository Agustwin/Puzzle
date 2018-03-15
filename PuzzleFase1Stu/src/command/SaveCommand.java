package command;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.InfoDB;
import org.basex.core.cmd.XQuery;

import control.Controller;
import model.AbstractModel;
import model.Model;
import model.PieceModel;
import view.PuzzleGUI;

public class SaveCommand implements Command {

	private Controller controller;
	public SaveCommand(Controller c) {
		controller=c;
	}	
	
	@Override
	public void undoCommand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redoCommand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		try {
			PuzzleGUI.getInstance().getBoardView().writeXML();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
