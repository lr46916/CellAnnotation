package hr.cell.annotation.gui.state.appstate;

import java.awt.Graphics2D;
import java.awt.Point;

import hr.cell.annotation.data.drawutil.objectholder.ObjectHolder;
import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.data.graphicalobject.impl.RectangleGraphicalObject;
import hr.cell.annotation.gui.state.State;

public class MarkerState implements State {

	private Point startPoint;
	private ObjectHolder holder;
	private GraphicalObject currentObj;

	public MarkerState(ObjectHolder holder) {
		super();
		this.holder = holder;
	}

	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		startPoint = mousePoint;
		currentObj = new RectangleGraphicalObject(startPoint.x, startPoint.y, 0, 0);
		holder.addGraphicalObject(currentObj);
	}

	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		if(startPoint != null) {
			currentObj.setHotPoint(1, mousePoint);
		}
	}

	@Override
	public void mouseDragged(Point mousePoint) {
		currentObj.setHotPoint(1, mousePoint);
	}

	@Override
	public void keyPressed(int keyCode) {
		// TODO Auto-generated method stub

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
