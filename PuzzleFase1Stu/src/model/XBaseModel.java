package model;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.InfoDB;
import org.basex.core.cmd.XQuery;

import command.Command;
import command.MoveCommand;

public class XBaseModel extends AbstractModel<PieceModel>{
	
	private static Context context;
	private String collectionPath;
	private String xmlPartida = "/Save.xml";
	 
	public XBaseModel(int rowNum, int columnNum, int pieceSize) {
		super(rowNum, columnNum, pieceSize);
		
		// TODO Auto-generated constructor stub
		this.collectionPath = System.getProperty("user.dir");
		try{
  			context = new Context();
  			String collectionPath = System.getProperty("user.dir");
  			System.out.println(collectionPath);  		
  			
  		    //-----------1---------------------------
  			System.out.println("\n* Create a collection.");  			  			  			 			
  			//Creamos una colecion anadimos los ficheros uno a uno
  			new CreateDB("saveGame",collectionPath).execute(context); 			
  			
  			//------------2----------------------
  			//System.out.println("\n* Create an empty collection and add documents.");
  			//new CreateDB("saveGame").execute(context);
  			new Add(xmlPartida, collectionPath).execute(context);
  			
  			
  			//Mostrar informacion de base de datos
  			System.out.println("\n* Show database information:");
  			System.out.println(new InfoDB().execute(context));
  			
  		}catch(BaseXException e){
  			System.out.println("No se ha podido generar coleccion: " + e.getMessage());
  		}
		
		try{
  			XQuery xQuery = new XQuery("/saveGame/Command");
  			System.out.println(xQuery.execute(context));
  		}catch(Exception e){
  			System.out.println("No se ha podido ejecutar la consulta: " + e.getMessage());
  		}		
		
		for(int i=0;i<rowNum*columnNum;i++) {  	
    		addNewPiece( i, i%rowNum,i/columnNum);	        		
        }
	}
	
	public XBaseModel(int rowNum, int columnNum,int pieceSize, String[] imageList) {
		super(rowNum,columnNum,pieceSize,imageList);
	        
		this.collectionPath = System.getProperty("user.dir");
		try{
  			context = new Context();
  			String collectionPath = System.getProperty("user.dir");
  			System.out.println(collectionPath);  		
  			
  		    //-----------1---------------------------
  			System.out.println("\n* Create a collection.");  			  			  			 			
  			//Creamos una colecion anadimos los ficheros uno a uno
  			new CreateDB("saveGame",collectionPath).execute(context); 			
  			
  			//------------2----------------------
  			//System.out.println("\n* Create an empty collection and add documents.");
  			//new CreateDB("saveGame").execute(context);
  			new Add(xmlPartida, collectionPath).execute(context);
  			
  			
  			//Mostrar informacion de base de datos
  			System.out.println("\n* Show database information:");
  			System.out.println(new InfoDB().execute(context));
  			
  		}catch(BaseXException e){
  			System.out.println("No se ha podido generar coleccion: " + e.getMessage());
  		}
		
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
		
		try{
  			System.out.println("Insertamos el comando a la partida: " + commandPartida);
  			XQuery insertQuery = new XQuery("insert node "+commandPartida+ " into /saveGame");
  			System.out.println(insertQuery.execute(context));
  		}catch(Exception e){
  			System.out.println("No se ha podido insertar " + e.getMessage());
  			e.printStackTrace();
  		}
		
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
	
	public void updateSaveGame(){
  		try {
  			XQuery serializeQuery = new XQuery("for $Command in /saveGame \n"+
  					"return file:write('"+this.collectionPath+this.xmlPartida+"',$Command)" ); 
			System.out.println(serializeQuery.execute(context));
		} catch (BaseXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
