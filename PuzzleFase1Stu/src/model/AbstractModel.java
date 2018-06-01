package model;

import java.awt.List;
import java.util.ArrayList;
import java.util.Stack;

import observer.Observer;

/**
 * Modelo abstracto de datos
 * @author Miguel Ã�ngel
 * @version 1.0
 * @param <E>
 */
public abstract class AbstractModel<PieceModel> implements Observer {
    //nÃºmero de filas
    protected int rowNum=0;
    //nÃºmero de columnas
    protected int columnNum=0;
    //tamaÃ±o de la pieza
    protected int pieceSize=0;
    //lista de images
    protected String[] imageList=null;
    //Lista de piezas
    
	protected ArrayList<PieceModel> listP=null;	

    
    //constructor de la clase.
    public AbstractModel(int rowNum, int columnNum,int pieceSize, String[] imageList) {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.pieceSize = pieceSize;
        this.imageList = imageList;
        
        listP=new ArrayList<PieceModel>();
        
    }

    //constructor de la clase.
    public AbstractModel(int rowNum, int columnNum,int pieceSize) {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.pieceSize = pieceSize;
        this.imageList = null;
        listP=new ArrayList<PieceModel>();
    }

    /**
     * AÃ±ade una nueva pieza en el modelo
     * @param id identificador de la pieza
     * @param indexRow Ã­ndice de fila donde se encuentra ubicada la pieza
     * @param indexCol Ã­ndice de columna donde se encuentra ubidada la pieza
     * @param imagePath ubicaciÃ³n de la imagen.
     */
    public abstract void addNewPiece(int id, int indexRow, int indexCol, String imagePath);

    /**
     * AÃ±ade una nueva pieza en el modelo
     * @param id identificador de la pieza
     * @param indexRow Ã­ndice de fila donde se encuentra ubicada la pieza
     * @param indexCol Ã­ndice de columna donde se encuentra ubidada la pieza
     */
    public abstract void addNewPiece(int id, int indexRow, int indexCol);

    //comprueba si el puzzle ha sido solucionado
    public abstract boolean isPuzzleSolve();
    //genera movimientos aleatorios
    public abstract int[] getRandomMovement(int lastPos, int pos);
    
    public abstract Stack loadMoves();

    public int getRowCount() {
        return rowNum;
    }

    public int getColumnCount() {
        return columnNum;
    }

    public int getPieceSize(){
        return pieceSize;
    }
    @Override
	public void setNewBoard(int rowNum, int columnNum, int imageSize) {
    	
		 this.rowNum=rowNum;
	        this.columnNum=columnNum;
	        this.pieceSize=imageSize;
	        
	        
	        
		for(int i=0;i<rowNum*columnNum;i++) {	        		
			addNewPiece(i, i%rowNum,i/columnNum);    		
		}		
	}
}
