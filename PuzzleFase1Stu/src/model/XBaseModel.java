package model;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.InfoDB;
import org.basex.core.cmd.XQuery;

public class XBaseModel extends AbstractModel {
	
	private static Context context;
	private String collectionPath;
	private String xmlPartida = "/Save.xml";
	 
	public XBaseModel(int rowNum, int columnNum, int pieceSize) {
		super(rowNum, columnNum, pieceSize);
		// TODO Auto-generated constructor stub
		//Paso1	        
       /* String collection = "saveGame"; 
        XQ = new XQueryController();
        this.XQ.createCollection(collection);	           
        
        this.XQ.queryPartidas("/saveGame/Command");*/
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
  			//new CreateDB(nameCollection).execute(context);
  			//new Add(xmlPartida, collectionPath).execute(context);
  			
  			
  			//Mostrar informacion de base de datos
  			System.out.println("\n* Show database information:");
  			System.out.println(new InfoDB().execute(context));
  			
  		}catch(BaseXException e){
  			System.out.println("No se ha podido generar coleccion: " + e.getMessage());
  		}
	}

	@Override
	public void update(int blankPos, int movedPos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNewPiece(int id, int indexRow, int indexCol, String imagePath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNewPiece(int id, int indexRow, int indexCol) {
		// TODO Auto-generated method stub
		
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

}
