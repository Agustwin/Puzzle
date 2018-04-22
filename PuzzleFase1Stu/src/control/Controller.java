package control;

import java.awt.event.ActionEvent;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.basex.core.Context;
import org.basex.core.cmd.XQuery;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import command.Command;
import command.MoveCommand;
import command.RandomCommand;
import command.SolveCommand;
import db.MongoController;
import db.XQueryController;
import model.Model;
import observer.Observer;
import view.BoardView;
import view.PieceView;
import view.PuzzleGUI;

public class Controller extends AbstractController{
	//Variable para recibir del PuzzleGUI la accion realizada
	private String action;
	
	private BoardView myView;
	private int posX;
	private int posY;
	public Stack<MoveCommand> moveCommands;
	
    private String db=null;
    
    //Parametros para las bases de datos
    private MongoController Mongo;
    private XQueryController XQ;	   
    
	public Controller() throws IOException{
		moveCommands=new Stack<MoveCommand>();
		
		//Lee el fichero de parametros para saber en que base de datos vamos a trabajar
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
    	
    	// Ponemos un contador para saber cuanto tiempo tarda iniciar y leer la base de datos
        long startTime = System.nanoTime();   
    	
    	//Comprobacion de la base de datos
    	if(db.equals("baseX")){
    		//Paso1	        
            String collection = "saveGame"; 
            XQ = new XQueryController();
            this.XQ.createCollection(collection);	           
            
            this.XQ.queryPartidas("/saveGame/Command");
                       
    	}else if(db.equals("mongo")){
    		
    		Mongo = new MongoController();
    		DBCursor cursor = Mongo.getPartidas().find();
    		
			try {
				while (cursor.hasNext()) {
					MoveCommand m= new MoveCommand((BasicDBObject) cursor.next());
					this.moveCommands.push(m);
				}
			} finally {
				cursor.close();
			}		
			
    	}else{
    		System.out.println("Vuestro documento XML de configuracion tiene mal la base de datos.");
    	}
    	
    	long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		double millis = duration / 1000000.0; // conversion a milisegundos.
		
		System.out.println("Tiempo en cargar Base de datos y los comandos: " + millis + "ms.");    
	}
	
	//Ejecutamos todas las acciones con su correspondiente command
	@Override
	public void actionPerformed(ActionEvent act) {
		// TODO Auto-generated method stub			
		this.action = act.getActionCommand();
		System.out.println(	act.getSource().toString());

		switch (action) {
			case "clutter": 
				RandomCommand random=new RandomCommand(this);
				random.execute();
				break;
				
			case "solve":
				Command solve=new SolveCommand(this);
				solve.execute();				
				break;
				
			case "load":				
				/*--------------------Meter en comando---------------------*/
				File f=PuzzleGUI.getInstance().showFileSelector();
				System.out.println("Path: "+f);
				if(f!=null) {
					PuzzleGUI.getInstance().updateBoard(f);
					notifyObserversReset();
					this.myView=PuzzleGUI.getInstance().getBoardView();
					
					this.getMoves().clear();
					System.out.println("Load Image");
				}				
				break;
				
			case "cleanDB":				
							
				
				// Ponemos un contador para saber cuanto tiempo tarda en limpiar base de datos 
		        long startTime = System.nanoTime();  
				
				if(db.equals("baseX")){		
					while(!this.moveCommands.isEmpty()) {
						System.out.println("Ora!");
						MoveCommand m=this.moveCommands.pop();
						
						this.XQ.removePartida();
						
						m.undoCommand();
					}	
				
					this.XQ.updateSaveGame();
					this.XQ.queryPartidas("/saveGame");
					
				}else if (db.equals("mongo")){	
					while(!this.moveCommands.isEmpty()) {
						System.out.println("Ora!");
						MoveCommand m=this.moveCommands.pop();

						m.undoCommand();
					}
					SaveGame s=new SaveGame();
					s.setStack(this.moveCommands);
								
					DBCursor cursor = Mongo.getPartidas().find();
					
					try{
						while (cursor.hasNext()) {
							Mongo.getPartidas().remove(cursor.next());
						}
					} finally {
						cursor.close();
					}	
				}
				
				long endTime = System.nanoTime();
				long duration = (endTime - startTime);
				double millis = duration / 1000000.0; // conversion a milisegundos.
				
				System.out.println("Tiempo en limpiar Base de datos: " + millis + "ms.");  
						
			break;
				
			case "info":
				JOptionPane.showMessageDialog(null,"Práctica de Agustín López Arribas y Zhong Hao Lin Chen");
				System.out.println("Práctica de Agustín López Arribas y Zhong Hao Lin Chen");		

				break;
				
			default:
				break;
		}
	}			
	
	public void notifyObserversReset() {
		//TODO Auto-generated method stub
		for(Observer o:observerList) {			
			o.setNewBoard();
		}		
	}

	public void mouseClicked(MouseEvent e) {		
		posX=e.getX();
		posY=e.getY();
		System.out.println("X: "+posX+" Y: "+posY);
		int pos[]=PuzzleGUI.getInstance().getBoardView().movePiece(posX, posY);
		if(pos!=null) {
			MoveCommand m=new MoveCommand(this,pos[0],pos[1]);
			this.moveCommands.push(m);
			
        	// Ponemos un contador para saber cuanto tiempo tarda en insertar un documento en mongo o query
            long startTime = System.nanoTime(); 
			
			//////////////////////////
			if(db.equals("baseX")){		
            
	            //Agregamos un comando de paretida a la base de datos
		        this.XQ.addCommandPartida(m);
		        
		        this.XQ.updateSaveGame();
    		
	    		//Mostramos todos los movimientos con el nuevo incluido
	    		this.XQ.queryPartidas("/saveGame/Command");
	    		
	        }else if (db.equals("mongo")){
                			    								
				BasicDBObject document = m.toDBObjectCommand();
				Mongo.getPartidas().insert(document);

	        }
			/////////////////////		
			
	        long endTime = System.nanoTime();
    		long duration = (endTime - startTime);
    		double millis = duration / 1000000.0; // conversion a milisegundos.
    		
    		System.out.println("Tiempo en insertar un comando: " + millis + "ms.");  
			
			m.execute();
		}		
	}
	
