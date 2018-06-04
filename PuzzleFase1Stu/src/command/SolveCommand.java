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
import model.XBaseModel;

public class SolveCommand implements Command {
	private Controller controller;
	private String db=null;
	//Controller tiene la lista de comandos cargada que es lo que hay que deshacer
	public SolveCommand(Controller c) {
		this.controller=c;
		
		SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
    	File xmlFile = new File( "./resources/Parameters.xml" );   		    	
    	try {   		
    		Document document = (Document) builder.build( xmlFile );
    		Element rootNode = document.getRootElement();
    		db=rootNode.getChildTextTrim("db");  		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
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
		Stack<MoveCommand> aux=controller.getMoves();
		while(!aux.isEmpty()) {
			MoveCommand m=aux.pop();	
			m.undoCommand();
		}
		
		///////////////////////////
		//Borramos todos los comandos de las bases de datos al solucionar el puzzle
		if(db.equals("baseX")){
			Stack<MoveCommand> auxStack=new Stack<MoveCommand>();
			Stack<MoveCommand> auxStack2=new Stack<MoveCommand>();
			
			try {			
				File file = new File("Save.xml");
				JAXBContext jaxbContext = JAXBContext.newInstance(SaveGame.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				SaveGame s = (SaveGame) jaxbUnmarshaller.unmarshal(file);
				auxStack=s.getStack();
				
				auxStack2=(Stack<MoveCommand>) auxStack.clone();
							
			} catch (JAXBException e) {
				e.printStackTrace();
			}	
			
			if(auxStack != null){
				//Elimina todos los nodos command de una partida
				while(!auxStack.isEmpty()) {
					auxStack.pop();
					
					XBaseModel.removePartida();
				}
				XBaseModel.updateSaveGame();
			}		
		
		}else if(db.equals("mongo")){		
			DBCursor cursor = MongoModel.getPartidas().find();
			
			try{
				while (cursor.hasNext()) {
					MongoModel.getPartidas().remove(cursor.next());
				}
			} finally {
				cursor.close();
			}
		}
		///////////////////////////
		
		//Mensaje de que se ha solucionado el puzzle
		JOptionPane.showMessageDialog(null,"Puzzle is solved");
		
	}
}
