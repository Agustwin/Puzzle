import view.BoardView;
import view.PuzzleGUI;
import model.AbstractModel;
import model.Model;
import model.MongoModel;
import model.BaseXModel;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.InfoDB;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;

import control.AbstractController;
import control.Controller;

/*
 * Copyright 2016 Miguel Ã�ngel RodrÃ­guez-GarcÃ­a (miguel.rodriguez@urjc.es).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Clase principal que ejecuta el juego
 * @Author Miguel Ã�ngel
 * @version 1.0
 */
public class PuzzleApp {
	static int imageSize;
    static int rowNum;
    static int columnNum;
    static String imagePath=null;
    static String db=null;
    
    public static void main(String args[]){      
            
        readXML();
        
        if(imagePath!=null && imagePath.length()!=0) {
    	   
    	   File f=new File(imagePath);
    	   
    	   // Creamos el modelo segun la base de datos que queramos usar
    	   AbstractModel myModel;
    	   if(db.equals("baseX")){
    		   myModel = new BaseXModel(rowNum, columnNum,imageSize);
	       }else if(db.equals("mongo")){
	    	   myModel = new MongoModel(rowNum, columnNum,imageSize);
	       }else{
	    	   //En caso de no recibir ninguna base datos entonces carga el modelo para la practica 1
	     	   myModel = new Model(rowNum, columnNum,imageSize);
	       }          
    	   	
           // Creamos el controlador
    	  //Le pasamos el modelo de datos al controller para que lo cargue
           Controller myController=new Controller();
           
           
           // Inicializamos la GUI
           PuzzleGUI.initialize(myController, rowNum, columnNum, imageSize, f);
           
           // Obtenemos la vista del tablero
           BoardView view=PuzzleGUI.getInstance().getBoardView();
        
           // AÃ±adimos un nuevo observador al controlador
           myController.addObserver(view);
           myController.addObserver(myModel);

           // Visualizamos la aplicaciÃ³n.
           PuzzleGUI.getInstance().setVisible(true);
           
           /*Cargamos los movimientos de las bases de datos, se controla con try catch por si los movimientos almacenados superan el tamaño de la base de datos
           si esto ocurre cargamos el puzzle con los parámetros de xml en su estado inicial. Por eso borramos los comandos de la base de datos y los cargados en la pila
           del controlador*/         
           try{
               myController.LoadGame(myModel);
               }catch(Exception e){
            	   System.err.print("Los movimientos contenidos en la base de datos se salen del tablero generado a partir del XML \nSe cargará el puzzle por defecto");
            	   myController.getMoves().clear();

               }
	    	   
       }else {
    	   String fileSeparator = System.getProperty("file.separator");
           imagePath=System.getProperty("user.dir")+fileSeparator+"resources"+fileSeparator;
          
           
           String[] imageList={imagePath+"blank.gif",imagePath+"one.gif",imagePath+"two.gif",imagePath+"three.gif",imagePath+ "four.gif",
                  imagePath+"five.gif",imagePath+"six.gif",imagePath+"seven.gif",imagePath+"eight.gif"};
          
           // Creamos el modelo segun la base de datos que queramos usar
    	   AbstractModel myModel;
    	   if(db.equals("baseX")){
    		   myModel = new BaseXModel(rowNum, columnNum,imageSize,imageList);
	       }else if(db.equals("mongo")){
	    	   myModel = new MongoModel(rowNum, columnNum,imageSize,imageList);
	       }else{
	    	   //En caso de no recibir ninguna base datos entonces carga el modelo para la practica 1
	    	   myModel = new Model(rowNum, columnNum,imageSize,imageList);
	       }  

           // Creamos el controlador
           Controller myController=new Controller();        

           // Inicializamos la GUI
           PuzzleGUI.initialize(myController, rowNum, columnNum, imageSize, imageList);
          
           // Obtenemos la vista del tablero
           BoardView view=PuzzleGUI.getInstance().getBoardView();
       
           // AÃ±adimos un nuevo observador al controlador
           myController.addObserver(view);
           myController.addObserver(myModel);
           
           // Visualizamos la aplicaciÃ³n.
           PuzzleGUI.getInstance().setVisible(true);  
           
           /*Cargamos los movimientos de las bases de datos, se controla con try catch por si los movimientos almacenados superan el tamaño de la base de datos
           si esto ocurre cargamos el puzzle con los parámetros de xml en su estado inicial. Por eso borramos los comandos de la base de datos y los cargados en la pila
           del controlador*/
           try{
           myController.LoadGame(myModel);
           }catch(Exception e){
        	   System.err.print("Los movimientos contenidos en la base de datos se salen del tablero generado a partir del XML \nSe cargará el puzzle por defecto");
        	   myController.getMoves().clear();
        	   myModel.remove();
           }
           }                          
    }
       //Método que se encarga de leer el fichero Parameters.xml
    public static void readXML()  {	    	
    	//Validamos el DTD
    	SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
    	File xmlFile = new File( "./resources/Parameters.xml" );
    	Document document=null;
    	try {
    		
    		document = (Document) builder.build( xmlFile );
    		
    		
    	}catch(Exception e) {
    		//Si el DTD no está validado cerramos la aplicación y mostramos un mensaje por consola
    		System.err.println("Error, no se ha podido validar el DTD");
    		System.err.println("La aplicación se cerrará");
    		System.exit(-1);
    	}	
    	System.out.println(xmlFile.getPath());
    	
		Element rootNode = document.getRootElement();
		imageSize = Integer.parseInt(rootNode.getChildTextTrim("imageSize"));
		rowNum=Integer.parseInt(rootNode.getChildTextTrim("rowNum"));
		columnNum=Integer.parseInt(rootNode.getChildTextTrim("columnNum"));
		imagePath=rootNode.getChildTextTrim("imagePath");
		db=rootNode.getChildTextTrim("db");
    }
    
}
