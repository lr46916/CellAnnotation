package hr.cell.annotation.data.drawutil;

import java.awt.Point;

public class GeometryUtil {
	public static double eucledianDistance(Point point1, Point point2) {
		return Math.sqrt((point1.getX() - point2.getX())
				* (point1.getX() - point2.getX())
				+ (point1.getY() - point2.getY())
				* (point1.getY() - point2.getY()));
	}
	
	public static void pointDiff(Point p1, Point p2, Point result) {
		result.x = p1.x - p2.x;
		result.y = p1.y - p2.y;
	}
	
	public static Point pointDiff(Point p1, Point p2) {
		Point res = new Point();
		pointDiff(p1, p2, res);
		return res;
	}
	
	public static boolean isInsideRectangle(Point p, Rectangle rec){
		return p.x >= rec.getX() && p.x <= rec.getX() + rec.getWidth() && p.y >= rec.getY() && p.y <= rec.getY() + rec.getHeight();
	}
	
}
