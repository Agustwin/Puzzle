package command;

import java.util.Stack;

import javax.swing.JOptionPane;

import control.Controller;

public class SolveCommand implements Command {
	private Controller controller;
	
	public SolveCommand(Controller c) {
		this.controller=c;
		
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
		Stack<MoveCommand> aux=controller.getMoves();
		while(!aux.isEmpty()) {
			System.out.println("holaaaaaaaaaaa");
			MoveCommand m=aux.pop();
			m.undoCommand();
		}
		//Mensaje de que se ha solucionado el puzzle
		JOptionPane.showMessageDialog(null,"Puzzle is solved");
	}
}
