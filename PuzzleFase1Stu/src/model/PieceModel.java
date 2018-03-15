package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PieceModel {
	private int id;
	private int rowNum;
	private int columnNum;
	private String imagePath;
	
	public PieceModel(){}
	
	public PieceModel(int id,int rowNum,int columnNum,String imagePath){		
		this.id=id;
		this.rowNum=rowNum;
		this.columnNum=columnNum;
		this.setImagePath(imagePath);		
	}

	
public PieceModel(int id,int rowNum,int columnNum){
		
		this.id=id;
		this.rowNum=rowNum;
		this.columnNum=columnNum;
		this.setImagePath(null);
		
		
		
	}
	
	

	public int getId() {
		return id;
	}
	
	@XmlElement
	public void setId(int id) {
		this.id = id;
	}
	
	public int getIndexRow() {
		return rowNum;
	}
	
	@XmlElement
	public void setIndexRow(int rowNum) {
		this.rowNum = rowNum;
	}
	
	public int getIndexColumn() {
		return columnNum;
	}
	
	@XmlElement
	public void setIndexColumn(int columnNum) {
		this.columnNum = columnNum;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	@XmlElement
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getImageSize() {
		// TODO Auto-generated method stub
		return 0;
	}
}