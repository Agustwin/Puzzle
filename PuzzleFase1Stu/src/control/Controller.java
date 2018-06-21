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

import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

import command.Command;
import command.LoadCommand;
import command.MoveCommand;
import command.RandomCommand;
import command.SaveCommand;
import command.SolveCommand;
import model.AbstractModel;
import model.MongoModel;
import model.BaseXModel;
import observer.Observer;
import view.BoardView;
import view.PieceView;
import view.PuzzleGUI;

public class Controller extends AbstractController{
	//Variable para recibir del PuzzleGUI la accion realizada
	private String action;
	

	private int posX;
	private int posY;
	private Stack<MoveCommand> moveCommands;
	private AbstractModel myModel;	
	
	public Controller() {
		moveCommands=new Stack<MoveCommand>();
    }	
	
	//Ejecutamos todas las acciones con su correspondiente command
	@Override
	public void actionPerformed(ActionEvent act) {
		
		long startTime,endTime;
		long Time;
		double initialStorage,finalStorage,TransactionStorage;
		// TODO Auto-generated method stub			
		this.action = act.getActionCommand();
		System.out.println(	act.getSource().toString());

		switch (action) {
			case "clutter": 
				//preguntar si se puede llamar
				RandomCommand random=new RandomCommand(this,PuzzleGUI.getInstance().rowNum*PuzzleGUI.getInstance().columnNum);
				 startTime=System.nanoTime();
				  initialStorage=myModel.getStorage();
				
				random.redoCommand();
				
				 endTime=System.nanoTime(); 
				  finalStorage=myModel.getStorage();
				
				 
				 Time = (long) ((endTime - startTime) / 1000000.0);
				  TransactionStorage=Math.abs(finalStorage-initialStorage);
				PuzzleGUI.getInstance().setStats("Clutter: ", Time,finalStorage,TransactionStorage,Time/TransactionStorage);
				
				break;
				
			case "solve":
				Command solve=new SolveCommand(this);
				
				 startTime=System.nanoTime();
				 initialStorage=myModel.getStorage();
				
				solve.redoCommand();
				
				 endTime=System.nanoTime(); 
				finalStorage=myModel.getStorage();
				
				 Time = (long) ((endTime - startTime) / 1000000.0);
				  TransactionStorage=Math.abs(finalStorage-initialStorage);
				  double ratio=0;
				  if(TransactionStorage!=0){
						 ratio=Time/TransactionStorage;

					}else{
						 ratio=0;

					}
				//Mensaje de que se ha solucionado el puzzle
					JOptionPane.showMessageDialog(null,"Puzzle is solved");
				  
					PuzzleGUI.getInstance().setStats("Solve: ", Time,finalStorage,TransactionStorage,ratio);
				break;
				
			case "load":
				
				/*--------------------Meter en comando---------------------*/
				File f=PuzzleGUI.getInstance().showFileSelector();
				System.out.println("Path: "+f);
								
			    if(f!=null) {
			    	//Método para introducir los parámetros
			    	PuzzleGUI.getInstance().enterParameters();
			    	//Método para actualizar la imagen de la boardView	
					PuzzleGUI.getInstance().updateBoard(f);
					notifyObserversReset(PuzzleGUI.getInstance().rowNum,PuzzleGUI.getInstance().columnNum,PuzzleGUI.getInstance().imageSize);						
					this.getMoves().clear();

					System.out.println("Load Image");
				}				
				
				break;
				
			case "saveGame":
				SaveCommand save=new SaveCommand(this);
				save.redoCommand();				
				System.out.println("Save data");
				break;
				
			case "loadGame":
				LoadCommand load=new LoadCommand(this);
				load.redoCommand();	
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
	
	public void notifyObserversReset(int rowNum,int columnNum,int imageSize) {
		//TODO Auto-generated method stub
		for(Observer o:observerList) {			
			o.setNewBoard(rowNum,columnNum,imageSize);
		}		
	}

	public void mouseClicked(MouseEvent e) {	
		
		long startTime,endTime;
		
		startTime=System.nanoTime();
		double initialStorage=myModel.getStorage();
		posX=e.getX();
		posY=e.getY();
		System.out.println("X: "+posX+" Y: "+posY);
		int pos[]=PuzzleGUI.getInstance().getBoardView().movePiece(posX, posY);
		
		if(pos!=null) {
			MoveCommand m=new MoveCommand(this,pos[0],pos[1]);
			System.err.println(moveCommands);
			this.moveCommands.push(m);
			m.redoCommand();
			
			endTime=System.nanoTime();
			double finalStorage=myModel.getStorage();
			
			long Time = (long) ((endTime - startTime) / 1000000.0);
			double TransactionStorage=Math.abs(finalStorage-initialStorage);
			PuzzleGUI.getInstance().setStats("Move: ", Time,finalStorage,TransactionStorage,Time/TransactionStorage);
			
			if(PuzzleGUI.getInstance().getBoardView().checkWin()) {
				JOptionPane.showMessageDialog(null,"Puzzle is solved");
			}
		}
		
		
		//Compruebo si ha ganado
		
		
	}
	
	public AbstractModel getMyModel() {
		return myModel;
	}

	public void setMyModel(AbstractModel Model) {
		this.myModel = Model;
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
			
			File file = new File("SaveGame.xml");
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

	@SuppressWarnings("unchecked")
	public void readXML(){
		
		try {
			
			while(!moveCommands.isEmpty()) {
				moveCommands.pop().redoCommand();
			}
			
			File file = new File("SaveGame.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(SaveGame.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			SaveGame s = (SaveGame) jaxbUnmarshaller.unmarshal(file);
			Stack<MoveCommand> aux=s.getStack();
			
			moveCommands.clear();
			if(aux != null){
				moveCommands=(Stack<MoveCommand>) aux.clone();
			
				System.err.println("Lectura");
				for(int i=0;i<moveCommands.size();i++) {
					
					System.err.println("Pos0: "+moveCommands.get(i).getPos0()+" Pos1: "+moveCommands.get(i).getPos1());
				}
				
				for(int i=0;i<moveCommands.size();i++) {
					moveCommands.get(i).setController(this);
					moveCommands.get(i).redoCommand();
				}	
			}
						
		} catch (JAXBException e) {
			e.printStackTrace();
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
	
	public Stack<MoveCommand> getMoves() {
		// TODO Auto-generated method stub
		return this.moveCommands;
	}
	
	//Se encarga de inicializar todos los modelos tras haber cargado de la base de datos los movimientos
	public void LoadGame(AbstractModel Model){
		
		this.myModel=Model;
		// Ponemos un contador para saber cuanto tiempo tarda iniciar y leer la base de datos
        long startTime = System.nanoTime();   
		double initialStorage=myModel.getStorage();

    	//Comprobacion de la base de datos y cargamos del modelo
    	moveCommands=Model.loadMoves();
				
		if(moveCommands!=null){
			for(int i=0;i<moveCommands.size();i++){
				moveCommands.get(i).setController(this);
    			moveCommands.get(i).redoCommand();
    		}
		}
		
		long endTime = System.nanoTime();
		double finalStorage=myModel.getStorage();

		
		long Time = (long) ((endTime - startTime) / 1000000.0);		
		double TransactionStorage=finalStorage;
		double ratio;
		if(TransactionStorage!=0){
			 ratio=Time/TransactionStorage;

		}else{
			 ratio=0;

		}
		
		PuzzleGUI.getInstance().setStats("Load: ", Time,finalStorage,TransactionStorage,ratio);
	}
}