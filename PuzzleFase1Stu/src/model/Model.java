package model;

import java.util.ArrayList;


public class Model extends AbstractModel<PieceModel>{

	protected ArrayList<PieceModel> listP=null;

	public Model(int rowNum, int columnNum, int pieceSize) {
		super(rowNum, columnNum, pieceSize);
		
		
		
	}

	 public Model(int rowNum, int columnNum,int pieceSize, String[] imageList) {
	        super(rowNum,columnNum,pieceSize,imageList);
	        listP=new ArrayList<PieceModel>();
	        
	        for(int i=0;i<rowNum*columnNum;i++) {
	        	
	        	
	        		addNewPiece( i, i%rowNum,i/columnNum,imageList[i]);
	        		
	    }
	        
	 }
	 
	@Override
	public void update(int blankPos, int movedPos) {
		// TODO Auto-generated method stub
		
		//muevo las coordenadas de las piezas
		int auxX=listP.get(movedPos).getIndexRow();
		int auxY=listP.get(movedPos).getIndexColumn();;
	
		listP.get(movedPos).setIndexRow(listP.get(blankPos).getIndexRow());
		listP.get(movedPos).setIndexColumn(listP.get(blankPos).getIndexColumn());
		
		listP.get(blankPos).setIndexColumn(auxY);
		listP.get(blankPos).setIndexRow(auxX);
		
		
		PieceModel blank=listP.get(blankPos);

    	listP.set(blankPos,listP.get(movedPos));
    	listP.set(movedPos, blank);
		
    	System.out.println("Model: ");
		for(PieceModel p:listP) {
			System.out.println("id: "+p.getId()+" X: "+p.getIndexRow()+" Y: "+p.getIndexColumn());
		}
	}

	@Override
	public void addNewPiece(int id, int indexRow, int indexCol, String imagePath) {
		// TODO Auto-generated method stub
		PieceModel p=new PieceModel( id, indexRow,indexCol,imagePath);
		listP.add(p); 
	}

	@Override
	public void addNewPiece(int id, int indexRow, int indexCol) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPuzzleSolve() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] getRandomMovement(int lastPos, int pos) {
		// TODO Auto-generated method stub
		return null;
	}

}
