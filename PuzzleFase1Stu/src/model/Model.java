package model;

import java.util.ArrayList;

import view.PieceView;

public class Model extends AbstractModel<PieceModel>{

	

	public Model(int rowNum, int columnNum, int pieceSize) {
		super(rowNum, columnNum, pieceSize);
		
		
		
	}

	 public Model(int rowNum, int columnNum,int pieceSize, String[] imageList) {
	        super(rowNum,columnNum,pieceSize,imageList);
	       
	        
	        for(int i=0;i<rowNum*columnNum;i++) {
	        	
	        	
	        		addNewPiece( i, i%rowNum,i/columnNum,imageList[i]);
	        		
	    }
	        
	 }
	 
	@Override
	public void update(int blankPos, int movedPos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNewPiece(int id, int indexRow, int indexCol, String imagePath) {
		// TODO Auto-generated method stub
		PieceModel p=new PieceModel( id, rowNum,columnNum,imagePath);
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
