package command;

import java.io.File;
import java.util.Stack;

import javax.swing.JOptionPane;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import control.Controller;
import control.SaveGame;
import db.MongoController;
import db.XQueryController;

public class SolveCommand implements Command {
	private Controller controller;
	private String db=null;
	
	private MongoController Mongo;
    private XQueryController XQ;
	
	public SolveCommand(Controller c) {
		this.controller=c;		
		
		SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
    	File xmlFile = new File( "./resources/Parameters.xml" );   		    	
    	try {   		
    		System.out.println(xmlFile.getPath());
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
			System.out.println("Ora!");
			MoveCommand m=aux.pop();
			
			
			///////////////////////////
			//Metemos todos los comandos para llegar a la solucion a la coleccion
			if(db.equals("baseX")){
				XQ = new XQueryController();
				
				XQ.addCommandPartida(m);
				XQ.updateSaveGame();
				
			}else if(db.equals("mongo")){		
				Mongo = new MongoController();
				
				BasicDBObject document = m.toDBObjectCommand();
				Mongo.getPartidas().insert(document);
			}
			///////////////////////////
			
			m.undoCommand();
		}
						
		//Mensaje de que se ha solucionado el puzzle
		JOptionPane.showMessageDialog(null,"Puzzle is solved");
	}
}
