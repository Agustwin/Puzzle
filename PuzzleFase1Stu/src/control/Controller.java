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
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import command.Command;
import command.LoadCommand;
import command.MoveCommand;
import command.RandomCommand;
import command.SaveCommand;
import command.SolveCommand;
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
	private Stack<MoveCommand> moveCommands;
	private Command save;
	private Command load;
	
	public Controller() {
		moveCommands=new Stack();
		save=new SaveCommand(this);
		load=new LoadCommand(this);
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
				
				JPanel j=new JPanel();
				JTextField Rows = new JTextField(5);
			      JTextField Columns = new JTextField(5);
			      JTextField Size = new JTextField(5);
			      
			      JPanel myPanel = new JPanel();
			      myPanel.add(new JLabel("Filas:"));
			      myPanel.add(Rows);
			      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
			      myPanel.add(new JLabel("Columnas:"));
			      myPanel.add(Columns);
			      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
			      myPanel.add(new JLabel("Tamaño de pieza:"));
			      myPanel.add(Size);

			      int result = JOptionPane.showConfirmDialog(null, myPanel, 
			               "Please Enter Values", JOptionPane.OK_CANCEL_OPTION);
			      if (result == JOptionPane.OK_OPTION) {
			         
			    	  if(f!=null) {
							PuzzleGUI.getInstance().updateBoard(f);
							notifyObserversReset(Integer.parseInt(Rows.getText()),Integer.parseInt(Columns.getText()),Integer.parseInt(Size.getText()));
							this.myView=PuzzleGUI.getInstance().getBoardView();
							
							this.getMoves().clear();

							System.out.println("Load Image");
						}
			       }
				
				
				
				break;
				
			case "saveGame":
				save.execute();
				/*
				try {
					writeXML();					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				
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