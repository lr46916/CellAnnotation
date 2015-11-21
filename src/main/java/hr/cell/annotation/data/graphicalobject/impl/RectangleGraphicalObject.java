package hr.cell.annotation.data.graphicalobject.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import java.util.Stack;

import hr.cell.annotation.data.drawutil.GeometryUtil;
import hr.cell.annotation.data.drawutil.Rectangle;
import hr.cell.annotation.data.drawutil.RenderUtil;
import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.data.graphicalobject.GraphicalObjectAbs;

public class RectangleGraphicalObject extends GraphicalObjectAbs {

	private static final Rectangle boundingBox = new Rectangle(0, 0, 0, 0);
	private Rectangle rectangle;
	private Point hotPointsDiff;

	public RectangleGraphicalObject(int x, int y, int width, int height) {
		super(new Point[] { new Point(x, y), new Point(x + width, y + height) });
		hotPointsDiff = new Point();
		rectangle = new Rectangle(x, y, width, height);
	}

	public RectangleGraphicalObject() {
		super(new Point[] { new Point(), new Point() });
		hotPointsDiff = new Point();
		rectangle = new Rectangle(0, 0, 0, 0);
	}

	private void updateRectangleSize(Point p1, Point p2) {
		GeometryUtil.pointDiff(p2, p1, hotPointsDiff);
		rectangle.setX(p1.x);
		rectangle.setY(p1.y);
		rectangle.setWidth(hotPointsDiff.x);
		rectangle.setHeight(hotPointsDiff.y);
	}

	@Override
	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	@Override
	public void setHotPoint(int index, Point point) {
		if(index == 1) {
			point.x = Integer.max(rectangle.getX(), point.x);
			point.y = Integer.max(rectangle.getY(), point.y);
		} else {
			point.x = Integer.min(rectangle.getX() + rectangle.getWidth(), point.x);
			point.y = Integer.min(rectangle.getY() + rectangle.getHeight(), point.y);
		}
		super.setHotPoint(index, point);
		updateRectangleSize(getHotPoint(0), getHotPoint(1));
		notifyListeners();
	}
	
	@Override
	public void translate(Point delta) {
		super.translate(delta);
		updateRectangleSize(getHotPoint(0), getHotPoint(1));
		notifyListeners();
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		if (GeometryUtil.isInsideRectangle(mousePoint, rectangle)){
			return 0;
		}

		if (mousePoint.x < rectangle.getX()) {
			if (mousePoint.y < rectangle.getY()) {
				return GeometryUtil.eucledianDistance(mousePoint, new Point(rectangle.getX(), rectangle.getY()));
			} else {
				if (mousePoint.y <= rectangle.getY() + rectangle.getHeight()) {
					return rectangle.getX() - mousePoint.getX();
				} else {
					return GeometryUtil.eucledianDistance(mousePoint,
							new Point(rectangle.getX(), rectangle.getY() + rectangle.getHeight()));
				}
			}
		} else {
			if (mousePoint.x <= rectangle.getX() + rectangle.getWidth()) {
				if (mousePoint.y < rectangle.getY()) {
					return rectangle.getY() - mousePoint.getY();
				} else {
					return mousePoint.getY() - rectangle.getY() - rectangle.getHeight();
				}
			} else {
				if (mousePoint.y < rectangle.getY()) {
					return GeometryUtil.eucledianDistance(mousePoint,
							new Point(rectangle.getX() + rectangle.getWidth(), rectangle.getY()));
				} else {
					if (mousePoint.y <= rectangle.getY() + rectangle.getHeight()) {
						return mousePoint.getX() - rectangle.getX() - rectangle.getWidth();
					} else {
						return GeometryUtil.eucledianDistance(mousePoint, new Point(
								rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight()));
					}
				}
			}
		}
	}

	@Override
	public void render(Graphics2D graphics) {
		RenderUtil.renderRectangle(rectangle, Color.RED, null, graphics);
	}

	@Override
	public String getShapeName() {
		return "RECTANGLE";
	}

	@Override
	public GraphicalObject duplicate() {
		return new RectangleGraphicalObject(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getWidth());
	}

	@Override
	public String getShapeID() {
		return "@REC";
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		String[] parts = data.split("\\s+");
		stack.push(new RectangleGraphicalObject(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
				Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
	}

	@Override
	public void save(List<String> rows) {
		rows.add(getShapeID() + ' ' + rectangle);
	}

}
