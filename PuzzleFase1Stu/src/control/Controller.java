package control;

import java.awt.event.ActionEvent;
<<<<<<< HEAD

=======
>>>>>>> branch 'master' of https://github.com/Agustwin/Puzzle.git
import java.awt.event.MouseEvent;
<<<<<<< HEAD

=======
>>>>>>> branch 'master' of https://github.com/Agustwin/Puzzle.git
import java.io.File;
import javax.swing.JFileChooser;

<<<<<<< HEAD
import observer.Observer;
import view.BoardView;
import view.PuzzleGUI;



=======
>>>>>>> branch 'master' of https://github.com/Agustwin/Puzzle.git

public class Controller extends AbstractController{

	//Variable para recibir del PuzzleGUI la accion realizada
	private String action;
	
	/*No se si es correcto*/
	private BoardView myView;

	
	
	
	/*  ¿HABRIA QUE METER TODAS LAS ACCIONES DENTRO DE UN COMMAND?  */
	@Override
	public void actionPerformed(ActionEvent act) {
		// TODO Auto-generated method stub
		
		
		this.action = act.getActionCommand();
		System.out.println(	act.getSource().toString());

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
		//TODO Auto-generated method stub
		for(Observer o:observerList) {
			
			o.update(blankPos, movedPos);
		}
		
	}


<<<<<<< HEAD
public void mouseClicked(MouseEvent e) {

	System.out.println(e.getX()+"   "+ e.getY());
	
	int[] pos=PuzzleGUI.getInstance().getBoardView().movePiece(e.getX(), e.getY());

	if(pos==null) {
		return;
	}
	
	notifyObservers(pos[0],pos[1]);
}

public BoardView getMyView() {
	return myView;
}

public void setMyView(BoardView myView) {
	this.myView = myView;
}
=======
	public void mouseClicked(MouseEvent e) {
		System.out.println("X: "+e.getX()+" Y: "+	e.getY());
			
	}
>>>>>>> branch 'master' of https://github.com/Agustwin/Puzzle.git
}