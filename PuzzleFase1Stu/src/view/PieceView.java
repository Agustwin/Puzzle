package view;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa la vista del tablero
 * @author Miguel Ã�ngel
 * @version 1.0
 */
public class PieceView extends ImageIcon implements Cloneable{

    //id de la imagen
    private int id;
    //Ã­ndice de fila
    private int indexRow;
    //Ã­ndice de columna
    private int indexColumn;
    //TamaÃ±o de la imagen
    private int imageSize;
    //Índice de fila dibujada
    private int drawnRowIndex;
     //Índice de columna dibujada
    private int drawnColumnIndex;    
     //imagen a guardar
    Image image;
    
	private String imagepath;
    
    /**
     * Constructor de una clase
     * @param indexRow indice de fila
     * @param indexColumn indice de columna
     * @param imagePath ubicaciÃ³n de la imagen.
     */
    public PieceView(int id,int indexRow, int indexColumn,int imageSize,String imagePath){
        super(imagePath);
        this.id=id;
        this.indexColumn=indexColumn;
        this.indexRow=indexRow;
        this.imageSize=imageSize;
        
        this.imagepath=imagePath;
    }

    public PieceView(int id, int indexRow, int indexColumn,int imageSize,Image image){
        super(image);
        this.id=id;
        this.indexColumn=indexColumn;
        this.indexRow=indexRow;
        this.imageSize=imageSize;
        this.image=image;
    }
    
    public PieceView(int id, int indexRow, int indexColumn){
        super();
    }


    public int getIndexRow() {
        return indexRow;
    }

    public int getIndexColumn() {
        return indexColumn;
    }
    
    public void setIndexRow(int i) {
         this.indexRow=i;
    }

    public void setIndexColumn(int i) {
        this.indexColumn=i;
   }


    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    public int getId(){
        return this.id;
    }
    
    public void setId(int id){
    	this.id = id;
    }

    public String toString(){
        return("id:"+id);
    }

	public int getDrawnRowIndex() {
		return drawnRowIndex;
	}

	public void setDrawnRowIndex(int drawnRowIndex) {
		this.drawnRowIndex = drawnRowIndex;
	}

	public int getDrawnColumnIndex() {
		return drawnColumnIndex;
	}

	public void setDrawnColumnIndex(int drawnColumnIndex) {
		this.drawnColumnIndex = drawnColumnIndex;
	}

	

    public String getImagePath() {
		return imagepath;
	}

	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}

}
