package hr.cell.annotation.data.graphicalobject;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import hr.cell.annotation.data.drawutil.GeometryUtil;
import hr.cell.annotation.data.graphicalobject.listener.GraphicalObjectListener;

public abstract class GraphicalObjectAbs implements GraphicalObject {
	
	private Point[] hotPoints;
	List<GraphicalObjectListener> listeners = new ArrayList<>();

	protected GraphicalObjectAbs(Point[] point) {
		hotPoints = point;
	}

	@Override
	public int getNumberOfHotPoints() {
		return hotPoints.length;
	}

	@Override
	public Point getHotPoint(int index) {
		return hotPoints[index];
	}

	@Override
	public void setHotPoint(int index, Point point) {
		hotPoints[index] = point;
	}

	@Override
	public double getHotPointDistance(int index, Point mousePoint) {
		return GeometryUtil.eucledianDistance(hotPoints[index], mousePoint);
	}

	@Override
	public void translate(Point delta) {
		for (int i = 0; i < hotPoints.length; i++) {
			hotPoints[i].translate(delta.x, delta.y);
		}
	}

	@Override
	public void addGraphicalObjectListener(GraphicalObjectListener l) {
		listeners.add(l);
	}

	@Override
	public void removeGraphicalObjectListener(GraphicalObjectListener l) {
		listeners.remove(l);
	}

	protected void notifyListeners() {
		for (GraphicalObjectListener list : listeners) {
			list.graphicalObjectUpdate(this);
		}
	}

}
