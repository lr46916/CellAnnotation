package hr.cell.annotation.gui.state.appstate;

import java.awt.Graphics2D;
import java.awt.Point;

import hr.cell.annotation.data.drawutil.objectholder.ObjectHolder;
import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.gui.state.State;

public class AddShapeState implements State {

	private GraphicalObject prototype;
	private ObjectHolder model;

	public AddShapeState(ObjectHolder model, GraphicalObject prototype) {
		this.prototype = prototype;
		this.model = model;
	}

	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		// dupliciraj zapamćeni prototip, pomakni ga na poziciju miša i dodaj u
		// model
		GraphicalObject obj = prototype.duplicate();
		obj.translate(mousePoint);
		model.addGraphicalObject(obj);
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
