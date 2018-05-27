
package model;

import java.util.logging.Level;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;


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
	        
	        // PASO 3: Obtenemos las colecciones
	        this.partidas = db.getCollection("Partidas");

		}catch(Exception e){
	    	System.out.println("Exception al conectar al server de Mongo: " + e.getMessage());
		}
	    
		for(int i=0;i<rowNum*columnNum;i++) {  	
    		addNewPiece( i, i%rowNum,i/columnNum);	        		
        }
		
		
		DBCursor cursor = this.partidas.find();
		
		try {
			while (cursor.hasNext()) {
				toJavaObjectCommand(cursor.next());
				
				System.out.println("pos0:"+this.pos0+" pos1:"+this.pos1);
				int auxX=listP.get(pos0).getIndexRow();
				int auxY=listP.get(pos1).getIndexColumn();
			
				listP.get(pos1).setIndexRow(listP.get(pos0).getIndexRow());
				listP.get(pos1).setIndexColumn(listP.get(pos0).getIndexColumn());
				
				listP.get(pos0).setIndexColumn(auxY);
				listP.get(pos0).setIndexRow(auxX);
								
				PieceModel blank=listP.get(pos0);

		    	listP.set(pos0,listP.get(pos1));
		    	listP.set(pos1, blank);
			}
		} finally {
			cursor.close();
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
	        
	        // PASO 3: Obtenemos las colecciones
	        this.partidas = db.getCollection("Partidas");

		}catch(Exception e){
	    	System.out.println("Exception al conectar al server de Mongo: " + e.getMessage());
		}
	    
		for(int i=0;i<rowNum*columnNum;i++) {  	
    		addNewPiece( i, i%rowNum,i/columnNum);	        		
        }
		
		DBCursor cursor = this.partidas.find();
		
		try {
			while (cursor.hasNext()) {
				toJavaObjectCommand(cursor.next());
				
				System.out.println("pos0:"+this.pos0+" pos1:"+this.pos1);
				int auxX=listP.get(pos0).getIndexRow();
				int auxY=listP.get(pos1).getIndexColumn();
			
				listP.get(pos1).setIndexRow(listP.get(pos0).getIndexRow());
				listP.get(pos1).setIndexColumn(listP.get(pos0).getIndexColumn());
				
				listP.get(pos0).setIndexColumn(auxY);
				listP.get(pos0).setIndexRow(auxX);
								
				PieceModel blank=listP.get(pos0);

		    	listP.set(pos0,listP.get(pos1));
		    	listP.set(pos1, blank);
			}
		} finally {
			cursor.close();
		}
	}

	@Override
	public void update(int blankPos, int movedPos) {
		// TODO Auto-generated method stub
		int auxX=listP.get(movedPos).getIndexRow();
		int auxY=listP.get(movedPos).getIndexColumn();
	
		listP.get(movedPos).setIndexRow(listP.get(blankPos).getIndexRow());
		listP.get(movedPos).setIndexColumn(listP.get(blankPos).getIndexColumn());
		
		listP.get(blankPos).setIndexColumn(auxY);
		listP.get(blankPos).setIndexRow(auxX);
		
		
		PieceModel blank=listP.get(blankPos);

    	listP.set(blankPos,listP.get(movedPos));
    	listP.set(movedPos, blank);
		
		BasicDBObject document = toDBObjectCommand(blankPos,movedPos);
		partidas.insert(document);
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
}

