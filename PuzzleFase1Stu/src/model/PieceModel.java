package model;

public class PieceModel {

	private int id;
	private int rowNum;
	private int columnNum;
	
	
	
	
	
	
	public PieceModel(int id,int rowNum,int columnNum){
		
		this.id=id;
		this.rowNum=rowNum;
		this.columnNum=columnNum;
	
		
		
		
	}
	
	
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public int getColumnNum() {
		return columnNum;
	}
	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}
}