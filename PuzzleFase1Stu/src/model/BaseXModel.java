package model;
import java.awt.List;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Stack;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.Info;
import org.basex.core.cmd.InfoDB;
import org.basex.core.cmd.InfoStorage;
import org.basex.core.cmd.Optimize;
import org.basex.core.cmd.XQuery;
import org.basex.data.Data;
import org.basex.data.MetaData;
import org.basex.data.MetaProp;
import org.basex.query.*;
import org.basex.server.Log;
import org.basex.server.Sessions;

import command.Command;
import command.MoveCommand;
import control.SaveGame;

public class BaseXModel extends AbstractModel<PieceModel>{
	
	private static Context context;
	private static String collectionPath;
	private static String xmlPartida = "/Save.xml";
	 
	public BaseXModel(int rowNum, int columnNum, int pieceSize) {
		super(rowNum, columnNum, pieceSize);
		
		// TODO Auto-generated constructor stub
		this.collectionPath = System.getProperty("user.dir");
		try{
  			context = new Context();
  			String collectionPath = System.getProperty("user.dir")+xmlPartida;
  			System.out.println(collectionPath);  		
  			
  		    //-----------1---------------------------
  			System.out.println("\n* Create a collection.");  			  			  			 			
  			//Creamos una coleccion anadimos los ficheros uno a uno
  			new CreateDB("saveGame",collectionPath).execute(context); 			  			
  			
  			//Mostrar informacion de base de datos
  			System.out.println("\n* Show database information:");
  			System.out.println(new InfoDB().execute(context));
  			
  		}catch(BaseXException e){
  			System.out.println("No se ha podido generar coleccion: " + e.getMessage());
  		}
		
		//Consulta todos los commands de una partida
		/*try{
  			XQuery xQuery = new XQuery("/saveGame/Command");
  			System.out.println(xQuery.execute(context));
  		}catch(Exception e){
  			System.out.println("No se ha podido ejecutar la consulta: " + e.getMessage());
  		}		*/
		
		for(int i=0;i<rowNum*columnNum;i++) {  	
    		addNewPiece( i, i%rowNum,i/columnNum);	        		
        }
	}
	
	public BaseXModel(int rowNum, int columnNum,int pieceSize, String[] imageList) {
		super(rowNum,columnNum,pieceSize,imageList);
	        
		this.collectionPath = System.getProperty("user.dir");
		try{
  			context = new Context();
  			String collectionPath = System.getProperty("user.dir")+xmlPartida;
  			System.out.println(collectionPath);  		
  			
  		    //-----------1---------------------------
  			System.out.println("\n* Create a collection.");  			  			  			 			
  			//Creamos una coleccion anadimos los ficheros uno a uno
  			new CreateDB("saveGame",collectionPath).execute(context); 			
  			  			  			
  			//Mostrar informacion de base de datos
  			System.out.println("\n* Show database information:");
  			System.out.println(new InfoDB().execute(context));
  			
  		}catch(BaseXException e){
  			System.out.println("No se ha podido generar coleccion: " + e.getMessage());
  		}
		
		//Consulta todos los commands de una partida
		try{
  			XQuery xQuery = new XQuery("/saveGame/Command");
  			System.out.println(xQuery.execute(context));
  		}catch(Exception e){
  			System.out.println("No se ha podido ejecutar la consulta: " + e.getMessage());
  		}	
		
        for(int i=0;i<rowNum*columnNum;i++) {  	
    		addNewPiece( i, i%rowNum,i/columnNum,imageList[i]);	        		
        }	        
	}	 	

	@Override
	public void update(int blankPos, int movedPos) {
		
	 
		
		Command commandPartida=new MoveCommand(null,blankPos,movedPos);
		// TODO Auto-generated method stub
		//muevo las coordenadas de las piezas
		int auxX=listP.get(movedPos).getIndexRow();
		int auxY=listP.get(movedPos).getIndexColumn();
	
		listP.get(movedPos).setIndexRow(listP.get(blankPos).getIndexRow());
		listP.get(movedPos).setIndexColumn(listP.get(blankPos).getIndexColumn());
		
		listP.get(blankPos).setIndexColumn(auxY);
		listP.get(blankPos).setIndexRow(auxX);
		
		
		PieceModel blank=listP.get(blankPos);

    	listP.set(blankPos,listP.get(movedPos));
    	listP.set(movedPos, blank);
		
    	//Agrega un comando al xml actual
		try{
  			System.out.println("Insertamos el comando a la partida: " + commandPartida);
  			XQuery insertQuery = new XQuery("insert node "+commandPartida+ " into /saveGame");
  			System.out.println(insertQuery.execute(context));
  		}catch(Exception e){
  			System.out.println("No se ha podido insertar " + e.getMessage());
  			e.printStackTrace();
  		}
		
		//Actualiza lo que hay en la base de datos al fichero Save.xml para que siempre coincidan
		updateSaveGame();
	
	}

	@Override
	public void addNewPiece(int id, int indexRow, int indexCol, String imagePath) {
		// TODO Auto-generated method stub
		PieceModel p=new PieceModel( id, indexRow,indexCol,imagePath);
		listP.add(p); 
	}

	@Override
	public void addNewPiece(int id, int indexRow, int indexCol) {
		// TODO Auto-generated method stub
		PieceModel p=new PieceModel( id, indexRow,indexCol);
		listP.add(p);
	}

	@Override
	public boolean isPuzzleSolve() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] getRandomMovement(int lastPos, int pos) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//Actualiza lo que hay en la base de datos al fichero Save.xml para que siempre coincidan
	public static void updateSaveGame(){
  		try {
  			XQuery serializeQuery = new XQuery("for $Command in /saveGame \n"+
  					"return file:write('"+collectionPath+xmlPartida+"',$Command)" ); 
			System.out.println(serializeQuery.execute(context));
		} catch (BaseXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		
	}

	@Override
	public Stack<MoveCommand> loadMoves() {
		// TODO Auto-generated method stub
		Stack<MoveCommand> auxStack=new Stack<MoveCommand>();
		String query=null;
		
		try {
			query=new XQuery("//saveGame").execute(context);
			System.err.println(query);
		} catch (BaseXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringReader reader = new StringReader(query);
		try {			
			
			JAXBContext jaxbContext = JAXBContext.newInstance(SaveGame.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			SaveGame s = (SaveGame) jaxbUnmarshaller.unmarshal(reader);
			auxStack=s.getStack();
			
			if(auxStack==null){
				auxStack=new Stack<MoveCommand>();
			}
			
					
		} catch (JAXBException e) {
			e.printStackTrace();
		}	
		
		remove();
			
		return auxStack;
	}	
	
	

	@Override
	public void remove() {
		
		XQuery eliminarQuery = new XQuery("delete node //Command");
			try {
				System.out.println(eliminarQuery.execute(context));
			} catch (BaseXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BaseXModel.updateSaveGame();
		
	}

	@Override
	public double getStorage() {
		File f=new File("Save.xml");
		
		XQuery serializeQuery = new XQuery("db:property('saveGame','size')" ); 
		try {
			System.out.println(serializeQuery.execute(context));
		} catch (BaseXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Dado que el fichero tiene un tamaño estando vacío de 11 bytes se toman esos 11 bytes como 0(como si estuviera vacía)
		return f.length()-11;
	} 
}
