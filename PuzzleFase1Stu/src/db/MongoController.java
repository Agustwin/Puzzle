package db;

import java.util.logging.Level;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoController {

	public MongoController(){
		java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
		
		try{
			// PASO 1: Conexión al Server de MongoDB Pasándole el host y el puerto
	        MongoClient mongoClient = new MongoClient("localhost",27017);
	        
	        // PASO 2: Conexión a la base de datos de la práctica
	        DB db = mongoClient.getDB("saveGame");
	        System.out.println("Conectado a la base de datos de partidas");
	        
	        // PASO 3: Obtenemos las colecciones
	        DBCollection partidas = db.getCollection("Partidas");

		}catch(Exception e){
	    	System.out.println("Exception al conectar al server de Mongo: " + e.getMessage());
	    }
	}
	
	
}
