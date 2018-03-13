package model;

public class PieceModel {

	private int id;
	private int rowNum;
	private int columnNum;
	private String imagePath;
	
	
	
	
	
	public PieceModel(int id,int rowNum,int columnNum,String imagePath){
		
		this.id=id;
		this.rowNum=rowNum;
		this.columnNum=columnNum;
		this.setImagePath(imagePath);
		
		
		
	}
	
	
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIndexRow() {
		return rowNum;
	}
	public void setIndexRow(int rowNum) {
		this.rowNum = rowNum;
	}
	public int getIndexColumn() {
		return columnNum;
	}
	public void setIndexColumn(int columnNum) {
		this.columnNum = columnNum;
	}






	public String getImagePath() {
		return imagePath;
	}






	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}