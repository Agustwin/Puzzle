import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Model")
public class Partida {
	
	private String Id;
	private String X;
	private String Y;
	private String Size;
	private String ImagePath;
	
	private String pos0;
	private String pos1;	
	
	public Partida(String id, String x, String y, String size, String imagePath, String pos0, String pos1) {
		super();
		this.Id = id;
		this.X = x;
		this.Y = y;
		this.Size = size;
		this.ImagePath = imagePath;
		this.pos0 = pos0;
		this.pos1 = pos1;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getX() {
		return X;
	}

	public void setX(String x) {
		X = x;
	}

	public String getY() {
		return Y;
	}

	public void setY(String y) {
		Y = y;
	}

	public String getSize() {
		return Size;
	}

	public void setSize(String size) {
		Size = size;
	}

	public String getImagePath() {
		return ImagePath;
	}

	public void setImagePath(String imagePath) {
		ImagePath = imagePath;
	}

	
	
}
