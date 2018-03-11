package control;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;


public class Controller extends AbstractController{

	//Variable para recibir del PuzzleGUI la accion realizada
	private String action;
	
	@Override
	public void actionPerformed(ActionEvent act) {
		// TODO Auto-generated method stub
		this.action = act.getActionCommand();
		
		switch (action) {
		case "clutter": 
			System.out.println("Clutter");
			break;
		case "solve":
			System.out.println("Solve");
			break;
		case "load":
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(null);
			if(returnVal==JFileChooser.APPROVE_OPTION){
				File file = fc.getSelectedFile();
			}
			
			System.out.println("Load Image");
			break;
		case "saveGame":
			System.out.println("Save data");
			break;
		case "loadGame":
			JFileChooser fc2 = new JFileChooser();
			int returnVal2 = fc2.showOpenDialog(null);
			if(returnVal2==JFileChooser.APPROVE_OPTION){
				File file = fc2.getSelectedFile();
			}
			
			System.out.println("Load data");
			break;
		case "info":
			System.out.println("Information");
			break;
		default:
			break;
		}
	}

	@Override
	public void notifyObservers(int blankPos, int movedPos) {
		// TODO Auto-generated method stub
		
	}

}
