package db;

import java.util.logging.Level;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoController {
	private MongoClient mongoClient;
	private DB db;
	private DBCollection partidas;

	public MongoController(){
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

	public DBCollection getPartidas() {
		return partidas;
	}

	public void setPartidas(DBCollection partidas) {
		this.partidas = partidas;
	}			
}
