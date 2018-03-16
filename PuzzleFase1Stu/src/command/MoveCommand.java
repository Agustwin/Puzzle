package command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import control.Controller;
import view.PuzzleGUI;

public class MoveCommand implements Command{

	private ArrayList<int[]> list=new ArrayList();
	private Controller controller;
	private int index=0;
	
	
	public MoveCommand(Controller c) {
		this.controller=c;	
	}
	
	@Override
	public void undoCommand() {		
		
		// TODO Auto-generated method stub
						
		int pos[]=list.get(index-1);
		System.err.println(index);
		index--;
		if(pos==null) {
			return;
		}
		
		controller.notifyObservers(pos[1],pos[0]);
							
	}		
			
	@Override
	public void redoCommand() {
		// TODO Auto-generated method stub
		
	}
	
	//Metodo para hacer el movimiento de las piezas
	public void execute() {
		
		int[] pos=PuzzleGUI.getInstance().getBoardView().movePiece(controller.getPosX(), controller.getPosY());
		
		if(pos==null) {
			return;
		}
		
		try {
			list.set(index,pos);
		}catch (Exception e) {
			list.add(index,pos);

		}
		index++;
		
		controller.notifyObservers(pos[0],pos[1]);
	}

	public int getIndex() {
		return index;
	}

	
	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	//Metodo invocado en el RandomCommand para que las piezas se coloquen de manera aleatoria
	public void Random(int blankPos, int movedPos) {
		
		int pos[]=new int[2];
				
		
		pos[0]=blankPos;
		pos[1]=movedPos;
		
		
		try {
			list.set(index,pos);
		}catch (Exception e) {
			list.add(index,pos);

		}
		index++;
		
		
		controller.notifyObservers(pos[0],pos[1]);	
	}
	
	public List<int[]> getMoves() {
		return this.list;
	}
	
	public void setMoves(ArrayList<int[]> a) {
		this.list=a;
	}
	
	public void setIndex(int a) {
		this.index=a;
	}
}
