
package model;

import java.awt.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import command.MoveCommand;


public class MongoModel extends AbstractModel<PieceModel>{

	private MongoClient mongoClient;
	private DB db;
	private static DBCollection partidas;
	
	private int pos0;
	private int pos1;
	
	@SuppressWarnings("deprecation")
	public MongoModel(int rowNum, int columnNum, int pieceSize) {
		super(rowNum, columnNum, pieceSize);
		
		// TODO Auto-generated constructor stub
		java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
		
		try{
			// PASO 1: Conexión al Server de MongoDB Pasándole el host y el puerto
	        this.mongoClient = new MongoClient("localhost",27017);
	        
	       
	     // PASO 2: Conexión a la base de datos de la práctica
	       this.db = mongoClient.getDB("saveGame");
	      
	        System.out.println("Conectado a la base de datos de partidas de Mongo");
	        
	        //PASO 3 si no existe la coleccion partidas se crea
	        if(!db.collectionExists("Partidas")){
	        	
	        	BasicDBObject dBObject = new BasicDBObject();

	        	//Objeto para configurar la colección
	    	    dBObject.append("capped",false);
	    	    dBObject.append("autoIndexId",true);
	        	
	        	db.createCollection("Partidas",dBObject);
	        }
	        
	        // PASO 4: Obtenemos las colecciones
	        this.partidas = db.getCollection("Partidas");

		}catch(Exception e){
	    	System.out.println("Exception al conectar al server de Mongo: " + e.getMessage());
		}
	    
		for(int i=0;i<rowNum*columnNum;i++) {  	
    		addNewPiece( i, i%rowNum,i/columnNum);	        		
        }
		
		
	
		
		System.out.println("Model: ");
		for(PieceModel p:listP) {
			System.out.println("id: "+p.getId()+" X: "+p.getIndexRow()+" Y: "+p.getIndexColumn());
		}
	}
	
	public MongoModel(int rowNum, int columnNum,int pieceSize, String[] imageList) {
		super(rowNum,columnNum,pieceSize,imageList);
	        
        for(int i=0;i<rowNum*columnNum;i++) {  	
    		addNewPiece( i, i%rowNum,i/columnNum,imageList[i]);	        		
        }	
        
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
		
		try{
			// PASO 1: Conexión al Server de MongoDB Pasándole el host y el puerto
	        this.mongoClient = new MongoClient("localhost",27017);
	        
	        // PASO 2: Conexión a la base de datos de la práctica
	        this.db = mongoClient.getDB("saveGame");
	        System.out.println("Conectado a la base de datos de partidas de Mongo");
	        
	        //PASO 3 si no existe la coleccion partidas se crea
	        if(!db.collectionExists("Partidas")){
	        	
	        	BasicDBObject dBObject = new BasicDBObject();

	        	//Objeto para configurar la colección
	    	    dBObject.append("capped",false);
	    	    dBObject.append("autoIndexId",true);
	        	
	        	db.createCollection("Partidas",dBObject);
	        }
	        
	        // PASO 4: Obtenemos las colecciones
	        this.partidas = db.getCollection("Partidas");

		}catch(Exception e){
	    	System.out.println("Exception al conectar al server de Mongo: " + e.getMessage());
		}
	    
		for(int i=0;i<rowNum*columnNum;i++) {  	
    		addNewPiece( i, i%rowNum,i/columnNum);	        		
        }
		
		
	}
	
	

	@Override
	public void update(int blankPos, int movedPos) {
		
		//Cambiamos las coordenadas y las piezas de posicion
		
		int auxX=listP.get(movedPos).getIndexRow();
		int auxY=listP.get(movedPos).getIndexColumn();
	
		listP.get(movedPos).setIndexRow(listP.get(blankPos).getIndexRow());
		listP.get(movedPos).setIndexColumn(listP.get(blankPos).getIndexColumn());
		
		listP.get(blankPos).setIndexColumn(auxY);
		listP.get(blankPos).setIndexRow(auxX);
		
		
		PieceModel blank=listP.get(blankPos);

    	listP.set(blankPos,listP.get(movedPos));
    	listP.set(movedPos, blank);
		
    	//Convertimos el objeto en un documento mongo y lo añadimos a la base de datos
		BasicDBObject document = toDBObjectCommand(blankPos,movedPos);
		partidas.insert(document);
		
		
	}
	//Añade una pieza al array de piezas
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

	
	
	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	public DB getDb() {
		return db;
	}

	public void setDb(DB db) {
		this.db = db;
	}

	public static DBCollection getPartidas() {
		return partidas;
	}

	public void setPartidas(DBCollection partidas) {
		this.partidas = partidas;
	}
	
	
	// Transformo un objecto que me da MongoDB a datos Java
	public void toJavaObjectCommand(DBObject dbObject) {
		this.pos0 = (int) dbObject.get("pos0");
		this.pos1 = (int) dbObject.get("pos1");
	}
	
	// Recibo Java y transformo a Mongo
	public BasicDBObject toDBObjectCommand(int blankPos,int movedPos) {
	    // Creamos una instancia BasicDBObject
	    BasicDBObject dBObjectCommand = new BasicDBObject();

	    dBObjectCommand.append("pos0", blankPos);
	    dBObjectCommand.append("pos1", movedPos);
	    
	    return dBObjectCommand;
	}
//Obtiene mediante una consulta find todos los movimientos de la base de datos y los mete en una pila que devuelve
	@Override
	public Stack<MoveCommand> loadMoves() {
		// TODO Auto-generated method stub
		Stack<MoveCommand> auxStack=new Stack<MoveCommand>();

		DBCursor cursor = getPartidas().find();		
		
		try {
			while (cursor.hasNext()) {
				MoveCommand m= new MoveCommand((BasicDBObject) cursor.next());
				auxStack.add(m);
			}
		} finally {
			cursor.close();
		}
		
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
		
		return auxStack;
	}
//Borra todos los movimientos de la base de datos
	@Override
	public void remove() {
		// TODO Auto-generated method stub
		DBCursor cursor = this.getPartidas().find();
		
		try{
			while (cursor.hasNext()) {
				MongoModel.getPartidas().remove(cursor.next());
			}
		} finally {
			cursor.close();
		}
	}
//Devuelve cuanto ocupa la base de datos
	@Override
	public double getStorage() {
		// TODO Auto-generated method stub
		
		CommandResult resultSet = partidas.getStats();
		
		
		double d=Double.valueOf((int)resultSet.get("size"));
		
		return d ;
		
	}
}

