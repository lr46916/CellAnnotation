package hr.cell.annotation.gui.state.appstate;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;

import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.gui.state.State;

public class IdleState implements State {

	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(Point mousePoint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_RIGHT:
			
			break;

		case KeyEvent.VK_LEFT:
			break;
		}
	}

	@Override
	public void afterDraw(Graphics2D r, GraphicalObject go) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterDraw(Graphics2D r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}

}
