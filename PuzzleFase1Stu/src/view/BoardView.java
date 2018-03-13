package view;

import observer.Observer;

import javax.swing.*;

import model.PieceModel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * Clase que representa la vista del tablero
 * @author Miguel Ã�ngel
 * @version 1.0
 */
public class BoardView extends JPanel implements Observer {
    public static final int imageWidth= 96;
    public static final int imageHeight= 96;
    private ArrayList<PieceView> iconArray = null;
    private int rowOff;
	private int colOff;
    
	
	/*-----------------EL ID 0 SIEMPRE VA A CORRESPONDER A LA PIEZA BLANCA------------------------*/
	
    public BoardView(int rowNum, int columnNum,int imageSize, String[] imageList){
        super();
      
       
        iconArray=new ArrayList<PieceView>();
       
        
       
        
        for(int i=0;i<rowNum*columnNum;i++) {
       
        		PieceView p=new PieceView( i,i%rowNum,i/rowNum,imageSize,imageList[i]);
        		SetCoordinates(p,imageSize);
        		
        		iconArray.add(p);
        		       		
        		
        }
        //Por convenio inicializamos la pieza blanca en el 0
        
    }
    
    private void SetCoordinates(PieceView p,int imageSize) {
    	
    
    	int drawnRow;
    	int drawnColumn;
    	
    	 drawnRow=p.getIndexRow()*imageSize;
    	 drawnColumn=p.getIndexColumn()*imageSize;

    	
    	 
    	 p.setDrawnColumnIndex(drawnColumn);
    	 p.setDrawnRowIndex(drawnRow);
	
    	
    }
    

    public BoardView(int rowNum, int columnNum, int imageSize, File imageFile){
        super();
    }

    //redimensionamos la imagen para 96*96
    private BufferedImage resizeImage(File fileImage){
        BufferedImage resizedImage = null;

        return(resizedImage);
    }

    //dividimos la imagen en el nÃºmero
    private BufferedImage[] splitImage(BufferedImage image){
        //Divisor de imÃ¡genes
        BufferedImage images[] = null;
        return(images);
    }

    public void update(int blankPos, int movedPos){
    	
    	PieceView blank=iconArray.get(blankPos);

    	iconArray.set(blankPos,iconArray.get(movedPos));
    	iconArray.set(movedPos, blank);
    	
    	//Actualizo las coordenadas de todo el puzle
    	for(int i=0;i<iconArray.size();i++) {
    		SetCoordinates(iconArray.get(i),iconArray.get(i).getImageSize());
    	}
    	
    	System.out.println("View: ");
		for(PieceView p:iconArray) {
			System.out.println("id: "+p.getId()+" X: "+p.getIndexRow()+" Y: "+p.getIndexColumn());
		}
    	
    	update(this.getGraphics());
    }

    public void update(Graphics g){
        paint(g);
    }

    public void paint(Graphics g){

    	
    	rowOff=0;
    	colOff=0;
    	
    	
    		rowOff=(this.getWidth()-this.imageWidth)/2;
    		colOff=(this.getHeight()-this.imageHeight)/2;
    	
    	
        for(PieceView iconImage:iconArray){
        	
        	
        	iconImage.setDrawnRowIndex(iconImage.getDrawnRowIndex()+rowOff);
            iconImage.setDrawnColumnIndex(iconImage.getDrawnColumnIndex()+colOff);           
        	
        	
            g.drawImage(iconImage.getImage(), iconImage.getDrawnRowIndex(), iconImage.getDrawnColumnIndex(), iconImage.getImageSize(), iconImage.getImageSize(), this);
            
        }
    }

    //Dado una posicion X e Y localizar una pieza
    //private int locatePiece(int posX,int posY){
     public int locatePiece(int posX,int posY){

    	for(int i=0;i<iconArray.size();i++) {
    		PieceView p=iconArray.get(i);
    		
    		//localizo la imagen sobre la que ha pulsado el puntero
    		if(posX>p.getDrawnRowIndex() && posX<p.getDrawnRowIndex()+p.getImageSize() && posY>p.getDrawnColumnIndex() && posY<p.getDrawnColumnIndex()+p.getImageSize()) {
    			
    				System.out.println("id: "+p.getId());
    				
    				return i;

    		}
    		
    	}
    	
        return(-1);
    }

    /**
     * Mueve la pieza y devuelve las coordenadas en un array de dos posiciones
     * donde: la primera posicion representa la posicion actual de la pieza blanca
     * y la segunda posicion representa la posicion actual de la pieza a mover.
     * @param posX posicion X del puntero
     * @param posY posicion Y del puntero.
     * @return Array de dos posiciones: posicion actual de la pieza blanca y posicion
     * actual de la pieza que tiene que ser movida.
     */
     
     //Método que devuelve la posicion en el array 
    public int[] movePiece(int posX,int posY){
    	//Array de 2 posiciones para devolver
    	int[] move =new int[2];
    	int pos=locatePiece(posX,posY);
    	int blankPos=checkMove(pos);		//Devuelve la pieza blanca solo si es adyacente a la pieza que se quiere mover
    	
    	if(pos==-1 || blankPos==-1) {
    		return null;
    	}
    	
    	//muevo las coordenadas de las piezas
    		int auxX=iconArray.get(pos).getIndexRow();
    		int auxY=iconArray.get(pos).getIndexColumn();;
    	
    		iconArray.get(pos).setIndexRow(iconArray.get(blankPos).getIndexRow());
    		iconArray.get(pos).setIndexColumn(iconArray.get(blankPos).getIndexColumn());
    		
    		iconArray.get(blankPos).setIndexColumn(auxY);
    		iconArray.get(blankPos).setIndexRow(auxX);
    		
    		
    	
    	//Intercambio las posiciones de las piezas
    	move[0]=pos;
    	move[1]=blankPos;
    	
    	
    	
    	
        return(move);
    }
    	//Comprueba si el movimiento es valido devuelve los ids 
    	public int checkMove(int pos) {
    	
    		int blankPos=-1;
    		int pieceWidth=imageWidth/iconArray.get(0).getImageSize();
    		
    		
    		
    		if(pos-1>=0) {
    			if(iconArray.get(pos-1).getId()==0) {
    				System.out.println("HIT");
    				return pos-1;
    			}
    			
    		}
    		
    		if(pos+1<iconArray.size()) {
    			if(iconArray.get(pos+1).getId()==0) {
    				System.out.println("HIT");
    				return pos+1;
    			}
    		}
    		
    		if(pos-pieceWidth>=0) {
    			if(iconArray.get(pos-pieceWidth).getId()==0) {
    				System.out.println("HIT");
    				return pos-pieceWidth;
    				}    		
    			}
    		
    		if(pos+pieceWidth<iconArray.size()) {
    			if(iconArray.get(pos+pieceWidth).getId()==0) {
    				System.out.println("HIT");
    				return pos+pieceWidth;
    			}
    		}
    		return blankPos;
    	}

}
