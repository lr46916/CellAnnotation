package hr.cell.annotation.data.drawutil;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class RenderUtil {

	public static void renderRectangle(Rectangle rectangle, Color boundColor, Color innerColor, Graphics2D graphics) {
		renderRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), boundColor,
				innerColor, graphics);
	}

	public static void renderRectangle(int x, int y, int width, int height, Color boundColor, Color innerColor,
			Graphics2D graphics) {

		if (innerColor != null) {
			graphics.setColor(innerColor);
			graphics.fillRect(x, y, width, height);
		}
		graphics.setColor(boundColor);
		graphics.drawRect(x, y, width, height);
	}

	public static void renderHotSpots(Point[] hotSpots, Graphics2D graphics) {
		for (Point p : hotSpots) {
			renderRectangle(p.x - 3, p.y - 3, 6, 6, Color.BLACK, Color.white, graphics);
		}
	}

}
