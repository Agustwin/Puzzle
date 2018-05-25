package command;

import control.Controller;
import view.PuzzleGUI;

public class RandomCommand implements Command {

	private Controller controller;
	public RandomCommand(Controller c) {
		controller=c;
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
		
		int random=(int) (Math.random()*10);
		
		
		int[]pos = new int[2];
		
		for(int i=0;i<10;i++) {
			
			pos[0]=(int)(Math.random()*PuzzleGUI.getInstance().getBoardView().getRowNum()*PuzzleGUI.getInstance().getBoardView().getRowNum());
			pos[1]=(int)(Math.random()*PuzzleGUI.getInstance().getBoardView().getColumnNum()*PuzzleGUI.getInstance().getBoardView().getColumnNum());
			
			System.out.println(pos[0]+" "+pos[1]);
			
			MoveCommand m=new MoveCommand(controller,pos[0],pos[1]);
			m.execute();
			controller.addCommand(m);
			
		}
		
		
	}

}
