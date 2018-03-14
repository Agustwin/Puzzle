import view.BoardView;
import view.PuzzleGUI;
import model.AbstractModel;
import model.Model;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

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
    
    public static void main(String args[]){
        
        
        
        readXML();
        
        String fileSeparator = System.getProperty("file.separator");
         imagePath=System.getProperty("user.dir")+fileSeparator+"resources"+fileSeparator;

        String[] imageList={imagePath+"blank.gif",imagePath+"one.gif",imagePath+"two.gif",imagePath+"three.gif",imagePath+ "four.gif",
                imagePath+"five.gif",imagePath+"six.gif",imagePath+"seven.gif",imagePath+"eight.gif"};

        File f=new File(imagePath+"kingdom.jpg");
         
        // Creamos el modelo
        AbstractModel myModel=new Model(rowNum, columnNum,imageSize,imageList);

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
        
       
      
        
    }
    
    
    public static void readXML()  {
    	
    	SAXBuilder builder = new SAXBuilder();
    	File xmlFile = new File( "./resources/Parameters.xml" );
    	System.out.println(xmlFile);
    	try {
    		Document document = (Document) builder.build( xmlFile );
    		Element rootNode = document.getRootElement();
    		 imageSize = Integer.parseInt(rootNode.getChildTextTrim("imageSize"));
    		 rowNum=Integer.parseInt(rootNode.getChildTextTrim("rowNum"));;
    		 columnNum=Integer.parseInt(rootNode.getChildTextTrim("columnNum"));
    		 imagePath=rootNode.getChildTextTrim("imagePath");
    		System.out.println(imagePath);
    	}catch(Exception e) {
    		System.err.println(e);
    	}
    	
    	
    	
    }
}