	public BoardView getMyView() {
		return myView;
	}
	
	public void setMyView(BoardView myView) {
		this.myView = myView;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public void setPosX(int posX) {
		this.posX = posX;
	}

	/*
	public void writeXML() throws IOException{	
		try {
			
			File file = new File("Save.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(SaveGame.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	
			SaveGame s=new SaveGame();
			s.setStack(moveCommands);
			System.err.println("ESCRITURA");
			for(int i=0;i<moveCommands.size();i++) {
			
				System.err.println("Pos0: "+moveCommands.get(i).getPos0()+" Pos1: "+moveCommands.get(i).getPos1());
				
			}
			
			jaxbMarshaller.marshal(s, file);
			jaxbMarshaller.marshal(s, System.out);
	
		} catch (JAXBException e) {
	    	  e.printStackTrace();
	    }
			
		System.out.println("File Saved!");
	}	

	public void writeMongo(){
	
			SaveGame s=new SaveGame();
			s.setStack(moveCommands);
			System.err.println("ESCRITURA");

			MongoClient mongoClient = new MongoClient("localhost",27017);
			DB db = mongoClient.getDB("saveGame");
			DBCollection collection = db.getCollection("Partidas");
			
			for(int i=0;i<moveCommands.size();i++) {
			
				System.err.println("Pos0: "+moveCommands.get(i).getPos0()+" Pos1: "+moveCommands.get(i).getPos1());
				
				MoveCommand m=new MoveCommand(this,moveCommands.get(i).getPos0(),moveCommands.get(i).getPos1());								
				BasicDBObject document = m.toDBObjectCommand();
				collection.insert(document);
			}		      
	}

	*/

	@SuppressWarnings("unchecked")
	public void readXML(){
		try {			
			while(!moveCommands.isEmpty()) {
				moveCommands.pop().execute();
			}
			
			File file = new File("Save.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(SaveGame.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			SaveGame s = (SaveGame) jaxbUnmarshaller.unmarshal(file);
			Stack<MoveCommand> aux=s.getStack();
			
			moveCommands.clear();
			moveCommands=(Stack<MoveCommand>) aux.clone();
			System.err.println("Lectura");
			for(int i=0;i<moveCommands.size();i++) {				
				System.err.println("Pos0: "+moveCommands.get(i).getPos0()+" Pos1: "+moveCommands.get(i).getPos1());
			}
			
			for(int i=0;i<moveCommands.size();i++) {
				moveCommands.get(i).setController(this);
				moveCommands.get(i).execute();
			}
	
		} catch (JAXBException e) {
			e.printStackTrace();
		}		
	}
	
	/*
	public void readMongo(){					
		while(!moveCommands.isEmpty()) {
			moveCommands.pop().execute();
		}
		
		MongoClient mongoClient = new MongoClient("localhost",27017);
		DB db = mongoClient.getDB("saveGame");
		DBCollection collection = db.getCollection("Partidas");
		
		moveCommands.clear();

		DBCursor cursor = collection.find();
		try {
			while (cursor.hasNext()) {
				//System.out.println(cursor.next().toString());
          
				MoveCommand m= new MoveCommand((BasicDBObject) cursor.next());
				this.moveCommands.push(m);
			}
		} finally {
			cursor.close();
		}
	
		System.err.println("Lectura");
		
		for(int i=0;i<moveCommands.size();i++) {
			moveCommands.get(i).setController(this);
			moveCommands.get(i).execute();
		}				      
	}*/

	@Override
	public void notifyObservers(int blankPos, int movedPos) {
		// TODO Auto-generated method stub
		for(Observer o:observerList) {
			o.update(blankPos, movedPos);
		}
	}
	
	public void addCommand(MoveCommand c) {
		this.moveCommands.push(c);		
		
		// Ponemos un contador para saber cuanto tiempo tarda en insertar un documento en mongo 
        long startTime = System.nanoTime();  
							
		//////////////////////////
		if(db.equals("baseX")){		
		
			//Agregamos un comando de paretida a la base de datos
			this.XQ.addCommandPartida(c);
			
			this.XQ.updateSaveGame();
			
			//Mostramos todos los movimientos con el nuevo incluido
			this.XQ.queryPartidas("/saveGame/Command");
		
		}else if (db.equals("mongo")){
					    								
			BasicDBObject document = c.toDBObjectCommand();
			Mongo.getPartidas().insert(document);

		}
		/////////////////////	
		
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		double millis = duration / 1000000.0; // conversion a milisegundos.
		
		System.out.println("Tiempo en insertar un comando: " + millis + "ms.");   
	}
	
	public Stack<MoveCommand> getMoves() {
		// TODO Auto-generated method stub
		return this.moveCommands;
	}
	
	//Para inicializar la aplicacion ya con los movimientos cargados de la base de datos persistente
	public void getDBMoves(){
    	for(int i=0;i<moveCommands.size();i++) {
			System.out.println(moveCommands.get(i).toString());
			moveCommands.get(i).setController(this);
			moveCommands.get(i).execute();
		}	
	}
}