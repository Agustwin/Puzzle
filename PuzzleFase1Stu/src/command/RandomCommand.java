package command;

import control.Controller;
import view.PuzzleGUI;

public class RandomCommand implements Command {

	private Controller controller;
	private int size;
	
	//Paso el tamaño del puzzle y el controlador como parámetros
	public RandomCommand(Controller c,int puzzleSize) {
		controller=c;
		size=puzzleSize;
	}
	
	
	@Override
	public void undoCommand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redoCommand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		

		
		
		int[]pos = new int[2];
		
		for(int i=0;i<10;i++) {
			
			pos[0]=(int)(Math.random()*size);
			pos[1]=(int)(Math.random()*size);
			
			
			MoveCommand m=new MoveCommand(controller,pos[0],pos[1]);
			m.execute();
			controller.addCommand(m);
			
		} 
		
		
	}

}
