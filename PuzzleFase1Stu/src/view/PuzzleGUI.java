 package view;

import control.AbstractController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Clase que representa la GUI principal.
 * @author Miguel Ã�ngel
 * @version 1.0
 */
public class PuzzleGUI extends JFrame{

    //Instancia singleton
    public static PuzzleGUI instance = null;
    //Controlador
    public static AbstractController controller;
    //NÃºmero de filas
    public static int rowNum=0;
    //NÃºmero de columnas
    public static int columnNum =0;
    //TamaÃ±o de imagen
    public static int imageSize =0;
    //Array de imagenes
    public static String[] imageList = null;
    
    public static File f=null;
    //Panel de juego
    private BoardView boardView;
    //ancho y alto de la ventana
    private int
     windowH=250;
	private int windowW=250;
	private JPanel j;
    /**
     * Constructor privado
     */
    private PuzzleGUI(){
        super("GMD PuzzleGUI");
        if(this.imageList==null) {
        	boardView = new BoardView(rowNum,columnNum,imageSize,f);
        }else {
        	boardView = new BoardView(rowNum,columnNum,imageSize,imageList);
        }
        
        boardView.addMouseListener(controller);
        this.getContentPane().setLayout(new BorderLayout());
        this.setJMenuBar(createMenuBar());
        this.getContentPane().add(boardView, BorderLayout.CENTER);
        j=createSouthPanel();
        this.getContentPane().add(j, BorderLayout.SOUTH);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(windowW,windowH);
        this.setLocation(centerFrame());
       
    }


    //Singleton
    public static PuzzleGUI getInstance(){
        if(instance==null){
            instance = new PuzzleGUI();
        }
        return(instance);
    }

    public static void initialize(AbstractController controller, int rowNum,int columnNum,int imageSize,String[] imageList){
        PuzzleGUI.controller = controller;
        PuzzleGUI.rowNum = rowNum;
        PuzzleGUI.columnNum = columnNum;
        PuzzleGUI.imageSize = imageSize;
        PuzzleGUI.imageList = imageList;    
    }
    
    public static void initialize(AbstractController controller, int rowNum,int columnNum,int imageSize,File f){
        PuzzleGUI.controller = controller;
        PuzzleGUI.rowNum = rowNum;
        PuzzleGUI.columnNum = columnNum;
        PuzzleGUI.imageSize = imageSize;
        PuzzleGUI.f = f;    
    }

    //MÃ©todo que crea el panel inferior
    private JPanel createSouthPanel(){
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton clutterButton = new JButton("Clutter");//botÃ³n de desordenar
        clutterButton.setActionCommand("clutter");
        JButton solveButton = new JButton("Solve");
        solveButton.setActionCommand("solve");

        clutterButton.addActionListener(controller);
        solveButton.addActionListener(controller);


        southPanel.add(clutterButton);
        southPanel.add(solveButton);

        return(southPanel);
    }

    //MÃ©todo que genera la barra de menus
    private JMenuBar createMenuBar(){
        JMenuBar menu = new JMenuBar();
        JMenu archive = new JMenu("Archive");
        JMenu help = new JMenu("Help");

        JMenuItem load = new JMenuItem("Load");
        load.setActionCommand("load");
        JMenuItem exit = new JMenuItem("Exit");
        exit.setActionCommand("exit");
        JMenuItem info = new JMenuItem("Info");
        info.setActionCommand("info");
        
        //Paso 4 para guardar y cargar partidas
        JMenuItem saveGame = new JMenuItem("saveGame");
        saveGame.setActionCommand("saveGame");
        JMenuItem loadGame = new JMenuItem("loadGame");
        loadGame.setActionCommand("loadGame");

        archive.add(load);
        archive.add(saveGame);
        archive.add(loadGame);       
        archive.add(exit);
        help.add(info);

        menu.add(archive);
        menu.add(help);

        load.addActionListener(controller);
        
        //Paso 4 para guardar y cargar partidas
        saveGame.addActionListener(controller);
        loadGame.addActionListener(controller);
        
        exit.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ev) {
	                System.exit(0);
	        }
	    });
        info.addActionListener(controller);

        return(menu);
    }

    //Centrar el frame en el centro de la pantalla.
    private Point centerFrame(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int xCoord = (screenSize.width - this.getWidth()) / 2;
        int yCoord = (screenSize.height - this.getHeight()) / 2;
        return(new Point(xCoord,yCoord));
    }

    public File showFileSelector(){
        File selectedFile = null;
        JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(null);
		if(returnVal==JFileChooser.APPROVE_OPTION){
			 selectedFile = fc.getSelectedFile();
		}
        return(selectedFile);
    }

    public BoardView getBoardView(){
        return(this.boardView);
    }

    //MÃ©todo para actualizar la imagen del tablero
    public void updateBoard(File imageFile){
    	
    	boardView.setImage(imageFile);
   
    }
    
    public void enterParameters(){
    	JPanel j=new JPanel();
		JTextField Rows = new JTextField(5);
	      JTextField Columns = new JTextField(5);
	      JTextField Size = new JTextField(5);
	      
	      JPanel myPanel = new JPanel();
	      myPanel.add(new JLabel("Filas:"));
	      myPanel.add(Rows);
	      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
	      myPanel.add(new JLabel("Columnas:"));
	      myPanel.add(Columns);
	      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
	      myPanel.add(new JLabel("Tamaño de pieza:"));
	      myPanel.add(Size);

	      int result = JOptionPane.showConfirmDialog(null, myPanel, 
	               "Please Enter Values", JOptionPane.OK_CANCEL_OPTION);
	      if (result == JOptionPane.OK_OPTION) {
	         
	    	this.rowNum=  Integer.parseInt(Rows.getText());
	    	 this.columnNum= Integer.parseInt(Columns.getText());
	    	 this.imageSize= Integer.parseInt(Size.getText());
	    	  
	       }
    }


    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

	public int getWindowW() {
		return windowW;
	}

	public void setWindowW(int windowW) {
		this.windowW = windowW;
	}

	public int getWindowH() {
		return windowH;
	}

	public void setWindowH(int windowH) {
		this.windowH = windowH;
	}

}
