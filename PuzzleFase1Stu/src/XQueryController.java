import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.InfoDB;
import org.basex.core.cmd.XQuery;

public class XQueryController {

    //Creamos la coleccion de XML
  	public static void createCollection(String nameCollection, Context context){
  		try{
  			System.out.println("\n* Create a collection.");
  			
  			String collectionPath = System.getProperty("user.dir");
  			String xmlPartida = "partida.xml";
  			
  			new CreateDB(nameCollection, collectionPath).execute(context);
  			
  			//2 creamos una colecion anadimos los ficheros uno a uno
  			new CreateDB("Collection").execute(context);
  			new Add(xmlPartida, collectionPath).execute(context);
  			
  			//Mostrar informacion de base de datos
  			System.out.println("\n* Show database information:");
  			System.out.println(new InfoDB().execute(context));
  			
  		}catch(Exception e){
  			System.out.println("No se ha podido generar coleccion: " + e.getMessage());
  		}
  	}
  	
  	//Paso2
  	public static void queryCatalog(String query, Context context){
  		try{
  			XQuery xQuery = new XQuery(query);
  			System.out.println(xQuery.execute(context));
  		}catch(Exception e){
  			System.out.println("No se ha podido ejecutar la consulta: " + e.getMessage());
  		}		
	}
}
