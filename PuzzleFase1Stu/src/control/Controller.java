package control;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

public class Controller extends AbstractController{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyObservers(int blankPos, int movedPos) {
		//TODO Auto-generated method stub
		
	}


public void mouseClicked(MouseEvent e) {
	System.out.println("X: "+e.getX()+" Y: "+	e.getY());
	

}
}