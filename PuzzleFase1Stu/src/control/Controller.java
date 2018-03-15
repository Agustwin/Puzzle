package control;

import java.awt.event.ActionEvent;

import java.awt.event.MouseEvent;

import java.io.File;
import javax.swing.JFileChooser;

import command.Command;
import command.MoveCommand;
import command.RandomCommand;
import command.SolveCommand;
import model.Model;
import observer.Observer;
import view.BoardView;
import view.PuzzleGUI;


public class Controller extends AbstractController{

	//Variable para recibir del PuzzleGUI la accion realizada
	private String action;
	
	/*No se si es correcto*/
	private BoardView myView;
	private Model myModel;
	private int posX;
	private int posY;
	private MoveCommand move;
	private Command solve;
	private Command random;
	
	public Controller() {
		move=new MoveCommand(this);
		solve=new SolveCommand(this);
		random=new RandomCommand(this);
	}
	/*  ¿HABRIA QUE METER TODAS LAS ACCIONES DENTRO DE UN COMMAND?  */
	@Override
	public void actionPerformed(ActionEvent act) {
		// TODO Auto-generated method stub
		
		
		this.action = act.getActionCommand();
		System.out.println(	act.getSource().toString());

		switch (action) {
			case "clutter": 
				random.execute();
				break;
				
			case "solve":
				solve.execute();
				
				break;
				
			case "load":
				
				/*--------------------Meter en comando---------------------*/
				File f=PuzzleGUI.getInstance().showFileSelector();
				System.out.println("Path: "+f);
				if(f!=null) {
					PuzzleGUI.getInstance().updateBoard(f);
				}
				notifyObserversReset();
				this.myView=PuzzleGUI.getInstance().getBoardView();
				reset();

				//PuzzleGUI.getInstance().getBoardView().update(PuzzleGUI.getInstance().getBoardView().getGraphics());
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

	public void notifyObserversReset() {
		//TODO Auto-generated method stub
		for(Observer o:observerList) {
			
			o.setNewBoard();
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
public MoveCommand getCommandMove() {
	return move;
}
public void reset() {
	move=new MoveCommand(this);
	solve=new SolveCommand(this);
	random=new RandomCommand(this);
}
}