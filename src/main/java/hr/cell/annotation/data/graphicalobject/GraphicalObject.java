package hr.cell.annotation.data.graphicalobject;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import java.util.Stack;

import hr.cell.annotation.data.drawutil.Rectangle;
import hr.cell.annotation.data.graphicalobject.listener.GraphicalObjectListener;

public interface GraphicalObject {
	
	int getNumberOfHotPoints();

	Point getHotPoint(int index);

	void setHotPoint(int index, Point point);

	double getHotPointDistance(int index, Point mousePoint);

	// Geometrijska operacija nad oblikom
	void translate(Point delta);

	Rectangle getBoundingBox();

	double selectionDistance(Point mousePoint);

	// Podrška za crtanje (dio mosta)
	void render(Graphics2D graphics);

	// Observer za dojavu promjena modelu
	public void addGraphicalObjectListener(GraphicalObjectListener l);

	public void removeGraphicalObjectListener(GraphicalObjectListener l);

	// Podrška za prototip (alatna traka, stvaranje objekata u crtežu, ...)
	String getShapeName();

	GraphicalObject duplicate();

	// Podrška za snimanje i učitavanje
	public String getShapeID();

	public void load(Stack<GraphicalObject> stack, String data);

	public void save(List<String> rows);
	
	
}
