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
import model.Model;
import model.MongoModel;
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
	private Stack<MoveCommand> moveCommands;
	private Command save;
	private Command load;
	
	private String db=null;
	
	public Controller() {
		moveCommands=new Stack<MoveCommand>();
		save=new SaveCommand(this);
		load=new LoadCommand(this);
		
		//Lee el fichero de parametros para saber en que base de datos vamos a trabajar
		SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
    	File xmlFile = new File( "./resources/Parameters.xml" );   		    	
    	try {   		
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
    		
                       
    	}else if(db.equals("mongo")){
    		
    		DBCursor cursor = MongoModel.getPartidas().find();
    		
			try {
				while (cursor.hasNext()) {
					MoveCommand m= new MoveCommand((BasicDBObject) cursor.next());
					this.moveCommands.push(m);
				}
			} finally {
				cursor.close();
			}
			
			long endTime = System.nanoTime();
			long duration = (endTime - startTime);
			double millis = duration / 1000000.0; // conversion a milisegundos.
			
			System.out.println("Tiempo en cargar Base de datos y los comandos: " + millis + "ms."); 
			
			//Una vez leidos los movimientos y puestos en la pila de comandos
			//borramos la base de datos y la cargamos desde controller para que se actualice tanto en model como view
			DBCursor cursor2 = MongoModel.getPartidas().find();
			
			try{
				while (cursor2.hasNext()) {
					MongoModel.getPartidas().remove(cursor2.next());
				}
			} finally {
				cursor2.close();
			}
			
    	}else{
    		System.out.println("Vuestro documento XML de configuracion no tiene definido la base de datos, por lo que cargara el modelo de la practica 1.");
    	}
   	
	}
	
	//Ejecutamos todas las acciones con su correspondiente command
	@Override
	public void actionPerformed(ActionEvent act) {
		// TODO Auto-generated method stub			
		this.action = act.getActionCommand();
		System.out.println(	act.getSource().toString());

		switch (action) {
			case "clutter": 
				//preguntar si se puede llamar
				RandomCommand random=new RandomCommand(this,PuzzleGUI.getInstance().rowNum*PuzzleGUI.getInstance().columnNum);
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
				save.execute();				
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
	
	public void notifyObserversReset(int rowNum,int columnNum,int imageSize) {
		//TODO Auto-generated method stub
		for(Observer o:observerList) {			
			o.setNewBoard(rowNum,columnNum,imageSize);
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
			m.execute();
		}
		//Compruebo si ha ganado
		if(PuzzleGUI.getInstance().getBoardView().checkWin()) {
			JOptionPane.showMessageDialog(null,"Puzzle is solved");

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

	   } catch (JAXBException e) {
			e.printStackTrace();
	   }
		
		System.out.println("File Saved!");
	}

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
	
	//Para inicializar la aplicacion ya con los movimientos cargados de la base de datos persistente
	public void getDBMoves(){
    	for(int i=0;i<moveCommands.size();i++) {
			System.out.println(moveCommands.get(i).toString());
			moveCommands.get(i).setController(this);
			moveCommands.get(i).execute();
		}	
	}
	
}