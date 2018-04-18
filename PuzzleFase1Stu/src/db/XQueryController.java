package db;
import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.InfoDB;
import org.basex.core.cmd.XQuery;

import command.MoveCommand;

public class XQueryController {
	
	public XQueryController(){
		
	}

    //Creamos la coleccion de XML que en Basex es unica como si fuera la db
  	public static void createCollection(String nameCollection, Context context){
  		try{
  			System.out.println("\n* Create a collection.");
  			
  			String collectionPath = System.getProperty("user.dir");
  			String xmlPartida = "Save.xml";
  			
  			//Creamos una colecion anadimos los ficheros uno a uno
  			new CreateDB(nameCollection).execute(context);
  			new Add(xmlPartida, collectionPath).execute(context);
  			
  			//Mostrar informacion de base de datos
  			System.out.println("\n* Show database information:");
  			System.out.println(new InfoDB().execute(context));
  			
  		}catch(Exception e){
  			System.out.println("No se ha podido generar coleccion: " + e.getMessage());
  		}
  	}
  	
  	//Paso2 consulta todos los commands de una partida
  	public static void queryPartidas(String query, Context context){
  		try{
  			XQuery xQuery = new XQuery(query);
  			System.out.println(xQuery.execute(context));
  		}catch(Exception e){
  			System.out.println("No se ha podido ejecutar la consulta: " + e.getMessage());
  		}		
	}
  	
  	//Paso3 agrega un comando al xml actual
  	public static void addCommandPartida(MoveCommand commandPartida, Context context){
  		try{
  			System.out.println("Insertamos el comando a la partida: " + commandPartida);
  			XQuery insertQuery = new XQuery("insert node "+commandPartida+ " into /saveGame");
  			System.out.println(insertQuery.execute(context));
  			//this.updatePartida(context);
  		}catch(Exception e){
  			System.out.println("No se ha podido insertar " + e.getMessage());
  			e.printStackTrace();
  		}
  	}
  	
    //Paso4 elimina todos los nodos command de una partida
  	public static void removePartida(Context context){
  		try{
  			System.out.println("Eliminamos los commandos de una partida.");
  			XQuery eliminarQuery = new XQuery("delete node /saveGame/.");
  			System.out.println(eliminarQuery.execute(context));
  		}catch(Exception e){
  			System.out.println("No se ha podido borrar " + e.getMessage());
  			e.printStackTrace();
  		}
  	}
  	
  	
  	private void updatePartida(Context context){
  		XQuery serializeQuery = new XQuery("for $item in / saveGame \n"+"return " );
  		
  	}
}
