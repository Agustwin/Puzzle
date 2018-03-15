package view;

import observer.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;

import model.PieceModel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Clase que representa la vista del tablero
 * @author Miguel Ã�ngel
 * @version 1.0
 */
public class BoardView extends JPanel implements Observer {
    public static final int imageWidth= 96;
    public static final int imageHeight= 96;
    private ArrayList<PieceView> iconArray = null;
    private File image;
    private int rowNum;
    private int columnNum;
    
    private int rowOff;
	private int colOff;

    private int imageSize;
	/*-----------------EL ID 0 SIEMPRE VA A CORRESPONDER A LA PIEZA BLANCA------------------------*/
	
    public BoardView(int rowNum, int columnNum,int imageSize, String[] imageList){
        super();
        this.rowNum=rowNum;
        this.columnNum=columnNum;
        this.imageSize=imageSize;
        
        iconArray=new ArrayList<PieceView>();
       
        
       
        
        for(int i=0;i<rowNum*columnNum;i++) {
       
        		
        		PieceView p=new PieceView( i,i%rowNum,i/rowNum,imageSize,imageList[i]);
        		
        		
        		iconArray.add(p);
            	
            	SetDrawnCoordinates(p,imageSize);
            	

        }
        
        
        //Por convenio inicializamos la pieza blanca en el 0
        
    }
    

    

    public BoardView(int rowNum, int columnNum, int imageSize, File imageFile){
        super();
        this.imageSize=imageSize;
        iconArray=new ArrayList<PieceView>();
        
        
        BufferedImage img=resizeImage(imageFile);
        BufferedImage[] listImg=splitImage(img);
    	

        
        for(int i=0;i< listImg.length;i++) {
        	PieceView p;
        	if(i==0) {
        		p=new PieceView( i,i%rowNum,i/rowNum,imageSize,"resources/blank.gif");
        	}else {
        		 p=new PieceView( i,i%rowNum,i/rowNum,imageSize,listImg[i]); 
        		
        	}
        	

        	
        	iconArray.add(p);
        	
        }
        
    }

    //redimensionamos la imagen para 96*96
    private BufferedImage resizeImage(File fileImage){
    	BufferedImage resizedImage=null;
		try {
			
			
				Image img=ImageIO.read(fileImage);
			
			    Image tmp = img.getScaledInstance(this.imageWidth, this.imageHeight, Image.SCALE_SMOOTH);
			    resizedImage = new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_ARGB);

			    Graphics2D g2d = resizedImage.createGraphics();
			    g2d.drawImage(tmp, 0, 0, null);
			    g2d.dispose();

			    
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
        return(resizedImage);
    }

    //dividimos la imagen en el nÃºmero
    private BufferedImage[] splitImage(BufferedImage image){
    	
    	BufferedImage images[]=new BufferedImage[columnNum*rowNum];
    	
        //Divisor de imÃ¡genes
    	for(int i=0;i<columnNum*rowNum;i++) {
    		images[i]=image.getSubimage((i%rowNum)*imageSize, (i/columnNum)*imageSize, imageSize, imageSize);
    		
    	}
        
        return(images);
    }

    public void update(int blankPos, int movedPos){
    	
    	
    	PieceView blank=iconArray.get(blankPos);

    	iconArray.set(blankPos,iconArray.get(movedPos));
    	iconArray.set(movedPos, blank);
    	
    	
    	//muevo las coordenadas de las piezas
		int auxX=iconArray.get(movedPos).getIndexRow();
		int auxY=iconArray.get(movedPos).getIndexColumn();;
	
		iconArray.get(movedPos).setIndexRow(iconArray.get(blankPos).getIndexRow());
		iconArray.get(movedPos).setIndexColumn(iconArray.get(blankPos).getIndexColumn());
		
		iconArray.get(blankPos).setIndexColumn(auxY);
		iconArray.get(blankPos).setIndexRow(auxX);
    	
    	
    	//Actualizo las coordenadas de todo el puzle
    	for(int i=0;i<iconArray.size();i++) {
    		SetDrawnCoordinates(iconArray.get(i),iconArray.get(i).getImageSize());
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
    		
    		//g.clearRect(0, 0,this.getWidth(), this.getHeight());

    		
    	
    	
    		for(PieceView iconImage:iconArray){	
    			
    		SetDrawnCoordinates(iconImage,iconImage.getImageSize());
    		
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
    	
    	System.out.println("poooooooooooooos"+pos);
    	System.out.println("blaaaaaaaaaaaaaank"+blankPos);
    	if(pos==-1 || blankPos==-1) {
    		return null;
    	}
    	
    	
    		
    		
    	
    	//Intercambio las posiciones de las piezas
    	move[0]=pos;
    	move[1]=blankPos;
    	
    	
    	
    	
        return(move);
    }
    	//Comprueba si el movimiento es valido devuelve los ids 
    	public int checkMove(int pos) {
    	
    		int blankPos=-1;
    		 rowNum=imageWidth/iconArray.get(0).getImageSize();
    		
    		
    		
    		if(pos-1>=0) {
    			if(iconArray.get(pos-1).getId()==0) {
    				System.out.println("HIT1");
    				return pos-1;
    			}
    			
    		}
    		
    		if(pos+1<iconArray.size()) {
    			if(iconArray.get(pos+1).getId()==0) {
    				System.out.println("HIT2");
    				return pos+1;
    			}
    		}
    		
    		if(pos-rowNum>=0) {
    			if(iconArray.get(pos-rowNum).getId()==0) {
    				System.out.println("HIT3");
    				return pos-rowNum;
    				}    		
    			}
    		
    		if(pos+rowNum<iconArray.size()) {
    			if(iconArray.get(pos+rowNum).getId()==0) {
    				System.out.println("HIT4");
    				return pos+rowNum;
    			}
    		}
    		return blankPos;
    	}


public void Clutter() {
	
	Collections.shuffle(iconArray);
	
	
}

/*public void SetCoordinates() {
	
for(int i=0;i<iconArray.size();i++) {
		
		PieceView p=iconArray.get(i);
		p.setIndexRow(i%3);
		p.setIndexColumn(i/3);
		System.out.println("id: "+p.getId()+" X: "+p.getIndexRow()+" Y: "+p.getIndexColumn());
		
	}
	
}*/
private void SetDrawnCoordinates(PieceView p,int imageSize) {
	
	
	rowOff=(this.getWidth()-this.imageWidth)/2;
	colOff=(this.getHeight()-this.imageHeight)/2;
	
	int drawnRow;
	int drawnColumn;
	
	 drawnRow=p.getIndexRow()*imageSize;
	 drawnColumn=p.getIndexColumn()*imageSize;    	 
	 p.setDrawnColumnIndex(drawnColumn+colOff);
	 p.setDrawnRowIndex(drawnRow+rowOff);

	
}





@Override
public void setNewBoard() {
	iconArray.clear();
	
	BufferedImage img=resizeImage(image);
    BufferedImage[] listImg=splitImage(img);
	

    
    for(int i=0;i< listImg.length;i++) {
    	PieceView p;
    	if(i==0) {
    		p=new PieceView( i,i%rowNum,i/rowNum,imageSize,"resources/blank.gif");
    	}else {
    		 p=new PieceView( i,i%rowNum,i/rowNum,imageSize,listImg[i]); 
    		
    	}

    	iconArray.add(p);
    	
    }
	this.update(getGraphics());
}




public File getImage() {
	return image;
}




public void setImage(File image) {
	this.image = image;
}
}
