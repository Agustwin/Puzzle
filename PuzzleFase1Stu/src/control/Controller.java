package control;

import java.awt.event.ActionEvent;

import java.awt.event.MouseEvent;

import java.io.File;
import javax.swing.JFileChooser;

import command.Command;
import command.MoveCommand;
import observer.Observer;
import view.BoardView;
import view.PuzzleGUI;


public class Controller extends AbstractController{

	//Variable para recibir del PuzzleGUI la accion realizada
	private String action;
	
	/*No se si es correcto*/
	private BoardView myView;
	private int posX;
	private int posY;
	Command move;
	
	
	public Controller() {
		move=new MoveCommand(this);
	}
	/*  ¿HABRIA QUE METER TODAS LAS ACCIONES DENTRO DE UN COMMAND?  */
	@Override
	public void actionPerformed(ActionEvent act) {
		// TODO Auto-generated method stub
		
		
		this.action = act.getActionCommand();
		System.out.println(	act.getSource().toString());

		switch (action) {
			case "clutter": 
				PuzzleGUI.getInstance().getBoardView().Clutter();
				break;
				
			case "solve":
				move.undoCommand();
				
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


public void mouseClicked(MouseEvent e) {

	
	posX=e.getX();
	posY=e.getY();
	
	move.execute();
}

public BoardView getMyView() {
	return myView;
}

public void setMyView(BoardView myView) {
	this.myView = myView;
}

public int getPosY() {
	return posY;
}

public void setPosY(int posY) {
	this.posY = posY;
}

public int getPosX() {
	return posX;
}

public void setPosX(int posX) {
	this.posX = posX;
}

}