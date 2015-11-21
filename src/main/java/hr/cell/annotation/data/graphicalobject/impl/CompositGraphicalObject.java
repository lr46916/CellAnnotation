package hr.cell.annotation.data.graphicalobject.impl;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import hr.cell.annotation.data.drawutil.Rectangle;
import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.data.graphicalobject.GraphicalObjectAbs;

public class CompositGraphicalObject extends GraphicalObjectAbs {

	private List<GraphicalObject> objects;
	private Rectangle bb;

	public CompositGraphicalObject(List<GraphicalObject> objects) {
		super(new Point[0]);
		this.objects = objects;
		calcBb();
	}

	public CompositGraphicalObject() {
		super(new Point[0]);
		objects = new ArrayList<>();
	}

	private void calcBb() {
		int x1 = Integer.MAX_VALUE;
		int y1 = Integer.MAX_VALUE;
		int x2 = 0;
		int y2 = 0;
		for (GraphicalObject go : objects) {
			Rectangle tmpB = go.getBoundingBox();
			if (x1 > tmpB.getX()) {
				x1 = tmpB.getX();
			}
			if (y1 > tmpB.getY()) {
				y1 = tmpB.getY();
			}
			int tmpX = tmpB.getX() + tmpB.getWidth();
			if (tmpX > x2) {
				x2 = tmpX;
			}
			int tmpY = tmpB.getY() + tmpB.getHeight();
			if (tmpY > y2) {
				y2 = tmpY;
			}
		}
		// System.out.println(x + "," + y + "," + width + "," + heigth);
		bb = new Rectangle(x1, y1, x2 - x1, y2 - y1);
	}

	@Override
	public Rectangle getBoundingBox() {
		return bb;
	}

	@Override
	public void translate(Point delta) {
		super.translate(delta);
		objects.stream().forEach((obj) -> obj.translate(delta));
		calcBb();
		notifyListeners();
	};

	@Override
	public double selectionDistance(Point mousePoint) {
		double min = Double.MAX_VALUE;
		for (GraphicalObject go : objects) {
			double tmp = go.selectionDistance(mousePoint);
			if (tmp < min) {
				min = tmp;
			}
		}
		return min;
	}

	@Override
	public void render(Graphics2D graphics) {
		for (GraphicalObject object : objects) {
			object.render(graphics);
		}
	}

	@Override
	public int getNumberOfHotPoints() {
		return 0;
	}

	@Override
	public Point getHotPoint(int index) {
		return null;
	}

	@Override
	public void setHotPoint(int index, Point point) {
	}

	@Override
	public String getShapeName() {
		return "COMPOSITE";
	}

	@Override
	public GraphicalObject duplicate() {
		return new CompositGraphicalObject(new ArrayList<>(objects));
	}

	@Override
	public String getShapeID() {
		return "@COMP";
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {

		Scanner scan = new Scanner(data);

		int num = scan.nextInt();

		List<GraphicalObject> list = new LinkedList<>();
		for (int i = 0; i < num; i++) {
			list.add(stack.pop());
		}

		stack.push(new CompositGraphicalObject(list));

		scan.close();
	}

	@Override
	public void save(List<String> rows) {

		rows.add(getShapeID() + ' ' + objects.size());

		for (GraphicalObject go : objects) {
			go.save(rows);
		}

	}

	public List<GraphicalObject> getParts() {
		return objects;
	}

}
