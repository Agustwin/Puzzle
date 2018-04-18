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
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import command.Command;
import command.LoadCommand;
import command.MoveCommand;
import command.RandomCommand;
import command.SaveCommand;
import command.SolveCommand;
import db.XQueryController;
import model.Model;
import observer.Observer;
import view.BoardView;
import view.PieceView;
import view.PuzzleGUI;


public class Controller extends AbstractController{
	//Variable para recibir del PuzzleGUI la accion realizada
	private String action;
	
	/*No se si es correcto*/
	private BoardView myView;
	private int posX;
	private int posY;
	private Stack<MoveCommand> moveCommands;
	private Command save;
	private Command load;
	
	public Controller() {
		moveCommands=new Stack();
		save=new SaveCommand(this);
		load=new LoadCommand(this);
		
		//myView=PuzzleGUI.getInstance().getBoardView();
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
					//PuzzleGUI.getInstance().getBoardView().update(PuzzleGUI.getInstance().getBoardView().getGraphics());
					System.out.println("Load Image");
				}
				
				break;
				
			case "saveGame":
				//save.execute();
				try {
					writeXML();
					//writeMongo();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Save data");
				break;
				
			case "loadGame":
				load.execute();	
				System.out.println("Load data");
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
			
			/////////////
			//addcommand xquery
			//////////////			
			
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
		
			/////////////////////
			//XQueryController XQ = new XQueryController();
	    	
	    	//Paso1
	        //Context context = new Context();
	        //String collection = "saveGame";
	        
	        //XQ.createCollection(collection, context);
		//////////////////

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

	public void readXML(){
		try {
			
			while(!moveCommands.isEmpty()) {
				moveCommands.pop().execute();
			}
			
			File file = new File("Save.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(SaveGame.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			SaveGame s = (SaveGame) jaxbUnmarshaller.unmarshal(file);
			Stack aux=s.getStack();
			
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
	
	public void readMongo(){					
			while(!moveCommands.isEmpty()) {
				moveCommands.pop().execute();
			}
			
			/*
			File file = new File("Save.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(SaveGame.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			SaveGame s = (SaveGame) jaxbUnmarshaller.unmarshal(file);
			Stack aux=s.getStack();
			*/
			
			MongoClient mongoClient = new MongoClient("localhost",27017);
			DB db = mongoClient.getDB("saveGame");
			DBCollection collection = db.getCollection("Partidas");
			
			moveCommands.clear();
			//moveCommands=(Stack<MoveCommand>) aux.clone();
			System.err.println("Lectura");
			//for(int i=0;i<moveCommands.size();i++) {
				
			//	System.err.println("Pos0: "+moveCommands.get(i).getPos0()+" Pos1: "+moveCommands.get(i).getPos1());
			//}
			
			for(int i=0;i<moveCommands.size();i++) {
				moveCommands.get(i).setController(this);
				moveCommands.get(i).execute();
			}
				      
}


@Override
public void notifyObservers(int blankPos, int movedPos) {
	// TODO Auto-generated method stub
	for(Observer o:observerList) {
		o.update(blankPos, movedPos);
	}
}
public void addCommand(MoveCommand c) {
	this.moveCommands.push(c);
	
}

public Stack getMoves() {
	// TODO Auto-generated method stub
	return this.moveCommands;
}



}