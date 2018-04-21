package db;
import java.io.File;

import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.InfoDB;
import org.basex.core.cmd.XQuery;

import command.MoveCommand;

public class XQueryController {
	
	private static Context context;
	private String collectionPath;
	private String xmlPartida = "/Save.xml";
	
	public XQueryController(){
		this.collectionPath = System.getProperty("user.dir");
	}

    //Creamos la coleccion de XML que en Basex es unica como si fuera la db
  	public static void createCollection(String nameCollection){
  		try{
  			context = new Context();
  			String collectionPath = System.getProperty("user.dir");
  			System.out.println(collectionPath);  		
  			
  		    //-----------1---------------------------
  			System.out.println("\n* Create a collection.");  			  			  			 			
  			//Creamos una colecion anadimos los ficheros uno a uno
  			new CreateDB(nameCollection,collectionPath).execute(context); 			
  			
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
  	
  	//Paso2 consulta todos los commands de una partida
  	public void queryPartidas(String query){
  		try{
  			XQuery xQuery = new XQuery(query);
  			System.out.println(xQuery.execute(context));
  		}catch(Exception e){
  			System.out.println("No se ha podido ejecutar la consulta: " + e.getMessage());
  		}		
	}
  	
  	//Paso3 agrega un comando al xml actual
  	public void addCommandPartida(MoveCommand commandPartida){
  		try{
  			System.out.println("Insertamos el comando a la partida: " + commandPartida);
  			XQuery insertQuery = new XQuery("insert node "+commandPartida+ " into /saveGame");
  			System.out.println(insertQuery.execute(context));
  		}catch(Exception e){
  			System.out.println("No se ha podido insertar " + e.getMessage());
  			e.printStackTrace();
  		}
  	}
  	
  	
  	
  	//----------------Esto no funciona-------------------
  	/////////////////////////////////////////////////////
  	//Paso4 elimina un nodo comando
  	public void removeCommandPartida(MoveCommand commandPartida){
  		try{
  			System.out.println("Eliminamos el comando partida: " + commandPartida);
  			//XQuery eliminarQuery = new XQuery("delete node /saveGame/" + commandPartida +"/.");
  			XQuery eliminarQuery = new XQuery("delete /saveGame/Command[matches(pos0,"+ commandPartida.getPos0()+")");
  			System.out.println(eliminarQuery.execute(context));
  		}catch(Exception e){
  			System.out.println("No se ha podido borrar " + e.getMessage());
  			e.printStackTrace();
  		}
  	}
  	
    //Paso5 elimina todos los nodos command de una partida
  	public void removePartida(){
  		try{
  			System.out.println("Eliminamos los commandos de una partida.");
  			XQuery eliminarQuery = new XQuery("for $Command in /saveGame \n"+
  					"return delete node /saveGame/Command/., $Command)" );
  			System.out.println(eliminarQuery.execute(context));
  		}catch(Exception e){
  			System.out.println("No se ha podido borrar partida" + e.getMessage());
  			e.printStackTrace();
  		}
  	} 
  	/////////////////////////////////////////////////////
  	
  	
  	
  	
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
