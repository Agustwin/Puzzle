package model;

import java.util.logging.Level;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import command.Command;
import command.MoveCommand;



public class MongoModel extends AbstractModel{

	private MongoClient mongoClient;
	private DB db;
	private DBCollection partidas;
	
	public MongoModel(int rowNum, int columnNum, int pieceSize) {
		super(rowNum, columnNum, pieceSize);
		// TODO Auto-generated constructor stub
java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
		
		try{
			// PASO 1: Conexión al Server de MongoDB Pasándole el host y el puerto
	        this.mongoClient = new MongoClient("localhost",27017);
	        
	        // PASO 2: Conexión a la base de datos de la práctica
	        this.db = mongoClient.getDB("saveGame");
	        System.out.println("Conectado a la base de datos de partidas");
	        
	        // PASO 3: Obtenemos las colecciones
	        this.partidas = db.getCollection("Partidas");

		}catch(Exception e){
	    	System.out.println("Exception al conectar al server de Mongo: " + e.getMessage());
		}
	    
	}

	@Override
	public void update(int blankPos, int movedPos) {
		// TODO Auto-generated method stub
		
		BasicDBObject document = toDBObjectCommand(blankPos,movedPos);
		partidas.insert(document);
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
	// Recibo Java y transformo a Mongo
		public BasicDBObject toDBObjectCommand(int blankPos,int movedPos) {
		    // Creamos una instancia BasicDBObject
		    BasicDBObject dBObjectCommand = new BasicDBObject();

		    dBObjectCommand.append("pos0", blankPos);
		    dBObjectCommand.append("pos1", movedPos);
		    
		    return dBObjectCommand;
		}
}
