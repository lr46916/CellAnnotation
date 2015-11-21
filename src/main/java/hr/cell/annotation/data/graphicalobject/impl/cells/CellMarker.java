package hr.cell.annotation.data.graphicalobject.impl.cells;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import java.util.Stack;

import hr.cell.annotation.data.drawutil.Rectangle;
import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.data.graphicalobject.impl.RectangleGraphicalObject;
import hr.cell.annotation.data.graphicalobject.listener.GraphicalObjectListener;

public class CellMarker implements GraphicalObject {
	
	private RectangleGraphicalObject rec;

	protected CellMarker(RectangleGraphicalObject rec) {
		this.rec = rec;
	}

	public CellMarker() {
		this.rec = new RectangleGraphicalObject(0, 0, 10, 10, getColor());
	}
	
	protected Color getColor() {
		return Color.red;
	}

	@Override
	public String getShapeName() {
		return "CellMarker";
	}

	@Override
	public String getShapeID() {
		return "@CELLMARKER";
	}

	@Override
	public int getNumberOfHotPoints() {
		return rec.getNumberOfHotPoints();
	}

	@Override
	public Point getHotPoint(int index) {
		return rec.getHotPoint(index);
	}

	@Override
	public void setHotPoint(int index, Point point) {
		rec.setHotPoint(index, point);
	}

	@Override
	public double getHotPointDistance(int index, Point mousePoint) {
		return rec.getHotPointDistance(index, mousePoint);
	}

	@Override
	public void translate(Point delta) {
		rec.translate(delta);
	}

	@Override
	public Rectangle getBoundingBox() {
		return rec.getBoundingBox();
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		return rec.selectionDistance(mousePoint);
	}

	@Override
	public void render(Graphics2D graphics) {
		rec.render(graphics);
	}

	@Override
	public void addGraphicalObjectListener(GraphicalObjectListener l) {
		rec.addGraphicalObjectListener(l);
	}

	@Override
	public void removeGraphicalObjectListener(GraphicalObjectListener l) {
		rec.removeGraphicalObjectListener(l);
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		rec.load(stack, data);
	}

	@Override
	public void save(List<String> rows) {
		rec.save(rows);
	}

	@Override
	public GraphicalObject duplicate() {
		return new CellMarker((RectangleGraphicalObject) rec.duplicate());
	}
}
